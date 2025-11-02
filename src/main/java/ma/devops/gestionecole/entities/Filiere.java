package ma.devops.gestionecole.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Builder
public class Filiere {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_filiere;
    private  String code;
    private  String libelle;
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL)
    private Collection<Etudiant> etudiants;

    public int getNombreEtudiants() {
        return etudiants!= null ? etudiants.size() : 0;
    }
}
