<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout>
	<jsp:attribute name="title">Login</jsp:attribute>
	<jsp:body>
		<form>
			<div class="form-group col-5 mx-auto">
			    <label for="userEmail" class="text-light">Email</label>
			    <input type="email" class="form-control" id="userEmail" placeholder="Ingrese su Email aquí">
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="userPassword" class="text-light">Password</label>
		    	<input type="password" class="form-control" id="userPassword" placeholder="Password">
	  		</div>
	  		<ul class="pagination justify-content-center">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Ingresar</button></li>
	  		</ul>
		</form>
		<ul class="pagination justify-content-center">
			<li class="page-item"><a href="register.jsp">¿No tiene un usuario? Regístrese aquí</a></li>
		</ul>
	</jsp:body>
</t:layout>