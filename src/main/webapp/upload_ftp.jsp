<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>Gestion des fichiers via FTP</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="css/style.css">
<!-- Bootstrap styles for responsive website layout, supporting different screen sizes -->
<link rel="stylesheet" href="css/bootstrap-responsive.min.css">
<!-- Bootstrap CSS fixes for IE6 -->
<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!-- Bootstrap Image Gallery styles -->
<link rel="stylesheet" href="css/bootstrap-image-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/jquery.fileupload-ui.css">

<!-- Shim to make HTML5 elements usable in older Internet Explorer versions -->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->

<!-- jQuery  -->
<script src="js/jquery/jquery-1.7.2.min.js"></script>
<script src="js/jquery/jquery.ui.widget.js"></script>

<script src="js/jquery-file-upload/tmpl.min.js"></script>
<script src="js/jquery-file-upload/load-image.min.js"></script>
<script src="js/jquery-file-upload/canvas-to-blob.min.js"></script>

<!-- Bootstrap -->
<script src="js/js-bootstrap/bootstrap.min.js"></script>
<script src="js/js-bootstrap/bootstrap-image-gallery.min.js"></script>

<script src="js/jquery-file-upload/jquery.iframe-transport.js"></script>

<!-- jQuery File Upload -->
<script src="js/jquery-file-upload/jquery.fileupload.js"></script>
<script src="js/jquery-file-upload/jquery.fileupload-fp.js"></script>
<script src="js/jquery-file-upload/jquery.fileupload-ui.js"></script>
<script src="js/jquery-file-upload/locale.js"></script>
<script src="js/jquery-file-upload/main.js"></script>

<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE8+ -->
<!--[if gte IE 8]><script src="js/jquery-file-upload/cors/jquery.xdr-transport.js"></script><![endif]-->

</head>
<body>
	<jsp:include page="/inc/menu-header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<jsp:include page="/inc/menu-left.jsp" />
			<div class="span9">
				<div class="legend" style="margin-bottom: 15px;">Gestion des fichiers
					via FTP</div>
				<div>
					<form:form commandName="UplaodFTPBean" class="form-inline"
						method="POST">
						<form:errors path="*" cssClass="alert alert-error"
							cssStyle="font-weight: bold; width: 500px;" element="div" />
						<c:if test="${not empty msg_error}">
							<div class="alert alert-error" style="width: 500px;">
								<button class="close" data-dismiss="alert">×</button>
								<strong>${msg_error}</strong>
							</div>
						</c:if>
						<c:if test="${not empty msg_succcess}">
							<div class="alert alert-success" style="width: 500px;">
								<button class="close" data-dismiss="alert">×</button>
								<strong>${msg_succcess}</strong>
							</div>
						</c:if>
						<div class="row" style="margin-left: 20px;">
							<label class="control-label"><strong> Connexion
									FTP :</strong></label>
							<form:select path="selectedFTPConnection"
								items="${ftpConnectionList}" multiple="false" />
							<button type="submit" class="btn" style="margin-left: 5px;">Connexion</button>
						</div>
					</form:form>
				</div>
				<c:if test="${isConnectedToFTP}">
					<form:form commandName="FileUploadBean" id="fileupload"
						method="POST" action="upload_ftp_form"
						enctype="multipart/form-data">
						<div class="row fileupload-buttonbar span10">
							<div class="span7">
								<span class="btn btn-success fileinput-button"> <i
									class="icon-plus icon-white"></i> <span>Ajouter</span> <form:input
										path="uploadedFile" type="file" name="files[]" id="files[]" />
								</span>
								<button type="submit" class="btn btn-primary start">
									<i class="icon-upload icon-white"></i> <span>Démarrer le
										téléchargement</span>
								</button>
								<button type="reset" class="btn btn-warning cancel">
									<i class="icon-ban-circle icon-white"></i> <span>Annuler</span>
								</button>
								<button type="button" class="btn btn-danger delete">
									<i class="icon-trash icon-white"></i> <span>Supprimer</span>
								</button>
								<input type="checkbox" class="toggle">
							</div>
							<!-- The global progress information -->
							<div class="span5 fileupload-progress fade">
								<!-- The global progress bar -->
								<div class="progress progress-success progress-striped active">
									<div class="bar" style="width: 0%;"></div>
								</div>
								<div class="progress-extended">&nbsp;</div>
							</div>
						</div>
						<div class="fileupload-loading"></div>
						<br>
						<table class="table table-striped">
							<tbody class="files" data-toggle="modal-gallery"
								data-target="#modal-gallery"></tbody>
						</table>
					</form:form>
					<br>
					<div class="well">
						<ul>
							<li>Seuls les fichiers texte et CSV <strong>(CSV,
									TXT)</strong> sont autorisés.
							</li>
							<li>Vous pouvez faire glisser &amp; déposer des fichiers
								depuis votre ordinateur sur cette page avec Google Chrome,
								Mozilla Firefox et Apple Safari.</li>
						</ul>
					</div>
				</c:if>
			</div>
		</div>
	</div>
	<!-- modal-gallery is the modal dialog used for the image gallery -->
	<div id="modal-gallery" class="modal modal-gallery hide fade"
		data-filter=":odd">
		<div class="modal-header">
			<a class="close" data-dismiss="modal">&times;</a>
			<h3 class="modal-title"></h3>
		</div>
		<div class="modal-body">
			<div class="modal-image"></div>
		</div>
		<div class="modal-footer">
			<a class="btn modal-download" target="_blank"> <i
				class="icon-download"></i> <span>Download</span>
			</a> <a class="btn btn-success modal-play modal-slideshow"
				data-slideshow="5000"> <i class="icon-play icon-white"></i> <span>Slideshow</span>
			</a> <a class="btn btn-info modal-prev"> <i
				class="icon-arrow-left icon-white"></i> <span>Previous</span>
			</a> <a class="btn btn-primary modal-next"> <span>Next</span> <i
				class="icon-arrow-right icon-white"></i>
			</a>
		</div>
	</div>
	<!-- The template to display files available for upload -->
	<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name"><span>{%=file.name%}</span></td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else if (o.files.valid && !i) { %}
            <td>
                <div class="progress progress-success progress-striped active"><div class="bar" style="width:0%;"></div></div>
            </td>
            <td class="start">{% if (!o.options.autoUpload) { %}
                <button class="btn btn-primary">
                    <i class="icon-upload icon-white"></i>
                    <span>{%=locale.fileupload.start%}</span>
                </button>
            {% } %}</td>
        {% } else { %}
            <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button class="btn btn-warning">
                <i class="icon-ban-circle icon-white"></i>
                <span>{%=locale.fileupload.cancel%}</span>
            </button>
        {% } %}</td>
    </tr>
{% } %}
</script>
	<!-- The template to display files available for download -->
	<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
            <td></td>
            <td class="name"><span>{%=file.name%}</span></td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else { %}
            <td class="preview">{% if (file.thumbnail_url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}"></a>
            {% } %}</td>
            <td class="name">
                <a href="{%=file.url%}" target="_blank" title="Télécharger {%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}"><i class="icon-download-alt"></i> {%=file.name%}</a>
            </td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td colspan="2"></td>
        {% } %}
        <td class="delete">
            <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <i class="icon-trash icon-white"></i>
                <span>{%=locale.fileupload.destroy%}</span>
            </button>
            <input type="checkbox" name="delete" value="1">
        </td>
    </tr>
{% } %}
</script>
</body>
</html>
