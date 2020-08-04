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
import java.util.ResourceBundle;

@Slf4j
@ViewScoped
@Named("pdfService")
public class PdfService implements Serializable {

    private PDFont font;
    private PDFont boldFont;

    private ResourceBundle invoiceBundle = ResourceBundle.getBundle("InvoiceMessages");

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

        InputStream fontStream = null;
        InputStream boldFontStream = null;
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            fontStream = getClass().getResourceAsStream("/ttf/liberation-sans/LiberationSans-Regular.ttf");
            boldFontStream = getClass().getResourceAsStream("/ttf/liberation-sans/LiberationSans-Bold.ttf");
            font = PDType0Font.load(document, fontStream);
            boldFont = PDType0Font.load(document, boldFontStream);

            contentStream.beginText();
            contentStream.setFont(boldFont, 28);
            contentStream.newLineAtOffset(50, 755);
            contentStream.showText("Podkarpaccy Ciesle");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(font, 24);
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Faktura VAT");
            contentStream.endText();

            initText(contentStream, font, 400, 720, "Numer:");
            initText(contentStream, boldFont, 445, 720, invoiceDto.getNumberOfInvoice());

            initText(contentStream, font, 400, 700, "Miejsce:");
            initText(contentStream, boldFont, 450, 700, invoiceDto.getPlaceOfCreation());


            initText(contentStream, font, 400, 680, "Data wystawienia:");
            initText(contentStream, boldFont, 500, 680, invoiceDto.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() );

            drawLine(contentStream, page, 130);

            //Employee
            initText(contentStream, font, 50, 630, "Sprzedawca:");
            initText(contentStream, boldFont, 125, 630, invoiceDto.getEmployeeDto().getFirstName() + " " + invoiceDto.getEmployeeDto().getLastName());

            initText(contentStream, font, 50, 610, "Adres: ");
            initText(contentStream, boldFont, 90, 610, invoiceDto.getEmployeeFullAddress());

            initText(contentStream, font, 50, 590, "NIP:");
            initText(contentStream, boldFont, 80, 590, invoiceDto.getEmployeeDto().getNipNumber());

            initText(contentStream, font, 50, 570, "Telefon: ");

            //Client
            initText(contentStream, font, 400, 630, "Nabywca:");
            initText(contentStream, boldFont, 460, 630, invoiceDto.getClientDto().getName());

            initText(contentStream, font, 400, 610, "Miasto: ");
            initText(contentStream, boldFont, 440, 610, invoiceDto.getClientDto().getCity());

            initText(contentStream, boldFont, 440, 590, invoiceDto.getClientFullAddress());

            initText(contentStream, font, 400, 570, "NIP:");
            initText(contentStream, boldFont, 440, 570, invoiceDto.getClientDto().getNip());

            initText(contentStream, font, 400, 550, "Telefon:");
            initText(contentStream, boldFont, 450, 550, invoiceDto.getClientDto().getPhoneNumber());
            drawLine(contentStream, page, 240);


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
            float yPosition = 530;

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
            row.createCell(26, invoiceDto.getDescription()).setFont(boldFont);
            row.createCell(5, "1");
            row.createCell(5, "szt.");
            row.createCell(17, invoiceDto.getNetValue().toPlainString() + " zł").setFont(boldFont);
            row.createCell(8, invoiceBundle.getString("invoice-vat-rate-" + invoiceDto.of(invoiceDto.getVatRate()).name())).setFont(boldFont);
            row.createCell(17, (invoiceDto.getGrossValue().subtract(invoiceDto.getNetValue())).toPlainString() + " zł").setFont(boldFont);
            row.createCell(17, invoiceDto.getGrossValue().toPlainString() + " zł").setFont(boldFont);

            table.draw();

            initText(contentStream, font, 400, 450, "Do zapłaty:");
            initText(contentStream, boldFont, 470, 450, invoiceDto.getGrossValue().toPlainString() + " PLN");

            drawLine(contentStream, page, 320);

            String[] splitedNumber = invoiceDto.getGrossValue().toPlainString().split("\\.");
            String decimal = splitedNumber[0];
            int decimalNumber = Integer.parseInt(decimal);

            String afterPeriod = splitedNumber[1];

            ULocale locale = new ULocale("Pl");

            NumberFormat format = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
            String result = format.format(decimalNumber);

            initText(contentStream, font, 50, 420, "Słownie: ");
            initText(contentStream, boldFont, 100, 420, result + " zł" + " i " + afterPeriod + "/100");

            initText(contentStream, font, 50, 400, "Termin zapłaty:");
            initText(contentStream, boldFont, 135, 400, invoiceDto.getPaymentDue().toString());

            initText(contentStream, font, 50, 380, "Nr. konta bankowego: " + invoiceDto.getEmployeeDto().getBankAccountNumber());

            contentStream.close();


            document.save(externalContext.getResponseOutputStream());
            document.close();
            fontStream.close();
            boldFontStream.close();
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

    private void initText(PDPageContentStream contentStream, PDFont font, int x, int y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}