<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.user.list.title" text="用户列表"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'userGrid',
    pageNo: ${page.pageNo},
    pageSize: ${page.pageSize},
    totalCount: ${page.totalCount},
    resultSize: ${page.resultSize},
    pageCount: ${page.pageCount},
    orderBy: '${page.orderBy == null ? "" : page.orderBy}',
    asc: ${page.asc},
    params: {
        'filter_LIKES_username': '${param.filter_LIKES_username}',
        'filter_EQI_status': '${param.filter_EQI_status}',
		'region': '${param.region}'
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'userGridForm',
	exportUrl: 'app-user!exportExcel.do'
};

var table;

$(function() {
    table = new Table(config);
    table.configPagination('.m-pagination');
    table.configPageInfo('.m-page-info');
});
    </script>
  </head>

  <body>
    <%@include file="/common/layout/header.jsp"%>

	<!-- start of secondary bar -->
    <section id="m-secondary-bar">
	  <%@include file="/common/layout/info.jsp"%>
      <div class="m-breadcrumbs-container">
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.user.list.title" text="用户列表"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

	  <article class="m-module m-width-full">
        <header>
		  <h3 style="width:50%">查询</h3>
		  <a style="float:right;margin-top:5px;margin-right:10px;" href="javascript:$('#userSearch').toggle(200);$('#userSearchIcon').toggleClass('icon-chevron-down');$('#userSearchIcon').toggleClass('icon-chevron-up');void(0);"><i id="userSearchIcon" class="icon-chevron-up"></i></a>
		</header>
        <div id="userSearch" class="m-module-content">

		  <form name="userForm" method="post" action="app-user.do?region=${param.region}" class="form-inline">
		    <label for="user_username"><spring:message code='auth.user.list.search.username' text='账号'/>:</label>
		    <input type="text" id="user_username" name="filter_LIKES_username" value="${param.filter_LIKES_username}">
		    <label for="user_enabled"><spring:message code='auth.user.list.search.status' text='状态'/>:</label>
		    <select id="user_enabled" name="filter_EQI_status">
			  <option value=""></option>
			  <option value="1" ${param.filter_EQI_status == 1 ? 'selected' : ''}><spring:message code='auth.user.list.search.enabled.true' text='启用'/></option>
			  <option value="0" ${param.filter_EQI_status == 0 ? 'selected' : ''}><spring:message code='auth.user.list.search.enabled.false' text='禁用'/></option>
		    </select>
			<button class="btn btn-small" onclick="document.userForm.submit()">查询</button>
		  </form>

		</div>
	  </article>

	  <article class="m-module-blank m-width-full">
	    <div style="float:left;">
		  <button class="btn btn-small" onclick="table.exportExcel()"><spring:message code="core.list.export" text="导出"/></button>
		</div>

		<div class="btn-group m-pagination" style="float:right;">
		  <button class="btn btn-small">&lt;</button>
		  <button class="btn btn-small">1</button>
		  <button class="btn btn-small">&gt;</button>
		</div>

	    <div class="m-clear"></div>
	  </article>

      <article class="m-module m-width-full">
        <header><h3><spring:message code="auth.user.list.title" text="用户列表"/></h3></header>

  <form id="userGridForm" name="userGridForm" method='post' action="app-user!removeAll.do">
    <table id="userGrid" class="m-table table-hover">
      <thead>
        <tr>
          <th width="10" style="text-indent:0px;text-align:center;"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
          <th class="sorting" name="id"><spring:message code="auth.user.list.id" text="编号"/></th>
          <th class="sorting" name="username"><spring:message code="auth.user.list.username" text="账号"/></th>
          <th name="password"><spring:message code="auth.user.list.password" text="密码"/></th>
          <th class="sorting" name="status"><spring:message code="auth.user.list.status" text="状态"/></th>
          <th name="authorities"><spring:message code="auth.user.list.authorities" text="权限"/></th>
          <th width="80">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <s:iterator value="page.result" var="item">
        <tr>
          <td><input type="checkbox" class="selectedItem" name="selectedItem" value="${item.id}"></td>
          <td>${item.id}</td>
          <td>${item.username}</td>
          <td>[protected]</td>
          <td>${item.enabled}</td>
          <td>${item.authorities}</td>
          <td>
			<region:region-permission permission="user:auth" region="${param.region}">
            <a href="app-user-role.do?id=${item.id}&region=${param.region}"><spring:message code="auth.user.list.role" text="设置权限"/></a>
			</region:region-permission>
          </td>
        </tr>
        </s:iterator>
      </tbody>
    </table>
  </form>

      </article>

	  <article class="m-module-blank m-width-full">
		<div class="btn-group m-pagination" style="float:right;">
		  <button class="btn btn-small">&lt;</button>
		  <button class="btn btn-small">1</button>
		  <button class="btn btn-small">&gt;</button>
		</div>

	    <div class="m-clear"></div>
      </article>

      <div class="m-spacer"></div>

    </section>
	<!-- end of main -->

  </body>

</html>
