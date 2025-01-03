package com.example.coursesapp;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificateGenerator {

    public static void generateCertificate(String userName, String courseName, int courseHours, OutputStream outputStream, String logoPath) {
        try {
            // إعداد ملف PDF
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // الألوان
            Color primaryColor = new DeviceRgb(121, 134, 203); // #7986CB
            Color textColor = new DeviceRgb(51, 51, 51);       // #333333

            // إعداد حجم الشهادة
            pdfDoc.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(595, 942));  // A4

            // إضافة إطار حول الشهادة
            document.setBorder(new SolidBorder(primaryColor, 6)); // حواف بارزة

            // إضافة اللوجو أعلى الشهادة (من المسار مباشرة) مع تكبيره
            if (logoPath != null && !logoPath.isEmpty()) {
                com.itextpdf.io.image.ImageData logoImageData = ImageDataFactory.create(logoPath);
                Image logoImage = new Image(logoImageData);
                logoImage.scaleToFit(600, 500); // تكبير اللوجو 5 مرات
                logoImage.setFixedPosition(0, 680); // نقل اللوجو للأسفل
                document.add(logoImage);
            }

            // تحريك النصوص بالكامل إلى الأسفل
            document.add(new Paragraph("\n\n\n\n\n\n\n\n"));

            // إضافة عنوان الشهادة
            document.add(new Paragraph("Certificate of Completion")
                    .setFontSize(28)
                    .setBold()
                    .setFontColor(primaryColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30));

            // إضافة اسم المستلم
            document.add(new Paragraph("\n\nThis certifies that")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(userName)
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\nhas successfully completed the course")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph(courseName)
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER));

            // إضافة عدد ساعات الكورس
            document.add(new Paragraph("\nCourse Duration: " + courseHours + " hours")
                    .setFontSize(16)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.CENTER));

            // إضافة التاريخ
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            document.add(new Paragraph("\n\nDate: " + date)
                    .setFontSize(16)
                    .setFontColor(textColor)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginRight(40));

            // إغلاق المستند
            document.close();
            System.out.println("Certificate generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
