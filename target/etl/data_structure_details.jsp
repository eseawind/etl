<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Structure de données</title>
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
				<div class="legend">Structure de données</div>
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
				<div class="span8">
					<div class="span8">
						<table class="table table-bordered table-striped">
							<tbody>
								<tr>
									<td style="border-top: 0 none;"><h5>Nom de la
											structure de données :</h5></td>
									<td style="border-top: 0 none;">${DataStructureBean.name}</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="span11" style="width: 1100px; margin-left: 5px;">
					<h5 style="margin-bottom: 10px;">Liste des colonnes :</h5>
						<table class="table table-bordered table-striped">
							<thead style="font-size: 12px;">
								<tr>
									<th>#</th>
									<th>Nom</th>
									<th>Type</th>
									<th>Longueur</th>
									<th>Valeur par défaut</th>
									<th>Valeur min</th>
									<th>Valeur max</th>
									<th style="width: 60px;">Nullable</th>
									<th style="width: 80px;">Clé primaire</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="column" items="${ColumnsList}">
									<tr>
										<td>${column.order}</td>
										<td>${column.name}</td>
										<td>${column.display_type}</td>
										<td>${column.lenght}</td>
										<td>${column.default_value}</td>
										<td>${column.min_value}</td>
										<td>${column.max_value}</td>
										<td style="text-align: center;"><c:choose>
												<c:when test='${column.nullable == true}'>
													<i class="icon-ok"></i>
												</c:when>
												<c:otherwise>
													<i class="icon-remove"></i>
												</c:otherwise>
											</c:choose></td>
										<td style="text-align: center;"><c:choose>
												<c:when test='${column.primary_Key == true}'>
													<i class="icon-ok"></i>
												</c:when>
												<c:otherwise>
													<i class="icon-remove"></i>
												</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>