package ma.devops.gestionecole.security.service.A;

import ma.devops.gestionecole.security.entities.AppRole;
import ma.devops.gestionecole.security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(String username, String password, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);
    AppUser loadUserByUsername(String username);
    List<AppUser> getAllUsers();
    List<AppRole> getAllRoles();
}