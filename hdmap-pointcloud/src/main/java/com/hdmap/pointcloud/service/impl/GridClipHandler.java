package com.hdmap.pointcloud.service.impl;

import com.hdmap.geo.model.Grid;
import com.hdmap.geo.utils.GeoHashUtil;
import com.hdmap.geo.utils.GeometryUtil;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.pointcloud.service.GridClipInterface;
import com.hdmap.pointcloud.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.util.*;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/23 13:31
 * @description: 网格划分模板
 */
@Slf4j
public abstract class GridClipHandler implements GridClipInterface {

    protected GridClipHandler nextHandler;

    public List<GridClipResult> clip(Long identifier, String filePath, int precision) throws Exception {
        List<GridClipResult> resultList = new ArrayList<>();
        // get bbox
        ReferencedEnvelope bbox = getBounds(filePath);

        // if crs is null or not 4326, transform it
        if (ResourceUtil.checkCRSTransform(bbox.getCoordinateReferenceSystem(), GeometryUtil.SRID_WGS84)) {
            bbox = GeometryUtil.transform(bbox, GeometryUtil.SRID_GAUSSIAN_KRUGER, GeometryUtil.SRID_WGS84);
        }
        // get grid from bbox
        List<Grid> gridList = GeoHashUtil.getGeoHashByBBox(bbox, precision);
        for (Grid grid : gridList) {
            Polygon polygon = (Polygon) grid.getGeometry();
            // if the crs of geohash is not 4547, transform it
            if (polygon.getSRID() != GeometryUtil.SRID_GAUSSIAN_KRUGER) {
                int srid = polygon.getSRID() != 0 ? polygon.getSRID() : GeometryUtil.SRID_WGS84;
                polygon = (Polygon) GeometryUtil.transform(polygon, srid, GeometryUtil.SRID_GAUSSIAN_KRUGER);
                grid.setGeometry(polygon);
            }
            GridClipResult result = clipByPolygon(identifier, grid, filePath);
            if (result != null) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    public void setNextHandler(GridClipHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract List<GridClipResult> handleClip(FileTypeEnum fileType, Long identifier, String filePath, int precision) throws Exception;

    public abstract String handleMerge(FileTypeEnum fileType, String identifier, String originCloudPath, String inputPath) throws Exception;

    public abstract ReferencedEnvelope getBounds(String filePath) throws IOException, FactoryException;

    public abstract GridClipResult clipByPolygon(Long identifier, Grid grid, String filePath) throws Exception;

}
