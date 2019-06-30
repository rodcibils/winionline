<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
	<jsp:attribute name="title">Login</jsp:attribute>
	<jsp:body>
		<form action="login" method="post">
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
	</jsp:body>
</t:layout>