package org.slbtty.yapyz;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class pdfExtractor {
    public static void main(String[] args) {
        var testPDF = Paths.get("F:", "desktop", "test", "test.pdf");

        if (Files.isRegularFile(testPDF)) {
            try (PdfReader reader = new PdfReader(testPDF.toAbsolutePath().toString());
                 PdfDocument document = new PdfDocument(reader)
            ) {
                var t = PdfTextExtractor.getTextFromPage(document.getPage(1));
                System.out.println(t);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("failed");
        }
    }
}
