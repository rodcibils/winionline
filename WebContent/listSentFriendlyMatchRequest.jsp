<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Solicitudes de Amistosos Enviadas</title>
</head>
<t:layout>
	<jsp:body>
		<table class="table table-hover table-dark">
		<thead>
			<tr>
			<th scope="col">Jugador Rival</th>
			<th scope="col">Fecha</th>
			<th scope="col"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${solicitudes}" var="solicitud">
				<tr>
				<td><p>${solicitud.getJugadorDos().getNombre()} - ${solicitud.getJugadorDos().getApodo()}</p></td>
				<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${solicitud.getFecha()}"/></p></td>
				<td>
					<p class="text-center">
					<a class="btn btn-primary">Ver Perfil</a>
					<a class="btn btn-danger" style="margin-left:20px">Eliminar</a>
					</p>
				</td>
				</tr>
			</c:forEach>
		</tbody>
		</table>
		<nav aria-label="Paginacion">
			<ul class="pagination justify-content-center">
			<c:if test="${skip == 0}">
				<li class="page-item disabled">
				<a class="page-link" href="#" aria-label="Anterior">
					<span aria-hidden="true">&laquo;</span>
				</a>
			</li>
			</c:if>
			<c:if test="${skip > 0}">
				<li class="page-item">
				<a class="page-link" href="sentFriendRequest?skip=${skip-10}" aria-label="Anterior">
					<span aria-hidden="true">&laquo;</span>
				</a>
				</li>
			</c:if>
			<c:forEach begin="0" end="${max_pages-1}" var="index">
			<c:if test="${current_page == index}">
				<li class="page-item active">
					<a class="page-link" href="#">${index+1}<span class="sr-only">(current)</span></a>
				</li>
			</c:if>
			<c:if test="${current_page != index}">
				<li class="page-item"><a class="page-link" href="sentFriendRequest?skip=${index*10}">${index + 1}</a></li>
			</c:if>	
			</c:forEach>
			<c:if test="${current_page == max_pages-1}">
			<li class="page-item disabled">
				<a class="page-link" href="#" aria-label="Siguiente">
					<span aria-hidden="true">&raquo;</span>
				</a>
			</li>
			</c:if>
			<c:if test="${current_page < max_pages-1}">
			<li class="page-item">
				<a class="page-link" href="sentFriendRequest?skip=${skip+10}" aria-label="Siguiente">
					<span aria-hidden="true">&raquo;</span>
				</a>
			</li>
			</c:if>
			</ul>
		</nav>
	</jsp:body>
</t:layout>