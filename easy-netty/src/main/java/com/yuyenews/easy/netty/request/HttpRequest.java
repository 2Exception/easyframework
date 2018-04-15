package com.yuyenews.easy.netty.request;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;

/**
 * 请求对象，用于存放 从当前请求中获取的 所需数据
 * @author yuye
 *
 */
public class HttpRequest {

	/**
	 * 请求方法
	 */
	private HttpMethod method;

	/**
	 * 请求的路径后半段
	 */
	private String uri;

	/**
	 * 请求的完整路径
	 */
	private String url;

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

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, Object> getParemeters() {
		return paremeters;
	}

	public void setParemeters(Map<String, Object> paremeters) {
		Object obj = paremeters.get("files");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) obj;
			this.files = files;
			paremeters.remove("files");
		}

		this.paremeters = paremeters;

	}

	public List<File> getFiles() {
		return files;
	}

	public File getFile() {
		if (files != null && files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

}
