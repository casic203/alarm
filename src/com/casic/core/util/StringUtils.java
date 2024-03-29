 package com.casic.core.util;
 
 import java.util.List;
 import org.apache.commons.lang3.StringEscapeUtils;
 
 public class StringUtils
 {
   public static boolean isNotBlank(String text)
   {
     return org.apache.commons.lang3.StringUtils.isNotBlank(text);
   }
 
   public static String capitalize(String text) {
     return org.apache.commons.lang3.StringUtils.capitalize(text);
   }
 
   public static String substring(String text, int offset, int limit) {
     return org.apache.commons.lang3.StringUtils.substring(text, offset, limit);
   }
 
   public static String substringBefore(String text, String token)
   {
     return org.apache.commons.lang3.StringUtils.substringBefore(text, token);
   }
 
   public static String substringAfter(String text, String token)
   {
     return org.apache.commons.lang3.StringUtils.substringAfter(text, token);
   }
 
   public static String[] splitByWholeSeparator(String text, String separator) {
     return org.apache.commons.lang3.StringUtils.splitByWholeSeparator(text, separator);
   }
 
   public static String join(List list, String separator)
   {
     return org.apache.commons.lang3.StringUtils.join(list, separator);
   }
 
   public static String escapeHtml(String text) {
     return StringEscapeUtils.escapeHtml4(text);
   }
 
   public static String unescapeHtml(String text) {
     return StringEscapeUtils.unescapeHtml4(text);
   }
 
   public static String escapeXml(String text) {
     return StringEscapeUtils.escapeXml(text);
   }
 
   public static String unescapeXml(String text) {
     return StringEscapeUtils.unescapeXml(text);
   }
 }

