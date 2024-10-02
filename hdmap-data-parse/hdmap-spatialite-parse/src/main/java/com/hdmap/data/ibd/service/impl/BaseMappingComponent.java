package com.hdmap.data.ibd.service.impl;

import com.hdmap.geo.utils.GeometryUtil;
import com.hdmap.patch.service.DataPatchService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2023/12/18 16:25
 * @description: TODO
 */
@Slf4j
@Component
@RefreshScope
@Getter
public class BaseMappingComponent {
    @Value("${default.geom.srid.spatialite:4547}")
    private int defaultSlSrid;
    @Value("${default.geom.srid.postgis:4326}")
    private int defaultPgSrid;
    @Value("${default.id.transfer.switch:true}")
    private boolean defaultIdTransferSwitch;
    @Value("${default.id.transfer.list:road_id,sr_node_id,er_node_id,jc_id,road_ids,i_road_ids,o_road_ids,r_area_id,lane_id,sl_node_id,el_node_id,llm_ids,rlm_ids,l_node_id,lane_ids,lm_id,sl_id,ts_ids,lanef_id,lm_ids,rm_id,ts_id,ss_id,lf_id,r_node_id,rl_fids,ll_fids,vr_id}")
    private List<String> defaultIdTransferList;
    @Value("${default.id.transfer.length:19}")
    private int defaultIdTransferLength;
    @Value("${default.unit:1000}")
    private int defaultUnit;
    @Resource
    private DataPatchService dataPatchService;


    // 生成 SimpleFeature
    SimpleFeature generateSimpleFeature(Map<String, Object> colValMap, SimpleFeatureType schema) throws FactoryException, TransformException, java.text.ParseException {
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
        // 遍历SimpleFeatureType的属性
        List<AttributeType> attributeTypes = schema.getTypes();
        String attributeName;
        Class binding;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (AttributeType attributeType : attributeTypes) {
            attributeName = attributeType.getName().toString();
            binding = attributeType.getBinding();
            Object val = colValMap.get(attributeName);
            // 不能转换的类型，赋默认值
            if (val != null && Number.class.isAssignableFrom(binding) && !NumberUtils.isParsable(val.toString())) {
                val = 0;
            } else if (binding == Timestamp.class) {
                // 当前时间
                val = ts;
            } else if (val instanceof Geometry) {
                // 坐标系转换
                val = GeometryUtil.transform((Geometry) val, defaultSlSrid, defaultPgSrid);
            } else if (binding == String.class && val != null && StringUtils.isBlank(val.toString())) {
                val = null;
            }
            // 长id转短id
            if (triggerLongIdToShortId(attributeName, val)) {
                //val = IdUtil.longIdToShortId(val.toString());
                val = dataPatchService.transId(val.toString(), false);
            }
            featureBuilder.add(val);
        }
        return featureBuilder.buildFeature(null);
    }

    boolean triggerLongIdToShortId(String attributeName, Object val) {
        // 长id转短id
        return defaultIdTransferSwitch && defaultIdTransferList.contains(attributeName) && val != null && val.toString().length() >= defaultIdTransferLength;
    }

}
