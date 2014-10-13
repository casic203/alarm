<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentMenu", "");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="auth.oper.list.title" text="操作列表"/></title>
    <%@include file="/common/s.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'operGrid',
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
	gridFormId: 'operGridForm',
	exportUrl: 'app-oper!exportExcel.do?region=${param.region}'
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
        <article class="m-breadcrumbs"><a href="${ctx}/">首页</a> <div class="m-breadcrumb-divider"></div> <a class="m-current"><spring:message code="auth.oper.list.title" text="操作列表"/></a></article>
      </div>
    </section>
	<!-- end of secondary bar -->

	<%@include file="/menu.jsp"%>

	<!-- start of main -->
    <section id="m-main" class="m-column">

	  <article class="m-module m-width-full">
        <header>
		  <h3 style="width:50%">查询</h3>
		  <a style="float:right;margin-top:5px;margin-right:10px;" href="javascript:$('#operSearch').toggle(200);$('#operSearchIcon').toggleClass('icon-chevron-down');$('#operSearchIcon').toggleClass('icon-chevron-up');void(0);"><i id="operSearchIcon" class="icon-chevron-up"></i></a>
		</header>
        <div id="operSearch" class="m-module-content">

		  <form name="operForm" method="post" action="app-oper.do?region=${param.region}" class="form-inline">
		    <label for="oper_name"><spring:message code="auth.oper.list.search.name" text="名称"/>:</label>
		    <input type="text" id="oper_name" name="filter_LIKES_name" value="${param.filter_LIKES_name}">
			<button class="btn btn-small" onclick="document.operForm.submit()">查询</button>
		  </form>

		</div>
	  </article>

	  <article class="m-module-blank m-width-full">
	    <div style="float:left;">
		  <region:region-permission permission="resc:create">
		  <button class="btn btn-small" onclick="location.href='app-oper!input.do?region=${param.region}'"><spring:message code="core.list.create" text="新建"/></button>
		  </region:region-permission>
		  <region:region-permission permission="resc:delete">
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
        <header><h3><spring:message code="auth.oper.list.title" text="操作列表"/></h3></header>

  <form id="operGridForm" name="operGridForm" method='post' action="app-oper!removeAll.do?region=${param.region}">
    <table id="operGrid" class="m-table table-hover">
      <thead>
        <tr>
          <th width="10" style="text-indent:0px;text-align:center;"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
          <th class="sorting" name="id"><spring:message code="auth.oper.list.id" text="编号"/></th>
          <th class="sorting" name="name"><spring:message code="auth.oper.list.name" text="名称"/></th>
          <th class="sorting" name="mask"><spring:message code="auth.oper.list.mask" text="掩码"/></th>
          <th class="sorting" name="code"><spring:message code="auth.oper.list.code" text="代码"/></th>
          <th width="50">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <s:iterator value="page.result" var="item">
        <tr>
          <td><input type="checkbox" class="selectedItem" name="selectedItem" value="${item.id}"></td>
          <td>${item.id}</td>
          <td>${item.name}</td>
          <td>${item.mask}</td>
          <td>${item.code}</td>
          <td>
			<region:region-permission permission="oper:write" region="${param.region}">
            <a href="app-oper!input.do?id=${item.id}&region=${param.region}"><spring:message code="core.list.edit" text="编辑"/></a>&nbsp;
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
