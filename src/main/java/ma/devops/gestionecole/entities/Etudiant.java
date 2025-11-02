package ma.devops.gestionecole.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//@ToString
@Builder
public class Etudiant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_etudiant;
    private String nom;
    private String prenom;
    private String cne;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photos;
    @ManyToOne
    private Filiere filiere;
}
