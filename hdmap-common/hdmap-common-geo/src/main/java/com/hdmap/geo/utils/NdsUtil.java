package com.hdmap.geo.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class NdsUtil {

    public final static Long OFFSET = 4294967296L;

    public final static Long NDS_180_DEGREES = (Long)1L << 31;

    public final static Long NDS_360_DEGREES = (Long)1L << 32;

    /**
     * 获取NDS网格
     * @param x
     * @param y
     * @param level
     * @return
     */
    public Mesh getMesh(double x, double y, Integer level){
        try {
            long xValue = Double.valueOf(x * OFFSET / 360).longValue();
            long yValue = Double.valueOf(y * OFFSET / 360).longValue();

            String[] a1 = new String[32];
            for(int i = 31;i >= 0; i--){
                a1[i] = String.valueOf(xValue >> i & 1);
            }
            String[] a2 = new String[31];
            for(int i = 30;i >= 0; i--){
                a2[i] = String.valueOf(yValue >> i & 1);
            }

            //组成Morton code
            String[] a = new String[63];
            a[0] = a1[31];
            int b = 1;
            for(int i = 30; i >= 0; i--){
                a[b] = a2[i];
                a[b + 1] = a1[i];
                b += 2;
            }

            String[] array = new String[16 + level + 1];
            array[0] = "1";

            int levelNum = 16 - level - 1;
            for(int i = 0; i < levelNum; i++){
                array[i + 1] = "0";
            }
            for(int i = 0; i < 2 * level + 1; i++){
                array[levelNum + 1 + i] = a[i];
            }
            Integer meshId = Integer.parseInt(String.join("", array), 2);

            return new Mesh(meshId, level);
        }catch (Exception e){
            log.error("获取mesh异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取经纬度坐标对应网格的行列
     * @param lon
     * @param lat
     * @param levelId
     * @return
     */
    public static Map<String, Integer> convertXyToRowCol(double lon, double lat, Integer levelId){
        Map<String, Integer> map = new HashMap<>();
        try {
            long x = Double.valueOf(lon * OFFSET / 360).longValue();
            long y = Double.valueOf(lat * OFFSET / 360).longValue();
            if(x < 0){
                x += NDS_360_DEGREES;
            }
            if(y < 0){
                y += NDS_180_DEGREES;
            }
            int level = 31 - levelId;
            int col = new Long(x >> level).intValue();
            int row = new Long(y >> level).intValue();
            map.put("col", col);
            map.put("row", row);
        }catch (Exception e){
            log.error("获取经纬度坐标对应网格的行列异常，异常信息为：", e);
        }
        return map;
    }

    /**
     * 获取网格的行列
     * @param meshId
     * @return
     */
    public static Map<String, Integer> convertMeshToRowCol(Integer meshId){
        try {
            return convertMortonCodeToRowCol(getMortonCode(meshId));
        }catch (Exception e){
            log.error("获取网格的行列异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取网格等级
     * @param meshId
     * @return
     */
    public static Integer getMeshLevel(Integer meshId){
        try {
            String[] array = Integer.toBinaryString(meshId).split("");
            return array.length - 17;
        }catch (Exception e){
            log.error("获取level异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取MortonCode和等级
     * @param meshId
     * @return
     */
    public static Integer getMortonCode(Integer meshId){
        try {
            String[] array = Integer.toBinaryString(meshId).split("");
            Integer level = getMeshLevel(meshId);
            if(Objects.nonNull(level)){
                int start = 16 - level;
                String[] arr = new String[array.length - start];
                for(int i = 0; i < arr.length; i++){
                    arr[i] = array[start + i];
                }
                return Integer.parseInt(String.join("", arr), 2);
            }
        }catch (Exception e){
            log.error("获取MortonCode异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取MortonCode对应网格的行列
     * @param mortonCode
     * @return
     */
    public static Map<String, Integer> convertMortonCodeToRowCol(Integer mortonCode){
        Map<String, Integer> map = new HashMap<>();
        try {
            int bit = 1;
            Integer col = 0;
            Integer row = 0;
            for(int i = 0; i < 32; i++){
                col |= ((mortonCode & bit) >> (i * 2) << i);
                bit <<= 1;
                row |= ((mortonCode & bit) >> (i * 2 + 1) << i);
                bit <<= 1;
            }
            map.put("col", col);
            map.put("row", row);
        }catch (Exception e){
            log.error("获取经纬度坐标对应网格的行列异常，异常信息为：", e);
        }
        return map;
    }

    /**
     * 获取行列对应网格的Morton code
     * @param col
     * @param row
     * @return
     */
    public static Integer convertRowColToMortonCode(Integer col, Integer row){
        try {
            long bit = 1L;
            long mortonCode = 0L;
            if(row < 0){
                row += 0x7FFFFFFF;
            }
            row <<= 1;
            for(int i = 0; i < 32; i++){
                mortonCode |= (col & bit);
                col <<= 1;
                bit <<= 1;
                mortonCode |= (row & bit);
                row <<= 1;
                bit <<= 1;
            }
            return new Long(mortonCode).intValue();
        }catch (Exception e){
            log.error("获取行列对应网格的Morton code异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取行列对应网格
     * @param col
     * @param row
     * @param level
     * @return
     */
    public static Integer convertRowColToMesh(Integer col, Integer row, Integer level){
        try {
            Integer mortonCode = convertRowColToMortonCode(col, row);
            return convertMortonCodeToMesh(mortonCode, level);
        }catch (Exception e){
            log.error("获取行列对应网格异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * MortonCode获取网格
     * @param mortonCode
     * @param level
     * @return
     */
    public static Integer convertMortonCodeToMesh(Integer mortonCode, Integer level){
        try {
            String[] array = Integer.toBinaryString(mortonCode).split("");
            int num = 16 - level;
            String[] arr = new String[33 - num];
            arr[0] = "1";
            int size = arr.length - array.length;
            for(int i = 1; i < size; i++){
                arr[i] = "0";
            }
            for(int i = 0; i < array.length; i++){
                arr[size + i] = array[i];
            }
            return Integer.parseInt(String.join("", arr), 2);
        }catch (Exception e){
            log.error("MortonCode获取网格异常，异常信息为：", e);
        }
        return null;
    }

    /**
     * 获取九宫格
     * @param meshId
     * @return
     */
    public static List<Integer> getNearbyMesh(Integer meshId){
        List<Integer> list = new ArrayList<>();
        try {
            Map<String, Integer> map = convertMeshToRowCol(meshId);
            Integer level = getMeshLevel(meshId);
            if(!(map == null || map.isEmpty())){
                Integer col = map.get("col");
                Integer row = map.get("row");

                for(int i = -1; i <= 1; i++){
                    for(int j = -1; j <= 1; j++){
                        Integer mesh = convertRowColToMesh(col + i, row + j, level);
                        if (Objects.nonNull(mesh)) {
                            list.add(mesh);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("获取九宫格异常，异常信息为：", e);
        }
        return list;
    }

    class Mesh{
        private Integer meshId;

        private Integer levelId;

        public Mesh(Integer meshId, Integer levelId) {
            this.meshId = meshId;
            this.levelId = levelId;
        }

        public Integer getMeshId() {
            return meshId;
        }

        public void setMeshId(Integer meshId) {
            this.meshId = meshId;
        }

        public Integer getLevelId() {
            return levelId;
        }

        public void setLevelId(Integer levelId) {
            this.levelId = levelId;
        }
    }
}
