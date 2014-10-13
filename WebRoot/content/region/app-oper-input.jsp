<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.oper.input.title" text="编辑操作"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
$(function() {
    $("#operForm").validate({
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
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.oper.input.title" text="编辑操作"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

      <article class="m-module m-width-full">
        <header><h3><spring:message code="auth.oper.input.title" text="编辑操作"/></h3></header>

		<div class="m-spacer"></div>

<form id="operForm" method="post" action="app-oper!save.do?operationMode=STORE&region=${param.region}" class="form-horizontal">
  <s:if test="model != null">
  <input id="oper_id" type="hidden" name="id" value="${model.id}">
  </s:if>
  <div class="control-group">
	<label class="control-label" for="oper_name"><spring:message code='auth.oper.input.name' text='名称'/></label>
    <div class="controls">
      <input id="oper_name" type="text" name="name" value="${model.name}" size="40" class="text required" minlength="2" maxlength="50">
    </div>
  </div>
  <div class="control-group">
	<label class="control-label" for="oper_mask"><spring:message code='auth.oper.input.mask' text='掩码'/></label>
    <div class="controls">
      <input id="oper_mask" type="text" name="mask" value="${model.mask}" size="40" class="text required" minlength="1" maxlength="10">
    </div>
  </div>
  <div class="control-group">
	<label class="control-label" for="oper_code"><spring:message code='auth.oper.input.code' text='代码'/></label>
    <div class="controls">
      <input id="oper_code" type="text" name="code" value="${model.code}" size="40" class="text required" minlength="1" maxlength="10">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="oper_descn"><spring:message code='auth.oper.input.description' text='描述'/></label>
    <div class="controls">
      <textarea id="oper_descn" name="descn" maxlength="60" rows="4">${model.descn}</textarea>
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
