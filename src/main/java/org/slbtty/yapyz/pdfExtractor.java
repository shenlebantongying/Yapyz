package org.slbtty.yapyz;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class pdfExtractor {

    private static final pdfExtractor instance = new pdfExtractor();

    private pdfExtractor(){}

    public static pdfExtractor getInstance(){
        return instance;
    }

    // TODO: rewrite return type with TokenStream
    public static String obtainAllTextFromPath(Path path){

        var resultStr = new StringBuilder();
        if (Files.isRegularFile(path)) {
            try (PdfReader reader = new PdfReader(path.toAbsolutePath().toString());
                 PdfDocument document = new PdfDocument(reader)
            ) {
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    resultStr.append(PdfTextExtractor.getTextFromPage(document.getPage(i)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            Logger.error("Couldn't find pdf file");
        }

        return resultStr.toString();
    }

}
