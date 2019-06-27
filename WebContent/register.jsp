<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:layout>
	<jsp:attribute name="title">Registro</jsp:attribute>
	<jsp:body>
		<form action="register" method="post">
			<div class="form-group col-5 mx-auto">
			    <label for="username" class="text-light">Nombre de Usuario</label>
			    <input type="text" maxlength="20" size="20" class="form-control" id="username" name="username" value="${old_nombre}"  placeholder="Ingrese su nombre de usuario aquí">		    	
		    	<small class="form-text" style="color:red">${err_nombre}</small>
		  	</div>
		  	<div class="form-group col-5 mx-auto">
			    <label for="password" class="text-light">Password</label>
		    	<input type="password" maxlength="20" size="20" class="form-control" id="password" name="password" value="${old_pass}" placeholder="Ingrese su contraseña aquí">
		    	<small class="form-text" style="color:red">${err_pass}</small>
	  		</div>
	  		
	  		<div class="form-group col-5 mx-auto">
			    <label for="repeatedPassword" class="text-light">Repetir Password</label>
		    	<input type="password" maxlength="20" size="20" class="form-control" id="repeatedPassword" name="repeatedPassword" value="${old_rpass}" placeholder="Repita su contraseña">
		    	<small class="form-text" style="color:red">${err_rpass}</small>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="birthdate" class="text-light">Fecha de nacimiento</label>
		    	<input type="text" class="form-control" id="birthdate" name="birthdate" value="${old_birthdate}" placeholder="ej: 12/12/2012">
		    	<c:forEach items="${err_date}" var="error">
			    	<small class="form-text" style="color:red">${error}</small>
			    </c:forEach>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="email" class="text-light">Email</label>
		    	<input type="email" maxlength="45" size="45" class="form-control" id="email" name="email" value="${old_email}" placeholder="Ingrese su email aquí">
		    	<small class="form-text" style="color:red">${err_mail}</small>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="apodo" class="text-light">Apodo</label>
		    	<input type="text" maxlength="20" size="20" class="form-control" id="apodo" name="apodo" value="${old_apodo}" placeholder="Ingrese su apodo aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="pais" class="text-light">País</label>
		    	<select class="custom-select" id="pais" name="pais">
		    		<option selected value="1">Argentina</option>
		    		<option value="2">Bolivia</option>
		    		<option value="3">Brasil</option>
		    		<option value="4">Chile</option>
		    		<option value="5">Colombia</option>
		    		<option value="6">Costa Rica</option>
		    		<option value="7">Ecuador</option>
		    		<option value="8">El Salvador</option>
		    		<option value="9">Estados Unidos</option>
		    		<option value="10">Guatemala</option>
		    		<option value="11">Haiti</option>
		    		<option value="12">Honduras</option>
		    		<option value="13">Jamaica</option>
		    		<option value="14">Mexico</option>
		    		<option value="15">Nicaragua</option>
		    		<option value="16">Panama</option>
		    		<option value="17">Paraguay</option>
		    		<option value="18">Peru</option>
		    		<option value="19">Puerto Rico</option>
		    		<option value="20">Republica Dominicana</option>
		    		<option value="21">Uruguay</option>
		    		<option value="22">Venezuela</option>
		    		<option value="23">Otro</option>
		    	</select>
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="skype" class="text-light">Skype</label>
		    	<input type="text" maxlength="45" size="45" class="form-control" id="skype" name="skype" value="${old_skype}" placeholder="Ingrese su ID de Skype aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="ip" class="text-light">Dirección IP</label>
		    	<input type="text" maxlength="40" size="40" class="form-control" id="ip" name="ip" value="${old_ip}" placeholder="Ingrese su dirección IP aquí">
	  		</div>
	  		<div class="form-group col-5 mx-auto">
			    <label for="avatar" class="text-light">Avatar</label>
		    	<input type="file" class="form-control-file" id="avatar" name="avatar">
	  		</div>
	  		<ul class="pagination justify-content-center">
	  			<li class="page-item"><button type="submit" class="btn btn-primary">Registrarse</button></li>
	  		</ul>
		</form>
	</jsp:body>
</t:layout>