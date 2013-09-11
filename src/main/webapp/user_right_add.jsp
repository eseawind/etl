<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Ajouter un droit utilisateur</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
<link rel="stylesheet" href="css/multi-select.css">
<link rel="stylesheet" href="css/style.css">

<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->

<script src="js/jquery/jquery-1.7.2.min.js"></script>
<script src="js/js-bootstrap/bootstrap.min.js"></script>
<script src="js/jquery.multi-select.js"></script>


<script>
	$(function() {
		$('#multiselect')
				.multiSelect(
						{
							selectableHeader : "<div class='custom-header'>Liste des droits d'accès</div>",
							selectedHeader : "<div class='custom-header'>Sélection</div>"

						});
		$('#select-all').click(function() {
			$('#multiselect').multiSelect('select_all');
			return false;
		});
		$('#deselect-all').click(function() {
			$('#multiselect').multiSelect('deselect_all');
			return false;
		});
	});
</script>

<style>
.custom-header {
	background: none repeat scroll 0 0 #000000;
	color: #FFFFFF;
	padding: 3px;
	text-align: center;
}
</style>

</head>
<body>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<form:form cssClass="form-horizontal" commandName="userRightBean"
					enctype="multipart/form-data" method="POST">
					<fieldset>
						<div class="legend">Ajouter un droit utilisateur</div>
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
						<div class="control-group">
							<label class="control-label-wide">Nom du droit
								utilisateur:</label>
							<div class="controls-wide">
								<form:input path="label" />
							</div>
						</div>
						<div style="margin-left: 40px;">
							<p>
								<strong>Droits d'accès :</strong>
							</p>
							<div class="well span10"
								style="background-color: #fff; margin-left: 0;">
								<div
									style="margin-bottom: 10px; text-align: right; width: 400px;">
									<a href='#' class="btn btn-mini" id='select-all'><i
										class="icon-tasks"></i> Tout sélectionner</a> <a href='#'
										class="btn btn-mini btn-danger" id='deselect-all'><i
										class="icon-ban-circle"></i> Tout désélectionner</a>
								</div>
								<form:select id="multiselect" path="selected_access_rights"
									items="${userRightBean.display_droits_acces}" />
							</div>
						</div>
					</fieldset>
					<div class="form-actions">
						<input class="btn" type="reset" value="Annuler" name="reset">
						<input class="btn btn-primary" type="submit" name="action"
							value="Sauvegarder" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>