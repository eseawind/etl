<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Gestion des configurations</title>
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

<script>
	$(function() {
		$("#dialog:ui-dialog").dialog("destroy");
		$("#dialog-confirm").hide();
		$(".remove-link").click(function(e) {
			e.preventDefault();
			var targetUrl = $(this).attr("href");
			$("#dialog-confirm").dialog({
				autoOpen : false,
				bgiframe : true,
				resizable : false,
				draggable : false,
				height : 156,
				width : 440,
				modal : true,
				overlay : {
					backgroundColor : '#000',
					opacity : 0.5
				},
				buttons : {
					'Annuler' : function() {
						$(this).dialog("close");
					},
					'Supprimer' : function() {
						window.location.href = targetUrl;
					}
				}
			});

			$("#dialog-confirm").dialog("open");
		});

	});
</script>

</head>
<body style="font-size: 12px;">
	<div id="dialog-confirm" title="Confirmer la suppression">
		<p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 0 7px 20px 0;"></span>Cet élément sera
			définitivement supprimé et ne peut pas être récupéré. Etes-vous sûr?
		</p>
	</div>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<div class="legend">Gestion des configurations</div>
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
				<div class="row" style="margin-left: 10px;">
					<div class="span2">
						<a class="btn btn-mini" href="configuration_add">
							<i class="icon-plus-sign"></i> Ajouter une
							configuration
						</a>
					</div>
				</div>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>Nom de la configuration</th>
							<th>Encodage de caractères</th>
							<th>Séparateur de colonne</th>
							<th>Caractère d'échappement</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="configuration" items="${configurationList}">
							<tr>
								<td>${configuration.config_name}</td>
								<td>${configuration.encoding}</td>
								<td>${configuration.display_field_separator}</td>
								<td>${configuration.display_escape_char}</td>
								<td><a class="btn btn-mini btn-info"
									href="${configuration.details_url}"> <i
										class="icon-info-sign icon-white"></i> Plus de détails &#187;
								</a></td>
								<td><a class="btn btn-mini"
									href="${configuration.edit_url}"> <i class="icon-edit"></i>
										Modifier
								</a></td>
								<td><a class="btn btn-mini btn-danger remove-link"
									href="${configuration.delete_url}"> <i
										class="icon-trash icon-white"></i> Supprimer
								</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>