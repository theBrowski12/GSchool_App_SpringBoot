package ma.devops.gestionecole.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ma.devops.gestionecole.entities.Etudiant;
import ma.devops.gestionecole.entities.Filiere;
import ma.devops.gestionecole.repository.EtudiantRepository;
import ma.devops.gestionecole.repository.FiliereRepository;
import ma.devops.gestionecole.services.ExportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class EtudiantController {
    private final ExportService exportService;
    private EtudiantRepository etudiantRepository;
    private FiliereRepository filiereRepository;


    @GetMapping("/user/listetudiant")
    public String listetudiant(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
                               @RequestParam(name = "size", defaultValue = "5") int s,
                               @RequestParam(name = "motCle", defaultValue = "") String mc) {

        Page<Etudiant> pageetudiants = etudiantRepository.chercher("%" + mc + "%", PageRequest.of(p, s));
        model.addAttribute("listetudiant", pageetudiants.getContent());
        int[] pages = new int[pageetudiants.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourant", p);
        model.addAttribute("motCle", mc);

        return "etudiant";
    }
    @GetMapping("/user/listeetudiantinfiliere")
    public String listeetudiantsparfiliere(Model model, @RequestParam(name = "id") Long idFiliere,
                                           @RequestParam(name = "page", defaultValue = "0") int p,
                                           @RequestParam(name = "size", defaultValue = "5") int s,
                                           @RequestParam(name = "motCle", defaultValue = "") String mc) {


        Filiere filiere = filiereRepository.findById(idFiliere).get();


        Page<Etudiant> pageEtudiants = etudiantRepository.findByFiliereAndCneContainingIgnoreCase(
                filiere,mc,  PageRequest.of(p, s));

        model.addAttribute("listetudiant", pageEtudiants.getContent());
        model.addAttribute("pages", new int[pageEtudiants.getTotalPages()]);
        model.addAttribute("size", s);
        model.addAttribute("pagecourant", p);
        model.addAttribute("motCle", mc);
        model.addAttribute("filiere", filiere);

        return "listeetudiantinfiliere";
    }

    @PostMapping("/admin/saveetudiant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveetudiant(Etudiant etudiant) {
        etudiantRepository.save(etudiant);

        return "redirect:/admin/nvetudiant";
    }
    @PostMapping("/admin/updateetudiant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateetudiant(Etudiant etudiant) {
        etudiantRepository.save(etudiant);

        return "redirect:/user/listetudiant";
    }
    @GetMapping ("/admin/nvetudiant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String nvetudiant(Model model) {
        model.addAttribute("etudiant", new Etudiant());

        model.addAttribute("filieres", filiereRepository.findAll());
        return "nvetudiant";
    }
    @GetMapping ("/admin/editetudiant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editfiliere(Model model,@RequestParam(name = "id")Long id) {
        Etudiant etudiant= etudiantRepository.findById(id).get();
        model.addAttribute("filieres", filiereRepository.findAll());
        model.addAttribute("etudiant",etudiant );
        return "editetudiant";
    }

    @RequestMapping(value="/admin/deleteetudiant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id,String motCle,String page,String size) {
        etudiantRepository.deleteById(id);
        return "redirect:/user/listetudiant?page="+page+"&size="+size+"&motCle="+motCle;
    }

    @GetMapping("/user/etudiants/pdf")
    public void exportEtudiantPdf(HttpServletResponse response) {
        try {
            byte[] pdfBytes = exportService.generateEtudiantPdf();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=etudiants.pdf");
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/user/etudiants/excel")
    public void exportEtudiantExcel(HttpServletResponse response) {
        try {
            byte[] excelBytes = exportService.generateEtudiantExcel();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=etudiants.xlsx");
            response.getOutputStream().write(excelBytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/user/pdf/{filiere}")
    public ResponseEntity<byte[]> exportEtudiantsToPdf(@PathVariable String filiere) {
        byte[] pdfBytes = exportService.generateEtudiantsPdfByFiliere(filiere);

        if (pdfBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "etudiants_" + filiere + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/user/excel/{filiere}")
    public ResponseEntity<byte[]> exportEtudiantsToExcel(@PathVariable String filiere) {
        byte[] excelBytes = exportService.generateEtudiantsExcelByFiliere(filiere);

        if (excelBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "etudiants_" + filiere + ".xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
