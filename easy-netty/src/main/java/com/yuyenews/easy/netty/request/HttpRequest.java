package com.yuyenews.easy.netty.request;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;

/**
 * 请求对象，对原生netty的request的补充
 * @author yuye
 *
 */
public class HttpRequest {
	
	/**
	 * netty原生request
	 */
	private FullHttpRequest httpRequest;
	
	/**
	 * 请求体
	 */
	private String body;

	/**
	 * 参数
	 */
	private Map<String, Object> paremeters;
	
	/**
	 * 请求的文件
	 */
	private List<File> files;
	
	/**
	 * 构造函数，框架自己用的，程序员用不到，用了也没意义
	 * @param httpRequest
	 */
	public HttpRequest(FullHttpRequest httpRequest) {
		this.body = getBody(httpRequest);
		this.setParemeters(getPams(httpRequest));
		this.httpRequest = httpRequest;
	}
	
	/**
	 * 获取请求方法
	 * @return
	 */
	public HttpMethod getMethod() {
		return httpRequest.method();
	}

	/**
	 * 获取要请求的uri
	 * @return
	 */
	public String getUri() {
		return httpRequest.uri();
	}

	/**
	 * 获取请求的参数集
	 * @return
	 */
	public Map<String, Object> getParemeters() {
		return paremeters;
	}

	/**
	 * 组装请求的参数
	 * @param paremeters
	 */
	private void setParemeters(Map<String, Object> paremeters) {
		Object obj = paremeters.get("files");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) obj;
			this.files = files;
			paremeters.remove("files");
		}

		this.paremeters = paremeters;

	}
	
	/**
	 * 获取单个请求的参数
	 * @param key
	 * @return
	 */
	public Object getParemeter(String key) {
		return paremeters.get(key);
	}

	/**
	 * 获取请求的文件
	 * @return
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * 获取单个请求的文件
	 * @return
	 */
	public File getFile() {
		if (files != null && files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取请求的url
	 * @return
	 */
	public String getUrl() {
		return httpRequest.uri();
	}

	/**
	 * 获取请求的body
	 * @return
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * 获取netty原生request
	 * @return
	 */
	public FullHttpRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * 获取body参数
	 * 
	 * @param request
	 * @return
	 */
	private String getBody(FullHttpRequest request) {
		ByteBuf buf = request.content();
		return buf.toString(CharsetUtil.UTF_8);
	}

	/**
	 * 将GET, POST所有请求参数转换成Map对象
	 * @param request
	 */
	private Map<String, Object> getPams(FullHttpRequest request) {
		try {
			return new RequestParser(request).parse();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return new Hashtable<>();
	}
}
