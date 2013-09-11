<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<div class="span2" style="width: 260px;">
	<div class="well sidebar-nav" style="padding: 19px 19px 19px 0;">
		<ul class="nav nav-list">
			<li><a href="home"><i class="icon-home"></i> Accueil</a></li>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_CONNEXION_BASE_DONNEES', 'ROLE_MODIFIER_CONNEXION_BASE_DONNEES', 'ROLE_SUPPRIMER_CONNEXION_BASE_DONNEES', 'ROLE_AJOUTER_CONNEXION_FTP', 'ROLE_MODIFIER_CONNEXION_FTP', 'ROLE_SUPPRIMER_CONNEXION_FTP')">
				<li class="nav-header">Gestion des connexions</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_CONNEXION_BASE_DONNEES', 'ROLE_MODIFIER_CONNEXION_BASE_DONNEES', 'ROLE_SUPPRIMER_CONNEXION_BASE_DONNEES')">
			<li><a href="connection_list"><i class="icon-inbox"></i>
					Bases de données</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_CONNEXION_FTP', 'ROLE_MODIFIER_CONNEXION_FTP', 'ROLE_SUPPRIMER_CONNEXION_FTP')">
			<li><a href="ftp_list"><i class="icon-refresh"></i> Serveurs
					FTP</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_GESTION_FICHIER_LOCAL', 'ROLE_GESTION_FICHIERS_FTP')">
			<li class="nav-header">Gestion des fichiers</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_GESTION_FICHIER_LOCAL')">
			<li><a href="upload_local"><i class="icon-hdd"></i> Fichiers
					en local</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_GESTION_FICHIERS_FTP')">
			<li><a href="upload_ftp"><i class="icon-share"></i> Fichiers
					via FTP</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_CONFIGURATION', 'ROLE_MODIFIER_CONFIGURATION', 'ROLE_SUPPRIMER_CONFIGURATION', 'ROLE_AJOUTER_STRUCTURE_DONNEES', 'ROLE_MODIFIER_STRUCTURE_DONNEES', 'ROLE_SUPPRIMER_STRUCTURE_DONNEES', 'ROLE_VERIFIER_FICHIER_CSV', 'ROLE_IMPORTER_FICHER_CSV')">
			<li class="nav-header">Processus d'importation</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_CONFIGURATION', 'ROLE_MODIFIER_CONFIGURATION', 'ROLE_SUPPRIMER_CONFIGURATION')">
			<li><a href="configuration_list"><i class="icon-cog"></i>
					Configuration</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_STRUCTURE_DONNEES', 'ROLE_MODIFIER_STRUCTURE_DONNEES', 'ROLE_SUPPRIMER_STRUCTURE_DONNEES')">
			<li><a href="data_structure_list"><i class="icon-th"></i>
					Structure de données</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_VERIFIER_FICHIER_CSV')">
			<li><a href="verification"><i class="icon-check"></i> Vérifcation</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_IMPORTER_FICHER_CSV')">
			<li><a href="importation"><i class="icon-share-alt"></i>
					Importation</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_UTILISATEUR', 'ROLE_MODIFIER_UTILISATEUR', 'ROLE_SUPPRIMER_UTILISATEUR')">
			<li class="nav-header">Utilisateurs de l'application</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_UTILISATEUR', 'ROLE_MODIFIER_UTILISATEUR', 'ROLE_SUPPRIMER_UTILISATEUR')">
			<li><a href="user_list"><i class="icon-user"></i> Gestion des
					utilisateurs</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_AJOUTER_DROIT', 'ROLE_MODIFIER_DROIT', 'ROLE_SUPPRIMER_DROIT')">
			<li><a href="user_right_list"><i class="icon-tags"></i> Gestion des
					droits</a></li>
			</sec:authorize>
			<li class="divider"></li>
			<li><a href="logout"> <i class="icon-off"></i> Déconnexion
			</a></li>
		</ul>
	</div>
</div>