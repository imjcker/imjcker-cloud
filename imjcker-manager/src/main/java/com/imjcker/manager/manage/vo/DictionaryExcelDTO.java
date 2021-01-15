package com.imjcker.manager.manage.vo;

import java.io.Serializable;

/**
 *
 */
public class DictionaryExcelDTO implements Serializable {

    private int level; //层级

    private String keyName; // key名称

    private String type; // 类型

    private String keyConvert; // key转换

    private String drawOut; // 是否抽取, 是-否

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyConvert() {
        return keyConvert;
    }

    public void setKeyConvert(String keyConvert) {
        this.keyConvert = keyConvert;
    }

    public String getDrawOut() {
        return drawOut;
    }

    public void setDrawOut(String drawOut) {
        this.drawOut = drawOut;
    }
}
