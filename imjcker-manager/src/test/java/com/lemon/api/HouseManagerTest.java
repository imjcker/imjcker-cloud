package com.lemon.api;

import java.io.IOException;
import java.util.Random;

public class HouseManagerTest {

    public static void main(String[] args) throws IOException {

        Random random = new Random();
        int r = random.nextInt(10) + 1;

        System.out.println(r);
//        for (int i = 0; i < 10; i++) {
//            BasicCookieStore cookieStore = new BasicCookieStore();
//            CloseableHttpClient httpClient = HttpClients.custom()
//                    .setDefaultCookieStore(cookieStore)
//                    .build();
//
//            String url = "http://172.32.3.175:5556/gateway/gateway/gateway/api/debugging";
//
////        String url = "http://172.32.12.244:2295/CCSFspServer/financial/pushSealHouse";
//            HttpPost httpPost = new HttpPost(url);
//
//            String requestBody = "{\"idInfo\":{\"apiId\":649},\"headerList\":[{\"paramName\":\"appKey\",\"paramValue\":\"\"}],\"queryList\":null,\"bodyList\":[{\"paramName\":\"one\",\"paramValue\":\"1\"},{\"paramName\":\"two\",\"paramValue\":\"1\"},{\"paramName\":\"three\",\"paramValue\":\"1\"},{\"paramName\":\"four\",\"paramValue\":\"1\"},{\"paramName\":\"five\",\"paramValue\":\"1\"}]}";
//
//
////        String requestBody = "{\"password\":\"ceshi123456\",\"loginid\":\"BOCDAZY\",\"regioncode\":\"4534534543\",\"data\":{\"condition\":{}}, \"signature\":\"6d21bdd87d38145db31b314d33065462853d4b1e\", \"authenticationKey\":\"Qk9DREFaWTpjZXNoaTEyMzQ1NjoyMDE3MTEyMzE2MjMxMA==\",\"nonce\":\"-762181210\" }";
//
//            HttpEntity stringEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
//            httpPost.setEntity(stringEntity);
//
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//
//            HttpEntity entity = response.getEntity();
//
//            System.out.println(response.getStatusLine());
//
//            System.out.println("status: " + EntityUtils.toString(entity));
//        }

    }


}
