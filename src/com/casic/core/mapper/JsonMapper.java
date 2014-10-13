 package com.casic.core.mapper;
 
 import com.fasterxml.jackson.databind.DeserializationFeature;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.util.JSONPObject;
 import java.io.IOException;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class JsonMapper
 {
   private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
   private ObjectMapper mapper;
 
   public JsonMapper()
   {
     this.mapper = new ObjectMapper();
     this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
   }
 
   public String toJson(Object object) {
     try {
       return this.mapper.writeValueAsString(object);
     } catch (IOException e) {
       logger.warn("write to json string error:" + object, e);
     }
     return null;
   }
 
   public <T> T fromJson(String jsonString, Class<T> clazz)
   {
     if ((jsonString == null) || ("".equals(jsonString.trim()))) {
       return null;
     }
     try
     {
       return this.mapper.readValue(jsonString, clazz);
     } catch (IOException e) {
       logger.warn("parse json string error:" + jsonString, e);
     }
     return null;
   }
 
   public String toJsonP(String functionName, Object object)
   {
     return toJson(new JSONPObject(functionName, object));
   }
 }

