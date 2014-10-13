<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="core.login.title" text="登录"/></title>
	<%@include file="/common/s.jsp"%>
  </head>

  <body onload='document.f.j_username.focus();'>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
      <div class="m-user">
        <p>&nbsp;
		</p>
      </div>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="core.login.title" text="登录"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<!-- start of sidebar -->
    <aside id="m-sidebar" class="m-column">
      <footer>
        <hr style="display:inline-block;" />
        <p><div id="m-footer">&copy; Mossle</div></p>
      </footer>
    </aside>

	<!-- start of main -->
    <section id="m-main" class="m-column">
	  <div class="alert m-alert-error" ${param.error==true ? '' : 'style="display:none"'}>
        <strong><spring:message code="core.login.failure" text="登陆失败"/></strong>
		&nbsp;
        ${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}
      </div>

      <article class="m-module m-width-full" style="margin: 10px 3% 0;">
        <header><h3><spring:message code="core.login.title" text="登录"/></h3></header>

		<div class="m-spacer"></div>

<form id="userForm" name="f" method="post" action="${ctx}/j_spring_security_check" class="form-horizontal">
  <div class="control-group">
    <label class="control-label" for="username"><spring:message code="core.login.username" text="账号"/></label>
	<div class="controls">
      <input type='text' id="username" name='j_username' class="text" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}">
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="password"><spring:message code="core.login.password" text="密码"/></label>
	<div class="controls">
      <input type='password' id="password" name='j_password' class="text" value=''>
    </div>
  </div>
  <div class="control-group">
    <div class="controls">
      <input class="btn" name="submit" type="submit" value="<spring:message code='core.login.submit' text='提交'/>"/>
    </div>
  </div>
</form>

      </article>

      <div class="m-spacer"></div>
	</section>
	<!-- end of main -->
  </body>
</html>
