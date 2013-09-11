<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Modifier colonne</title>
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
				<div class="legend">Modifier colonne</div>
				<form:form id="form" cssClass="form-horizontal"
					commandName="ColumnBean" enctype="multipart/form-data"
					method="POST" action="${actionUrl}">
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
						<div class="span8">
							<div class="control-group">
								<label class="control-label-wide">Ordre dans le fichier:</label>
								<div class="controls-wide">
									<form:input path="order" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Nom :</label>
								<div class="controls-wide">
									<form:input path="name" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Type:</label>
								<div class="controls-wide">
									<form:select path="selectedType"
										items="${ColumnBean.type_list}" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Longueur:</label>
								<div class="controls-wide">
									<form:input path="lenght" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Valeur par défaut:</label>
								<div class="controls-wide">
									<form:input path="default_value" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Valeur min:</label>
								<div class="controls-wide">
									<form:input path="min_value" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Valeur max:</label>
								<div class="controls-wide">
									<form:input path="max_value" />
								</div>
							</div>
							<div class="control-group">
								<div class="controls-wide">
									<label class="checkbox"> <form:checkbox path="nullable" />Nullable
									</label>
								</div>
							</div>
							<div class="control-group">
								<div class="controls-wide">
									<label class="checkbox"> <form:checkbox
											path="primary_Key" />Clé primaire
									</label>
								</div>
							</div>
						</div>
					</fieldset>
					<div class="form-actions">
						<input class="btn" type="reset" name="reset" value="Annuler" /> <input
							class="btn btn-primary" type="submit" name="action"
							value="Sauvegarder" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>