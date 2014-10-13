<%@page import="org.jasig.cas.client.validation.Assertion"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
    Assertion assertion = (Assertion)session.getAttribute("_const_cas_assertion_");
    String userName = "";
    if(null != assertion) {
        userName = assertion.getPrincipal().getName();
    }
%>
<div class="head-img">
    <span class="left-img"><img src="${ctx}/images/banner_L_img.png" style=" height:48px"/></span> 
   
    <span class="right-img"><img src="${ctx}/images/banner_R_img.png" style="height:48px"/></span>
</div>
<div class="back-img">
   <img style="height:37px" src="${ctx}/images/banner.png"/>
</div>
<div style="position: absolute; right: 15px; bottom:15px;">
    <span style="margin-right:25px; "><label style="font-weight: bold;">用户名：<%=userName%></label></span> 
    <span style="cursor:pointer"><a href="cas-logout.jsp">注销</a></label>
</div>