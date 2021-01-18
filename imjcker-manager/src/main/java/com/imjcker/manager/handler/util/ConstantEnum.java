package com.imjcker.api.handler.util;

//import com.lemon.client.util.EnumUtils.IValueProperty;

/**
 * 枚举常量
 */
public class ConstantEnum {


    /**
     * 参数类型
     */
    public static enum ParamsType implements EnumUtils.IValueProperty {
        variable(1), constant(2), json(3), xml(4);

        private int value;

        ParamsType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }


    /**
     * 参数类型
     */
    public static enum ParamsLocation implements EnumUtils.IValueProperty {
        head(1), query(2), body(3);

        private int value;

        ParamsLocation(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    /**
     * 请求类型
     */
    public static enum HttpMethod implements EnumUtils.IValueProperty {
        get(1, "GET"), post(2, "POST");

        private int value;
        private String name;

        HttpMethod(int value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public int getValue() {
            return value;
        }

        public String getName() {
            return this.name;
        }
    }

    /**
     * 协议类型
     */
    public static enum ProtocolType implements EnumUtils.IValueProperty {
        http(1, "HTTP"), https(2, "HTTPS"), socket(3, "SOCKET");

        private int value;
        private String name;

        ProtocolType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static int getValueByName(String name) {
            for (ProtocolType p : values()) {
                if (name.equalsIgnoreCase(p.getName())) {
                    return p.getValue();
                }
            }
            return 0;
        }

        @Override
        public int getValue() {
            return value;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum YesNo implements EnumUtils.IValueProperty {
        yes(1), no(2);

        private int value;

        YesNo(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    public static enum MQType implements EnumUtils.IValueProperty {
        rabbbitmq(1), kafka(2);

        private int value;

        MQType(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }
}
