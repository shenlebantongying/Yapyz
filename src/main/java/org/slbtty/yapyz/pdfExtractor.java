package org.slbtty.yapyz;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class pdfExtractor {

    private static final pdfExtractor instance = new pdfExtractor();

    private pdfExtractor(){};

    public static pdfExtractor getInstance(){
        return instance;
    }

    /**
     * Temp method, just return entire doc.
     * @param path path should be composed via Paths.get
     * @return
     */
    public static PdfDocument obtainPDFDocument(Path path){
        if (Files.isRegularFile(path)) {
            try (PdfReader reader = new PdfReader(path.toAbsolutePath().toString());
                 PdfDocument document = new PdfDocument(reader)
            ) {
                return document;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("failed");
        }
        return null;
    };

}
