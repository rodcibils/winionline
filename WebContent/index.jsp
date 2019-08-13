<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Menu Principal</title>
</head>
<c:choose>
	<c:when test="${sessionScope.usuario != null}">
	<c:if test="${data_getted == null || data_getted == false}">
		<c:redirect url="index"/>
	</c:if>
		<t:layout>
			
			<c:if test="${err_vote != null}">
				<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
				    <div class="toast-header">
				        <strong class="mr-auto"><i class="fa fa-grav"></i>No puede votar disputas</strong>
				        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
				    </div>
				    <div class="toast-body">
				        ${err_vote}
				    </div>
				</div>
				<script>
					$(document).ready(function(){
						$("#myToast").toast('show');
					});
				</script>
			</c:if>
		
			<div class="card-columns" style="margin-top:20px; margin-left:20px">
				<div class="card text-white bg-info text-center">
					<div class="card-header" style="font-weight:bold">Datos Personales</div>
					<div>
						<img src="showAvatar" class="card-img-top img-fluid" alt="user_avatar" width="200px" height="200px">
					</div>
					<div class="card-body">
						<h5 class="card-title" style="font-weight:bold">${sessionScope.usuario.getNombre()}</h5>
						<p class="card-text"><b>Apodo:</b> ${sessionScope.usuario.getApodo()}</p>
						<p class="card-text"><b>IP:</b> ${sessionScope.usuario.getIp()}</p>
						<p class="card-text"><b>Skype:</b> ${sessionScope.usuario.getSkype()}</p>
						<p class="card-text"><b>Email:</b> ${sessionScope.usuario.getEmail()}</p>
						<p class="card-text"><b>Fecha Nacimiento:</b> <fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${sessionScope.usuario.getFechanac()}"/></p>
					</div>
				</div>
				<div class="card bg-light">
					<div class="card-header">Solicitudes de Amistosos Recibidas</div>
					<div class="card-body">
						<c:choose>
						<c:when test="${sol_am_rec_pend == 0}">
						<p class="card-text">No tiene solicitudes de amistoso pendientes de rta.</p>
						</c:when>
						<c:when test="${sol_am_rec_pend > 0}">
						<p class="card-text">Tiene ${sol_am_rec_pend} solicitudes de amistoso pendientes de rta.</p>
						<a href="receivedFriendRequest?skip=0&search=" class="btn btn-primary">Verificar</a>
						</c:when>
						</c:choose>
					</div>
				</div>
				<div class="card bg-light">
					<div class="card-header">Solicitudes de Amistosos Enviadas</div>
					<div class="card-body">
						<c:choose>
						<c:when test="${sol_am_env_pend == 0}">
						<p class="card-text">No tiene solicitudes de amistoso pendientes de rta.</p>
						</c:when>
						<c:when test="${sol_am_env_pend > 0}">
						<p class="card-text">Tiene ${sol_am_env_pend} solicitudes de amistoso pendientes de rta.</p>
						<a href="sentFriendRequest?skip=0&search=" class="btn btn-primary">Verificar</a>
						</c:when>
						</c:choose>
					</div>
				</div>
				<div class="card bg-light">
					<div class="card-header">Amistosos Pendientes</div>
					<div class="card-body">
						<c:choose>
						<c:when test="${am_pend == 0}">
						<p class="card-text">No tiene amistosos pendientes.</p>
						</c:when>
						<c:when test="${am_pend > 0}">
						<p class="card-text">Tiene ${am_pend} amistosos pendientes</p>
						<a href="pendingFriendlyMatch?skip=0&search=" class="btn btn-primary">Verificar</a>
						</c:when>
						</c:choose>
					</div>
				</div>
				<div class="card bg-light">
					<div class="card-header">Ligas</div>
					<div class="card-body">
						<p class="card-text">No esta participando en ninguna liga</p>
					</div>
				</div>
				<div class="card bg-light">
					<div class="card-header">Disputas en Curso</div>
					<div class="card-body">
						<c:choose>
						<c:when test="${disputas == 0}">
						<p class="card-text">No tiene disputas en curso</p>
						</c:when>
						<c:when test="${disputas > 0}">
						<p class="card-text">Tiene ${disputas} disputas en curso</p>
						<a href="misDisputas?skip=0&search=" class="btn btn-primary">Verificar</a>
						</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<c:if test="${param.update_success == true}">
				<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
				    <div class="toast-header">
				        <strong class="mr-auto"><i class="fa fa-grav"></i>Datos Actualizados Correctamente</strong>
				        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
				    </div>
				    <div class="toast-body">
				        Su información ha sido correctamente guardada en el servidor
				    </div>
				</div>
				<script>
					$(document).ready(function(){
						$("#myToast").toast('show');
					});
				</script>
			</c:if>
			<c:if test="${param.password_changed == true}">
				<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
				    <div class="toast-header">
				        <strong class="mr-auto"><i class="fa fa-grav"></i>Contraseña Actualizada Correctamente</strong>
				        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
				    </div>
				    <div class="toast-body">
				        Su contraseña ha sido actualizada correctamente
				    </div>
				</div>
				<script>
					$(document).ready(function(){
						$("#myToast").toast('show');
					});
				</script>
			</c:if>
		</t:layout>
	</c:when>
	<c:when test="${sessionScope.usuario == null}">
		<c:redirect url="login.jsp"/>
	</c:when>
</c:choose>