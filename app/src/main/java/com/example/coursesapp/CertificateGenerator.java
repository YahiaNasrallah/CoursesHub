package com.example.coursesapp;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificateGenerator {

    public static void generateCertificate(String userName, String courseName, String filePath) {
        try {
            // إنشاء ملف PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // إضافة عنوان
            document.add(new Paragraph("Certificate of Completion")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            // إضافة النص
            document.add(new Paragraph("\n\nThis certifies that")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(userName)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\nhas successfully completed the course")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(courseName)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            // إضافة التاريخ
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            document.add(new Paragraph("\n\nDate: " + date)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.RIGHT));

            // إغلاق المستند
            document.close();
            System.out.println("Certificate generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
