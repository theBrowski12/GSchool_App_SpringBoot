package ma.devops.gestionecole.security.web;

import lombok.AllArgsConstructor;
import ma.devops.gestionecole.security.entities.AppRole;
import ma.devops.gestionecole.security.entities.AppUser;
import ma.devops.gestionecole.security.repository.AppRoleRepository;
import ma.devops.gestionecole.security.service.A.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    //@Autowired


    @GetMapping("/admin/add-user")
    public String showAddUserForm(Model model) {

        model.addAttribute("user", new AppUser());
        model.addAttribute("roles",accountService.getAllRoles() );
        return "add-user";
    }

    @PostMapping("/addUser")
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam List<String> roles, // Récupérer plusieurs rôles
            Model model) {

        try {
            // Ajouter l'utilisateur
            AppUser user = accountService.addNewUser(username, password, confirmPassword);

            // Ajouter les rôles sélectionnés à l'utilisateur
            for (String role : roles) {
                accountService.addRoleToUser(username, role);
            }

            model.addAttribute("message", "Utilisateur ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        // Recharger la liste des rôles disponibles
        model.addAttribute("roles", accountService.getAllRoles());

        return "add-user"; // Retourner à la page du formulaire
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", accountService.getAllUsers());
        return "users";
    }
}
