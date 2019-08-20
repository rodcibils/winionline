<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Estadisticas liga </title>
</head>
<t:layout>
	<jsp:body>
		
		<c:if test="${challenge_success != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Partido Solicitado</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${challenge_success}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
	
		<table class="table table-hover table-dark" style="margin-top:40px">
			<thead>
				<tr>
				<th scope="col">#</th>
				<th scope="col">Jugador</th>
				<th scope="col">P</th>				
				<th scope="col">J</th>
				<th scope="col">G</th>
				<th scope="col">E</th>				
				<th scope="col">P</th>
				<th scope="col">GF</th>
				<th scope="col">GC</th>				
				<th scope="col">DG</th>
				<th scope="col"></th>				
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${estadisticasUsuarios}" var="eu" varStatus="index">
					<tr>
					<td>${index.index + 1}</td>
					<td>${eu.getNombre()}</td>
					<td>${eu.getPartJugados()}</td>
					<td>${eu.getPuntos()}</td>
					<td>${eu.getPartGanados()}</td>	
					<td>${eu.getPartEmpatados()}</td>
					<td>${eu.getPartPerdidos()}</td>
					<td>${eu.getGolesFavor()}</td>	
					<td>${eu.getGolesContra()}</td>
					<td>${eu.getGolesDiferencia()}</td>
					<td>
						<a class="btn btn-primary" href="partidosusuarioliga?idliga=${idLiga}&idusuario=${eu.getIdUsuario()}">Ver Partidos</a>
						<c:if test="${liga_terminada == false}">
						<c:if test="${eu.isPuedeJugar() == true && eu.getIdUsuario() != sessionScope.usuario.getId()}">
							<a class="btn btn-success" style="margin-left:20px" href="estadisticasLiga?desafiar=${eu.getIdUsuario()}&id=${id}">Desafiar</a>
						</c:if>
						<c:if test="${eu.isPuedeJugar() == false}">
							<a class="btn btn-success disabled" style="margin-left:20px" href="#">Desafiar</a>
						</c:if>
						</c:if>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<ul class="pagination justify-content-center">
			<li class="page-item"><button onclick="window.location='misLigas';return false;" class="btn btn-danger" style="margin:20px 5px 0px 5px">Volver</button></li>
		</ul>
	</jsp:body>
</t:layout>