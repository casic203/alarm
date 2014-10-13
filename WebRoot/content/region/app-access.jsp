<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.access.list.title" text="资源权限"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'accessGrid',
    pageNo: ${page.pageNo},
    pageSize: ${page.pageSize},
    totalCount: ${page.totalCount},
    resultSize: ${page.resultSize},
    pageCount: ${page.pageCount},
    orderBy: '${page.orderBy == null ? "" : page.orderBy}',
    asc: ${page.asc},
    params: {
        'filter_LIKES_value': '${param.filter_LIKES_value}',
		'region': '${param.region}'
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'accessGridForm',
	exportUrl: 'app-access!exportExcel.do?region=${param.region}'
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
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.access.list.title" text="资源权限"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

	  <article class="m-module m-width-full">
        <header>
		  <h3 style="width:50%">查询</h3>
		  <a style="float:right;margin-top:5px;margin-right:10px;" href="javascript:$('#accessSearch').toggle(200);$('#accessSearchIcon').toggleClass('icon-chevron-down');$('#accessSearchIcon').toggleClass('icon-chevron-up');void(0);"><i id="accessSearchIcon" class="icon-chevron-up"></i></a>
		</header>
        <div id="accessSearch" class="m-module-content">

		  <form name="accessForm" method="post" action="app-access.do?region=${param.region}" class="form-inline">
		    <label for="access_name"><spring:message code="auth.access.list.search.value" text="资源"/>:</label>
		    <input type="text" id="access_value" name="filter_LIKES_value" value="${param.filter_LIKES_value}">
			<button class="btn btn-small" onclick="document.userForm.submit()">查询</button>
		  </form>

		</div>
	  </article>

	  <article class="m-module-blank m-width-full">
	    <div style="float:left;">
		  <region:region-permission permission="app:create">
		  <button class="btn btn-small" onclick="location.href='app-access!input.do?region=${param.region}'"><spring:message code="core.list.create" text="新建"/></button>
		  </region:region-permission>
		  <region:region-permission permission="app:delete">
		  <button class="btn btn-small" onclick="table.removeAll()"><spring:message code="core.list.delete" text="删除"/></button>
		  </region:region-permission>
		  <button class="btn btn-small" onclick="table.exportExcel()">导出</button>
		</div>

		<div class="btn-group m-pagination" style="float:right;">
		  <button class="btn btn-small">&lt;</button>
		  <button class="btn btn-small">1</button>
		  <button class="btn btn-small">&gt;</button>
		</div>

	    <div class="m-clear"></div>
	  </article>

      <article class="m-module m-width-full">
        <header><h3><spring:message code="auth.access.list.title" text="资源权限"/></h3></header>

  <form id="accessGridForm" name="accessGridForm" method='post' action="app-access!removeAll.do?region=${param.region}">
    <table id="accessGrid" class="m-table table-hover">
      <thead>
        <tr>
          <th width="10" style="text-indent:0px;text-align:center;"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
          <th class="sorting" name="id"><spring:message code="auth.access.list.id" text="编号"/></th>
          <th class="sorting" name="type"><spring:message code="auth.access.list.type" text="类型"/></th>
          <th class="sorting" name="value"><spring:message code="auth.access.list.value" text="资源"/></th>
          <th class="sorting" name="perm"><spring:message code="auth.access.list.perm" text="权限"/></th>
          <th class="sorting" name="priority"><spring:message code="auth.access.list.priority" text="排序"/></th>
          <th class="sorting" name="app"><spring:message code="auth.app.list.app" text="应用"/></th>
          <th class="sorting" name="descn"><spring:message code="auth.access.list.descn" text="备注"/></th>
          <th width="50">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <s:iterator value="page.result" var="item">
        <tr>
          <td><input type="checkbox" class="selectedItem" name="selectedItem" value="${item.id}"></td>
          <td>${item.id}</td>
          <td>${item.type}</td>
          <td>${item.value}</td>
          <td>${item.perm.name}</td>
          <td>${item.priority}</td>
          <td>${item.app.name}</td>
          <td>${item.descn}</td>
          <td>
			<region:region-permission permission="app:write">
            <a href="app-access!input.do?id=${item.id}&region=${param.region}"><spring:message code="core.list.edit" text="编辑"/></a>&nbsp;
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
