package com.wildlifebackend.wildlife.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.wildlifebackend.wildlife.entitiy.OpenSubmission;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateService {

    private final ResourceLoader resourceLoader;

    @Value("${app.cert.dir:certificates}")
    private String certificatesDir;

    private static final String CLASSPATH_TEMPLATE = "classpath:templates/certificate.pdf";

    public CertificateService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws IOException {
        Path dir = Paths.get(certificatesDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
    }

    public File generateCertificate(OpenSubmission submission) {
        try {
            // Load template from resources
            Resource resource = resourceLoader.getResource(CLASSPATH_TEMPLATE);
            if (!resource.exists()) {
                throw new RuntimeException("Certificate template not found: " + CLASSPATH_TEMPLATE);
            }

            Path outDir = Paths.get(certificatesDir).toAbsolutePath().normalize();
            String outName = "Certificate_" + submission.getId() + ".pdf";
            Path outPath = outDir.resolve(outName);

            try (InputStream templateStream = resource.getInputStream();
                 PdfReader reader = new PdfReader(templateStream);
                 PdfWriter writer = new PdfWriter(outPath.toString());
                 PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfPage page = pdfDoc.getFirstPage();
                Rectangle pageSize = page.getPageSize();

                // Fonts
                PdfFont nameFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                // FIXED: Canvas creation
                PdfCanvas pdfCanvas = new PdfCanvas(page);
                Canvas canvas = new Canvas(pdfCanvas, pageSize);

                float centerX = pageSize.getWidth() / 2f;

                // Photographer Name
                Paragraph nameP = new Paragraph(submission.getPhotographer().getName())
                        .setFont(nameFont)
                        .setFontSize(28)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(nameP, centerX, pageSize.getTop() - 220f, TextAlignment.CENTER);

                // Entry Title
                Paragraph titleP = new Paragraph("Entry: " + submission.getEntryTitle())
                        .setFont(normalFont)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(titleP, centerX, pageSize.getTop() - 260f, TextAlignment.CENTER);

                // Category
                Paragraph catP = new Paragraph("Category: " + submission.getEntryCategory())
                        .setFont(normalFont)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(catP, centerX, pageSize.getTop() - 284f, TextAlignment.CENTER);

                // Date
                String dateText = "Date: " + submission.getDateOfPhotograph().format(DateTimeFormatter.ISO_LOCAL_DATE);
                Paragraph dateP = new Paragraph(dateText)
                        .setFont(normalFont)
                        .setFontSize(12);
                canvas.showTextAligned(dateP, pageSize.getRight() - 100f, 100f, TextAlignment.RIGHT);

                canvas.close();
                pdfDoc.close();
            }

            return outPath.toFile();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate certificate: " + ex.getMessage(), ex);
        }
    }

    public File generateCertificate(SchoolSubmission submission) {
        try {
            // Load template from resources
            Resource resource = resourceLoader.getResource(CLASSPATH_TEMPLATE);
            if (!resource.exists()) {
                throw new RuntimeException("Certificate template not found: " + CLASSPATH_TEMPLATE);
            }

            Path outDir = Paths.get(certificatesDir).toAbsolutePath().normalize();
            String outName = "Certificate_" + submission.getId() + ".pdf";
            Path outPath = outDir.resolve(outName);

            try (InputStream templateStream = resource.getInputStream();
                 PdfReader reader = new PdfReader(templateStream);
                 PdfWriter writer = new PdfWriter(outPath.toString());
                 PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfPage page = pdfDoc.getFirstPage();
                Rectangle pageSize = page.getPageSize();

                // Fonts
                PdfFont nameFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                // FIXED: Canvas creation
                PdfCanvas pdfCanvas = new PdfCanvas(page);
                Canvas canvas = new Canvas(pdfCanvas, pageSize);

                float centerX = pageSize.getWidth() / 2f;

                // Photographer Name
                Paragraph nameP = new Paragraph(submission.getPhotographer().getName())
                        .setFont(nameFont)
                        .setFontSize(28)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(nameP, centerX, pageSize.getTop() - 220f, TextAlignment.CENTER);

                // Entry Title
                Paragraph titleP = new Paragraph("Entry: " + submission.getEntryTitle())
                        .setFont(normalFont)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(titleP, centerX, pageSize.getTop() - 260f, TextAlignment.CENTER);

                // Category
                Paragraph catP = new Paragraph("Category: " + submission.getEntryCategory())
                        .setFont(normalFont)
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER);
                canvas.showTextAligned(catP, centerX, pageSize.getTop() - 284f, TextAlignment.CENTER);

                // Date
                String dateText = "Date: " + submission.getDateOfPhotograph().format(DateTimeFormatter.ISO_LOCAL_DATE);
                Paragraph dateP = new Paragraph(dateText)
                        .setFont(normalFont)
                        .setFontSize(12);
                canvas.showTextAligned(dateP, pageSize.getRight() - 100f, 100f, TextAlignment.RIGHT);

                canvas.close();
                pdfDoc.close();
            }

            return outPath.toFile();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate certificate: " + ex.getMessage(), ex);
        }
    }

}
