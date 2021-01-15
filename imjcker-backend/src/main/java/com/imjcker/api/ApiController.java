package com.imjcker.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.common.ajax.ApiResult;
import com.imjcker.common.http.HttpClientResult;
import com.imjcker.common.http.HttpClientUtils;
import com.imjcker.domain.ApiInfo;
import com.imjcker.domain.RequestParam;
import com.imjcker.repository.ApiRepository;
import com.imjcker.repository.RequestParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {
    private final ApiRepository apiRepository;
    private final RequestParamRepository requestParamRepository;

    public ApiController(ApiRepository apiRepository, RequestParamRepository requestParamRepository) {
        this.apiRepository = apiRepository;
        this.requestParamRepository = requestParamRepository;
    }

    /**
     * post请求，参数为json字符串
     *
     * @param jsonString json字符串
     * @return 响应
     */
    private static String postJson(String jsonString) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://openapi.tuling123.com/openapi/api/v2");
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        CloseableHttpResponse response = null;

        try {
            StringEntity stringEntity = new StringEntity(jsonString);
            stringEntity.setContentEncoding(StandardCharsets.UTF_8.toString());
            stringEntity.setContentType("application/json");
            post.setEntity(stringEntity);

            response = httpClient.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                JSONObject jsonObject = JSONObject.parseObject(result);//new JSONObject(result); // 将字符串转化为JSONObject类型
                JSONArray ja = jsonObject.getJSONArray("results");
                JSONObject jo = ja.getJSONObject(ja.size() - 1); // 提取出最后一个 text类型的数据-
                jsonObject = jo.getJSONObject("values");
                result = jsonObject.getString("text"); // 去除 value 中的 text数据
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping("/api/{apiId}")
    public ApiResult api(@RequestBody JSONObject jsonObject, @PathVariable int apiId) throws Exception {
        // TODO 获取接口信息,
        ApiInfo apiInfo = apiRepository.findOne(apiId);

        // TODO 获取参数信息
        Example<RequestParam> example = Example.of(RequestParam.builder().apiId(apiId).build());
        List<RequestParam> paramList = requestParamRepository.findAll(example);

        // TODO 参数组装

        // TODO 请求数据
        Map<String, Object> paramMap = jsonObject.getInnerMap();
        HttpClientResult httpClientResult = HttpClientUtils.doGet(apiInfo.getRequestUrl(), paramMap);
        String result = httpClientResult.getContent();
        log.info(result);
        // TODO 解析结果并

        // TODO 后续处理

        // TODO 返回结果

        return ApiResult.getSuccessResult(result);
    }
}
