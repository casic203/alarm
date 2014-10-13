<%@ page language="java" pageEncoding="UTF-8" %>
	<!-- start of sidebar -->
    <aside id="m-sidebar" class="m-column">
	<region:region-permission permission="system" region="system">
      <h3 class="m-toggle-link ${currentMenu == 'auth' ? 'current' : ''}"><spring:message code="layout.leftmenu.authmanage" text="权限管理"/></h3>
      <ul class="m-toggle ${currentMenu == 'auth' ? 'current' : ''}">
		<region:region-permission permission="user">
		<!--
        <li class="m-icn-view-users"><a href="${ctx}/connector/user-connector.do"><spring:message code="layout.leftmenu.usermanage" text="用户管理"/></a></li>
		-->
        <li class="m-icn-view-users"><a href="${ctx}/auth/user-status.do"><spring:message code="layout.leftmenu.usermanage" text="用户管理"/></a></li>
        <li class="m-icn-view-users"><a href="${ctx}/auth/user-repo.do"><spring:message code="layout.leftmenu.userrepo" text="用户库管理"/></a></li>
		</region:region-permission>
		<region:region-permission permission="role">
		<li class="m-icn-view-users"><a href="${ctx}/auth/role.do"><spring:message code="layout.leftmenu.rolemanage" text="角色管理"/></a></li>
		</region:region-permission>
		<region:region-permission permission="perm">
		<li class="m-icn-view-users"><a href="${ctx}/auth/perm.do"><spring:message code="layout.leftmenu.permmanage" text="授权管理"/></a></li>
		</region:region-permission>
		<region:region-permission permission="oper">
		<li class="m-icn-view-users"><a href="${ctx}/auth/oper.do"><spring:message code="layout.leftmenu.opermanage" text="操作管理"/></a></li>
		</region:region-permission>
		<region:region-permission permission="resc">
		<li class="m-icn-view-users"><a href="${ctx}/auth/resc.do"><spring:message code="layout.leftmenu.rescmanage" text="资源管理"/></a></li>
		<li class="m-icn-view-users"><a href="${ctx}/auth/access.do"><spring:message code="layout.leftmenu.accessmanage" text="访问权限"/></a></li>
		<li class="m-icn-view-users"><a href="${ctx}/auth/app.do"><spring:message code="layout.leftmenu.appmanage" text="应用管理"/></a></li>
		</region:region-permission>
      </ul>
      <h3 class="m-toggle-link ${currentMenu == 'region' ? 'current' : ''}">范围管理</h3>
      <ul class="m-toggle ${currentMenu == 'region' ? 'current' : ''}">
		<li class="m-icn-view-users"><a href="${ctx}/auth/region-type.do">范围类型</a></li>
		<li class="m-icn-view-users"><a href="${ctx}/auth/region.do">范围</a></li>
		<li class="m-icn-view-users"><a href="${ctx}/auth/region-role.do">范围角色</a></li>
      </ul>
	</region:region-permission>

  <region:region entityType="app">
  <region:region-permission permission="*" region="${region.key}">
      <h3 class="m-toggle-link ${param.region == region.key ? 'current' : ''}">${region.entityName}</h3>
      <ul class="m-toggle ${param.region == region.key ? 'current' : ''}">
		<region:region-permission permission="user" region="${region.key}">
		<li class="m-icn-view-users"><a href="${ctx}/region/app-user.do?region=${region.key}">用户管理</a></li>
		</region:region-permission>
		<region:region-permission permission="role" region="${region.key}">
		<li class="m-icn-view-users"><a href="${ctx}/region/app-role.do?region=${region.key}">角色管理</a></li>
		</region:region-permission>
		<region:region-permission permission="perm" region="${region.key}">
		<li class="m-icn-view-users"><a href="${ctx}/region/app-perm.do?region=${region.key}">授权管理</a></li>
		</region:region-permission>
		<li class="m-icn-view-users"><a href="${ctx}/region/app-resc.do?region=${region.key}">资源管理</a></li>
		<li class="m-icn-view-users"><a href="${ctx}/region/app-oper.do?region=${region.key}">操作管理</a></li>
		<li class="m-icn-view-users"><a href="${ctx}/region/app-access.do?region=${region.key}">访问管理</a></li>
      </ul>
  </region:region-permission>
  </region:region>

      <footer>
        <hr style="display:inline-block;" />
        <p><div id="m-footer">&copy; Mossle</div></p>
      </footer>
    </aside>
	<!-- end of sidebar -->
