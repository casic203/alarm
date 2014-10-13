<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="zh_CN">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.access.input.title" text="编辑访问"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
$(function() {
    $("#accessForm").validate({
        submitHandler: function(form) {
			bootbox.animate(false);
			var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
            form.submit();
        },
        errorClass: 'validate-error'
    });
})
    </script>
  </head>

  <body>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
	  <%@include file="/common/layout/info.jsp"%>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.access.input.title" text="编辑访问"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

      <article class="m-module m-width-full">
        <header><h3><spring:message code="auth.access.input.title" text="编辑访问"/></h3></header>

		<div class="m-spacer"></div>

<form id="accessForm" method="post" action="app-access!save.do?operationMode=STORE" class="form-horizontal">
  <input type="hidden" name="region" value="${param.region}">
  <s:if test="model != null">
  <input id="access_id" type="hidden" name="id" value="${model.id}">
  </s:if>
  <div class="control-group">
	<label class="control-label" for="access_type"><spring:message code='auth.access.input.type' text='类型'/></label>
    <div class="controls">
	  <select id="access_type" name="type">
	    <option value="URL" ${model.type == 'URL' ? selected : ''}>URL</option>
	    <option value="METHOD" ${model.type == 'METHOD' ? selected : ''}>METHOD</option>
	  </select>
    </div>
  </div>
  <div class="control-group">
	<label class="control-label" for="access_value"><spring:message code='auth.access.input.value' text='资源'/></label>
    <div class="controls">
      <input id="access_value" type="text" name="value" value="${model.value}" size="40" class="text required" minlength="2" maxlength="200">
    </div>
  </div>
  <div class="control-group">
	<label class="control-label" for="access_perm"><spring:message code='auth.access.input.perm' text='权限'/></label>
    <div class="controls">
	  <select id="access_perm" name="permId">
	    <s:iterator value="perms" var="item">
	    <option value="${item.id}" ${model.perm.id==item.id ? 'selected' : ''}>${item.name}</option>
		</s:iterator>
	  </select>
    </div>
  </div>
  <div class="control-group">
	<label class="control-label" for="access_priority"><spring:message code='auth.access.input.priority' text='排序'/></label>
    <div class="controls">
      <input id="access_priority" type="text" name="priority" value="${model.priority}" size="40" class="text required number" minlength="1" maxlength="10">
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
