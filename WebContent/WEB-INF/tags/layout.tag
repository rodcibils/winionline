<%@tag description="Page layout" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="title" fragment="true" %>
<%@attribute name="javascript" fragment="true"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
  </head>
  <body style="background-color:black">
  	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-maxlength/1.7.0/bootstrap-maxlength.min.js"></script>
    <c:if test="${has_datepicker == true }">
    	<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    </c:if>
	<script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
  	<header>
  		<div class="container">
  			<div class="row">
  				<div class="col"></div>
  				<div class="col-md-auto">
		  			<img alt="banner" src="Resources/img/banner.jpg">
  				</div>
  				<div class="col"></div>
  			</div>
  		</div>
  	</header>
  	
  	<c:if test="${sessionScope.usuario != null}">
	  	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	  		<a class="navbar-brand" href="#">Menú Principal</a>
	  		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
	    		<span class="navbar-toggler-icon"></span>
	  		</button>
	  		<div class="collapse navbar-collapse" id="navbarNavAltMarkup">
	  			<ul class="navbar-nav">
	  				<c:if test="${sessionScope.usuario.isAdmin()}">
		  				<li class="nav-item dropdown">
		  					<a class="nav-link dropdown-toggle" href="#" id="dropAdmin" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Administrador</a>
		  					<div class="dropdown-menu" aria-labelledby="dropAdmin">
		  						<a class="dropdown-item" href="wwligas">Ligas</a>
		  					</div>
		  				</li>
	  				</c:if>
	  				<li class="nav-item dropdown">
	  					<a class="nav-link dropdown-toggle" href="#" id="dropUsuario" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Usuario</a>
	  					<div class="dropdown-menu" aria-labelledby="dropUsuario">
	  						<a class="dropdown-item" href="usuarios">Lista de Usuarios</a>
	  						<a class="dropdown-item" href="editUser">Editar Datos Personales</a>
	  						<a class="dropdown-item" href="editPassword.jsp">Cambiar Contraseña</a>
	  						<a class="dropdown-item" data-toggle="modal" data-target="#userDeleteModal">Eliminar Usuario</a>
	  					</div>
	  				</li>
	  				<li class="nav-item dropdown">
	  					<a class="nav-link dropdown-toggle" href="#" id="dropAmistosos" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Amistosos</a>
	  					<div class="dropdown-menu" aria-labelledby="dropAmistosos">
	  						<a class="dropdown-item" href="sentFriendRequest?skip=0">Solicitudes Enviadas</a>
	  						<a class="dropdown-item" href="receivedFriendRequest?skip=0">Solicitudes Recibidas</a>
	  					</div>
	  				</li>
	  				<li class="nav-item">
	  					<a class="nav-link" href="login">Cerrar Sesión</a>
	  				</li>	  				
	  			</ul>
	  		</div>
	  	</nav>
	  	
	  	<div id="userDeleteModal" class="modal fade" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title">Esta seguro que desea eliminar su usuario?</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <p>Perderá acceso a esta cuenta y sólo podrá recuperarlo contactándose con los administradores
		        de esta página. Toda la información de su cuenta permanecerá guardada en nuestros servidores.</p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
		        <button type="button" class="btn btn-primary" onclick="location.href='deleteUser';">Aceptar</button>
		      </div>
		    </div>
		  </div>
		</div>
  	</c:if>
  	<jsp:doBody/>
  </body>
</html>