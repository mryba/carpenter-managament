package com.carpenter.core.control.pdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ViewScoped
@Named("pdfService")
public class PdfService implements Serializable {

    public void renderPdf() {

        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=invoice-" + ".pdf");


        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Hello world");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(250, 750);
            contentStream.showText("Dupki");
            contentStream.endText();


            contentStream.close();

            document.save(externalContext.getResponseOutputStream());
            document.close();
        } catch (IOException e) {
            log.error("Error kurwa", e);
        }
        finally {
        facesContext.responseComplete();
        }
    }
}