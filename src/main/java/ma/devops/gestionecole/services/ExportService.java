package ma.devops.gestionecole.services;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.AllArgsConstructor;
import ma.devops.gestionecole.entities.Etudiant;
import ma.devops.gestionecole.entities.Filiere;
import ma.devops.gestionecole.repository.EtudiantRepository;
import ma.devops.gestionecole.repository.FiliereRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class ExportService {
    private final FiliereRepository filiereRepository;
    private final EtudiantRepository etudiantRepository;

    public byte[] generateFilierePdf() {
        List<Filiere> filieres = filiereRepository.findAll();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);


            document.add(new Paragraph("Liste des Filières").setBold().setFontSize(18));


            Table table = new Table(3);
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Code")));
            table.addHeaderCell(new Cell().add(new Paragraph("Libellé")));


            for (Filiere filiere : filieres) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(filiere.getId_filiere()))));
                table.addCell(new Cell().add(new Paragraph(filiere.getCode())));
                table.addCell(new Cell().add(new Paragraph(filiere.getLibelle())));
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] generateFiliereExcel() {
        List<Filiere> filieres = filiereRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Filières");


            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Code", "Libellé"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i); // Spécifie que c'est un Cell de POI
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Filiere filiere : filieres) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(filiere.getId_filiere());
                row.createCell(1).setCellValue(filiere.getCode());
                row.createCell(2).setCellValue(filiere.getLibelle());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public byte[] generateEtudiantPdf() {
        List<Etudiant> etudiants = etudiantRepository.findAll();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Liste des Étudiants").setBold().setFontSize(18));

            Table table = new Table(5);
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Nom")));
            table.addHeaderCell(new Cell().add(new Paragraph("Prénom")));
            table.addHeaderCell(new Cell().add(new Paragraph("CNE")));
            table.addHeaderCell(new Cell().add(new Paragraph("Filière")));

            for (Etudiant etudiant : etudiants) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(etudiant.getId_etudiant()))));
                table.addCell(new Cell().add(new Paragraph(etudiant.getNom())));
                table.addCell(new Cell().add(new Paragraph(etudiant.getPrenom())));
                table.addCell(new Cell().add(new Paragraph(etudiant.getCne())));
                table.addCell(new Cell().add(new Paragraph(etudiant.getFiliere().getLibelle())));
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] generateEtudiantExcel() {
        List<Etudiant> etudiants = etudiantRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Étudiants");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nom", "Prénom", "CNE", "Filière"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Etudiant etudiant : etudiants) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(etudiant.getId_etudiant());
                row.createCell(1).setCellValue(etudiant.getNom());
                row.createCell(2).setCellValue(etudiant.getPrenom());
                row.createCell(3).setCellValue(etudiant.getCne());
                row.createCell(4).setCellValue(etudiant.getFiliere().getLibelle());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public byte[] generateEtudiantsPdfByFiliere(String filiere) {
        List<Etudiant> etudiants = etudiantRepository.findByFiliere_Libelle(filiere);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Liste des Étudiants de la Filière: " + filiere).setBold().setFontSize(18));

            Table table = new Table(4);
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Nom")));
            table.addHeaderCell(new Cell().add(new Paragraph("Prénom")));
            table.addHeaderCell(new Cell().add(new Paragraph("CNE")));

            for (Etudiant etudiant : etudiants) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(etudiant.getId_etudiant()))));
                table.addCell(new Cell().add(new Paragraph(etudiant.getNom())));
                table.addCell(new Cell().add(new Paragraph(etudiant.getPrenom())));
                table.addCell(new Cell().add(new Paragraph(etudiant.getCne())));
            }

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] generateEtudiantsExcelByFiliere(String filiere) {
        List<Etudiant> etudiants = etudiantRepository.findByFiliere_Libelle(filiere);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(filiere);

            // En-tête
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nom", "Prénom", "CNE"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Remplissage des données
            int rowNum = 1;
            for (Etudiant etudiant : etudiants) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(etudiant.getId_etudiant());
                row.createCell(1).setCellValue(etudiant.getNom());
                row.createCell(2).setCellValue(etudiant.getPrenom());
                row.createCell(3).setCellValue(etudiant.getCne());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

