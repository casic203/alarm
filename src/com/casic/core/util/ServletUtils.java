 package com.casic.core.util;
 
 import java.io.UnsupportedEncodingException;
 import java.util.Enumeration;
 import java.util.Map;
 import java.util.StringTokenizer;
 import java.util.TreeMap;
 import javax.servlet.ServletRequest;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.util.Assert;
 
 public class ServletUtils
 {
   public static final String TEXT_TYPE = "text/plain";
   public static final String JSON_TYPE = "application/json";
   public static final String XML_TYPE = "text/xml";
   public static final String HTML_TYPE = "text/html";
   public static final String JS_TYPE = "text/javascript";
   public static final String EXCEL_TYPE = "application/vnd.ms-excel";
   public static final String AUTHENTICATION_HEADER = "Authorization";
   public static final long ONE_YEAR_SECONDS = 31536000L;
   public static final int MILL_SECONDS = 1000;
 
   public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds)
   {
     response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000L);
 
     response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
   }
 
   public static void setDisableCacheHeader(HttpServletResponse response)
   {
     response.setDateHeader("Expires", 1L);
     response.addHeader("Pragma", "no-cache");
 
     response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
   }
 
   public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate)
   {
     response.setDateHeader("Last-Modified", lastModifiedDate);
   }
 
   public static void setEtag(HttpServletResponse response, String etag)
   {
     response.setHeader("ETag", etag);
   }
 
   public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified)
   {
     long ifModifiedSince = request.getDateHeader("If-Modified-Since");
 
     if ((ifModifiedSince != -1L) && (lastModified < ifModifiedSince + 1000L))
     {
       response.setStatus(304);
 
       return false;
     }
 
     return true;
   }
 
   public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag)
   {
     String headerValue = request.getHeader("If-None-Match");
 
     if (headerValue != null) {
       boolean conditionSatisfied = false;
 
       if (!"*".equals(headerValue)) {
         StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
 
         while ((!conditionSatisfied) && (commaTokenizer.hasMoreTokens())) {
           String currentToken = commaTokenizer.nextToken();
 
           if (currentToken.trim().equals(etag))
             conditionSatisfied = true;
         }
       }
       else {
         conditionSatisfied = true;
       }
 
       if (conditionSatisfied) {
         response.setStatus(304);
         response.setHeader("ETag", etag);
 
         return false;
       }
     }
 
     return true;
   }
 
   public static void setFileDownloadHeader(HttpServletResponse response, String fileName)
   {
     try
     {
       String encodedfileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
 
       response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
     }
     catch (UnsupportedEncodingException e) {
       throw new IllegalArgumentException("cannot set download file name", e);
     }
   }
 
   public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix)
   {
     Assert.notNull(request, "Request must not be null");
 
     Enumeration paramNames = request.getParameterNames();
     Map params = new TreeMap();
 
     String thePrefix = prefix == null ? "" : prefix;
 
     while ((paramNames != null) && (paramNames.hasMoreElements())) {
       String paramName = (String)paramNames.nextElement();
 
       if (("".equals(thePrefix)) || (paramName.startsWith(thePrefix))) {
         String unprefixed = paramName.substring(thePrefix.length());
         String[] values = request.getParameterValues(paramName);
 
         if ((values != null) && (values.length != 0))
         {
           if (values.length > 1)
             params.put(unprefixed, values);
           else {
             params.put(unprefixed, values[0]);
           }
         }
       }
     }
     return params;
   }
 
   public static String encodeHttpBasic(String userName, String password)
   {
     try
     {
       String encode = userName + ":" + password;
 
       return "Basic " + EncodeUtils.base64Encode(encode.getBytes("UTF-8"));
     }
     catch (UnsupportedEncodingException e) {
       throw new IllegalArgumentException("cannot encode http basic", e);
     }
   }
 }

