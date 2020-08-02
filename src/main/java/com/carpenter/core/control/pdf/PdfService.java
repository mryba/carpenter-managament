package com.carpenter.core.control.pdf;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Row;
import com.carpenter.core.control.dto.InvoiceDto;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.*;
import java.time.ZoneId;

@Slf4j
@ViewScoped
@Named("pdfService")
public class PdfService implements Serializable {

    private PDFont font;

    public void renderPdf(InvoiceDto invoiceDto) {

        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        //Open
        externalContext.setResponseHeader("Content-Disposition", "inline; filename=invoice-" + ".pdf");
        //Download
//        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=invoice-" + ".pdf");

        InputStream inputStream = null;
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            inputStream = getClass().getResourceAsStream("/ttf/liberation-sans/LiberationSans-Regular.ttf");
            font = PDType0Font.load(document, inputStream);

            contentStream.beginText();
            contentStream.setFont(font, 24);
            contentStream.newLineAtOffset(50, 740);
            contentStream.showText("Faktura VAT");
            contentStream.endText();

            initText(contentStream, 350, 760, "Numer: " + invoiceDto.getNumberOfInvoice());
            initText(contentStream, 350, 740, "Data wystawienia faktury: " + invoiceDto.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

            drawLine(contentStream, page, 60);

            //Employee
            initText(contentStream, 50, 700, "Sprzedawca: " + invoiceDto.getEmployeeFirstName() + " " + invoiceDto.getEmployeeLastName());
            initText(contentStream, 50, 680, "Adres: ");
            initText(contentStream, 50, 660, "NIP: " + invoiceDto.getEmployeeNipNumber());
            initText(contentStream, 50, 640, "Telefon: ");

            //Client
            initText(contentStream, 350, 700, "Nabywca: " + invoiceDto.getClientName());
            initText(contentStream, 350, 680, "Adres: ");
            initText(contentStream, 350, 660, "NIP: " + invoiceDto.getClientNipNumber());
            initText(contentStream, 350, 640, "Telefon: ");

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
            headerRow.createCell(5, "LP");
            headerRow.createCell(26, "Nazwa").setFont(font);
            headerRow.createCell(5, "Ilość").setFont(font);
            headerRow.createCell(5, "J.m");
            headerRow.createCell(17, "Wartość netto").setFont(font);
            headerRow.createCell(8, "Vat [%]");
            headerRow.createCell(17, "Kwota Vat");
            headerRow.createCell(17, "Wartość brutto").setFont(font);


            table.addHeaderRow(headerRow);

            Row<PDPage> row = table.createRow(15f);
            row.createCell(5, "1");
            row.createCell(26, invoiceDto.getDescription());
            row.createCell(5, "1");
            row.createCell(5, "szt.");
            row.createCell(17, invoiceDto.getNetValue().toPlainString() + " zł").setFont(font);
            row.createCell(8, invoiceDto.of(invoiceDto.getVatRate()).name());
            row.createCell(17, (invoiceDto.getGrossValue().subtract(invoiceDto.getNetValue())).toPlainString() + " zł").setFont(font);
            row.createCell(17, invoiceDto.getGrossValue().toPlainString() + " zł").setFont(font);

            table.draw();

            drawLine(contentStream, page, 260);

            String[] splitedNumber = invoiceDto.getGrossValue().toPlainString().split("\\.");
            String decimal = splitedNumber[0];
            int decimalNumber = Integer.parseInt(decimal);

            String afterPeriod = splitedNumber[1];

            ULocale locale = new ULocale("Pl");

            NumberFormat format = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
            String result = format.format(decimalNumber);

            initText(contentStream, 50, 500, "Słownie: ");
            initText(contentStream, 100, 500, result + " zł" + " i " + afterPeriod + "/100");

            initText(contentStream, 50, 450, "Termin zapłaty: " + invoiceDto.getPaymentDue().toString());
            initText(contentStream, 50, 400, "Nr. konta bankowego: " + invoiceDto.getEmployeeAccountNumber());

            contentStream.close();


            document.save(externalContext.getResponseOutputStream());
            document.close();
            inputStream.close();
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
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}