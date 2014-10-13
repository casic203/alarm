<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.user.role.title" text="设置角色"/></title>
    <%@include file="/common/s.jsp"%>
  </head>

  <body>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
	  <%@include file="/common/layout/info.jsp"%>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.user.role.title" text="设置角色"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

	  <article class="m-module m-width-full">
        <header><h3><spring:message code="user.user.role.title" text="设置角色"/></h3></header>

<form id="userForm2" method="post" action="app-user-role!save.do?operationMode=STORE" class="form-horizontal">
  <input type="hidden" name="id" value="${id}">
  <input type="hidden" name="region" value="${param.region}">
  <div class="control-group">
    <div class="controls">
	  <region:region-role regionPath="|${param.region}">
        <input id="selectedItem-${role.id}" type="checkbox" name="selectedItem" value="${role.id}" <s:if test='%{#action.containsRole(#attr.role.id)}'>checked</s:if>>&nbsp;
        <label for="selectedItem-${role.id}" style="display:inline;">${role.name}</label><br>
      </region:region-role>
    </div>
  </div>
  <div class="control-group">
    <div class="controls">
      <button id="submitButton" class="btn"><spring:message code='core.input.save' text='保存'/></button>
	  &nbsp;
      <button type="button" onclick="history.back();" class="btn"><spring:message code='core.input.back' text='返回'/></button>
    </div>
  </div>
</form>

	  </article>

      <div class="m-spacer"></div>

    </section>
	<!-- end of main -->

  </body>

</html>
