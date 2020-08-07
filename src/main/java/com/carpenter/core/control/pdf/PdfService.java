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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.ZoneId;
import java.util.Objects;
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


//            File imageFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("images/builder.png")).getPath());
//            BufferedImage image = ImageIO.read(getClass().getResource("/images/builder.png"));
            BufferedImage image = ImageIO.read(getClass().getResource("/images/constructor.png"));
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);


            font = PDType0Font.load(document, fontStream);
            boldFont = PDType0Font.load(document, boldFontStream);

            contentStream.beginText();
            contentStream.setFont(boldFont, 28);
            contentStream.newLineAtOffset(150, 755);
            contentStream.showText("Podkarpaccy Ciesle");
            contentStream.endText();

            contentStream.drawImage(pdImage, 50, 700, image.getWidth()/5.0f, image.getHeight()/5.0f);

            contentStream.beginText();
            contentStream.setFont(font, 24);
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Faktura VAT");
            contentStream.endText();

            initText(contentStream, font, 400, 670, "Numer:");
            initText(contentStream, boldFont, 445, 670, invoiceDto.getNumberOfInvoice());

            initText(contentStream, font, 400, 650, "Miejsce:");
            initText(contentStream, boldFont, 450, 650, invoiceDto.getPlaceOfCreation());


            initText(contentStream, font, 400, 630, "Data wystawienia:");
            initText(contentStream, boldFont, 500, 630, invoiceDto.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

            drawLine(contentStream, page, 180);

            //Employee
            initText(contentStream, font, 50, 580, "Sprzedawca:");
            initText(contentStream, boldFont, 125, 580, invoiceDto.getEmployeeDto().getFirstName() + " " + invoiceDto.getEmployeeDto().getLastName());

            initText(contentStream, font, 50, 560, "Adres: ");
            initText(contentStream, boldFont, 90, 560, invoiceDto.getEmployeeFullAddress());

            initText(contentStream, font, 50, 540, "NIP:");
            initText(contentStream, boldFont, 80, 540, invoiceDto.getEmployeeDto().getNipNumber());

            initText(contentStream, font, 50, 520, "Telefon: ");
            initText(contentStream, boldFont, 100, 520, invoiceDto.getEmployeeDto().getPhone());

            //Client
            initText(contentStream, font, 400, 580, "Nabywca:");
            initText(contentStream, boldFont, 460, 580, invoiceDto.getClientDto().getName());

            initText(contentStream, font, 400, 560, "Miasto: ");
            initText(contentStream, boldFont, 445, 560, invoiceDto.getClientDto().getCity());

            initText(contentStream, boldFont, 445, 540, invoiceDto.getClientFullAddress());

            initText(contentStream, font, 400, 520, "NIP:");
            initText(contentStream, boldFont, 430, 520, invoiceDto.getClientDto().getNip());

            initText(contentStream, font, 400, 500, "Telefon:");
            initText(contentStream, boldFont, 450, 500, invoiceDto.getClientDto().getPhoneNumber());

            drawLine(contentStream, page, 320);


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
            float yPosition = 450;

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

            initText(contentStream, font, 400, 380, "Do zapłaty:");
            initText(contentStream, boldFont, 470, 380, invoiceDto.getGrossValue().toPlainString() + " PLN");

            drawLine(contentStream, page, 430);

            String[] splitedNumber = invoiceDto.getGrossValue().toPlainString().split("\\.");
            String decimal = splitedNumber[0];
            int decimalNumber = Integer.parseInt(decimal);

            String afterPeriod = splitedNumber[1];

            ULocale locale = new ULocale("Pl");

            NumberFormat format = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
            String result = format.format(decimalNumber);

            initText(contentStream, font, 50, 320, "Słownie: ");
            initText(contentStream, boldFont, 100, 320, result + " zł" + " i " + afterPeriod + "/100");

            initText(contentStream, font, 50, 300, "Termin zapłaty:");
            initText(contentStream, boldFont, 135, 300, invoiceDto.getPaymentDue().toString());

            initText(contentStream, font, 50, 280, "Nr. konta bankowego: " + invoiceDto.getEmployeeDto().getBankAccountNumber());


            contentStream.moveTo(100, 100);
            contentStream.lineTo(250, 100);
            contentStream.setLineDashPattern(new float[]{3}, 0);
            contentStream.stroke();

            initText(contentStream, font, 125, 80, "Podpis sprzedawcy");


            contentStream.moveTo(355, 100);
            contentStream.lineTo(500, 100);
            contentStream.setLineDashPattern(new float[]{3}, 0);
            contentStream.stroke();

            initText(contentStream, font, 385, 80, "Podpis nabywcy");


            contentStream.moveTo(0, 50);
            contentStream.lineTo(610, 50);
            contentStream.setLineDashPattern(new float[]{3}, 0);
            contentStream.stroke();

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