<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Connexion</title>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<style>
fieldset {
	width: 355px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 14px;
}

legend {
	width: 100px;
	text-align: center;
	background: #DDE7F0;
	border: solid 1px;
	margin: 1px;
	font-weight: bold;
	color: #0000FF;
}

label {
	font-size: 12px;
}

.connexion-form {
	margin-left: 600px;
	margin-top: 200px;
}

.errorblock {
	color: #ff0000;
	background-color: #ffEEEE;
	border: 1px solid #ff0000;
	padding: 8px;
	margin-bottom: 10px;
	width: 361px;
}

.button.customColor {
	background-color: #ECECEC;
	background-image: -moz-linear-gradient(#F4F4F4, #ECECEC);
	border: 1px solid #D4D4D4;
	color: #333333;
	text-shadow: 1px 1px 0 #FFFFFF;
	padding: 4px;
}

.button.customColor:hover,.button.customColor:focus,.button.customColor:active,.button.customColor.on
	{
	-moz-border-bottom-colors: none;
	-moz-border-image: none;
	-moz-border-left-colors: none;
	-moz-border-right-colors: none;
	-moz-border-top-colors: none;
	background-color: #3072B3;
	background-image: -moz-linear-gradient(#599BDC, #3072B3);
	border-color: #3072B3 #3072B3 #2A65A0;
	border-style: solid;
	border-width: 1px;
	color: #FFFFFF;
	text-shadow: 1px 1px 0 #000000;
}

.alert-danger, .alert-error {
	font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 13px;
    line-height: 18px;
    background-color: #F2DEDE;
    border-color: #EED3D7;
    color: #B94A48;
    width: 300px;
}

.alert {
    border: 1px solid #FBEED5;
    border-radius: 4px 4px 4px 4px;
    color: #B94A48;
    margin-bottom: 18px;
    padding: 8px 35px 8px 14px;
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
}

.alert .close {
    line-height: 18px;
    position: relative;
    right: -21px;
    top: -2px;
}

button.close {
    background-color: transparent;
    border: 0 none;
    cursor: pointer;
    padding: 0;
}

.close {
    color: #000000;
    float: right;
    font-size: 20px;
    font-weight: bold;
    line-height: 18px;
    opacity: 0.2;
    text-shadow: 0 1px 0 #FFFFFF;
}

input, button, select, textarea {
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
}

strong {
    font-weight: bold;
}

.label-important, .badge-important {
    background-color: #B94A48;
}


.label {
    border-radius: 3px 3px 3px 3px;
    padding: 1px 4px 2px;
}

.label, .badge {
    background-color: #B94A48;
    color: #FFFFFF;
    font-size: 10.998px;
    font-weight: bold;
    line-height: 14px;
    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
    vertical-align: baseline;
    white-space: nowrap;
}
</style>
</head>
<body onload='document.f.j_username.focus();'>

	<div align="center" style="margin-top: 200px;">
	<c:if test="${error}">
		<div class="alert alert-error">
			<span class="label label-important" style="margin-right: 20px;">Erreur</span><strong>Partamètres de connexion invalides</strong>
		</div>
	</c:if>
	</div>
	<div align="center">
		<form name='f' action="<c:url value='j_spring_security_check' />"
		method='POST'>
			<fieldset>
				<legend>Connexion</legend>
				<table>
					<tr>
						<td><label for="username">Nom d'utilisateur :</label></td>
						<td><input type="text" name='j_username' value=''
							size="30" /></td>
					</tr>
					<tr>
						<td><label for="password">Mot de passe :</label></td>
						<td><input type='password' name='j_password'
							size="30" /></td>
					</tr>
					<tr>
						<td class="submit"></td>
						<td><input  class="button customColor" name="submit" type="submit" value="Connexion" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
</body>
</html>