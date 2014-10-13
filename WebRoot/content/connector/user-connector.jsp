<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "auth");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="user.user.list.title" text="用户列表"/></title>
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
        'filter_EQI_status': '${param.filter_EQI_status}'
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'userGridForm',
	exportUrl: 'user-connector!exportExcel.do'
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
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="user.user.list.title" text="用户列表"/></a></article>
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

		  <form name="userForm" method="post" action="user-connector.do" class="form-inline">
		    <label for="user_username"><spring:message code='user.user.list.search.username' text='账号'/>:</label>
		    <input type="text" id="user_username" name="filter_LIKES_username" value="${param.filter_LIKES_username}">
		    <label for="user_enabled"><spring:message code='user.user.list.search.status' text='状态'/>:</label>
		    <select id="user_enabled" name="filter_EQI_status" class="input-mini">
			  <option value=""></option>
			  <option value="1" ${param.filter_EQI_status == 1 ? 'selected' : ''}><spring:message code='user.user.list.search.enabled.true' text='启用'/></option>
			  <option value="0" ${param.filter_EQI_status == 0 ? 'selected' : ''}><spring:message code='user.user.list.search.enabled.false' text='禁用'/></option>
		    </select>
			<button class="btn btn-small" onclick="document.userForm.submit()">查询</button>
		  </form>

		</div>
	  </article>

	  <article class="m-module-blank m-width-full">
	    <s:if test="userRepos.size()>1">
	    <div style="float:left;">
		  <div class="btn-group">
		    <s:iterator value="userRepos" var="item">
		    <button class="btn btn-small ${((empty param.repoCode && item.repoCode == 1) || param.repoCode == item.repoCode) ? 'btn-primary' : ''}" onclick="location.href='user-connector.do?repoCode=${item.repoCode}'">${item.name}</button>
			</s:iterator>
		  </div>
		</div>
		</s:if>

		<div class="btn-group m-pagination" style="float:right;">
		  <button class="btn btn-small">&lt;</button>
		  <button class="btn btn-small">1</button>
		  <button class="btn btn-small">&gt;</button>
		</div>

	    <div class="m-clear"></div>
	  </article>

      <article class="m-module m-width-full">
        <header><h3><spring:message code="user.user.list.title" text="用户列表"/></h3></header>

<form id="userGridForm" name="userGridForm" method='post' action="user-connector!removeAll.do" style="margin:0px;">
  <table id="userGrid" class="m-table table-hover">
    <thead>
      <tr>
        <th class="sorting" name="username"><spring:message code="user.user.list.username" text="账号"/></th>
        <th name="password"><spring:message code="user.user.list.password" text="密码"/></th>
        <th class="sorting" name="status"><spring:message code="user.user.list.status" text="状态"/></th>
        <th name="description"><spring:message code="user.user.list.authorities" text="权限"/></th>
        <th width="150">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <s:iterator value="page.result" var="item">
      <tr>
        <td>${item.username}</td>
        <td>[protected]</td>
        <td>${item.enabled ? '启用' : '禁用'}</td>
        <td>${item.authorities}</td>
        <td>
			<perm:permission permission="user:auth">
            <a href="user-connector!password.do?id=${item.id}"><spring:message code="user.user.list.password" text="设置密码"/></a>
            <a href="javascript:void(0);location.href='user-connector!configRole.do?username=' + encodeURIComponent('${item.username}') + '&repoCode=${empty param.repoCode ? '1' : param.repoCode}'"><spring:message code="user.user.list.role" text="设置权限"/></a>
			</perm:permission>
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
