package ma.devops.gestionecole.security.repository;

import ma.devops.gestionecole.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, String> {
}
