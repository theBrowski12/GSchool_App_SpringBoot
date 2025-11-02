package ma.devops.gestionecole.repository;

import ma.devops.gestionecole.entities.Filiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FiliereRepository extends JpaRepository<Filiere, Long> {
    @Query("select p from Filiere p where p.libelle like :x")
    public Page<Filiere> chercher(@Param("x") String mc, Pageable pageable);
}
