package com.imjcker.api.handler.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.DefaultUriTemplateHandler;


/**
 * 操作Http的Template
 */
public class HttpTemplate {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private DefaultUriTemplateHandler uriTemplateHandler;
	private FormHttpMessageConverter messageConverter;
	private SimpleClientHttpRequestFactory clientHttpRequestFactory;

	public HttpTemplate(){

		uriTemplateHandler = new DefaultUriTemplateHandler();
		messageConverter = new FormHttpMessageConverter();
		clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
	}

	public ClientHttpResponse doRequest(String url,ConstantEnum.HttpMethod method,Params params,int timeout) throws IOException, URISyntaxException{

		Map<String, String> uriVariables = params.getUriVariables();		//uri的参数
		Map<String, String> headerVariables = params.getHeaderVariables();	//header中的参数
		Map<String, String> bodyVariables = params.getBodyVariables();		//body中的参数,post方式有

		HttpMethod httpMethod = method.equals(ConstantEnum.HttpMethod.post)?HttpMethod.POST:HttpMethod.GET;

		return doRequest(url, httpMethod , uriVariables, headerVariables, bodyVariables, timeout);
	}

	private ClientHttpResponse doRequest(String url,HttpMethod method,Map<String, String> uriVariables,Map<String, String> headerVariables,Map<String,String> bodyVariables,int timeout) throws IOException, URISyntaxException{

		//处理uri参数
		URI uri = new URI(url);;
		if(uriVariables != null && !uriVariables.isEmpty()){
			StringBuilder builder = new StringBuilder(url);
			builder.append("?");

			for (String name : uriVariables.keySet()) {
				builder.append(String.format("%s={%s}&", name,name));
			}

			uri = uriTemplateHandler.expand(builder.toString(), uriVariables);
		}

		//创建请求
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		ClientHttpRequest request = clientHttpRequestFactory.createRequest(uri, method);

		//处理header参数
		if(headerVariables != null && !headerVariables.isEmpty()){

			HttpHeaders headers = request.getHeaders();
			for (Entry<String, String> entry : headerVariables.entrySet()) {
				headers.add(entry.getKey(), entry.getValue());
			}
		}

		//处理post的body参数
		if(method.equals(HttpMethod.POST) && bodyVariables != null && !bodyVariables.isEmpty()){
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.setAll(bodyVariables);
			messageConverter.write(body , MediaType.APPLICATION_FORM_URLENCODED, request);
		}

		//发送请
		ClientHttpResponse response = request.execute();

		return response;
	}

	public static String getBody(ClientHttpResponse response) throws IOException{

		MediaType contentType = response.getHeaders().getContentType();
		Charset charset = (contentType.getCharset() != null ? contentType.getCharset() : HttpTemplate.DEFAULT_CHARSET);
		return StreamUtils.copyToString(response.getBody(), charset);
	}

	public static class Params{

		private Map<String, String> uriVariables;		//uri的参数
		private Map<String, String> headerVariables;	//header中的参数
		private Map<String, String> bodyVariables;		//body中的参数,post方式有

		public Params(Map<String, String> uriVariables, Map<String, String> headerVariables,Map<String, String> bodyVariables) {
			this.uriVariables = uriVariables;
			this.headerVariables = headerVariables;
			this.bodyVariables = bodyVariables;
		}
		public Map<String, String> getUriVariables() {
			return uriVariables;
		}
		public void setUriVariables(Map<String, String> uriVariables) {
			this.uriVariables = uriVariables;
		}
		public Map<String, String> getHeaderVariables() {
			return headerVariables;
		}
		public void setHeaderVariables(Map<String, String> headerVariables) {
			this.headerVariables = headerVariables;
		}
		public Map<String, String> getBodyVariables() {
			return bodyVariables;
		}
		public void setBodyVariables(Map<String, String> bodyVariables) {
			this.bodyVariables = bodyVariables;
		}
	}
}


