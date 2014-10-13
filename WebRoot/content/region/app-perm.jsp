<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.perm.list.title" text="权限列表"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'permGrid',
    pageNo: ${page.pageNo},
    pageSize: ${page.pageSize},
    totalCount: ${page.totalCount},
    resultSize: ${page.resultSize},
    pageCount: ${page.pageCount},
    orderBy: '${page.orderBy == null ? "" : page.orderBy}',
    asc: ${page.asc},
    params: {
        'filter_LIKES_name': '${param.filter_LIKES_name}',
		'region': '${param.region}'
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'permGridForm',
	exportUrl: 'app-perm!exportExcel.do?region=${param.region}'
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
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.perm.list.title" text="权限列表"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

	  <article class="m-module m-width-full">
        <header>
		  <h3 style="width:50%">查询</h3>
		  <a style="float:right;margin-top:5px;margin-right:10px;" href="javascript:$('#permSearch').toggle(200);$('#permSearchIcon').toggleClass('icon-chevron-down');$('#permSearchIcon').toggleClass('icon-chevron-up');void(0);"><i id="permSearchIcon" class="icon-chevron-up"></i></a>
		</header>
        <div id="permSearch" class="m-module-content">

		  <form name="permForm" method="post" action="app-perm.do?region=${param.region}" class="form-inline">
		    <label for="perm_name"><spring:message code="auth.perm.list.search.name" text="名称"/>:</label>
		    <input type="text" id="perm_name" name="filter_LIKES_name" value="${param.filter_LIKES_name}">
			<button class="btn btn-small" onclick="document.permForm.submit()">查询</button>
		  </form>

		</div>
	  </article>

	  <article class="m-module-blank m-width-full">
	    <div style="float:left;">
		  <region:region-permission permission="perm:create">
		  <button class="btn btn-small" onclick="location.href='app-perm!input.do?region=${param.region}'"><spring:message code="core.list.create" text="新建"/></button>
		  </region:region-permission>
		  <region:region-permission permission="perm:delete">
		  <button class="btn btn-small" onclick="table.removeAll()"><spring:message code="core.list.delete" text="删除"/></button>
		  </region:region-permission>
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
        <header><h3><spring:message code="auth.perm.list.title" text="权限列表"/></h3></header>

  <form id="permGridForm" name="permGridForm" method='post' action="app-perm!removeAll.do?region=${param.region}">
    <table id="permGrid" class="m-table table-hover">
      <thead>
        <tr>
          <th width="10" style="text-indent:0px;text-align:center;"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
          <th class="sorting" name="id"><spring:message code="auth.perm.list.id" text="编号"/></th>
          <th class="sorting" name="name"><spring:message code="auth.perm.list.name" text="名称"/></th>
          <th width="50">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <s:iterator value="page.result" var="item">
        <tr>
          <td><input type="checkbox" class="selectedItem" name="selectedItem" value="${item.id}"></td>
          <td>${item.id}</td>
          <td>${item.name}</td>
          <td>
			<region:region-permission permission="perm:write">
            <a href="app-perm!input.do?id=${item.id}&region=${param.region}"><spring:message code="core.list.edit" text="编辑"/></a>
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
