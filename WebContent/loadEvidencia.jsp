<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="negocio.Evidencia" %>
<head>
	<title>Winionline | Cargar Evidencia Disputa</title>
</head>
<t:layout>
	<jsp:body>
	
		<c:if test="${err_gral != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Error cargando evidencia</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${err_gral}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
	
		<c:if test="${err_video != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Error cargando video</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${err_video}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
		<c:if test="${err_imagen != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Error cargando imagen</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${err_imagen}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
		<!-- Modal de imagen -->
		<form action="cargarEvidenciaDisputa" method="post" enctype="multipart/form-data">
		<div id="agregarEvidenciaModalImagen" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Carga de Evidencia - Imagen</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      	<div class="form-group">
		      		<label for="imagen" class="text">Subir Imagen</label>
		      		<input type="file" id="imagen" name="imagen"/>
		      	</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
		        <button type="submit" class="btn btn-primary" id="btnDisputar">Aceptar</button>
		      </div>
		    </div>
		  </div>
		</div>
		</form>
		
		<!-- Modal de video -->
		<form action="cargarEvidenciaDisputa" method="post" enctype="multipart/form-data">
		<div id="agregarEvidenciaModalVideo" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Carga de Evidencia - Link a Video</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		      	<div class="form-group">
		      		<label for="video" class="text">Link a Video</label>
		      		<input type="text" class="form-control" id="video" name="video" placeholder="Ingrese link de video aqui">
		      	</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
		        <button type="submit" class="btn btn-primary">Aceptar</button>
		      </div>
		    </div>
		  </div>
		</div>
		</form>
	
		<div style="margin-top:20px; margin-left:20px;">
		<p class="h3" style="color:white">Evidencias presentadas para disputa</p>
		<p style="color:white">Datos del partido:</p>
		<p style="color:white">Fecha: ${fecha_partido}</p>
		<p style="color:white">Rival: ${rival_partido}</p>
		<p style="color:white">Resultado: ${resultado_partido}</p>
		<button class="btn btn-primary" onclick="agregarImagenClicked()">Agregar Imagen</button>
		<button class="btn btn-primary" onclick="agregarVideoClicked()" style="margin-left:20px">Agregar Link a Video</button>
		</div>
		<c:if test="${count > 0}">
			<table class="table table-hover table-dark" style="margin-top:20px">
			<thead>
				<tr>
				<th scope="col">Tipo</th>
				<th scope="col">Fecha archivo</th>
				<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${evidencias}" var="evidencia">
					<tr>
						<td><p>${evidencia.getTipo()}</p></td>
						<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy HH:mm:ss" value="${evidencia.getFecha()}"/></p></td>
						<td>
							<c:if test="${evidencia.getTipo() == Evidencia.VIDEO}">
							<a class="btn btn-primary" href="${evidencia.getLink()}" target="_blank" rel="noopener noreferrer">Ver</a>
							</c:if>
							<c:if test="${evidencia.getTipo() == Evidencia.IMAGEN}">
							<a class="btn btn-primary" href="downloadEvidencia?path=${evidencia.getPath()}">Ver</a>
							</c:if>
							<a class="btn btn-danger" href="cargarEvidenciaDisputa?delete=${evidencia.getPath()}" style="margin-left:20px">Eliminar</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay evidencias cargadas</p>
		</c:if>
		<script>
			function agregarImagenClicked(){
				$('#agregarEvidenciaModalImagen').modal('show');
			}
			function agregarVideoClicked(){
				$('#agregarEvidenciaModalVideo').modal('show');
			}
		</script>
	</jsp:body>
</t:layout>