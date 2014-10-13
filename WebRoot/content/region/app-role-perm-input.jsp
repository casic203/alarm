<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.role.perm.title" text="设置权限"/></title>
    <%@include file="/common/s.jsp"%>
  </head>

  <body>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
	  <%@include file="/common/layout/info.jsp"%>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.role.perm.title" text="设置权限"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

      <article class="m-module m-width-full">
        <header><h3><spring:message code="auth.role.perm.title" text="设置权限"/></h3></header>

		<div class="m-spacer"></div>

<form id="roleForm" method="post" action="app-role-perm!save.do?operationMode=STORE&region=${param.region}" class="form-horizontal">
  <input type="hidden" name="id" value="${id}">
  <div class="control-group">
    <div class="controls">
      <s:iterator value="perms">
        <input id="selectedItem-${id}" type="checkbox" name="selectedItem" value="${id}" <s:if test='#action.selectedItem.contains(id)'>checked</s:if>>&nbsp;
        <label for="selectedItem-${id}" style="display:inline;">${name}</label><br>
      </s:iterator>
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
