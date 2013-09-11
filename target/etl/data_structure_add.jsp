<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Ajouter strucuture de données</title>
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

		$('#btn-display-local-structure').click(function() {
			$("#form-local").attr("action", "display_local_data_structure");
			$("#form-local").submit();
		});

		$('#btn-save-local-structure').click(function() {
			$("#form-local").attr("action", "save_local_structure");
			$("#form-local").submit();
		});

		$('#btn-connect-ftp').click(function() {
			$("#form-ftp").attr("action", "connect_ftp");
			$("#form-ftp").submit();
		});

		$('#btn-display-ftp-structure').click(function() {
			$("#form-ftp").attr("action", "display_ftp_data_structure");
			$("#form-ftp").submit();
		});

		$('#btn-save-ftp-structure').click(function() {
			$("#form-ftp").attr("action", "save_ftp_data_structure");
			$("#form-ftp").submit();
		});

		$('#btn-save-manual-structure').click(function() {
			$("#form-manual").attr("action", "save_manual_data_structure");
			$("#form-manual").submit();
		});
	});
</script>
</head>
<body>
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
				<div class="legend">Ajouter une structure de données</div>
				<div class="tabbable">
					<!-- Only required for left/right tabs -->
					<ul class="nav nav-tabs">
						<li
							<c:if test="${not((not empty tab_number) and (tab_number != 1))}">
						class="active" </c:if>>
							<a href="#tab1" data-toggle="tab">Détecter à partir d'un
								fichier en local</a>
						</li>
						<li
							<c:if test="${(not empty tab_number) and (tab_number == 2)}">
 								class="active" </c:if>>
							<a href="#tab2" data-toggle="tab">Détecter à partir d'un
								fichier sur FTP</a>
						</li>
						<li
							<c:if test="${(not empty tab_number) and (tab_number == 3)}">
 								class="active" </c:if>>
							<a href="#tab3" data-toggle="tab">Définir manuellement</a>
						</li>
					</ul>
					<div class="tab-content">
						<div
							class="tab-pane 
							<c:if test="${not((not empty tab_number) and (tab_number != 1))}">
								active
							</c:if> "
							id="tab1">
							<form:form id="form-local" commandName="DataStructureBean"
								cssClass="form-horizontal" enctype="multipart/form-data"
								method="POST" action="">
								<fieldset>
									<c:if test="${not empty msg_error_local}">
										<div class="alert alert-error">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-important"
												style="margin-right: 20px;">Erreur</span><strong>${msg_error_local}</strong>
										</div>
									</c:if>
									<c:if test="${not empty msg_succcess_local}">
										<div class="alert alert-success">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-success" style="margin-right: 20px;">Succès</span><strong>${msg_succcess_local}</strong>
										</div>
									</c:if>
									<div class="row" style="margin-left: 0;">
										<div class="span8">
											<div class="control-group">
												<label class="control-label-wide"><strong>Nom
														:</strong></label>
												<div class="controls-wide">
													<form:input path="name" />
												</div>
											</div>
											<div class="control-group">
												<label class="control-label-wide"><strong>Fichier
														:</strong></label>
												<div class="controls-wide">
													<form:select path="selectedLocalFile"
														items="${filesSelectionList}" multiple="false" />
												</div>
											</div>
											<div class="control-group">
												<label class="control-label-wide"><strong>Configuration
														:</strong></label>
												<div class="controls-wide">
													<form:select path="selectedconfiguration"
														items="${ConfigurationsSelectionList}" multiple="false" />
													<button class="btn" type="submit"
														id="btn-display-local-structure" style="margin-left: 5px;">Valider</button>
												</div>
											</div>
										</div>
									</div>
									<c:if test="${not empty LocalColumnsList}">
										<div class="row" style="margin-left: 10px;">
											<div class="span2">
												<a class="btn btn-mini btn-success" href="column_local_add">
													<i class="icon-plus-sign icon-white"></i> Ajouter une
													colonne
												</a>
											</div>
										</div>
										<div class="span11" style="width: 1100px; margin-left: 5px;">
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
														<th></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach var="column" items="${LocalColumnsList}">
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
															<td style="width: 90px;"><a class="btn btn-mini"
																href="${column.edit_url}"> <i class="icon-edit"></i>
																	Modifier
															</a></td>
															<td style="width: 90px;"><a
																class="btn btn-mini btn-danger remove-link"
																href="${column.delete_url}"> <i
																	class="icon-trash icon-white"></i> Supprimer
															</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:if>
								</fieldset>
								<div class="form-actions">
									<c:if test="${not empty LocalColumnsList}">
										<input class="btn" type="reset" name="reset" value="Annuler" />
										<input class="btn btn-primary" type="submit"
											id="btn-save-local-structure" name="action"
											value="Sauvegarder" />
									</c:if>
								</div>
							</form:form>
						</div>
						<div
							class="tab-pane 
						<c:if test="${(not empty tab_number) and (tab_number == 2)}">
								active
							</c:if> "
							id="tab2">
							<form:form id="form-ftp" commandName="DataStructureBean"
								cssClass="form-horizontal" enctype="multipart/form-data"
								method="POST" action="">
								<fieldset>
									<c:if test="${not empty msg_error_ftp}">
										<div class="alert alert-error">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-important"
												style="margin-right: 20px;">Erreur</span><strong>${msg_error_ftp}</strong>
										</div>
									</c:if>
									<c:if test="${not empty msg_succcess_ftp}">
										<div class="alert alert-success">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-success" style="margin-right: 20px;">Succès</span><strong>${msg_succcess_ftp}</strong>
										</div>
									</c:if>
									<div class="row" style="margin-left: 0;">
										<div class="span8">
											<div class="control-group">
												<label class="control-label-wide"><strong>Nom
														:</strong></label>
												<div class="controls-wide">
													<form:input path="name" />
												</div>
											</div>
											<div class="control-group">
												<label class="control-label-wide"><strong>Connexion
														FTP :</strong></label>
												<div class="controls-wide">
													<form:select path="selectedFTPConnection"
														items="${ftpConnectionList}" multiple="false" />
													<button id="btn-connect-ftp" type="submit" class="btn"
														style="margin-left: 5px;">Connexion</button>
												</div>
											</div>
											<c:if test="${not empty FTPFilesSelectionList}">
												<div class="control-group">
													<label class="control-label-wide"><strong>Fichier
															:</strong></label>
													<div class="controls-wide">
														<form:select path="selectedFTPFile"
															items="${FTPFilesSelectionList}" multiple="false" />
													</div>
												</div>
												<div class="control-group">
													<label class="control-label-wide"><strong>Configuration
															:</strong></label>
													<div class="controls-wide">
														<form:select path="selectedconfiguration"
															items="${ConfigurationsSelectionList}" multiple="false" />
														<button class="btn" type="submit"
															id="btn-display-ftp-structure" style="margin-left: 5px;">Valider</button>
													</div>
												</div>
											</c:if>
										</div>
									</div>
									<c:if test="${not empty FTPColumnsList}">
										<div class="row" style="margin-left: 10px;">
											<div class="span2">
												<a class="btn btn-mini btn-success" href="column_ftp_add">
													<i class="icon-plus-sign icon-white"></i> Ajouter une
													colonne
												</a>
											</div>
										</div>
										<div class="span11" style="width: 1100px; margin-left: 5px;">
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
														<th></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach var="column" items="${FTPColumnsList}">
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
															<td style="width: 90px;"><a class="btn btn-mini"
																href="${column.edit_url}"> <i class="icon-edit"></i>
																	Modifier
															</a></td>
															<td style="width: 90px;"><a
																class="btn btn-mini btn-danger remove-link"
																href="${column.delete_url}"> <i
																	class="icon-trash icon-white"></i> Supprimer
															</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:if>
								</fieldset>
								<div class="form-actions">
									<c:if test="${not empty FTPColumnsList}">
										<input class="btn" type="reset" name="reset" value="Annuler" />
										<input class="btn btn-primary" type="submit"
											id="btn-save-ftp-structure" name="action" value="Sauvegarder" />
									</c:if>
								</div>
							</form:form>
						</div>
						<div
							class="tab-pane 
						<c:if test="${(not empty tab_number) and (tab_number == 3)}">
							active
						</c:if> "
							id="tab3">
							<form:form id="form-manual" commandName="DataStructureBean"
								cssClass="form-horizontal" enctype="multipart/form-data"
								method="POST" action="">
								<fieldset>
									<c:if test="${not empty msg_error_manual}">
										<div class="alert alert-error">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-important"
												style="margin-right: 20px;">Erreur</span><strong>${msg_error_manual}</strong>
										</div>
									</c:if>
									<c:if test="${not empty msg_succcess_manual}">
										<div class="alert alert-success">
											<button class="close" data-dismiss="alert">×</button>
											<span class="label label-success" style="margin-right: 20px;">Succès</span><strong>${msg_succcess_manual}</strong>
										</div>
									</c:if>
									<div class="row" style="margin-left: 0;">
										<div class="span8">
											<div class="control-group">
												<label class="control-label-wide"><strong>Nom
														:</strong></label>
												<div class="controls-wide">
													<form:input path="name" />
												</div>
											</div>
										</div>
									</div>
									<div class="row" style="margin-left: 10px;">
										<div class="span2">
											<a class="btn btn-mini btn-success" href="column_manual_add">
												<i class="icon-plus-sign icon-white"></i> Ajouter une
												colonne
											</a>
										</div>
									</div>
									<c:if test="${not empty ManualColumnsList}">
										<div class="span11" style="width: 1100px; margin-left: 5px;">
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
														<th></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach var="column" items="${ManualColumnsList}">
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
															<td style="width: 90px;"><a class="btn btn-mini"
																href="${column.edit_url}"> <i class="icon-edit"></i>
																	Modifier
															</a></td>
															<td style="width: 90px;"><a
																class="btn btn-mini btn-danger remove-link"
																href="${column.delete_url}"> <i
																	class="icon-trash icon-white"></i> Supprimer
															</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
									</c:if>
								</fieldset>
								<div class="form-actions">
									<c:if test="${not empty ManualColumnsList}">
										<input class="btn" type="reset" name="reset" value="Annuler" />
										<input class="btn btn-primary" type="submit"
											id="btn-save-manual-structure" name="action"
											value="Sauvegarder" />
									</c:if>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>