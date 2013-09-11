<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Configuration</title>
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
				<div class="legend" style="margin-bottom: 15px;">Configuration</div>
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
				<a class="btn" style="float: right; margin-right: 5px;" href="${configurationBean.edit_url}"> <i
					class="icon-edit"></i> Modifier
				</a>
				</div>
				<div class="span8">
					<table class="table table-bordered table-striped">
						<tbody>
							<tr>
								<td style="border-top: 0 none;"><h5>Nom de la
										configuration</h5></td>
								<td style="border-top: 0 none;">${configurationBean.config_name}</td>
							</tr>
							<tr>
								<td><h5>Encodage de caractères</h5></td>
								<td>${configurationBean.encoding}</td>
							</tr>
							<tr>
								<td><h5>Séparateur de colonne</h5></td>
								<td>${configurationBean.display_field_separator}</td>
							</tr>
							<tr>
								<td><h5>Caractère d'échappement</h5></td>
								<td>${configurationBean.display_escape_char}</td>
							</tr>
							<c:if
								test="${configurationBean.ignore_empty_lines or configurationBean.get_titles_from_first_line}">
								<tr>
									<td><h5>Options</h5></td>
									<td><ul>
											<c:if test="${configurationBean.ignore_empty_lines}">
												<li>Ignorer les lignes vides</li>
											</c:if>

											<c:if test="${configurationBean.get_titles_from_first_line}">
												<li>Extraire les noms des colonnes à partir de la
													première ligne du fichier</li>
											</c:if>
										</ul></td>
								</tr>
							</c:if>
							<c:if test="${configurationBean.limit_by_line_number}">
								<tr>
									<td><h5>Limiter par nombre de lignes</h5></td>
									<td>${configurationBean.number_of_lines}</td>
								</tr>
							</c:if>
							<c:if test="${configurationBean.limit_by_interval}">
								<tr>
									<td><h5>Limiter par intervalles de lignes</h5></td>
									<td>
										<h6>Liste des intervalles:</h6>
										<div class="span4" style="margin-left: 0;">
											<ul>
												<c:forEach var="interval"
													items="${configurationBean.intervalSelectItems}">
													<li>Intevalle ${interval.key + 1} : <span
														style="font-style: italic;"> ${interval.value}</span></li>
												</c:forEach>
											</ul>
										</div>
									</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>