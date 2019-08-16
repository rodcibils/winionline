<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
	<title>Winionline | Mis Disputas Cerradas</title>
</head>
<t:layout>
	<jsp:body>
		
		<div id="matchAppealModal" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Esta seguro que desea apelar esta disputa?</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <p>El veredicto final de la disputa estará a cargo de 5 administradores 
		        que oficiarán de jueces de la misma. Su desición final será inalterable e 
		        inapelable. En caso de reiteraciones injustificadas su cuenta puede ser 
		        baneada.</p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
		        <button type="button" class="btn btn-primary" id="btnApelar">Aceptar</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<script>
			function apelarClicked(id){
				$('#btnApelar').click(function(){
					location.href = "misDisputasCerradas?apelar=" + id;
				});
				$('#matchAppealModal').modal('show');
			}
		</script>
	
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				window.location.href = "misDisputasCerradas?search=" + toSearch;
			}
		</script>
		
		<c:if test="${apelar_success != null}">
			<div class="toast" id="myToast" data-delay="5000" style="position: absolute; top:85%; right:50px;">
			    <div class="toast-header">
			        <strong class="mr-auto"><i class="fa fa-grav"></i>Disputa apelada</strong>
			        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast">&times;</button>
			    </div>
			    <div class="toast-body">
			        ${apelar_success}
			    </div>
			</div>
			<script>
				$(document).ready(function(){
					$("#myToast").toast('show');
				});
			</script>
		</c:if>
		
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
				<th scope="col">Fecha Cierre</th>
				<th scope="col">Resultado</th>
				<th scope="col"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${disputas}" var="disputa">
					<tr>
					<c:if test="${disputa.getPartido().getSolicitud().getJugadorUno().getId() == (sessionScope.usuario.getId())}">
						<td>${disputa.getPartido().getSolicitud().getJugadorDos().getNombre()} - ${disputa.getPartido().getSolicitud().getJugadorDos().getApodo()}</td>
					</c:if>
					<c:if test="${disputa.getPartido().getSolicitud().getJugadorDos().getId() == sessionScope.usuario.getId()}">
						<td>${disputa.getPartido().getSolicitud().getJugadorUno().getNombre()} - ${disputa.getPartido().getSolicitud().getJugadorUno().getApodo()}</td>
					</c:if>
					<td><p><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${disputa.getVencimiento()}"/></p></td>
					<td> 
					<c:if test="${disputa.getPartido().getSolicitud().getJugadorUno().getId() == sessionScope.usuario.getId()}">
						<c:if test="${disputa.getVotosUno() > disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (V)</p>
						</c:if>
						<c:if test="${disputa.getVotosUno() < disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (D)</p>
						</c:if>
						<c:if test="${disputa.getVotosUno() == disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (E)</p>
						</c:if>
					</c:if>
					<c:if test="${disputa.getPartido().getSolicitud().getJugadorDos().getId() == sessionScope.usuario.getId()}">
						<c:if test="${disputa.getVotosUno() < disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (V)</p>
						</c:if>
						<c:if test="${disputa.getVotosUno() > disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (D)</p>
						</c:if>
						<c:if test="${disputa.getVotosUno() == disputa.getVotosDos()}">
							<p>${disputa.getVotosUno()} - ${disputa.getVotosDos()} (E)</p>
						</c:if>
					</c:if>
					</td>
					<td>
					<c:if test="${disputa.isApelable()}">
						<a class="btn btn-danger" onclick="apelarClicked(${disputa.getPartido().getId()})">Apelar Disputa</a>
					</c:if>
					<c:if test="${!disputa.isApelable()}">
						<a class="btn btn-danger disabled" href="#" >Apelar Disputa</a>
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
					<a class="page-link" href="misDisputasCerradas?skip=${skip-10}&search=${search}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="misDisputasCerradas?skip=${index*10}&search=${search}">${index + 1}</a></li>
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
					<a class="page-link" href="misDisputasCerradas?skip=${skip+10}&search=${search}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay disputas cerradas que mostrar</p>
		</c:if>
	</jsp:body>
</t:layout>