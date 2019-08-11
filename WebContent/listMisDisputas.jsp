<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Historial de Amistosos</title>
</head>
<t:layout>
	<jsp:body>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "misDisputas?search=" + toSearch;
			}
		</script>
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre o apodo de usuario...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>
		<c:if test="${count > 0}">
			<table class="table table-hover table-dark">
			<thead>
				<tr>
				<th scope="col">Jugador Rival</th>
				<th scope="col">Fecha Partido</th>
				<th scope="col">Resultado</th>
				<th scope="col">Fecha Disputa</th>
				<th scope="col">Fecha Vencimiento</th>
				<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${disputas}" var="disputa">
					<tr>
					<c:if test="${disputa.getPartido().getResultadoUno().getJugador().getNombre().contentEquals(sessionScope.usuario.getNombre())}">
						<td>${disputa.getPartido().getResultadoDos().getJugador().getNombre()} - ${disputa.getPartido().getResultadoDos().getJugador().getApodo()}</td>
					</c:if>
					<c:if test="${disputa.getPartido().getResultadoDos().getJugador().getNombre().contentEquals(sessionScope.usuario.getNombre())}">
						<td>${disputa.getPartido().getResultadoUno().getJugador().getNombre()} - ${disputa.getPartido().getResultadoUno().getJugador().getApodo()}</td>
					</c:if>
					<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getPartido().getFecha()}"/></p></td>
					<td> 
						<c:if test="${disputa.getPartido().getResultadoUno().getGoles() > disputa.getPartido().getResultadoDos().getGoles()}">
							<p>${disputa.getPartido().getResultadoUno().getGoles()} - ${disputa.getPartido().getResultadoDos().getGoles()} (V)</p>
						</c:if>
						<c:if test="${disputa.getPartido().getResultadoUno().getGoles() < disputa.getPartido().getResultadoDos().getGoles()}">
							<p>${disputa.getPartido().getResultadoUno().getGoles()} - ${disputa.getPartido().getResultadoDos().getGoles()} (D)</p>
						</c:if>
						<c:if test="${disputa.getPartido().getResultadoUno().getGoles() == disputa.getPartido().getResultadoDos().getGoles()}">
							<p>${disputa.getPartido().getResultadoUno().getGoles()} - ${disputa.getPartido().getResultadoDos().getGoles()} (E)</p>
						</c:if>
					</td>
					<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getFecha()}"/></p></td>
					<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getVencimiento()}"/></p></td>
					<td>
						<a class="btn btn-primary" href="cargarEvidenciaDisputa?id=${disputa.getPartido().getId()}">Evidencias</a>
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
					<a class="page-link" href="misDisputas?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="misDisputas?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="misDisputas?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay disputas en curso que mostrar</p>
		</c:if>
	</jsp:body>
</t:layout>