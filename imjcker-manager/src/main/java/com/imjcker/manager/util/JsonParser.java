package com.imjcker.manager.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private JsonParser() {
    }

    public static List<JsonNode> toTreeList(String jsonString, int pid, int id) {
        List<JsonNode> nodeList = new ArrayList<>();
        JSONObject json = JSONObject.parseObject(jsonString);
        for (String key : json.keySet()) {
            JsonNode node = new JsonNode();
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                node.setPid(pid);
                node.setId(id++);
                node.setName(key);
                node.setType("JSONObject");
//                node.setValue(value.toString());
                node.setMemo(key);
                nodeList.add(node);
                nodeList.addAll(toTreeList(value.toString(), node.getId(), id));
            } else if (value instanceof JSONArray) {
                node.setPid(pid);
                node.setId(id++);
                node.setName(key);
                node.setType("JSONArray");
//                node.setValue(value.toString());
                node.setMemo(key);
                nodeList.add(node);
                JSONArray jsonArray = (JSONArray) value;
                if (jsonArray.size() <= 0) continue;
                if (jsonArray.get(0) instanceof JSONObject) {
                    nodeList.addAll(toTreeList(jsonArray.get(0).toString(), node.getId(), id));
                }
            } else {
                node.setPid(pid);
                node.setId(id++);
                node.setName(key);
                node.setType("String");
//                node.setValue(value.toString());
                node.setMemo(key);
                nodeList.add(node);
            }
        }
        return nodeList;
    }

    @Data
    public static class JsonNode {
        private int id;
        private int pid;
        private String name;
        private String type;
        private String value;
        private String memo;
        private List<JsonNode> children = new ArrayList<>();

    }
}
