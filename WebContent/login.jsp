<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>Winionline | Iniciar Sesion</title>
</head>
<t:layout>
	<jsp:body>
		<form action="login" method="post" style="margin-top:50px">
			<div class="form-group col-5 mx-auto">
			    <label for="userEmail" class="text-light">Email</label>
			    <input type="email" class="form-control" id="userEmail" name='userEmail' value='${old_nombre}' placeholder="Ingrese su Email aquí">
			    <small class="form-text" style="color:red">${err_email}</small>
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="userPassword" class="text-light">Password</label>
		    	<input type="password" class="form-control" id="userPassword" name='userPassword' placeholder="Password">
		    	<small class="form-text" style="color:red">${err_pass}</small>
	  		</div>
	  		<ul class="pagination justify-content-center">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Ingresar</button></li>
	  		</ul>
		</form>
		<ul class="pagination justify-content-center">
			<li class="page-item"><a href="register">¿No tiene un usuario? Regístrese aquí</a></li>
		</ul>
		
		<c:if test="${param.success == true}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i> Te has registrado correctamente!</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        Inicia Sesión para comenzar a participar de la comunidad
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
	</jsp:body>
</t:layout>