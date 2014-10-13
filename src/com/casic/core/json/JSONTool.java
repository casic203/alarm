package com.casic.core.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.casic.core.mapper.JsonMapper;

public class JSONTool {
	public static void writeDataResult(Object object) throws IOException {
		JsonMapper jsonMapper = new JsonMapper();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		System.out.println(jsonMapper.toJson(object));
		writer.write(jsonMapper.toJson(object));
		writer.flush();
		writer.close();
	}

	public static void writeMsgResult(boolean result, String msg)
			throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", result);
		map.put("msg", msg);
		writeDataResult(map);
	}
}
