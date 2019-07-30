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
				<input readonly type="text" class="form-congrol" id="estado" name="estado" value="${old_estado}" style="display:none">
				<input readonly type="text" class="form-congrol" id="id" name="id" value="${old_id}" style="display:none">
			</div>
			<div class="form-group col-5 mx-auto">
			    <label for="name" class="text-light">Nombre</label>
			    <c:choose>
			    	<c:when test="${old_estado == 4}">
			    		<input type="text" readonly maxlength="45" size="45" class="form-control" id="name" name="name" value="${old_nombre}"  placeholder="Ingrese nombre de la liga aquí">
			    	</c:when>
			    	<c:otherwise>
			    		<input type="text" maxlength="45" size="45" class="form-control" id="name" name="name" value="${old_nombre}"  placeholder="Ingrese nombre de la liga aquí">
			    	</c:otherwise>
			    </c:choose>		    	
		    	<small class="form-text" style="color:red">${err_nombre}</small>
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="season" class="text-light">Temporada</label>
			    <input type="text" maxlength="45" size="45" class="form-control" id="season" name="season" value="${cur_season}" readonly>		    	
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="datepicker-start" class="text-light">Fecha de Inicio</label>
			    <c:choose>
			    	<c:when test="${old_estado == 4}">
			    		<input type="text" readonly class="form-control" id="noDatepicker" name="start-day" value="${old_sday}" placeholder="Seleccione fecha de inicio"/>
			    	</c:when>
			    	<c:otherwise>
			    		<input type="text" readonly class="form-control" id="datepicker-start" name="start-day" value="${old_sday}" placeholder="Seleccione fecha de inicio"/>
			    	</c:otherwise>
			    </c:choose>
			    <small class="form-text" style="color:red">${err_sday}</small>	    	
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="datepicker-end" class="text-light">Fecha de Fin</label>
			    <input type="text" readonly class="form-control" id="datepicker-end" name="end-day" value="${old_eday}" placeholder="Seleccione fecha de fin"/>
			    <small class="form-text" style="color:red">${err_eday}</small>		    	
		  	</div>
		  	<ul class="pagination justify-content-center">
  				<c:choose>
	  				<c:when test="${old_mode == 'update'}">
	  					<li class="page-item"><button type="submit" class="btn btn-primary" style="margin:20px 5px 0px 5px">Editar Liga</button></li>
	  				</c:when>
	  				<c:otherwise>
	  					<li class="page-item"><button type="submit" class="btn btn-primary" style="margin:20px 5px 0px 5px">Crear Liga</button></li>
	  				</c:otherwise>
  				</c:choose>
  				<li class="page-item"><button onclick="window.location='wwligas';return false;" class="btn btn-danger" style="margin:20px 5px 0px 5px">Cancelar</button></li>
  			</ul>
		</form>
	</jsp:body>
</t:layout>