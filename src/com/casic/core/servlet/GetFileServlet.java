 package com.casic.core.servlet;
 
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.IOException;
 import java.net.URLDecoder;
 import javax.servlet.ServletException;
 import javax.servlet.ServletOutputStream;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 
 public class GetFileServlet extends HttpServlet
 {
   public static final int DEBAULT_BUFFER_SIZE = 1024;
   private static final long serialVersionUID = 0L;
 
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     String requestUri = request.getRequestURI();
     requestUri = requestUri.substring(request.getContextPath().length());
     requestUri = requestUri.substring("/userfiles".length());
 
     File file = new File("/home/ckfinder/userfiles/" + URLDecoder.decode(requestUri, "UTF-8"));
 
     FileInputStream fis = null;
     try
     {
       fis = new FileInputStream(file);
 
       byte[] b = new byte[1024];
       int len = 0;
 
       while ((len = fis.read(b, 0, 1024)) != -1)
         response.getOutputStream().write(b, 0, len);
     }
     finally {
       if (fis != null)
         fis.close();
     }
   }
 }

