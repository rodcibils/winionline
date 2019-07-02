<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${sessionScope.usuario != null}">
		<t:layout>
		</t:layout>
	</c:when>
	<c:when test="${sessionScope.usuario == null}">
		<c:redirect url="login.jsp"/>
	</c:when>
</c:choose>