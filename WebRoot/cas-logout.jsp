<%
    session.invalidate();
	String url = "http://192.168.10.123:8080/cas/logout?service=" + java.net.URLEncoder.encode("http://192.168.10.124:8080/alarm");
%>
<script>
location.href = "<%=url%>";
</script>