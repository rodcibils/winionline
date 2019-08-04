<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
	<title>Winionline | Registrar Resultado Partido</title>
</head>
<t:layout>
	<jsp:body>
		<form action="registerMatchResult" method="post" style="margin-top:50px">
			<c:if test="${partido.getSolicitud().getLiga() == null}">
			<h2 class="text-center" style="color:white; margin-bottom:30px;">Resultado Partido Amistoso</h2>
			</c:if>
			<div class="card-group justify-content-center">
				<div class="card mb-3 text-right" style="max-width: 540px;">
				  <div class="row no-gutters">
				    <div class="col-md-4">
				      <img src="showAvatar?id_usuario=${partido.getSolicitud().getJugadorUno().getId()}" class="card-img" alt="avatarJugadorUno">
				    </div>
				    <div class="col-md-8">
				      <div class="card-body">
				        <h5 class="card-title">${partido.getSolicitud().getJugadorUno().getNombre()}</h5>
				        <p class="card-text">${partido.getSolicitud().getJugadorUno().getApodo()}</p>
				        <input type="number" maxlength="2" size="2" class="form-control" id="golesUno" name="golesUno" value="${old_guno}" placeholder="Cantidad de goles de ${partido.getSolicitud().getJugadorUno().getNombre()}">
				        <small class="form-text" style="color:red">${err_guno}</small>
				      </div>
				    </div>
				  </div>
				</div>
				<div class="card mb-3" style="max-width: 540px;">
				  <div class="row no-gutters">
				    <div class="col-md-8">
				      <div class="card-body">
				        <h5 class="card-title">${partido.getSolicitud().getJugadorDos().getNombre()}</h5>
				        <p class="card-text">${partido.getSolicitud().getJugadorDos().getApodo()}</p>
				        <input type="number" maxlength="2" size="2" class="form-control" id="golesDos" name="golesDos" value="${old_gdos}" placeholder="Cantidad de goles de ${partido.getSolicitud().getJugadorDos().getNombre()}">
				        <small class="form-text" style="color:red">${err_gdos}</small>
				      </div>
				    </div>
				    <div class="col-md-4">
				      <img src="showAvatar?id_usuario=${partido.getSolicitud().getJugadorDos().getId()}" class="card-img" alt="avatarJugadorDos">
				    </div>
				  </div>
				</div>
			</div>
			<ul class="pagination justify-content-center" style="margin-top:30px">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Aceptar</button></li>
	  			<li class="page-item"><button class="btn btn-danger" onclick="window.location='${coming_from}'; return false;" style="margin-left:50px">Cancelar</button></li>
	  		</ul>
		</form>
	</jsp:body>
</t:layout>