<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%if (session.getAttribute("usuario")!=null){
	request.setAttribute("id_usuario", ((negocio.Usuario)session.getAttribute("usuario")).getId());%>
<t:layout>
<!-- TODO: still pending of programming -->
</t:layout>
<%} else {
	response.sendRedirect("login.jsp");
}%>