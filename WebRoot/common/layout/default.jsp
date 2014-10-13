<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title>crud:title</title>
	<%@include file="/common/s.jsp"%>
  </head>

  <body>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
	  <%@include file="/common/layout/info.jsp"%>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current">&nbsp;</a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/common/layout/left.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">
	</section>
	<!-- end of main -->
  </body>
</html>
