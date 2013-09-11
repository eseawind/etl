<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Droti Utilisateur</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
<link rel="stylesheet" href="css/style.css">

<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->

<script src="js/jquery/jquery-1.7.2.min.js"></script>
<script src="js/js-bootstrap/bootstrap.min.js"></script>

</head>
<body>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<div class="legend" style="margin-bottom: 15px;">Droit
					Utilisateur</div>
				<c:if test="${not empty msg_error}">
					<div class="alert alert-error">
						<button class="close" data-dismiss="alert">×</button>
						<span class="label label-important" style="margin-right: 20px;">Erreur</span><strong>${msg_error}</strong>
					</div>
				</c:if>
				<c:if test="${not empty msg_succcess}">
					<div class="alert alert-success">
						<button class="close" data-dismiss="alert">×</button>
						<span class="label label-success" style="margin-right: 20px;">Succès</span><strong>${msg_succcess}</strong>
					</div>
				</c:if>
				<div class="row span8" style="margin-bottom: 15px;">
					<a class="btn" style="float: right; margin-right: 5px;"
						href="${userRightBean.edit_url}"> <i class="icon-edit"></i>
						Modifier
					</a>
				</div>
				<div class="span8">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<td style="border-top: 0 none;"><h5>Droit utilisateur</h5></td>
								<td style="border-top: 0 none;">${userRightBean.label}</td>
							</tr>
							<tr>
								<td><h5>Droits d'accès</h5></td>
								<td><ul>
										<c:forEach var="droit_acces"
											items="${userRightBean.droits_acces}">
											<li>${droit_acces.value}</li>
										</c:forEach>
									</ul></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>