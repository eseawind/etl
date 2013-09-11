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
		$('#btn-import_previous_step').click(function(e) {
						$("#importaion-form").attr("action", "importation");
						$("#importaion-form").submit();
		});

		$('#btn-import_next_step').click(function(e) {
			$("#importaion-form").attr("action", "begin_importation");
			$("#importaion-form").submit();
		});

	});
</script>
</head>
<body>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid" id="activity_pane">
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
						<c:if test="${not empty importationBean.columnsList}">
							<div class="span11" style="width: 1100px; margin-left: 5px;">
								<table class="table table-bordered table-striped">
									<thead style="font-size: 12px;">
										<tr>
											<th>Ordre</th>
											<th>Nom</th>
											<th>Type</th>
											<th>Longueur</th>
											<th>Valeur par défaut</th>
											<th>Valeur min</th>
											<th>Valeur max</th>
											<th style="width: 60px;">Nullable</th>
											<th style="width: 80px;">Clé primaire</th>
											<th style="width: 160px;">Colonne base de données</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="column" items="${importationBean.columnsList}"
											varStatus="columnBean">
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
												<td><form:select
														path="columnsList[${columnBean.index}].db_table_column_index"
														items="${importationBean.databaseTableColumnsList}"
														multiple="false" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:if>
					</fieldset>
					<div class="form-actions" style="padding-left: 950px;">
						<input class="btn" type="submit" name="submit" value="Précident"
							id="btn-import_previous_step" /> <input class="btn btn-primary"
							type="submit" name="submit" value="Importer"
							id="btn-import_next_step" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>