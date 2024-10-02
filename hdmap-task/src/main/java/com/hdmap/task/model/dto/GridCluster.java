package com.hdmap.task.model.dto;

import com.hdmap.geo.model.Grid;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.io.Serializable;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/22 16:55
 * @description: feature 转 point[]
 */
public class GridCluster implements Clusterable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Point coordinates. */
    private final double[] point;

    private final Grid grid;

    public GridCluster(final double[] point, final Grid grid) {
        this.point = point;
        this.grid = grid;
    }

    /**
     * Gets the n-dimensional point.
     *
     * @return the point array
     */
    @Override
    public double[] getPoint() {
        return point;
    }

    public Grid getGeoHashInfo() {
        return grid;
    }
}
