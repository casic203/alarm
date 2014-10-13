 package com.casic.core.util;
 
 import java.io.UnsupportedEncodingException;
 import java.net.URLDecoder;
 import java.net.URLEncoder;
 import org.apache.commons.codec.DecoderException;
 import org.apache.commons.codec.binary.Base64;
 import org.apache.commons.codec.binary.Hex;
 
 public class EncodeUtils
 {
   private static final String DEFAULT_URL_ENCODING = "UTF-8";
   private static final int UNPRINTABLE_CHAR_CODE = 16;
   private static final int ANSI_CHAR_CODE = 256;
   private static final int HEX = 16;
   private static final int UNICODE_LENGTH = "\\u0000".length();
 
   private static final int ANSI_LENGTH = "%FF".length();
 
   public static String hexEncode(byte[] input)
   {
     return Hex.encodeHexString(input);
   }
 
   public static byte[] hexDecode(String input)
   {
     try
     {
       return Hex.decodeHex(input.toCharArray());
     } catch (DecoderException e) {
       throw new IllegalStateException("Hex Decoder exception", e);
     }
   }
 
   public static String base64Encode(byte[] input)
   {
     try
     {
       return new String(Base64.encodeBase64(input), "UTF-8");
     } catch (UnsupportedEncodingException e) {
       throw new IllegalArgumentException("cannot encode base64", e);
     }
   }
 
   public static String base64UrlSafeEncode(byte[] input)
   {
     return Base64.encodeBase64URLSafeString(input);
   }
 
   public static byte[] base64Decode(String input)
   {
     return Base64.decodeBase64(input);
   }
 
   public static String urlEncode(String input)
   {
     try
     {
       return URLEncoder.encode(input, "UTF-8");
     } catch (UnsupportedEncodingException e) {
       throw new IllegalArgumentException("Unsupported Encoding Exception", e);
     }
   }
 
   public static String urlDecode(String input)
   {
     try
     {
       return URLDecoder.decode(input, "UTF-8");
     } catch (UnsupportedEncodingException e) {
       throw new IllegalArgumentException("Unsupported Encoding Exception", e);
     }
   }
 
   public static String htmlEscape(String html)
   {
     return StringUtils.escapeHtml(html);
   }
 
   public static String htmlUnescape(String htmlEscaped)
   {
     return StringUtils.unescapeHtml(htmlEscaped);
   }
 
   public static String xmlEscape(String xml)
   {
     return StringUtils.escapeXml(xml);
   }
 
   public static String xmlUnescape(String xmlEscaped)
   {
     return StringUtils.unescapeXml(xmlEscaped);
   }
 
   public static String escapeJS(String src)
   {
     StringBuffer tmp = new StringBuffer();
     tmp.ensureCapacity(src.length() * UNICODE_LENGTH);
 
     for (int i = 0; i < src.length(); i++) {
       char j = src.charAt(i);
 
       if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j)))
       {
         tmp.append(j);
       } else if (j < 'Ā') {
         tmp.append("%");
 
         if (j < '\020') {
           tmp.append("0");
         }
 
         tmp.append(Integer.toString(j, 16));
       } else {
         tmp.append("%u");
         tmp.append(Integer.toString(j, 16));
       }
     }
 
     return tmp.toString();
   }
 
   public static String unescapeJS(String src)
   {
     StringBuffer tmp = new StringBuffer();
     tmp.ensureCapacity(src.length());
 
     int lastPos = 0;
     int pos = 0;
 
     while (lastPos < src.length()) {
       pos = src.indexOf('%', lastPos);
 
       if (pos == lastPos) {
         if (src.charAt(pos + 1) == 'u') {
           char ch = (char)Integer.parseInt(src.substring(pos + 2, pos + UNICODE_LENGTH), 16);
 
           tmp.append(ch);
           lastPos = pos + UNICODE_LENGTH;
         } else {
           char ch = (char)Integer.parseInt(src.substring(pos + 1, pos + ANSI_LENGTH), 16);
 
           tmp.append(ch);
           lastPos = pos + ANSI_LENGTH;
         }
       }
       else if (pos == -1) {
         tmp.append(src.substring(lastPos));
         lastPos = src.length();
       } else {
         tmp.append(src.substring(lastPos, pos));
         lastPos = pos;
       }
 
     }
 
     return tmp.toString();
   }
 }

