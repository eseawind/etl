package org.intercom.springSecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.intercom.dao.UtilisateurDao;
import org.intercom.domain.DroitSysteme;
import org.intercom.domain.DroitUtilisateur;
import org.intercom.domain.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A custom {@link UserDetailsService} where user information is retrieved from
 * a JPA repository
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UtilisateurDao userDao;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		UserDetails user = null;
		try {
			Utilisateur utilisateur = userDao.findByUsername(username);
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;

			user = new User(utilisateur.getLogin(),
					utilisateur.getMotDePasse(), enabled, accountNonExpired,
					credentialsNonExpired, accountNonLocked,
					getAuthorities(utilisateur.getDroitUtilisateurs()));
		} catch (Exception e) {
		}
		return user;
	}

	public Collection<? extends GrantedAuthority> getAuthorities(
			Set<DroitUtilisateur> droits_utilisateur) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (DroitUtilisateur droit_utilisateur : droits_utilisateur) {
			Set<DroitSysteme> droits_systeme = droit_utilisateur.getDroitSystemes();
			for (DroitSysteme droit_systeme : droits_systeme) {
				authorities.add(new GrantedAuthorityImpl(droit_systeme.getRole()));
			}
		}
		return authorities;
	}
}