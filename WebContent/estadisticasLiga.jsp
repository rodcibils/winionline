<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Mis Ligas </title>
</head>
<t:layout>
	<jsp:body>		
		<table class="table table-hover table-dark" style="margin-top:40px">
			<thead>
				<tr>
				<th scope="col">#</th>
				<th scope="col">Juador</th>
				<th scope="col">P</th>				
				<th scope="col">J</th>
				<th scope="col">G</th>
				<th scope="col">E</th>				
				<th scope="col">P</th>
				<th scope="col">GF</th>
				<th scope="col">GC</th>				
				<th scope="col">DG</th>				
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${estadisticasUsuarios}" var="eu">
					<tr>
					<td>${eu.getPos()}</td>
					<td>${eu.getNombre()}</td>	
					<td>${eu.getPartJugados()}</td>
					<td>${eu.getPuntos()}</td>
					<td>${eu.getPartGanados()}</td>	
					<td>${eu.getPartEmpatados()}</td>
					<td>${eu.getPartPerdidos()}</td>
					<td>${eu.getGolesFavor()}</td>	
					<td>${eu.getGolesContra()}</td>
					<td>${eu.getGolesDiferencia()}</td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<ul class="pagination justify-content-center">
			<li class="page-item"><button onclick="window.location='misLigas';return false;" class="btn btn-danger" style="margin:20px 5px 0px 5px">Volver</button></li>
		</ul>
	</jsp:body>
</t:layout>