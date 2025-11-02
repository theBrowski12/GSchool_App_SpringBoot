package ma.devops.gestionecole.repository;

import ma.devops.gestionecole.entities.Etudiant;
import ma.devops.gestionecole.entities.Filiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant,Long > {
    @Query("select p from Etudiant p where p.cne like :x")
    public Page<Etudiant> chercher(@Param("x") String mc, Pageable pageable);

    Page<Etudiant> findByFiliereAndCneContainingIgnoreCase(Filiere filiere, String mc, Pageable pageable);
    List<Etudiant> findByFiliere_Libelle(String libelle);
    List<Etudiant> findByNom(String name);

}
