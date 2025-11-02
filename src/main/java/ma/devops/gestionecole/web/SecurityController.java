package ma.devops.gestionecole.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class SecurityController {
    @GetMapping("/notAutorized")
    public String home1() {

        return "/notAutorized";
    }
}
