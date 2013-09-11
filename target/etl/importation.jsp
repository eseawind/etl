<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Importaion</title>
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
		if (($('#use_local_files').is(":checked"))) {
			$('#ftp_connection_select_list').prop("disabled", true);
			$('#ftp_files_list').prop("disabled", true);
		} else {
			if (($('#use_ftp_files').is(":checked")))
				$('#local_files_list').prop("disabled", true);
		}

		$('#use_local_files').click(function() {
			$('#use_ftp_files').attr('checked', false);
			$('#ftp_connection_select_list').prop("disabled", true);
			$('#ftp_files_list').prop("disabled", true);
			var unchecked = !($(this).is(":checked"));
			$('#local_files_list').prop("disabled", unchecked);

		});

		$('#use_ftp_files').click(function() {
			$('#use_local_files').attr('checked', false);
			$('#local_files_list').prop("disabled", true);
			var unchecked = !($(this).is(":checked"));
			$('#ftp_connection_select_list').prop("disabled", unchecked);
			$('#ftp_files_list').prop("disabled", unchecked);
		});

		$('#btn-connect_ftp').click(function(e) {
			if (($('#use_ftp_files').is(":checked"))) {
				$("#importaion-form").attr("action", "import_connect_ftp");
				$("#importaion-form").submit();
			} else
				e.preventDefault();
		});

		$('#btn-connect_db').click(function(e) {
			$("#importaion-form").attr("action", "import_connect_db");
			$("#importaion-form").submit();
		});

		$('#btn-import_next_step').click(function(e) {
			$("#importaion-form").attr("action", "import_start_mapping");
			$("#importaion-form").submit();
		});
	});
</script>
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
				<div class="legend">Importation</div>
				<form:form cssClass="form-horizontal" commandName="importationBean"
					enctype="multipart/form-data" method="POST" id="importaion-form">
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
						<div class="row" style="margin-bottom: 15px; margin-left: 0;">
							<span class="badge">1</span> <span>Se connecter à la base
								de données et sélectionner une table pour importer les données
								dedans</span>
						</div>
						<div class="control-group">
							<label class="control-label-wide"><strong>Connexion
									à la base de données :</strong></label>
							<div class="controls-wide">
								<form:select path="selectedDBConnection"
									items="${importationBean.databaseConnectionList}"
									multiple="false" />
								<button class="btn btn-small" style="margin-left: 5px;"
									type="submit" id="btn-connect_db">Connexion</button>
							</div>
						</div>
						<c:if test="${not empty importationBean.databaseTablesList}">
							<div class="control-group">
								<label class="control-label-wide"><strong>Table
										de base de données :</strong></label>
								<div class="controls-wide">
									<form:select path="selectedDatabaseTable"
										items="${importationBean.databaseTablesList}" multiple="false" />
								</div>
							</div>
						</c:if>
						<div class="row" style="margin-bottom: 15px; margin-left: 0;">
							<span class="badge badge-success">2</span> <span>Choisir
								la configuration et la structure de données à utiliser</span>
						</div>
						<div class="control-group">
							<label class="control-label-wide"><strong>Configuration
									:</strong></label>
							<div class="controls-wide">
								<form:select path="selectedConfiguration"
									items="${importationBean.configurationList}" multiple="false" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label-wide"><strong>Structure
									de données :</strong></label>
							<div class="controls-wide">
								<form:select path="selectedDataStructure"
									items="${importationBean.dataStructureList}" multiple="false" />
							</div>
						</div>
						<div class="row" style="margin-bottom: 15px; margin-left: 0;">
							<span class="badge badge-warning">3</span> <span>Sélectionner
								le fichier de données à partir de la liste des fichiers locaux
								ou la liste des fichiers sur le serveur FTP en se connectant sur
								le FTP d'abord</span>
						</div>
						<p style="margin-left: 30px;">
							<strong>Sélectionner un fichier</strong>
						</p>
						<div class="row-fluid span11 well" style="background-color: #fff;">
							<div class="span4">
								<div class="control-group">
									<div class="controls" style="margin-left: 0px;">
										<label class="checkbox"> <form:checkbox
												path="use_local_files" id="use_local_files" /> Utiliser les
											fichiers locaux
										</label>
									</div>
								</div>
								<div class="control-group">
									<div>
										<label style="display: inline;">Fichier :</label>
										<form:select path="selectedLocalFile"
											items="${importationBean.localfilesList}" multiple="false"
											id="local_files_list" />
									</div>
								</div>
							</div>
							<div class="span5">
								<div class="control-group">
									<div class="">
										<label class="checkbox"> <form:checkbox
												path="use_ftp_files" id="use_ftp_files" />Utiliser des
											fichiers sur un serveur FTP
										</label>
									</div>
								</div>
								<div class="control-group" style="width: 420px;">
									<label style="display: inline;">Connexion FTP :</label>
									<form:select path="selectedFTPConnection"
										items="${importationBean.ftpConnectionsList}" multiple="false"
										id="ftp_connection_select_list" />
									<button class="btn btn-small" style="margin-left: 5px;"
										type="submit" id="btn-connect_ftp">Connexion</button>
								</div>
								<c:if test="${not empty importationBean.ftpFilesList}">
									<div class="control-group">
										<label style="display: inline; margin-left: 50px">Fichier
											:</label>
										<form:select path="selectedFTPFile"
											items="${importationBean.ftpFilesList}" multiple="false"
											id="ftp_files_list" />
									</div>
								</c:if>
							</div>
						</div>
					</fieldset>
					<div class="form-actions" style="padding-left: 1000px;">
						<input class="btn btn-primary" type="submit" name="submit"
							value="Suivant »" id="btn-import_next_step" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>