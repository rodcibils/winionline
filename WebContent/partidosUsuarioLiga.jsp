<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Partidos de Liga</title>
</head>
<t:layout>
	<jsp:body>
	
		<script>
			function disputarClicked(id){
				$('#btnDisputar').click(function(){
					location.href = "partidosusuarioliga?dispute=" + id;
				});
				$('#matchDisputeModal').modal('show');
			}
		</script>
		
		<div id="matchDisputeModal" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Esta seguro que desea disputar este partido?</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <p>El resultado de este partido será sometido a juicio público por los usuarios de la 
		        comunidad, que decidirán el ganador final. Esta acción no es reversible y en caso 
		        de reiteración injustificada su cuenta puede ser baneada.</p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
		        <button type="button" class="btn btn-primary" id="btnDisputar">Aceptar</button>
		      </div>
		    </div>
		  </div>
		</div>
		<c:if test="${err_dispute != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>No puede disputar el partido</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${err_dispute}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
		<c:if test="${dispute_success != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Partido disputado</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${dispute_success}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
		<c:if test="${err_edit != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>No puede editar el resultado de este partido</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${err_edit}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		<c:if test="${register_success == true}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Resultado de Partido Editado</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        El resultado del partido se edito correctamente.
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
	
		<p class="h3 text-light text-center" style="margin-top:30px">Partidos de ${nombre_usuario} en liga ${nombre_liga}</p>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "partidosusuarioliga?search=" + toSearch;
			}
		</script>
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre o apodo de usuario...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>
		<c:if test="${count > 0}">
			<table class="table table-hover table-dark">
			<thead>
				<tr>
				<th scope="col">Jugador Rival</th>
				<th scope="col">Fecha Partido</th>
				<th scope="col">Resultado</th>
				<th scope="col">Registrado Por</th>
				<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${partidos}" var="partido">
					<tr>
					<c:if test="${partido.getResultadoUno().getJugador().getId() == idUsuario}">
					<td>${partido.getResultadoDos().getJugador().getNombre()} - ${partido.getResultadoDos().getJugador().getApodo()}</td>
					</c:if>
					<c:if test="${partido.getResultadoDos().getJugador().getId() == idUsuario}">
					<td>${partido.getResultadoUno().getJugador().getNombre()} - ${partido.getResultadoUno().getJugador().getApodo()}</td>
					</c:if>
					<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${partido.getFecha()}"/></td>
					<td> 
					<c:if test="${partido.getResultadoUno().getJugador().getId() == idUsuario}">
						<c:if test="${partido.getResultadoUno().getGoles() > partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (V)
						</c:if>
						<c:if test="${partido.getResultadoUno().getGoles() < partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (D)
						</c:if>
						<c:if test="${partido.getResultadoUno().getGoles() == partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (E)
						</c:if>
					</c:if>
					<c:if test="${partido.getResultadoDos().getJugador().getId() == idUsuario}">
						<c:if test="${partido.getResultadoUno().getGoles() < partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (V)
						</c:if>
						<c:if test="${partido.getResultadoUno().getGoles() > partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (D)
						</c:if>
						<c:if test="${partido.getResultadoUno().getGoles() == partido.getResultadoDos().getGoles()}">
							${partido.getResultadoUno().getGoles()} - ${partido.getResultadoDos().getGoles()} (E)
						</c:if>
					</c:if>
					</td>
					<td>${partido.getRegistro().getNombre()} - ${partido.getRegistro().getApodo()}</td>
					<td>
						<c:if test="${sessionScope.usuario.getId() == idUsuario && sessionScope.usuario.getId() == partido.getRegistro().getId()}">
						<a class="btn btn-success" style="margin-left:20px" href="partidosusuarioliga?edit=${partido.getId()}">Editar Resultado</a>
						<a class="btn btn-danger disabled" style="margin-left:20px">Disputar Resultado</a>
						</c:if>
						<c:if test="${sessionScope.usuario.getId() == idUsuario && sessionScope.usuario.getId() != partido.getRegistro().getId()}">
						<a class="btn btn-success disabled" style="margin-left:20px">Editar Resultado</a>
						<a class="btn btn-danger" style="margin-left:20px" onclick="disputarClicked(${partido.getId()})">Disputar Resultado</a>
						</c:if>
					</td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
			<nav aria-label="Paginacion">
				<ul class="pagination justify-content-center">
				<c:if test="${skip == 0}">
					<li class="page-item disabled">
					<a class="page-link" href="#" aria-label="Anterior">
						<span aria-hidden="true">&laquo;</span>
					</a>
				</li>
				</c:if>
				<c:if test="${skip > 0}">
					<li class="page-item">
					<a class="page-link" href="partidosusuarioliga?skip=${skip-10}&search=${search}" aria-label="Anterior">
						<span aria-hidden="true">&laquo;</span>
					</a>
					</li>
				</c:if>
				<c:forEach begin="0" end="${max_pages-1}" var="index">
				<c:if test="${current_page == index}">
					<li class="page-item active">
						<a class="page-link" href="#">${index+1}<span class="sr-only">(current)</span></a>
					</li>
				</c:if>
				<c:if test="${current_page != index}">
					<li class="page-item"><a class="page-link" href="partidosusuarioliga?skip=${index*10}&search=${search}">${index + 1}</a></li>
				</c:if>	
				</c:forEach>
				<c:if test="${current_page == max_pages-1}">
				<li class="page-item disabled">
					<a class="page-link" href="#" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				<c:if test="${current_page < max_pages-1}">
				<li class="page-item">
					<a class="page-link" href="partidosusuarioliga?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay partidos que mostrar</p>
		</c:if>
		<ul class="pagination justify-content-center">
			<li class="page-item"><button onclick="window.location='estadisticasLiga?id=${idLiga}';return false;" class="btn btn-danger" style="margin:20px 5px 0px 5px">Volver</button></li>
		</ul>
	</jsp:body>
</t:layout>