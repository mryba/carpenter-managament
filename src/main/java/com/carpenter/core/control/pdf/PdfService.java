package com.carpenter.core.control.pdf;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.jboss.security.xacml.jaxb.PDP;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
        //Open
        externalContext.setResponseHeader("Content-Disposition", "inline; filename=invoice-" + ".pdf");
        //Download
//        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=invoice-" + ".pdf");


        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.newLineAtOffset(50, 740);
            contentStream.showText("Faktura:");
            contentStream.endText();

            drawLine(contentStream, page, 60);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(150, 740);
            contentStream.showText("NUMER FAKTURY");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Wystawiajacy:");
            contentStream.endText();

            //Employee
            initText(contentStream, 50, 680, "Adres:");
            initText(contentStream, 50, 660, "NIP:");
            initText(contentStream, 50, 640, "Telefon:");

            //Client
            initText(contentStream, 350, 700, "Nabywca:");
            initText(contentStream, 350, 680, "Adres:");
            initText(contentStream, 350, 660, "NIP:");
            initText(contentStream, 350, 640, "Telefon:");

            drawLine(contentStream, page, 160);


            //Dummy Table
            float margin = 30;
// starting y position is whole page height subtracted by top and bottom margin
            float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
// we want table across whole page width (subtracted by left and right margin ofcourse)
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

            boolean drawContent = true;
            float yStart = yStartNewPage;

            float bottomMargin = 70;

// y position is your coordinate of top left corner of the table
            float yPosition = 600;

            BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);

//            Row<PDPage> headerRow = table.createRow(15f);
//            Cell<PDPage> cell = headerRow.createCell(100, "Header");
//            table.addHeaderRow(headerRow);

            Row<PDPage> headerRow = table.createRow(15f);
            headerRow.createCell(5, "LP").setFont(PDType1Font.HELVETICA_BOLD);
            headerRow.createCell(26, "Nazwa");
            headerRow.createCell(5, "Ilosc");
            headerRow.createCell(5, "J.m");
            headerRow.createCell(17, "Wartosc netto");
            headerRow.createCell(8, "Vat [%]");
            headerRow.createCell(17, "Kwota Vat");
            headerRow.createCell(17, "Wartosc brutto");


            table.addHeaderRow(headerRow);

            Row<PDPage> row = table.createRow(15f);
            row.createCell(5, "99");

            table.draw();

//            List<List> data = new ArrayList();
//            data.add(new ArrayList<>(
//                    Arrays.asList("Column One", "Column Two", "Column Three", "Column Four", "Column Five")));
//            for (int i = 1; i <= 10; i++) {
//                data.add(new ArrayList<>(
//                        Arrays.asList("Row " + i + " Col One", "Row " + i + " Col Two", "Row " + i + " Col Three", "Row " + i + " Col Four", "Row " + i + " Col Five")));
//            }
//            BaseTable dataTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, true);
//            DataTable t = new DataTable(dataTable, page);
//            t.addListToTable(data, DataTable.HASHEADER);
//
//            dataTable.draw();


            contentStream.close();

            document.save(externalContext.getResponseOutputStream());
            document.close();
        } catch (IOException e) {
            log.error("Error kurwa", e);
        } finally {
            facesContext.responseComplete();
        }
    }

    private void drawLine(PDPageContentStream contentStream, PDPage page, int y) throws IOException {
        float yCordinate = page.getCropBox().getUpperRightY() - y;
        float startX = page.getCropBox().getLowerLeftX() + 30;
        float endX = page.getCropBox().getUpperRightX() - 30;

        contentStream.setStrokingColor(61, 148, 56);
        contentStream.moveTo(startX, yCordinate);
        contentStream.lineTo(endX, yCordinate);
        contentStream.stroke();
    }

    private void initText(PDPageContentStream contentStream, int x, int y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}