<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Partidos de liga </title>
</head>
<t:layout>
	<jsp:body>		
		<table class="table table-hover table-dark" align="center" style="width:50%; margin-top:40px;">
			<thead>
				<tr>
					<th scope="col" style="text-align:center">Juador uno</th>
					<th scope="col" style="text-align:center">Goles</th>				
					<th scope="col"></th>
					<th scope="col" style="text-align:center">Goles</th>
					<th scope="col" style="text-align:center">Jugador dos</th>		
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${partidosligausuario}" var="plu">
					<tr>
						<td style="text-align:center">${plu.getNombreJugadorUno()}</td>
						<td style="text-align:center">${plu.getGolesJugadorUno()}</td>	
						<td style="text-align:center"> - </td>
						<td style="text-align:center">${plu.getNombreJugadorDos()}</td>
						<td style="text-align:center">${plu.getGolesJugadorDos()}</td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<ul class="pagination justify-content-center">
			<li class="page-item"><button onclick="window.location='estadisticasLiga?id=${idLiga}';return false;" class="btn btn-danger" style="margin:20px 5px 0px 5px">Volver</button></li>
		</ul>
	</jsp:body>
</t:layout>