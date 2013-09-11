<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Détection des erreurs</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
<link rel="stylesheet" href="css/style.css">
<link type="text/css" href="css/jquery/themes/base/ui.all.css"
	rel="stylesheet" />
<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->
<script src="js/jquery/jquery-1.7.2.min.js"></script>
<script src="js/jquery/jquery-ui.min.js"></script>
<script src="js/js-bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.core.min.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.draggable.min.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.resizable.min.js"></script>
<script type="text/javascript" src="js/jquery/ui/ui.dialog.min.js"></script>
<style>
.form-horizontal .control-label-wide {
	float: left;
	padding-top: 5px;
	text-align: right;
	width: 220px;
}

.form-horizontal .controls-wide {
	margin-left: 230px;
}

.btn-small {
	font-size: 12px;
	line-height: 16px;
	padding: 3px 9px;
}
</style>
</head>
<body>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<div class="legend">Détection des erreurs</div>

				<form:form cssClass="form-horizontal" commandName="verificationBean"
					enctype="multipart/form-data" method="POST" id="verification-form">
					<fieldset>
						<form:errors path="*" cssClass="alert alert-error"
							cssStyle="font-weight: bold;" element="div" />
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
						<c:if test="${nb_primary_key_errors gt 0}">
								<ul class="breadcrumb">
									<li class="active"><span class="label label-important"
										style="margin-right: 20px;">Erreur</span><strong>Clé
											primaire nulle</strong></li>
								</ul>
								<div class="row-fluid" style="margin: 0;">
									<table class="table table-bordered table-condensed">
										<thead>
											<tr style="color: grey;">
												<th>#</th>
												<th>N° ligne| N° colonne</th>
												<th>Description de l'erreur</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="i" value="1" />
											<c:forEach var="error"
												items="${verificationBean.primatyKeyNullErrors}">
												<tr>
													<td width=20px;><c:out value='${i}' /></td>
													<td style="font-style: italic; width: 130px;">Ligne
														${error.line_nb} | colonne ${error.column_nb}</td>
													<td>${error.description}</td>
												</tr>
												<c:set var="i" value="${i+1}" />
											</c:forEach>
										</tbody>
									</table>
								</div>
						</c:if>
						<c:if test="${nb_pk_redanduncy_errors gt 0}">
								<ul class="breadcrumb">
									<li class="active"><span class="label label-important"
										style="margin-right: 20px;">Erreur</span><strong>Redondance de clé
											primaire</strong></li>
								</ul>
								<div class="row-fluid" style="margin: 0;">
									<table class="table table-bordered table-condensed">
										<thead>
											<tr style="color: grey;">
												<th>#</th>
												<th>N° ligne| N° colonne</th>
												<th>Description de l'erreur</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="i" value="1" />
											<c:forEach var="error"
												items="${verificationBean.pkRedundancyErrors}">
												<tr>
													<td width=20px;><c:out value='${i}' /></td>
													<td style="font-style: italic; width: 130px;">Ligne
														${error.line_nb} | colonne ${error.column_nb}</td>
													<td>${error.description}</td>
												</tr>
												<c:set var="i" value="${i+1}" />
											</c:forEach>
										</tbody>
									</table>
								</div>
						</c:if>
						<c:if test="${nb_type_mismatch_errors gt 0}">
								<ul class="breadcrumb">
									<li class="active"><span class="label label-important"
										style="margin-right: 20px;">Erreur</span><strong>Type de donnée incompatible</strong></li>
								</ul>
								<div class="row-fluid" style="margin: 0;">
									<table class="table table-bordered table-condensed">
										<thead>
											<tr style="color: grey;">
												<th>#</th>
												<th>N° ligne| N° colonne</th>
												<th>Description de l'erreur</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="i" value="1" />
											<c:forEach var="error"
												items="${verificationBean.typeMismatchErrors}">
												<tr>
													<td width=20px;><c:out value='${i}' /></td>
													<td style="font-style: italic; width: 130px;">Ligne
														${error.line_nb} | colonne ${error.column_nb}</td>
													<td>${error.description}</td>
												</tr>
												<c:set var="i" value="${i+1}" />
											</c:forEach>
										</tbody>
									</table>
								</div>
						</c:if>
						<c:if test="${nb_max_lenght_errors gt 0}">
								<ul class="breadcrumb">
									<li class="active"><span class="label label-important"
										style="margin-right: 20px;">Erreur</span><strong>Longueur maximale autorisée dépassée</strong></li>
								</ul>
								<div class="row-fluid" style="margin: 0;">
									<table class="table table-bordered table-condensed">
										<thead>
											<tr style="color: grey;">
												<th>#</th>
												<th>N° ligne| N° colonne</th>
												<th>Description de l'erreur</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="i" value="1" />
											<c:forEach var="error"
												items="${verificationBean.maximumLenghtErrors}">
												<tr>
													<td width=20px;><c:out value='${i}' /></td>
													<td style="font-style: italic; width: 130px;">Ligne
														${error.line_nb} | colonne ${error.column_nb}</td>
													<td>${error.description}</td>
												</tr>
												<c:set var="i" value="${i+1}" />
											</c:forEach>
										</tbody>
									</table>
								</div>
						</c:if>
						<c:if test="${nb_out_of_interval_errors gt 0}">
								<ul class="breadcrumb">
									<li class="active"><span class="label label-important"
										style="margin-right: 20px;">Erreur</span><strong>Valeur hors de l'intervalle autorisé</strong></li>
								</ul>
								<div class="row-fluid" style="margin: 0;">
									<table class="table table-bordered table-condensed">
										<thead>
											<tr style="color: grey;">
												<th>#</th>
												<th>N° ligne| N° colonne</th>
												<th>Description de l'erreur</th>
											</tr>
										</thead>
										<tbody>
											<c:set var="i" value="1" />
											<c:forEach var="error"
												items="${verificationBean.outOfIntervalErrors}">
												<tr>
													<td width=20px;><c:out value='${i}' /></td>
													<td style="font-style: italic; width: 130px;">Ligne
														${error.line_nb} | colonne ${error.column_nb}</td>
													<td>${error.description}</td>
												</tr>
												<c:set var="i" value="${i+1}" />
											</c:forEach>
										</tbody>
									</table>
								</div>
						</c:if>
					</fieldset>
					<div class="form-actions">
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>