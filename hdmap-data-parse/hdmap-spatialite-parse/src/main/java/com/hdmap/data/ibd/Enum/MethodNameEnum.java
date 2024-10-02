package com.hdmap.data.ibd.Enum;

import lombok.Getter;

@Getter
public enum MethodNameEnum {
    ONE2ONE("one2One"),
    ONE2NEGATIVEONE("one2NegativeOne"),
    MANY2MANY("many2ManyByValuesMapping"),
    GETCENTERPOINT("getCenterPoint"),
    GETLENGTH("getLength"),
    GETDEFAULTVALUE("getDefaultValue"),
    GETMAXVALUE("getRelAttriMaxValue"),
    GETMINVALUE("getRelAttriMinValue"),
    GETSINGLEVALUE("getRelSingleAttriValue"),
    GETMULTIVALUE("getRelMultiAttriValue"),
    GETRANDOMID("getRandomId"),
    // 高度定制化
    CUSTOM1("IBD_ROADLINK.LINK_PID -> IBD_LINE_FACILITY.ROADIDS -> features(IBD_LINE_FACILITY) -> checkLeft -> road.ll_fids"),
    CUSTOM2("IBD_ROADLINK.LINK_PID -> IBD_LINE_FACILITY.ROADIDS -> features(IBD_LINE_FACILITY) -> !checkLeft -> road.rl_fids"),
    CUSTOM3("IBD_ROADLINK.JCID -> road_node.jc_id"),
    CUSTOM4("IBD_OBJECT_OTHER_POLYGON.OBJECT_PID -> road.jc_id -> preAndSuc -> junction.o_road_ids"),
    CUSTOM5("IBD_LANE_LINK.LINK_PID -> IBD_ROADLINK.JCID -> 1 or 2"),
    GETGEOMBYOFFSET("getGeomByOffset"),

    // 临时
    TEMPMEMO("getLaneIdsFromMemo"),
    TEMPLLMIDS("LANE_LINK_PID -> IBD_LANE_BOUNDARY_REL.side = 2 -> LANE_BOUNDARY_PID"),
    TEMPRLMIDS("LANE_LINK_PID -> IBD_LANE_BOUNDARY_REL.side = 1 -> LANE_BOUNDARY_PID"),
    TEMPLLMIDS1("LANE_LINK_PID -> IBD_OBJECT_OTHER_POLYLINE.OBJECT_ID == LANE_LINK_PID && IBD_LANE_BOUNDARY_REL.TYPE = 2 -> LANE_BOUNDARY_PID || One2One"),
    TEMPRLMIDS1("LANE_LINK_PID -> IBD_OBJECT_OTHER_POLYLINE.OBJECT_ID == LANE_LINK_PID && IBD_LANE_BOUNDARY_REL.TYPE = 1 -> LANE_BOUNDARY_PID || One2One"),
    ;

    private final String name;

    MethodNameEnum(String name) {
        this.name = name;
    }

    public static MethodNameEnum getEnum(String name) {
        for (MethodNameEnum e : MethodNameEnum.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
