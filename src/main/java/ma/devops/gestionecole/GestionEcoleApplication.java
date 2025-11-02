package ma.devops.gestionecole;

import ma.devops.gestionecole.entities.Etudiant;
import ma.devops.gestionecole.entities.Filiere;
import ma.devops.gestionecole.repository.EtudiantRepository;
import ma.devops.gestionecole.repository.FiliereRepository;
import ma.devops.gestionecole.security.service.A.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;


@SpringBootApplication
public class GestionEcoleApplication {




    public static void main(String[] args) {
        SpringApplication.run(GestionEcoleApplication.class, args);
    }

    private FiliereRepository filiereRepository;
    public GestionEcoleApplication(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }
    @Autowired
    private  EtudiantRepository etudiantRepository;
    @Bean
    public CommandLineRunner commandLineRunner() {

        return args -> {





//            Filiere filiere1 = Filiere.builder().code("FIL001").libelle("Informatique").build();
//            Filiere filiere2 = Filiere.builder().code("FIL002").libelle("Mathématiques").build();
//            Filiere filiere3 = Filiere.builder().code("FIL003").libelle("Physique").build();
//            Filiere filiere4 = Filiere.builder().code("FIL004").libelle("Chimie").build();
//            Filiere filiere5 = Filiere.builder().code("FIL005").libelle("Biologie").build();
//
//            filiereRepository.save(filiere1);
//            filiereRepository.save(filiere2);
//            filiereRepository.save(filiere3);
//            filiereRepository.save(filiere4);
//            filiereRepository.save(filiere5);
//
//          Etudiant etudiant1 = Etudiant.builder()
//                    .id_etudiant(null)
//                    .nom("ouseffar")
//                    .prenom("othman")
//                    .cne("ddcz")
//                    .filiere(filiere1)
//                    .build();
//            etudiantRepository.save(etudiant1);
//            Etudiant etudiant2 = Etudiant.builder()
//                    .id_etudiant(null)
//                    .nom("elkamari")
//                    .prenom("nassime")
//                    .cne("ddczaa221")
//                    .filiere(filiere2)
//                    .build();
//            etudiantRepository.save(etudiant2);
        };


    }
    //@Bean
    CommandLineRunner commandLineRunnerDetailUser(AccountService accountService) {
        return args -> {
            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("USER","1111","1111");
            accountService.addNewUser("ADMIN","1111","1111");
            accountService.addRoleToUser("ADMIN","ADMIN");
            accountService.addRoleToUser("USER","USER");
        };
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
