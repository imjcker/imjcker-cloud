package com.imjcker.manager.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum RequestMappingEnum {

    WJ("E01", "WjImpl"), TD("E02","CommonImpl"), YG("E03","CommonImpl"),
    FG("E04","HouseManagerImpl"), QXB("E05","CommonImpl"), GPW("E06", "GpImpl"),
    JD("E07","JdImpl"), DYGJJ("E08","DyGjjImpl"), SCGS("E09","SiChuanGSRequestImpl"),
    GZGS("E10","GuiZhouGSRequestImpl"),BR("E11","BaiRongRequestImpl"), ZCX("E12", "ZcxImpl"),
    GXD("E13","GxdImpl"), SNSB("E14", "SnsbImpl"),BBD("E15","CommonImpl"),
    BBDCompany("E15-company","BBDCompanyImpl"), BBDIndex("E15-index","CommonImpl"),ICBC("E16","ICBCRequestImpl"),
    DXM("E18","DuXiaoManImpl"),ZLHJ("E19", "CommonImpl"),PZHGJJ("E20","CommonImpl"),GYZFW("E21","GyZfwRequestImpl")
    ,YDRX("E22","CommonImpl"),ZHJK("T01","CombinationRequestImpl");//易得融信

    private String group;

    private String serviceName;

    RequestMappingEnum(String group, String serviceName) {
        this.group = group;
        this.serviceName = serviceName;
    }

    public String getGroup() {
        return group;
    }

//    public void setGroup(String group) {
//        this.group = group;
//    }

    public String getServiceName() {
        return serviceName;
    }

//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }

    private static final Map<String, String> map = new HashMap<>();

    static {
        for (RequestMappingEnum item : RequestMappingEnum.values()) {
            map.put(item.getGroup(), item.getServiceName());
        }
    }

    public static String getServiceByGroup(String group) {
        return map.get(group);
    }

    public static Set<String> getGroupList(){
        return map.keySet();
    }

    public static RequestMappingEnum getEnumOfGroup(String group) {
        RequestMappingEnum[] values = RequestMappingEnum.values();
        return Arrays.stream(values)
                .filter(requestMappingEnum ->
                        group.equals(requestMappingEnum.getGroup()))
                .findAny().orElseThrow(() -> new RuntimeException("枚举值不存在"));
    }
}
