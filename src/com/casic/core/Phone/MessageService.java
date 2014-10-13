package com.casic.core.Phone;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class MessageService {
	private String uid;
	private String key;
	private String url = WebUrl.GBK.getUrl();
	private String code = "gbk";

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setUrl(WebUrl url) {
		if (url.getUrl().contains("gbk")) {
			code = "gbk";
		}
		if (url.getUrl().contains("utf8")) {
			code = "utf-8";
		}
		this.url = url.getUrl();
	}

	public int send(String smsMob, String smsMsg) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=" + code);// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", uid),
				new NameValuePair("Key", key),
				new NameValuePair("smsMob", smsMob),
				new NameValuePair("smsText", smsMsg) };
		post.setRequestBody(data);

		client.executeMethod(post);
		int statusCode = post.getStatusCode();
		post.releaseConnection();
		return statusCode;
	}

	public void send(ArrayList<MessageEntity> list) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=" + code);
		NameValuePair[] data = new NameValuePair[4];
		data[0] = new NameValuePair("Uid", uid);
		data[1] = new NameValuePair("Key", key);
		for (int i = 0; i < list.size(); i++) {
			data[2] = new NameValuePair("smsMob", list.get(i).getSmsMob());
			data[3] = new NameValuePair("smsText", list.get(i).getSmsMsg());
			post.setRequestBody(data);
			client.executeMethod(post);
			list.get(i).setResult(post.getStatusCode());
		}
		post.releaseConnection();
	}

	public int send(ArrayList<String> smsMobs, String smsMsg)
			throws HttpException, IOException {
		if (smsMobs.size() <= 0) {
			return 0;
		}
		StringBuilder mobs = new StringBuilder();
		for (String string : smsMobs) {
			mobs.append(string).append(",");
		}
		mobs.deleteCharAt(mobs.length());
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=" + code);
		NameValuePair[] data = new NameValuePair[4];
		data[0] = new NameValuePair("Uid", uid);
		data[1] = new NameValuePair("Key", key);
		data[2] = new NameValuePair("smsMob", mobs.toString());
		data[3] = new NameValuePair("smsText", smsMsg);
		post.setRequestBody(data);
		client.executeMethod(post);
		int result = post.getStatusCode();
		post.releaseConnection();
		return result;
	}
}
