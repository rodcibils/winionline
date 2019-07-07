<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Winionline | Editar Usuario</title>
</head>
<t:layout>
	<jsp:body>
		<form action="editUser" method="post" enctype="multipart/form-data" style="margin-top:50px">
			<div class="form-group col-5 mx-auto">
			    <label for="username" class="text-light">Nombre de Usuario</label>
			    <input type="text" maxlength="20" size="20" class="form-control" id="username" name="username" value="${sessionScope.usuario.getNombre()}"  placeholder="Ingrese su nombre de usuario aquí" readonly>		    	
		  	</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="birthdate" class="text-light">Fecha de nacimiento</label>
		    	<input type="text" class="form-control" id="birthdate" name="birthdate" value="${old_date}" placeholder="ej: 12/12/2012">
		    	<c:forEach items="${err_date}" var="error">
			    	<small class="form-text" style="color:red">${error}</small>
			    </c:forEach>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="email" class="text-light">Email</label>
		    	<input type="email" maxlength="45" size="45" class="form-control" id="email" name="email" value="${sessionScope.usuario.getEmail()}" placeholder="Ingrese su email aquí">
		    	<small class="form-text" style="color:red">${err_mail}</small>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="apodo" class="text-light">Apodo</label>
		    	<input type="text" maxlength="20" size="20" class="form-control" id="apodo" name="apodo" value="${sessionScope.usuario.getApodo()}" placeholder="Ingrese su apodo aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="pais" class="text-light">País</label>
		    	<select class="custom-select" id="pais" name="pais">
		    		<c:forEach items="${paises}" var="pais">
		    		<c:choose>
			    		<c:when test="${pais.getNombre() == sessionScope.usuario.getPais().getNombre()}">
		    				<option value="${pais.getNombre()}" selected>${pais.getNombre()}</option>
	    				</c:when>
	    				<c:when test="${pais.getNombre() != sessionScope.usuario.getPais().getNombre()}">
		    				<option value="${pais.getNombre()}">${pais.getNombre()}</option>
	    				</c:when>
    				</c:choose>
		    		</c:forEach>
		    	</select>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="skype" class="text-light">Skype</label>
		    	<input type="text" maxlength="45" size="45" class="form-control" id="skype" name="skype" value="${sessionScope.usuario.getSkype()}" placeholder="Ingrese su ID de Skype aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="ip" class="text-light">Dirección IP</label>
		    	<input type="text" maxlength="40" size="40" class="form-control" id="ip" name="ip" value="${sessionScope.usuario.getIp()}" placeholder="Ingrese su dirección IP aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
	  			<c:if test="${has_avatar == true}">
	  				<img src="showAvatar" width=100 height=100/>
	  			</c:if>
			    <label for="userAvatar" class="text-light">Cambiar avatar</label>
		    	<input type="file" id="userAvatar" name="avatar"/>
		    	<small class="form-text" style="color:red">${err_avatar}</small>
	  		</div>
	  		<ul class="pagination justify-content-center">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Aceptar</button></li>
	  			<li class="page-item"><button type="button" class="btn btn-danger" onclick="window.location='index.jsp';return false;" style="margin-left:25px">Cancelar</button></li>
	  		</ul>
		</form>
	</jsp:body>
</t:layout>