<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="negocio.Estado" %>

<head>
	<title>Winionline | Ligas</title>
</head>
<t:layout>
	<jsp:body>
		<a href="newLeague?mode=insert&id=0&estado=0" class="btn btn-primary" style="margin:20px">Agregar</a>
		<script>
			function search(){
				var toSearch = document.getElementById('txtSearch').value;
				var yearSearch = document.getElementById('year').value;
				window.location.href = "wwligas?search=" + toSearch + '&yearSearch=' + yearSearch;
			}
		</script>
		<div class="input-group col-6 float-right" style="margin-top:20px;margin-bottom:20px;margin-right:20px">
			<select id="year" name="year" class="custom-select" style="max-width: 100px">
				<c:if test="${year==0}">
				<option value="0" selected>Todos</option>
				</c:if>
				<c:if test="${year!=0}">
				<option value="0">Todos</option>
				</c:if>
	            <c:forEach items="${temporadas}" var="temporada">
	            	<c:if test="${year == temporada}">
	                <option value="${temporada}" selected>${temporada}</option>
	                </c:if>
	                <c:if test="${year != temporada}">
	                <option value="${temporada}">${temporada}</option>
	                </c:if>
	            </c:forEach>
			</select>
		  	<input type="text" id="txtSearch" class="form-control" value="${search}" placeholder="Buscar por nombre...">
		  	<div class="input-group-append">
		    	<button type="button" class="btn btn-primary" onclick="search()">Buscar</button>
		  	</div>
		</div>
		<c:if test="${count > 0}">
			<table class="table table-hover table-dark">
				<thead>
					<tr>
						<th scope="col">Nombre</th>
						<th scope="col">Temporada</th>
						<th scope="col">Inicio</th>
						<th scope="col">Fin</th>
						<th scope="col">Estado</th>
						<th scope="col"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ligas}" var="liga">
						<tr>
							<td>${liga.getNombre()}</td>
							<td>${liga.getTemporada()}</td>
							<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${liga.getInicio()}"/></td>
							<td><fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${liga.getFin()}"/></td>
							<td>${liga.getEstado().getDescripcion()}</td>
							<c:choose>
								<c:when test="${liga.getEstado().getId() == Estado.LIGA_FINALIZADA}">
									<td>
										<a class="btn btn-primary disabled" href="#">Editar</a>
										<a class="btn btn-danger disabled" style="margin-left:20px" href="#">Eliminar</a>
									</td>
								</c:when>
								<c:otherwise>
									<td>
										<a class="btn btn-primary" href="newLeague?mode=update&id=${liga.getId()}&estado=${liga.getEstado().getId()}">Editar</a>
										<c:if test="${liga.getEstado().getId() != Estado.LIGA_INICIADA}">
										<a class="btn btn-danger" href="#" style="margin-left:20px" data-toggle="modal" onclick="eliminarClicked(${liga.getId()})">Eliminar</a>
										</c:if>
										<c:if test="${liga.getEstado().getId() == Estado.LIGA_INICIADA}">
										<a class="btn btn-danger disabled" href="#" style="margin-left:20px" data-toggle="modal" onclick="eliminarClicked(${liga.getId()})">Eliminar</a>
										</c:if>
									</td>
								</c:otherwise>
							</c:choose>
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
					<a class="page-link" href="wwligas?skip=${skip-10}" aria-label="Anterior">
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
					<li class="page-item"><a class="page-link" href="wwligas?skip=${index*10}">${index + 1}</a></li>
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
					<a class="page-link" href="wwligas?skip=${skip+10}" aria-label="Siguiente">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				</ul>
			</nav>
			<div id="leagueDeleteModal" class="modal fade" tabindex="-1" role="dialog">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">Eliminar</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <p>Esta seguro que desea eliminar liga?</p>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
			        <button type="button" id="btnEliminar" class="btn btn-primary">Aceptar</button>
			      </div>
			    </div>
			  </div>
			</div>
		</c:if>
		<c:if test="${count==0}">
			<p class="h3 text-center" style="color:white; margin-left:20px; margin-top:20px">No hay ligas que mostrar</p>
		</c:if>
		<script>
			function eliminarClicked(id){
				console.log(id);
				$("#btnEliminar").click(function(){
					console.log("prueba");
					location.href = 'wwligas?action=eliminar&id=' + id;
				});
				$("#leagueDeleteModal").modal('show');
			}
		</script>
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
	</jsp:body>
</t:layout>