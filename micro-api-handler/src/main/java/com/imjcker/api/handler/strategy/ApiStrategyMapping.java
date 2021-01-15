package com.imjcker.api.handler.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ApiStrategyMapping {
    COMMON("Common", "CommonImpl"),
    SCYS("E27", "CommonImpl"),
    QBT("E30", "CommonImpl");

    private String group;

    private String serviceName;

    ApiStrategyMapping(String group, String serviceName) {
        this.group = group;
        this.serviceName = serviceName;
    }

    public ApiStrategyMapping getMappingByGroup(String group) {
        for (ApiStrategyMapping api : values()) {
            if (api.getGroup().equalsIgnoreCase(group)) {
                return api;
            }
        }
        return null;
    }

    public String getGroup() {
        return group;
    }

    public String getServiceName() {
        return serviceName;
    }

    private static final Map<String, String> map = new HashMap<>();

    static {
        for (ApiStrategyMapping item : ApiStrategyMapping.values()) {
            map.put(item.getGroup(), item.getServiceName());
        }
    }

    public static String getServiceByGroup(String group) {
        return map.get(group);
    }

    public static Set<String> getGroupList() {
        return map.keySet();
    }

    public static ApiStrategyMapping getEnumOfGroup(String group) {
        ApiStrategyMapping[] values = ApiStrategyMapping.values();
        return Arrays.stream(values)
                .filter(apiStrategyMapping ->
                        group.equals(apiStrategyMapping.getGroup()))
                .findAny().orElseThrow(() -> new RuntimeException("枚举值不存在"));
    }
}
