package pl.edu.agh.to.cinemanager.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.cinemanager.model.SecurityUser;
import pl.edu.agh.to.cinemanager.model.UserRole;

import java.util.Collection;

@Service
@AllArgsConstructor
public class AuthService {
    private final RoleHierarchy roleHierarchy;

    public boolean hasRole(UserRole userRole, Collection<? extends GrantedAuthority> authorities) {
        return roleHierarchy.getReachableGrantedAuthorities(authorities).stream()
                .anyMatch(authority -> authority.getAuthority().equals(SecurityUser.ROLE_PREFIX + userRole));
    }
}
