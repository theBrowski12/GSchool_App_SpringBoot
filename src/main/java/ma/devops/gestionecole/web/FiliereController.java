package ma.devops.gestionecole.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ma.devops.gestionecole.entities.Filiere;
import ma.devops.gestionecole.repository.FiliereRepository;
import ma.devops.gestionecole.services.ExportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FiliereController {
    private FiliereRepository filiereRepository;
    private final ExportService exportService;

    @GetMapping("/user/listefiliere")
    public String listefiliere(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
                               @RequestParam(name = "size", defaultValue = "7") int s,
                               @RequestParam(name = "motCle", defaultValue = "") String mc) {

        Page<Filiere> pagefilieres = filiereRepository.chercher("%" + mc + "%", PageRequest.of(p, s));
        model.addAttribute("listFilieres", pagefilieres.getContent());
        int[] pages = new int[pagefilieres.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourant", p);
        model.addAttribute("motCle", mc);

        return "filieres";
    }


    @GetMapping("/user/home")
    public String dashboard(Model model) {
        List<Filiere> filieres = filiereRepository.findAll();
        List<String> filiereNames = filieres.stream().map(Filiere::getLibelle).collect(Collectors.toList());
        List<Integer> nombreEtudiants = filieres.stream().map(Filiere::getNombreEtudiants).collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.addAttribute("filieres", objectMapper.writeValueAsString(filiereNames));
            model.addAttribute("nombreEtudiants", objectMapper.writeValueAsString(nombreEtudiants));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }
    @GetMapping("/")
    public String home1() {

        return "redirect:/user/home";
    }

    @PostMapping("/admin/savefiliere")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(Filiere filiere) {
        filiereRepository.save(filiere);

        return "redirect:/user/listefiliere";
    }
    @GetMapping ("/admin/nvfiliere")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String nvfiliere(Model model) {
        model.addAttribute("filiere", new Filiere());
        return "nvfiliere";
    }
    @GetMapping ("/admin/editfiliere")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
        public String editfiliere(Model model,@RequestParam(name = "id")Long id) {
            Filiere filiere= filiereRepository.findById(id).get();
            model.addAttribute("filiere",filiere );
            return "editfiliere";
    }

    @RequestMapping(value="/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id,String motCle,String page,String size) {
        filiereRepository.deleteById(id);
        return "redirect:/user/listefiliere?page="+page+"&size="+size+"&motCle="+motCle;
    }


    // Export PDF
    @GetMapping("/user/export/pdf")
    public void exportPdf(HttpServletResponse response) {
        try {
            byte[] pdfBytes = exportService.generateFilierePdf();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=filieres.pdf");
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Export Excel
    @GetMapping("/user/export/excel")
    public void exportExcel(HttpServletResponse response) {
        try {
            byte[] excelBytes = exportService.generateFiliereExcel();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=filieres.xlsx");
            response.getOutputStream().write(excelBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
