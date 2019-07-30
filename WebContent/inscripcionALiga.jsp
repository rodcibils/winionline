<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Inscripción a Liga </title>
</head>
<t:layout>
	<jsp:body>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "ligas?search=" + toSearch;
			}
		</script>
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre de liga...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>		
		<c:if test="${count > 0}">
		<table class="table table-hover table-dark">
			<thead>
				<tr>
				<th scope="col">Nombre</th>
				<th scope="col">Temporada</th>
				<th scope="col">Inicio</th>
				<th scope="col">Fin</th>
				<th scope="col">Inscriptos</th>
				<th scope="col"></th>				
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ligas}" var="liga">
					<tr>
					<td>${liga.getNombre()}</td>
					<td>${liga.getTemporada()}</td>
					<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${liga.getInicio()}"/></td>
					<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${liga.getFin()}"/></td>
					<td>${liga.getCantidadInscriptos()}</td>
					<td>
						<p class="text-center">						
 						<a class="btn btn-danger" style="margin-left:20px" href="ligas?inscripcion=${liga.getId()}">Inscribirme</a>
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
					<a class="page-link" href="ligas?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="ligas?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="ligas?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
	</c:if>
	<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay ligas que mostrar</p>
		</c:if>
	</jsp:body>
</t:layout>