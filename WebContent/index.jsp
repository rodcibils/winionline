<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${sessionScope.usuario != null}">
		<t:layout>
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
			<c:if test="${param.new_league_success == true}">
				<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
				    <div class="toast-header">
				        <strong class="mr-auto"><i class="fa fa-grav"></i>Liga Creada Exitosamente</strong>
				        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
				    </div>
				    <div class="toast-body">
				        Los jugadores ya pueden inscribirse para participar de la nueva liga.
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