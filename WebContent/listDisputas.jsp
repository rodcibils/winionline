<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="negocio.Evidencia" %>
<head>
	<title>Winionline | Votar en Disputas</title>
</head>
<t:layout>
	<jsp:body>
		
		<c:if test="${vote_success != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Voto registrado</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${vote_success}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
	
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre o apodo de usuario...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "listDisputas?search=" + toSearch;
			}
		</script>
		<c:if test="${count > 0}">
			<table class="table table-hover table-dark" style="margin-top:20px">
			<thead>
				<tr>
				<th scope="col">Jugador Uno</th>
				<th scope="col">Jugador Dos</th>
				<th scope="col">Fecha Partido</th>
				<th scope="col">Resultado</th>
				<th scope="col">Vencimiento Disputa</th>
				<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${disputas}" var="disputa">
					<tr>
						<td><p>${disputa.getPartido().getResultadoUno().getJugador().getNombre()} - ${disputa.getPartido().getResultadoUno().getJugador().getApodo()}</p></td>
						<td><p>${disputa.getPartido().getResultadoDos().getJugador().getNombre()} - ${disputa.getPartido().getResultadoDos().getJugador().getApodo()}</p></td>
						<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getPartido().getFecha()}"/></p></td>
						<td><p>${disputa.getPartido().getResultadoUno().getGoles()} - ${disputa.getPartido().getResultadoDos().getGoles()}</p></td>
						<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getVencimiento()}"/></p></td>
						<td>
							<a class="btn btn-primary" href="evidencia?id=${disputa.getPartido().getId()}&jugador=${disputa.getPartido().getResultadoUno().getJugador().getId()}">Evidencia Jugador Uno</a>
							<a class="btn btn-primary" style="margin-left:20px" href="evidencia?id=${disputa.getPartido().getId()}&jugador=${disputa.getPartido().getResultadoDos().getJugador().getId()}">Evidencia Jugador Dos</a>
							<a class="btn btn-success" style="margin-left:20px" href="listDisputas?vote=${disputa.getPartido().getId()}&jugador=${disputa.getPartido().getResultadoUno().getJugador().getId()}">Votar Jugador Uno</a>
							<a class="btn btn-success" style="margin-left:20px" href="listDisputas?vote=${disputa.getPartido().getId()}&jugador=${disputa.getPartido().getResultadoDos().getJugador().getId()}">Votar Jugador Dos</a>
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
					<a class="page-link" href="listDisputas?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="listDisputas?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="listDisputas?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay disputas que mostrar</p>
		</c:if>
	</jsp:body>
</t:layout>