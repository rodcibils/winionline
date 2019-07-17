<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Winionline | Usuarios </title>
</head>
<t:layout>
	<jsp:body>
	<div style="margin-top:50px; margin-left:50px; margin-right:50px">
		<table class="table table-hover table-dark">
			<thead>
				<tr>
				<th scope="col">ID</th>
				<th scope="col">Apodo</th>
				<th scope="col">Email</th>
				<th scope="col">Pais</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${usuarios}" var="usuario">
					<tr>
					<td><p><c:out value="${usuario.id}" /></p></td>
					<td><c:out value="${usuario.apodo}" /></td>
					<td><c:out value="${usuario.email}" /></td>
					<td><c:out value="${usuario.getPais().getNombre()}" /></td>
					<td>
						<p class="text-center">
						<a class="btn btn-primary">Ver Perfil</a>
						<a class="btn btn-warning" style="margin-left:20px" href="usuarios?desafiar=${usuario.getId()}">Desafiar</a>
						</p>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</jsp:body>
</t:layout>