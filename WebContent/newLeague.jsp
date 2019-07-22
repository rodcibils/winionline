<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Winionline | Crear Nueva Liga</title>
</head>
<t:layout>
	<jsp:body>
		<script>
			$(function() {
			    $( "#datepicker-start" ).datepicker({
			    	dateFormat: 'dd/mm/yy'
			    });
			});
			$(function() {
			    $( "#datepicker-end" ).datepicker({
			    	dateFormat: 'dd/mm/yy'
			    });
			});
		</script>
		<form action="newLeague" method="post" style="margin-top:50px">
			<div class="form-group col-5 mx-auto">
				<input readonly type="text" class="form-congrol" id="mode" name="mode" value="${old_mode}" style="display:none">
				<input readonly type="text" class="form-congrol" id="id" name="id" value="${old_id}" style="display:none">
			</div>
			<div class="form-group col-5 mx-auto">
			    <label for="name" class="text-light">Nombre</label>
			    <input type="text" maxlength="45" size="45" class="form-control" id="name" name="name" value="${old_nombre}"  placeholder="Ingrese nombre de la liga aquí">		    	
		    	<small class="form-text" style="color:red">${err_nombre}</small>
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="season" class="text-light">Temporada</label>
			    <input type="text" maxlength="45" size="45" class="form-control" id="season" name="season" value="${cur_season}" readonly>		    	
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="datepicker-start" class="text-light">Fecha de Inicio</label>
			    <input type="text" class="form-control" id="datepicker-start" name="start-day" value="${old_sday}" placeholder="Seleccione fecha de inicio"/>
			    <small class="form-text" style="color:red">${err_sday}</small>	    	
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="datepicker-end" class="text-light">Fecha de Fin</label>
			    <input type="text" class="form-control" id="datepicker-end" name="end-day" value="${old_eday}" placeholder="Seleccione fecha de fin"/>
			    <small class="form-text" style="color:red">${err_eday}</small>		    	
		  	</div>
	  		<ul class="pagination justify-content-center">
	  			<c:choose>
	  				<c:when test="${old_mode == 'update'}">
	  					<li class="page-item"><button type="submit" class="btn btn-primary" style="margin-top:20px">Editar Liga</button></li>
	  				</c:when>
	  				<c:otherwise>
	  					<li class="page-item"><button type="submit" class="btn btn-primary" style="margin-top:20px">Crear Liga</button></li>
	  				</c:otherwise>
	  			</c:choose>
	  		</ul>
		</form>
	</jsp:body>
</t:layout>