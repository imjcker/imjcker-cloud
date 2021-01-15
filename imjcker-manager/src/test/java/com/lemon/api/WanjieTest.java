package com.lemon.api;

import com.lemon.common.util.DesUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WanjieTest {

    public static void main(String args[]) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        String authID = "56335328";
//        String authID = "12345678";
//        String url = "http://172.32.12.244:2311/zxsp/enterprise/uQueryEntBaseInfo?appid=tianfu_Bank&desparams=";
        String url = "http://172.32.12.244:2311/zxsp/telphone/telStatusVerify?appid=tianfu_Bank&desparams=";
//        String url = "http://172.32.12.244:2311/zxsp/telphone/telStatusVerify?appid=tianfu_Bank&desparams=ew8YDraMutvD9DZhhtvAd4G7I7hI0rxo3U8ChTzD9xx6Q1eP3u8RNg%3D%3D";

        Map<String, String> requestParam = new LinkedHashMap<>();

        // 银行卡三要素
//        String userName = "谢惠声";
//        String certCode = "441203199811140714";
//        String bankCardNO = "6228481159407711178";
//        requestParam.put("userName", userName);
//        requestParam.put("certCode", certCode);
//        requestParam.put("bankCardNO", bankCardNO);

        //
//        String entityName = "小米科技有限责任公司";
//        requestParam.put("entityName", entityName);
//        requestParam.put("authID", authID);

//        requestParam.put("carNO", "京 N3xx2");
//        requestParam.put("cityCode", "2");
//        requestParam.put("carCode", "");
//        requestParam.put("carDriveCode","");
        requestParam.put("telNO", "18708154351");
        requestParam.put("authID", authID);
        String deparams = DesUtil.getDesparam(authID, requestParam);
        System.out.println("params:" + deparams);
        String wanjieUrl = url + deparams;//"FSWDj7GdOW4nnqsoU3LNtwcy89b4mVOxXDyknO+qStAPfvf7ve5jr0gMIShXObOe3U8ChTzD9xx6Q1eP3u8RNg==";
        System.out.println("status: " + wanjieUrl);

        HttpGet httpGet = new HttpGet(wanjieUrl);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        System.out.println("status: " + response.getStatusLine());
        System.out.println("status: " + EntityUtils.toString(response.getEntity()));
    }
}
