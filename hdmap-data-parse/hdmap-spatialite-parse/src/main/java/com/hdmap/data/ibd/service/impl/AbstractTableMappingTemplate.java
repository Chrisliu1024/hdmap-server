package com.hdmap.data.ibd.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdmap.data.ibd.Enum.ErrorLogEnum;
import com.hdmap.data.ibd.Enum.MethodNameEnum;
import com.hdmap.data.ibd.dto.TableMapping;
import com.hdmap.data.ibd.manager.GeometryManager;
import com.hdmap.data.ibd.utils.JsonUtil;
import com.hdmap.core.enums.ErrorEnum;
import com.hdmap.core.exception.DefinitionException;
import com.hdmap.data.ibd.dto.ColValMapping;
import com.hdmap.data.ibd.dto.ErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class AbstractTableMappingTemplate {
    private final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
    private BaseMappingComponent baseTemplate;
    private GeometryManager geometryManager;

    public AbstractTableMappingTemplate(BaseMappingComponent baseTemplate, GeometryManager geometryManager) {
        this.baseTemplate = baseTemplate;
        this.geometryManager = geometryManager;
    }

    abstract List<SimpleFeature> buildFeatures(Map<String, Object> colValMap, SimpleFeatureType schema, SimpleFeature feature) throws FactoryException, TransformException, java.text.ParseException;

    SimpleFeature generateSimpleFeature(Map<String, Object> colValMap, SimpleFeatureType schema) throws FactoryException, TransformException, java.text.ParseException {
        return baseTemplate.generateSimpleFeature(colValMap, schema);
    }

    Object getMappingValue(ColValMapping colValMapping, SimpleFeature feature, DataStore targetDs, DataStore sourceDs, String sourceIdColName) throws CQLException, IOException, ParseException {
        String mappingFunction = colValMapping.getMappingFunction();
        DataStore ds = colValMapping.getIsTargetDs() ? targetDs : sourceDs;
        String sourceColName;
        Object sourceColValue;
        List<SimpleFeature> relFeatures;
        List<String> relTargetColNames;
        List<Object> attrValueList;
        SimpleFeatureCollection collection, relCollection;
        SimpleFeature[] filterArray;
        List<SimpleFeature> filterList;
        List<String> ids;
        String id;
        Filter filter;
        // 转换函数
        MethodNameEnum methodNameEnum = MethodNameEnum.getEnum(mappingFunction);
        switch (Objects.requireNonNull(methodNameEnum)) {
            case ONE2ONE:
                sourceColName = colValMapping.getSourceColName();
                return feature.getAttribute(sourceColName);
            case ONE2NEGATIVEONE:
                sourceColName = colValMapping.getSourceColName();
                Class<?> binding = feature.getFeatureType().getType(sourceColName).getBinding();
                // 正数 -> 负数
                if (binding == Integer.class) {
                    return -NumberUtils.toInt(feature.getAttribute(sourceColName).toString());
                }
                else if (binding == Float.class) {
                    return -NumberUtils.toFloat(feature.getAttribute(sourceColName).toString());
                } else if (binding == Double.class) {
                    return -NumberUtils.toDouble(feature.getAttribute(sourceColName).toString());
                } else {
                    return -NumberUtils.toInt(feature.getAttribute(sourceColName).toString());
                }
            case MANY2MANY:
                sourceColName = colValMapping.getSourceColName();
                sourceColValue = feature.getAttribute(sourceColName);
                return handleValueMapping(sourceColValue, colValMapping, feature.getAttribute(sourceIdColName).toString());
            case GETCENTERPOINT:
                sourceColName = colValMapping.getSourceColName();
                sourceColValue = feature.getAttribute(sourceColName);
                Point centroid = ((Geometry) sourceColValue).getCentroid();
                // 创建新点
                return gf.createPoint(new Coordinate(centroid.getX(), centroid.getY(), 0));
            case GETLENGTH:
                sourceColName = colValMapping.getSourceColName();
                sourceColValue = feature.getAttribute(sourceColName);
                return ((Geometry) sourceColValue).getLength();
            case GETDEFAULTVALUE:
                return colValMapping.getDefaultValue();
            case GETMAXVALUE:
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                if (relFeatures.isEmpty()) {
                    return 0;
                }
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                // 获取关联表的目标字段值的最大值
                List<SimpleFeature> finalRelFeatures1 = relFeatures;
                return relTargetColNames.stream().map(relTargetCol -> geometryManager.getAttriMaxValueFromArray(finalRelFeatures1, relTargetCol)).filter(Objects::nonNull)
                        .max(Comparator.comparing(o -> Double.parseDouble(o.toString()))).orElse(null);
            case GETMINVALUE:
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                if (relFeatures.isEmpty()) {
                    return 0;
                }
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                // 获取关联表的目标字段值的最小值
                List<SimpleFeature> finalRelFeatures = relFeatures;
                return relTargetColNames.stream().map(relTargetCol -> geometryManager.getAttriMinValueFromArray(finalRelFeatures, relTargetCol)).filter(Objects::nonNull)
                        .min(Comparator.comparing(o -> Double.parseDouble(o.toString()))).orElse(null);
            case GETSINGLEVALUE:
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                // 获取关联表的目标字段值
                attrValueList = getRelAttrValues(relTargetColNames, relFeatures);
                if (attrValueList.size() != 1) {
                    return new ErrorLog(feature.getAttribute(sourceIdColName).toString(), ErrorLogEnum.VALUE_IS_NOT_ONLY, colValMapping.getSourceColName(), colValMapping.getTargetColName(),
                            colValMapping.getSourceColName(), colValMapping.getTargetColName());
                }
                return attrValueList.get(0);
            case GETMULTIVALUE:
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                // 获取关联表的目标字段值
                attrValueList = getRelAttrValues(relTargetColNames, relFeatures);
                // |分割的字符串
                return attrValueList.stream().map(Object::toString).collect(Collectors.joining("|"));
            case CUSTOM1: // 高度定制化
                relCollection = ds.getFeatureSource(colValMapping.getRelTableName()).getFeatures();
                filterArray = geometryManager.getLikeFilterArray(relCollection, colValMapping.getRelColName().get(0), feature.getAttribute(colValMapping.getSourceColName()).toString());
                ids = new ArrayList<>();
                for (SimpleFeature simpleFeature : filterArray) {
                    // 在左侧
                    if (geometryManager.checkLeft1(feature, simpleFeature)) {
                        ids.add(simpleFeature.getAttribute(colValMapping.getRelTargetColName().get(0)).toString());
                    }
                }
                // |分割的字符串
                return String.join("|", ids);

            case CUSTOM2: // 高度定制化
                relCollection = ds.getFeatureSource(colValMapping.getRelTableName()).getFeatures();
                filterArray = geometryManager.getLikeFilterArray(relCollection, colValMapping.getRelColName().get(0), feature.getAttribute(colValMapping.getSourceColName()).toString());
                ids = new ArrayList<>();
                for (SimpleFeature simpleFeature : filterArray) {
                    // 不在左侧
                    if (!geometryManager.checkLeft1(feature, simpleFeature)) {
                        ids.add(simpleFeature.getAttribute(colValMapping.getRelTargetColName().get(0)).toString());
                    }
                }
                // |分割的字符串
                return String.join("|", ids);

            case CUSTOM3: // 高度定制化
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                // 获取关联表的目标字段值
                attrValueList = getRelAttrValues(relTargetColNames, relFeatures);
                if (attrValueList.isEmpty()) {
                    return null;
                } else if (attrValueList.size() == 1) {
                    return attrValueList.get(0);
                }
                relCollection = ds.getFeatureSource("IBD_OBJECT_OTHER_POLYGON").getFeatures();
                // 获取相交的feature
                collection = geometryManager.getIntersectFilterCollection(relCollection, feature, 0.1);
                SimpleFeature[] compareFeatures = collection.toArray(new SimpleFeature[0]);
                // 获取相交的feature的jc_id
                List<String> compareJcidList = new ArrayList<>();
                for (SimpleFeature compareFeature : compareFeatures) {
                    compareJcidList.add(compareFeature.getAttribute("OBJECT_PID").toString());
                }
                // 获取compareJcidList和attrValueList的交集
                List<String> intersection = attrValueList.stream().map(Object::toString).filter(compareJcidList::contains).collect(Collectors.toList());
                if (intersection.size() != 1) {
                    // 记录错误日志
                    return new ErrorLog(feature.getAttribute(sourceIdColName).toString(), ErrorLogEnum.VALUE_IS_NOT_ONLY, colValMapping.getSourceColName(), colValMapping.getTargetColName(),
                            colValMapping.getSourceColName(), colValMapping.getTargetColName());
                }
                return intersection.get(0);
            case CUSTOM4: // 高度定制化
                sourceColName = colValMapping.getSourceColName();
                sourceColValue = feature.getAttribute(sourceColName);
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                // 关联的表目标字段名
                relTargetColNames = colValMapping.getRelTargetColName();
                collection = ds.getFeatureSource("IBD_ROADLINK").getFeatures();
                // 获取前驱和后继
                List<SimpleFeature> preAndSucList = new ArrayList<>();
                for (SimpleFeature feature1 : relFeatures) {
                    List<SimpleFeature> preFeatures = geometryManager.getPreFeature(feature1, collection, "S_NODE_PID", "E_NODE_PID");
                    List<SimpleFeature> sucFeature = geometryManager.getSucFeature(feature1, collection, "S_NODE_PID", "E_NODE_PID");
                    preAndSucList.addAll(preFeatures);
                    preAndSucList.addAll(sucFeature);
                }
                List<String> relColName = colValMapping.getRelColName();
                // 过滤sourceColValue==feature.getAttribute(relColName.get(i))的feature
                List<SimpleFeature> filterFeatureList = new ArrayList<>();
                for (String relCol : relColName) {
                    // 排除jc_id和前驱和后继的jc_id一致的情况
                    preAndSucList = preAndSucList.stream().filter(f -> f.getAttribute(relCol) != null).filter(f -> !f.getAttribute(relCol).equals(sourceColValue)).collect(Collectors.toList());
                    filterFeatureList.addAll(preAndSucList);
                }
                // 获取关联表的目标字段值
                attrValueList = getRelAttrValues(relTargetColNames, filterFeatureList);
                // |分割的字符串
                return attrValueList.stream().map(Object::toString).collect(Collectors.joining("|"));
            case CUSTOM5: // 高度定制化
                relCollection = geometryManager.getEqualFilterCollection(sourceDs, "IBD_ROADLINK", "LINK_PID", feature.getAttribute("LINK_PID").toString());
                // 为空则写入1，不为空写入2
                if (relCollection.isEmpty()) {
                    return 1;
                } else {
                    return 2;
                }
            case GETGEOMBYOFFSET: // 高度定制化
                String offset = feature.getAttribute("LATERAL_OFFSET").toString();
                // String -> double
                double offsetDouble = Double.parseDouble(offset);
                // 负数向左偏移，正数向右偏移
                // 新方法
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                // 构建缓冲区，原单位：厘米
                Geometry buffer = geom.buffer(Math.abs(offsetDouble / baseTemplate.getDefaultUnit()), 2, BufferParameters.CAP_ROUND);
                Geometry boundary = buffer.getBoundary();
                Geometry clipBuffer = geom.buffer(Math.abs(offsetDouble / baseTemplate.getDefaultUnit()) + 1, 2, BufferParameters.CAP_FLAT);
                Geometry interGeom = boundary.intersection(clipBuffer);
                // MultiLineString -> LineString
                List<Geometry> interGeomList = new ArrayList<>();
                if (interGeom instanceof MultiLineString) {
                    MultiLineString multiLineString = (MultiLineString) interGeom;
                    int numGeometries = multiLineString.getNumGeometries();
                    for (int i = 0; i < numGeometries; i++) {
                        Geometry geometryN = multiLineString.getGeometryN(i);
                        if (geometryN instanceof LineString) {
                            interGeomList.add(geometryN);
                        }
                    }
                }
                Coordinate[] lineCoords = geom.getCoordinates();
                if (offsetDouble < 0) {
                    // 返回左边线
                    Coordinate[] leftCoords;
                    if (interGeomList.size() == 3) {
                        // 融合子线2和子线0
                        Coordinate[] leftCoords2 = interGeomList.get(2).getCoordinates();
                        Coordinate[] leftCoords0 = interGeomList.get(0).getCoordinates();
                        // 合并leftCoords2和leftCoords0
                        leftCoords = new Coordinate[leftCoords2.length + leftCoords0.length];
                        System.arraycopy(leftCoords2, 0, leftCoords, 0, leftCoords2.length);
                        System.arraycopy(leftCoords0, 0, leftCoords, leftCoords2.length, leftCoords0.length);
                    } else { // interGeomList.size() == 2
                        leftCoords = interGeomList.get(1).getCoordinates();
                    }

                    // 按照距离去除重复点
                    Coordinate[] leftCoordsDistinct = geometryManager.getDistinctCoords(leftCoords, 0.02);
                    // 如果去重后的点数和lineCoords的点数不一致，则调整阈值
                    if (leftCoordsDistinct.length != lineCoords.length) {
                        double threshold = 0.02;
                        double step = 0.005;
                        double count = 0;
                        boolean isInLeft = false;
                        boolean isInRight = false;
                        while (true) {
                            if (leftCoordsDistinct.length > lineCoords.length) {
                                threshold += step;
                                step = isInRight ? step / 2 : step;
                                leftCoordsDistinct = geometryManager.getDistinctCoords(leftCoords, threshold);
                                isInLeft = true;
                            }
                            if (leftCoordsDistinct.length < lineCoords.length) {
                                threshold -= step;
                                step = isInLeft ? step / 2 : step;
                                leftCoordsDistinct = geometryManager.getDistinctCoords(leftCoords, threshold);
                                isInRight = true;
                            }
                            // 如果去重后的点数和lineCoords的点数一致，则跳出循环
                            if (leftCoordsDistinct.length == lineCoords.length || count == 20) {
                                break;
                            }
                            count++;
                        }
                    }
                    // 根据lineCoords给leftCoordsDistinct赋z值
                    double sum = 0;
                    for (int i = 0; i < leftCoordsDistinct.length; i++) {
                        if (i < lineCoords.length) {
                            leftCoordsDistinct[i].z = lineCoords[i].z;
                            sum += lineCoords[i].z;
                        } else {
                            // 超出lineCoords的点赋值为平均值
                            leftCoordsDistinct[i].z = sum / lineCoords.length;
                        }
                    }
                    return gf.createLineString(leftCoordsDistinct);
                } else {
                    // 返回右边线-子线1
                    Coordinate[] rightCoords;
                    if (interGeomList.size() == 3) {
                        rightCoords = interGeomList.get(1).getCoordinates();
                    } else { // interGeomList.size() == 2
                        rightCoords = interGeomList.get(0).getCoordinates();
                    }
                    // 按照距离去除重复点
                    Coordinate[] rightCoordsDistinct = geometryManager.getDistinctCoords(rightCoords, 0.05);
                    // 反转rightCoordsDistinct
                    Coordinate[] rightCoordsDistinctReverse = new Coordinate[rightCoordsDistinct.length];
                    for (int i = 0; i < rightCoordsDistinct.length; i++) {
                        rightCoordsDistinctReverse[i] = rightCoordsDistinct[rightCoordsDistinct.length - 1 - i];
                    }
                    // 根据lineCoords给rightCoordsDistinctReverse赋z值
                    double sum = 0;
                    for (int i = 0; i < rightCoordsDistinctReverse.length; i++) {
                        if (i < lineCoords.length) {
                            rightCoordsDistinctReverse[i].z = lineCoords[i].z;
                            sum += lineCoords[i].z;
                        } else {
                            // 超出lineCoords的点赋值为平均值
                            rightCoordsDistinctReverse[i].z = sum / lineCoords.length;
                        }
                    }
                    return gf.createLineString(rightCoordsDistinctReverse);
                }
            case GETRANDOMID:
                // 随机生成11位数
                return RandomStringUtils.randomNumeric(11);
            case TEMPMEMO:  // TODO 临时兼容老版本解析MEMO字段
                sourceColName = colValMapping.getSourceColName();
                sourceColValue = feature.getAttribute(sourceColName);
                if (sourceColValue == null) {
                    return null;
                }
                String valStr = sourceColValue.toString();
                if (StringUtils.isBlank(valStr) || "null".equals(valStr)) {
                    return null;
                }
                String jsonStr = JsonUtil.wrapperJson(valStr);
                // String转json
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                return jsonObject.getString("LANEIDS");
            case TEMPLLMIDS:
                // 优先取IBD_OBJECT_OTHER_POLYLINE
                id = feature.getAttribute(colValMapping.getSourceColName()).toString();
                filter = ECQL.toFilter("LANE_LINK_PID = " + id);
                collection = ds.getFeatureSource("IBD_OBJECT_OTHER_POLYLINE").getFeatures(filter);
                relFeatures = Stream.of(collection.toArray(new SimpleFeature[0])).collect(Collectors.toList());
                if (!relFeatures.isEmpty()) {
                    filterList = relFeatures.stream().filter(f -> f.getAttribute("TYPE").toString().equals("2")).collect(Collectors.toList());
                    if (!filterList.isEmpty()) {
                        return filterList.stream().map(f -> f.getAttribute("OBJECT_PID").toString()).collect(Collectors.joining("|"));
                    }
                }
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                if (relFeatures.isEmpty()) {
                    return null;
                }
                filterList = relFeatures.stream().filter(f -> f.getAttribute("SIDE").toString().equals("2")).collect(Collectors.toList());
                // |分割的字符串
                return filterList.stream().map(f -> f.getAttribute("LANE_BOUNDARY_PID").toString()).collect(Collectors.joining("|"));
            case TEMPRLMIDS:
                // 优先取IBD_OBJECT_OTHER_POLYLINE
                id = feature.getAttribute(colValMapping.getSourceColName()).toString();
                filter = ECQL.toFilter("LANE_LINK_PID = " + id);
                collection = ds.getFeatureSource("IBD_OBJECT_OTHER_POLYLINE").getFeatures(filter);
                relFeatures = Stream.of(collection.toArray(new SimpleFeature[0])).collect(Collectors.toList());
                if (!relFeatures.isEmpty()) {
                    filterList = relFeatures.stream().filter(f -> f.getAttribute("TYPE").toString().equals("1")).collect(Collectors.toList());
                    if (!filterList.isEmpty()) {
                        return filterList.stream().map(f -> f.getAttribute("OBJECT_PID").toString()).collect(Collectors.joining("|"));
                    }
                }
                // 关联的Feature(List)
                relFeatures = getEqualRelFeatures(colValMapping, ds, feature);
                if (relFeatures.isEmpty()) {
                    return null;
                }
                filterList = relFeatures.stream().filter(f -> f.getAttribute("SIDE").toString().equals("1")).collect(Collectors.toList());
                // |分割的字符串
                return filterList.stream().map(f -> f.getAttribute("LANE_BOUNDARY_PID").toString()).collect(Collectors.joining("|"));
            case TEMPLLMIDS1:
                // 优先取IBD_OBJECT_OTHER_POLYLINE
                id = feature.getAttribute("LANE_LINK_PID").toString();
                filter = ECQL.toFilter("LANE_LINK_PID = " + id);
                collection = ds.getFeatureSource("IBD_OBJECT_OTHER_POLYLINE").getFeatures(filter);
                relFeatures = Stream.of(collection.toArray(new SimpleFeature[0])).collect(Collectors.toList());
                if (!relFeatures.isEmpty()) {
                    filterList = relFeatures.stream().filter(f -> f.getAttribute("TYPE").toString().equals("2")).collect(Collectors.toList());
                    if (!filterList.isEmpty()) {
                        return filterList.stream().map(f -> f.getAttribute("OBJECT_PID").toString()).collect(Collectors.joining("|"));
                    }
                }
                // 找不到，则直接映射
                sourceColName = colValMapping.getSourceColName();
                return feature.getAttribute(sourceColName);
            case TEMPRLMIDS1:
                // 优先取IBD_OBJECT_OTHER_POLYLINE
                id = feature.getAttribute("LANE_LINK_PID").toString();
                filter = ECQL.toFilter("LANE_LINK_PID = " + id);
                collection = ds.getFeatureSource("IBD_OBJECT_OTHER_POLYLINE").getFeatures(filter);
                relFeatures = Stream.of(collection.toArray(new SimpleFeature[0])).collect(Collectors.toList());
                if (!relFeatures.isEmpty()) {
                    filterList = relFeatures.stream().filter(f -> f.getAttribute("TYPE").toString().equals("1")).collect(Collectors.toList());
                    if (!filterList.isEmpty()) {
                        return filterList.stream().map(f -> f.getAttribute("OBJECT_PID").toString()).collect(Collectors.joining("|"));
                    }
                }
                // 找不到，则直接映射
                sourceColName = colValMapping.getSourceColName();
                return feature.getAttribute(sourceColName);
            default:
                sourceColName = colValMapping.getSourceColName();
                return feature.getAttribute(sourceColName);
        }
    }

    private Object handleValueMapping(Object sourceVal, ColValMapping colValMapping, String srcFeatureId) {
        Map<String, String> valuesMapping = colValMapping.getValuesMapping();
        // 记录错误的初步信息
        String targetVal = valuesMapping.get(sourceVal.toString());
        if (targetVal == null) {
            return null;
        }
        switch (targetVal) {
            case "errLog":
                // 记录错误日志
                return new ErrorLog(srcFeatureId, ErrorLogEnum.NO_MAPPING_VALUE, colValMapping.getSourceColName(), colValMapping.getTargetColName(),
                        colValMapping.getSourceColName(), colValMapping.getTargetColName());
            case "other":
                // 其他，拓展接口
                return null;
            default:
                return targetVal;
        }
    }

    List<SimpleFeature> parseMappingAndBuildFeatures(SimpleFeature[] features, TableMapping tableMapping, DataStore sourceDs, DataStore targetDs, SimpleFeatureType sft, List<ErrorLog> errorLogs) throws IOException {
        List<SimpleFeature> featureList = new ArrayList<>(features.length);
        SimpleFeature flagFeature = null;
        try {
            for (SimpleFeature feature : features) {
                flagFeature = feature;
                //log.info("解析数据，图层：{}，数据：{}", tableMapping.getSourceTableName(), geometryManager.feature2Json(feature));
                featureList.addAll(parseMappingAndBuildFeatures(feature, tableMapping, sourceDs, targetDs, sft, errorLogs));
            }
        } catch (Exception e) {
            assert flagFeature != null;
            String msg = String.format("解析数据时出错，错误图层：%s，错误数据：%s，错误信息：%s", tableMapping.getSourceTableName(), geometryManager.feature2Json(flagFeature), e.getMessage());
            log.error(msg, e);
            throw new DefinitionException(ErrorEnum.SOURCE_DATA_ERROR.getCode(), msg);
        }
        return featureList;
    }

    private List<SimpleFeature> parseMappingAndBuildFeatures(SimpleFeature feature, TableMapping tableMapping, DataStore sourceDs, DataStore targetDs, SimpleFeatureType sft, List<ErrorLog> errorLogs) throws CQLException, IOException, java.text.ParseException, FactoryException, TransformException {
        Map<String, Object> colValMap = new HashMap<>();
        for (ColValMapping colValMapping : tableMapping.getColValMappings()) {
            // 获取源表字段
            String sourceCol = colValMapping.getSourceColName();
            // 获取源表字段值
            Object sourceVal = feature.getAttribute(sourceCol);
            // 如果源表字段值不为空，且取值为null时，跳过
            if (StringUtils.isNotEmpty(sourceCol) && sourceVal == null) {
                continue;
            }
            // 获取目标表字段
            String targetCol = colValMapping.getTargetColName();

            // 根据转换函数名解析为具体的转换
            Object targetVal = getMappingValue(colValMapping, feature, targetDs, sourceDs, tableMapping.getSourceIdColName());
            if (targetVal instanceof ErrorLog) {
                // 记录错误日志
                errorLogs.add((ErrorLog) targetVal);
                continue;
            }
            colValMap.put(targetCol, targetVal);
        }
        // 生成SimpleFeature，存入featureList集合
        return buildFeatures(colValMap, sft, feature);
    }

    private List<SimpleFeature> getEqualRelFeatures(ColValMapping colValMapping, DataStore ds, SimpleFeature feature) throws IOException, CQLException, ParseException {
        // 源表字段名
        String sourceColName = colValMapping.getSourceColName();
        // 关联的表名
        String relTableName = colValMapping.getRelTableName();
        // 关联的表字段名
        List<String> relColNames = colValMapping.getRelColName();
        // 关联表的FeatureCollection
        SimpleFeatureCollection relCollection = ds.getFeatureSource(relTableName).getFeatures();
        // 获取源表的字段值
        Object attriValueObj = feature.getAttribute(sourceColName);
        if (attriValueObj == null) {
            return new ArrayList<>();
        }
        String attriValue = attriValueObj.toString();

        // 获取关联表的feature
        List<SimpleFeature> features = new ArrayList<>();
        for (String relColName : relColNames) {
            if (colValMapping.getIsTargetDs()) {
                // 数据源为目标源，需要对ID进行处理，由长id转为短id
                if (baseTemplate.triggerLongIdToShortId(relColName, attriValueObj)) {
                    //attriValue = IdUtil.longIdToShortId(attriValueObj.toString());
                    attriValue = baseTemplate.getDataPatchService().transId(attriValueObj.toString(), false);
                }
            }
            features.addAll(geometryManager.getEqualFeatures(attriValue, relCollection, relColName));
        }
        return features;
    }

    private List<Object> getRelAttrValues(List<String> relTargetColNames, List<SimpleFeature> relFeatures) {
        // 获取关联表的目标字段值
        List<Object> resList = new ArrayList<>();
        for (String relTargetColName : relTargetColNames) {
            resList.addAll(relFeatures.stream().map(relFeature -> relFeature.getAttribute(relTargetColName)).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        // 根据Object对应的String值进行去重
        resList = resList.stream().map(Object::toString).distinct().collect(Collectors.toList());
        return resList;
    }

}
