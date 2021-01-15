package com.lemon.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonNodeTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String json = "{\n" +
                "    \"success\":true,\n" +
                "    \"id\":\"WF2018032417140013323701\",\n" +
                "    \"result_desc\":{\n" +
                "        \"AUTHENTICATION_INFOQUERY\":{\n" +
                "            \"MobileAndNameCheck\":{\n" +
                "                \"mobile_name_consistence\":1\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JsonNode root = objectMapper.readTree(json);

        JsonNode node = root.path("result_desc").path("AUTHENTICATION_INFOQUERY").path("MobileAndNameCheck");
        JsonNode node1 = root.at("/result_desc/AUTHENTICATION_INFOQUERY");

        ((ObjectNode) node).put("hello", "world");
        System.out.println(objectMapper.writeValueAsString(root));
        System.out.println(objectMapper.writeValueAsString(node));
        System.out.println(objectMapper.writeValueAsString(node1));

    }
}
