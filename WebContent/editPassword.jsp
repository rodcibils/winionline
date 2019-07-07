<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Winionline | Cambiar Contraseña</title>
</head>
<t:layout>
	<jsp:body>
		<form action="updatePass" method="post" style="margin-top:50px">
			<div class="form-group col-5 mx-auto">
			    <label for="oldPassword" class="text-light">Password</label>
		    	<input type="password" maxlength="20" size="20" class="form-control" id="oldPassword" name="oldPassword" placeholder="Ingrese su contraseña actual">
		    	<small class="form-text" style="color:red">${err_opass}</small>
	  		</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="password" class="text-light">Password</label>
		    	<input type="password" maxlength="20" size="20" class="form-control" id="password" name="password" value="${old_pass}" placeholder="Ingrese su nueva contraseña aquí">
		    	<small class="form-text" style="color:red">${err_pass}</small>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="repeatedPassword" class="text-light">Repetir Password</label>
		    	<input type="password" maxlength="20" size="20" class="form-control" id="repeatedPassword" name="repeatedPassword" value="${old_rpass}" placeholder="Repita su nueva contraseña">
		    	<small class="form-text" style="color:red">${err_rpass}</small>
	  		</div>
	  		<ul class="pagination justify-content-center">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Aceptar</button></li>
	  		</ul>
		</form>
	</jsp:body>
</t:layout>