<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="negocio.Evidencia" %>

<head>
	<title>Winionline | Ver Evidencias Disputa</title>
</head>
<t:layout>
	<jsp:body>
		<div style="margin-top:20px" class="text-center">
		<p class="h3" style="color:white">Evidencias presentadas para disputa</p>
		<p style="color:white">Jugador: ${jugador}</p>
		<p style="color:white">Fecha Partido: ${fecha_partido}</p>
		<p style="color:white">Resultado: ${resultado_partido} (vs.${rival})</p>
		<p style="color:white">Vencimiento: ${vencimiento}</p>
		<p>
			<a class="btn btn-success" href="#">Votar por Jugador</a>
			<a class="btn btn-danger" style="margin-left:20px" href="#">Volver</a>
		</p>
		
		<c:if test="${count > 0}">
		<c:forEach items="${evidencias}" var="evidencia" varStatus="index">
		<p class="h2" style="color:white; margin-top:20px;">Evidencia nº ${index.index} - <fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm:ss" value="${evidencia.getFecha()}"/></p>
		<c:if test="${evidencia.getTipo() == Evidencia.IMAGEN}">
			<img src="showEvidencia?path=${evidencia.getPath()}" />
		</c:if>
		<c:if test="${evidencia.getTipo() == Evidencia.VIDEO}">
			 <iframe width="560" height="315" src="${evidencia.getLink()}" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
		</c:if>
		</c:forEach>
		</c:if>
		<c:if test="${count == 0}">
			<p class="h3" style="color:white; margin-top:20px">No hay evidencias cargadas para mostrar</p>
		</c:if>
		</div>
	</jsp:body>
</t:layout>