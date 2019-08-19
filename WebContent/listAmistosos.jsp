<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Historial de Amistosos</title>
</head>
<t:layout>
	<jsp:body>
		<div id="matchDisputeModal" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Esta seguro que desea disputar este amistoso?</h5>
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
			        <strong class="mr-auto"><i class="fa fa-grav"></i>No puede disputar el amistoso</strong>
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
		<c:if test="${err_edit != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>No puede editar el resultado de este amistoso</strong>
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
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Resultado de Amistoso Editado</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        El resultado del partido amistoso se edito correctamente.
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "listFriendlyMatches?search=" + toSearch;
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
				<c:forEach items="${amistosos}" var="amistoso">
					<tr>
					<td>${amistoso.getResultadoDos().getJugador().getNombre()} - ${amistoso.getResultadoDos().getJugador().getApodo()}</td>
					<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${amistoso.getFecha()}"/></td>
					<td> 
						<c:if test="${amistoso.getResultadoUno().getGoles() > amistoso.getResultadoDos().getGoles()}">
							${amistoso.getResultadoUno().getGoles()} - ${amistoso.getResultadoDos().getGoles()} (V)
						</c:if>
						<c:if test="${amistoso.getResultadoUno().getGoles() < amistoso.getResultadoDos().getGoles()}">
							${amistoso.getResultadoUno().getGoles()} - ${amistoso.getResultadoDos().getGoles()} (D)
						</c:if>
						<c:if test="${amistoso.getResultadoUno().getGoles() == amistoso.getResultadoDos().getGoles()}">
							${amistoso.getResultadoUno().getGoles()} - ${amistoso.getResultadoDos().getGoles()} (E)
						</c:if>
					</td>
					<td>${amistoso.getRegistro().getNombre()} - ${amistoso.getRegistro().getApodo()}</td>
					<td>
						<c:if test="${sessionScope.usuario.getId() == amistoso.getRegistro().getId()}">
						<a class="btn btn-success" style="margin-left:20px" href="listFriendlyMatches?edit=${amistoso.getId()}">Editar Resultado</a>
						<a class="btn btn-danger disabled" style="margin-left:20px">Disputar Resultado</a>
						</c:if>
						<c:if test="${sessionScope.usuario.getId() != amistoso.getRegistro().getId()}">
						<a class="btn btn-success disabled" style="margin-left:20px">Editar Resultado</a>
						<a class="btn btn-danger" style="margin-left:20px" data-toggle="modal" onclick="disputarClicked(${amistoso.getId()})">Disputar Resultado</a>
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
					<a class="page-link" href="listFriendlyMatches?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="listFriendlyMatches?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="listFriendlyMatches?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay amistosos que mostrar</p>
		</c:if>
		<script>
			function disputarClicked(id){
				$('#btnDisputar').click(function(){
					location.href = "listFriendlyMatches?dispute=" + id;
				});
				$('#matchDisputeModal').modal('show');
			}
		</script>
	</jsp:body>
</t:layout>