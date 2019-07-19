<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Usuarios </title>
</head>
<t:layout>
	<jsp:body>
			<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "usuarios?search=" + toSearch;
			}
		</script>
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre o apodo de usuario...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>
		<c:if test="${count > 0}">
<!-- 	<div style="margin-top:50px; margin-left:50px; margin-right:50px"> -->
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
<%-- 						<a class="btn btn-warning" style="margin-left:20px" href="usuarios?desafiar=${usuario.getId()}">Desafiar</a> --%>
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
					<a class="page-link" href="usuarios?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="usuarios?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="usuarios?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
<!-- 	</div> -->
	</c:if>
	</jsp:body>
</t:layout>