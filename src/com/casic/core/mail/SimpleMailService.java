 package com.casic.core.mail;
 
 import java.net.InetAddress;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
 
 public class SimpleMailService
 {
   private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);
   public static final int MODE_NORMAL = 0;
   public static final int MODE_TEST = 1;
   private JavaMailSender mailSender;
   private String defaultFrom;
   private String defaultTo;
   private String defaultSubject;
   private String defaultContent;
   private int mode = 0;
 
   private String testMail = "demo.mossle@gmail.com";
 
   public void send()
   {
     send(this.defaultFrom, this.defaultTo, this.defaultSubject, this.defaultContent);
   }
 
   public void send(String to)
   {
     send(this.defaultFrom, to, this.defaultSubject, this.defaultContent);
   }
 
   public void send(String to, String subject, String content)
   {
     send(this.defaultFrom, to, subject, content);
   }
 
   public void send(String from, String to, String subject, String content)
   {
     if (this.mode == 0)
       sendRealMail(from, to, subject, content);
     else if (this.mode == 1)
       sendTestMail(from, to, subject, content);
     else
       logger.warn("unknown mode : {}", Integer.valueOf(this.mode));
   }
 
   protected void sendTestMail(String from, String to, String subject, String content)
   {
     String address = "";
     try
     {
       address = InetAddress.getLocalHost().getHostName() + "/" + InetAddress.getLocalHost().getHostAddress();
     }
     catch (Exception ex) {
       logger.error("", ex);
     }
 
     String decoratedContent = "address : " + address + "\nfrom : " + from + "\nto : " + to + "\nsubject : " + subject + "\ncontent : " + content;
 
     String decoratedSubject = "[test]" + subject;
     String decoratedFrom = address;
 
     logger.info("send mail from {} to {}", decoratedFrom, this.testMail);
     logger.info("subject : {}, content : {}", decoratedSubject, decoratedContent);
 
     sendRealMail(decoratedFrom, this.testMail, decoratedSubject, decoratedContent);
   }
 
   protected void sendRealMail(String from, String to, String subject, String content)
   {
     SimpleMailMessage msg = new SimpleMailMessage();
     msg.setFrom(from);
     msg.setTo(to);
     msg.setSubject(subject);
 
     msg.setText(content);
     try
     {
       this.mailSender.send(msg);
       logger.debug("send mail from {} to {}", from, to);
     } catch (Exception e) {
       logger.error("send mail error", e);
     }
   }
   
   public void setMailSender(JavaMailSender mailSender)
   {
     this.mailSender = mailSender;
   }
 
   public void setDefaultFrom(String defaultFrom)
   {
     this.defaultFrom = defaultFrom;
   }
 
   public void setDefaultTo(String defaultTo)
   {
     this.defaultTo = defaultTo;
   }
 
   public void setDefaultSubject(String defaultSubject)
   {
     this.defaultSubject = defaultSubject;
   }
 
   public void setDefaultContent(String defaultContent)
   {
     this.defaultContent = defaultContent;
   }
 
   public int getMode() {
     return this.mode;
   }
 
   public void setMode(int mode)
   {
     this.mode = mode;
   }
 
   public String getTestMail() {
     return this.testMail;
   }
 
   public void setTestMail(String testMail)
   {
     this.testMail = testMail;
   }
 }

