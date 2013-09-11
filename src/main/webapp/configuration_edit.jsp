<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Modifier configuration</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
<link rel="stylesheet" href="css/style.css">

<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->

<script src="js/jquery/jquery-1.7.2.min.js"></script>
<script src="js/js-bootstrap/bootstrap.min.js"></script>

<script type="text/javascript">
	$(function() {
		init();

		$(':input').click(function() {
			$('.alert').hide();
		});

		$("form").bind("reset", function() {
			setTimeout(function() {
				update_field_sep();
				update_row_sep();
				update_escape_char();
				$("#multi-select-interval").empty();
				$('#limit_lines').prop("disabled", true);
				$('#use_limit_lines').attr('checked', false);
				$('#use_intervals').attr('checked', false);
				$('#multi-select-interval').prop("disabled", true);
				$("#add-interval-bt").attr("href", "");
			}, 100);
		});

		$('#sep_colonne').change(function() {
			update_field_sep();
		});
		$('#sep_ligne').change(function() {
			update_row_sep();
		});
		$('#escape_char').change(function() {
			update_escape_char();
		});

		$('#use_limit_lines').click(function() {
			$('#use_intervals').attr('checked', false);
			$('#multi-select-interval').prop("disabled", true);
			$("#add-interval-bt").attr("href", "");

			var unchecked = !($(this).is(":checked"));
			if (unchecked)
				$('#limit_lines').val('');
			$('#limit_lines').prop("disabled", unchecked);

		});

		$('#use_intervals').click(function() {
			$('#limit_lines').val('');
			$('#limit_lines').prop("disabled", true);
			$('#use_limit_lines').attr('checked', false);
			var unchecked = !($(this).is(":checked"));
			$('#multi-select-interval').prop("disabled", unchecked);

			if (unchecked)
				$("#add-interval-bt").attr("href", "");
			else
				$("#add-interval-bt").attr("href", "#modal-form");
		});

		$('#remove-items-bt').click(function(event) {
			var is_checked = $('#use_intervals').is(":checked");
			if (is_checked) {
				$("#multi-select-interval option:selected").each(function() {
					var id = $(this).val();
					$.getJSON("ajax/delete_interval", {
						id : id,
					}, function(data) {
						$("#multi-select-interval option:selected").remove();
					});
				});
			} else
				event.preventDefault();
		});

		$('#start_line_num').click(function() {
			$("#error-interval").hide();
		});

		$('#end_line_num').click(function() {
			$("#error-interval").hide();
		});

		$('#modal-form').on('hidden', function() {
			$("#error-interval").hide();
		});

	});

	function init() {
		var selected_field_separator = $('#sep_colonne').val();
		if (selected_field_separator == 1)
			$('#carac_sep_colonne').val(';');

		var selected_esc_char = $('#escape_char').val();
		if (selected_esc_char == 1) {
			$('#carac_escape_char').val('');
			$('#carac_escape_char').prop("disabled", true);
		}

		var use_intervals_checked = $('#use_intervals').is(":checked");
		if (!use_intervals_checked) {
			$('#multi-select-interval').prop("disabled", true);
			$("#add-interval-bt").attr("href", "");
		} else
			$("#add-interval-bt").attr("href", "#modal-form");

		var use_limit_lines_checked = $('#use_limit_lines').is(":checked");
		if (!use_limit_lines_checked) {
			$('#limit_lines').val('');
			$('#limit_lines').prop("disabled", true);

		}
	}

	function save_interval() {
		var start_val = $('#start_line_num').val();
		var end_val = $('#end_line_num').val();
		var is_valid = validate_interval(start_val, end_val);
		if (is_valid) {
			$.getJSON("ajax/add_interval", {
				start : start_val,
				end : end_val
			}, function(data) {
				$("#multi-select-interval").empty();
				$.each(data,
						function(key, row) {
							var id = row['id'];
							var val = row['start_line_nb'] + ' - '
									+ row['end_line_nb'];
							var option = new Option(val, id);
							$(option).html(val);
							$("#multi-select-interval").append(option);
						});
				closeDialog();
			});
		} else {
			$("#error-interval").show();
		}
	};

	function closeDialog() {
		$('#start_line_num').val('');
		$('#end_line_num').val('');
		$('#modal-form').modal('hide');
	};

	function isNumber(num) {
		return (typeof num == 'string' || typeof num == 'number')
				&& !isNaN(num - 0) && num !== '';
	};

	function validate_interval(start_val, end_val) {
		if (!(isNumber(start_val) && isNumber(end_val)))
			return false;
		if ((parseInt(end_val) < parseInt(start_val))
				|| (parseInt(end_val) == parseInt(start_val)))
			return false;
		return true;
	}

	function update_field_sep() {
		var sep_value = '";"';
		var selected_field_separator = $('#sep_colonne').val();
		switch (selected_field_separator) {
		case '1':
			sep_value = ';';
			$('#carac_sep_colonne').val(sep_value);
			break;
		case '2':
			sep_value = ',';
			$('#carac_sep_colonne').val(sep_value);
			break;
		case '3':
			sep_value = '\\t';
			$('#carac_sep_colonne').val(sep_value);
			break;
		case '4':
			sep_value = ' ';
			$('#carac_sep_colonne').val(sep_value);
			break;
		case '5':
			sep_value = '';
			$('#carac_sep_colonne').val(sep_value);
			$('#carac_sep_colonne').focus();
			break;
		}
	}

	function update_escape_char() {
		var esc_char_value = '';
		var selected_esc_char = $('#escape_char').val();
		switch (selected_esc_char) {
		case '1':
			esc_char_value = '';
			$('#carac_escape_char').val(esc_char_value);
			$('#carac_escape_char').prop("disabled", true);
			break;
		case '2':
			esc_char_value = '\\';
			$('#carac_escape_char').val(esc_char_value);
			$('#carac_escape_char').prop("disabled", false);
			break;
		case '3':
			esc_char_value = "'";
			$('#carac_escape_char').val(esc_char_value);
			$('#carac_escape_char').prop("disabled", false);
			break;
		case '4':
			esc_char_value = '';
			$('#carac_escape_char').val(esc_char_value);
			$('#carac_escape_char').prop("disabled", false);
			$('#carac_escape_char').focus();
			break;
		}
	}
</script>

</head>
<body>
	<div class="modal hide fade" id="modal-form">
		<div class="modal-header">
			<a href="#" class="close" data-dismiss="modal">&times;</a>
			<h4>Ajouter un intervalle de lignes</h4>
		</div>
		<div class="modal-body">
			<form class="form-horizontal">
				<div id="error-interval" class="alert alert-error"
					style="display: none;">
					<span class="label label-important" style="margin-right: 10px;">Attention</span>
					<strong> L'intervalle saisi est invalide.</strong>
				</div>
				<div class="control-group">
					<label class="control-label-wide">Intervalle: </label>
					<div class="controls" style="margin-left: 200px;">
						<input type="text" placeholder="début" class="input-mini"
							id="start_line_num" /> - <input type="text" placeholder="fin"
							class="input-mini" id="end_line_num" />
					</div>
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" onclick="closeDialog();">Annuler</a> <a
				href="#" class="btn btn-primary" onclick="save_interval();">Ajouter</a>
		</div>
	</div>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<div class="legend">Modifier configuration</div>
				<form:form id="form" cssClass="form-horizontal"
					commandName="configurationBean" enctype="multipart/form-data"
					method="POST">
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
								<label class="control-label-wide">Nom de la
									configuration:</label>
								<div class="controls-wide">
									<form:input path="config_name" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Encodage de
									caractères:</label>
								<div class="controls-wide">
									<form:select path="selected_encoding"
										items="${configurationBean.encoding_list}" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Séparateur de colonne:</label>
								<div class="controls" style="margin-left: 200px;">
									<form:select path="selected_field_separator"
										items="${configurationBean.field_separator_list}"
										cssClass="input-medium" id="sep_colonne" />
									<form:input path="display_field_separator" cssClass="input-mini"
										cssStyle="margin-left:20px;" id="carac_sep_colonne" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label-wide">Caractère
									d'échappement:</label>
								<div class="controls" style="margin-left: 200px;">
									<form:select path="selected_escape_char"
										items="${configurationBean.escape_char_list}"
										cssClass="input-medium" id="escape_char" />
									<form:input path="display_escape_char" cssClass="input-mini"
										cssStyle="margin-left:20px;" id="carac_escape_char" />
								</div>
							</div>
							<div class="control-group">
								<div class="controls-wide">
									<label class="checkbox"> <form:checkbox
											path="ignore_empty_lines" />Ignorer les lignes vides
									</label>
								</div>
							</div>
							<div class="control-group">
								<div class="controls-wide">
									<label class="checkbox"> <form:checkbox
											path="get_titles_from_first_line" />Extraire les noms des
										colonnes à partir de la première ligne du fichier
									</label>
								</div>
							</div>
							<p>
								<strong>Limiter le nombre de lignes</strong>
							</p>
							<div class="row-fluid" style="width: 900px;">
								<div class="span12">
									<div class="row-fluid">
										<div class="span4 well" style="background-color: #fff;">
											<div class="control-group">
												<p class="help-block">Si le nombre de lignes doit être
													limitée, indiquez ce nombre</p>
												<label style="margin-top: 5px;">Limiter<form:checkbox
														path="limit_by_line_number"
														cssStyle="margin-left: 10px; margin-top: 0;"
														id="use_limit_lines" />
												</label>
												<form:input type="text" path="number_of_lines"
													placeholder="nombre de lignes" cssClass="input-medium"
													id="limit_lines" />
											</div>
										</div>
										<div class="span8 well" style="background-color: #fff;">
											<div class="control-group">
												<p class="help-block">Si l'extraction des données doit
													être limité à un intervalle de ligne, indiquez cet
													intervalle</p>
												<div class="row" style="margin-left: 0px; margin-top: 5px;">
													<label style="margin-top: 5px;">Utiliser des
														intervalles <form:checkbox id="use_intervals"
															path="limit_by_interval"
															cssStyle="margin-left: 10px; margin-top: 0;" />
													</label>
													<div class="span2 interval-selection" style="width: 120px;">
														<label class="control-label-wide" style="width: 120px;">Liste
															des intervalles:</label>
														<form:select id="multi-select-interval" path=""
															items="${configurationBean.intervalSelectItems}"
															multiple="multiple" cssStyle="width: 120px;" />
													</div>
													<div class="span3 interval-selection">
														<br /> <a id="add-interval-bt" class="btn btn-mini"
															data-toggle="modal" href="" style="margin-top: 10px;">
															<i class="icon-plus"></i> Ajouter
														</a> <br /> <a id="remove-items-bt"
															class="btn btn-mini btn-danger" style="margin-top: 5px;">
															<i class="icon-remove"></i> Supprimer
														</a>
													</div>
												</div>
												<p style="margin-top: 5px;">
													<strong>Note: </strong>L'utilisation de cet option
													désactive automatiquement l'option ci-dessus.
												</p>
											</div>
										</div>
									</div>
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