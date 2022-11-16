package com.jchaaban.cmsshoppingcard.utilities;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;

import com.jchaaban.cmsshoppingcard.models.data.Order;
import com.jchaaban.cmsshoppingcard.models.data.OrderItem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class OrderPdfExporter extends PDFTableMaker {

    private final int IMAGE_HEIGHT = 10;
    private final int PDF_TEXT_FONT_SIZE = 15;
    private final int TABLE_BODY_FONT_SIZE = 15;
    private final int TABLE_MARGIN = 15;

    public File exportOrderToPdf(Order order, String exportedFileName) throws IOException {

        exportedFileName = exportedFileName + ".pdf";
        PdfWriter writer = new PdfWriter(exportedFileName);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        setPdfTitle("Ihre Rechnung:",document);
        addText("Bestellungsnummer: " + order.getOrderTrackingNumber(),document);
        addText("Kinde: " + order.getUser().getUsername(),document);
        addText("Email: " + order.getUser().getEmail(),document);
        addText("Addresse: " + order.getFullAddress(),document);
        addText("Bestellt am: " +  getFormattedDateAndTimeString(), document);

        String [] tableHeader = {"Product", "Image", "Quantity", " Price", "Total"};
        Table table = createOrderTable(order,tableHeader);
        document.add(table);

        addText("Gesamtpreis: " + Math.round(order.getTotalPrice() * 100) / 100.00 + " €",document);
        document.close();

        return new File(exportedFileName);
    }

    private Table createOrderTable(Order order, String [] tableHeader) throws MalformedURLException {
        Table table = createTableHavingTitles(tableHeader.length);
        table.setMargin(TABLE_MARGIN);
        table.setMarginBottom(TABLE_MARGIN);
        writeTableHeader(table,tableHeader);
        writeTableData(table,order);

        return table;
    }

    private void addText(String content, Document document){
        document.add(new Paragraph(content).setFontSize(PDF_TEXT_FONT_SIZE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));
    }

    private String getOrderItemImagePath(OrderItem orderItem){
        String url = orderItem.getImageUrl().substring(1);
        Path uploadPath = Paths.get("media");
        Path filePath = uploadPath.resolve(url.replace("media/", ""));
        return filePath.toString();
    }


    private void addImageCell(Table table, Image image){
        Cell cell = new Cell();
        cell.add(image.setAutoScale(true));
        table.addCell(cell);
    }

    private Image getImage(String imageFile) throws MalformedURLException {
        ImageData data = ImageDataFactory.create(imageFile);
        Image image = new Image(data);
        image.setHeight(IMAGE_HEIGHT);
        image.setWidth(IMAGE_HEIGHT);
        return image;
    }

    private void writeTableData(Table table, Order order) throws MalformedURLException {
        int bodyFontSize = TABLE_BODY_FONT_SIZE;
        for (OrderItem orderItem : order.getOrderItems()) {
            addContentCell(table,bodyFontSize, orderItem.getItemName());
            String imagePath = getOrderItemImagePath(orderItem);
            Image image = getImage(imagePath);
            addImageCell(table,image);
            addContentCell(table,bodyFontSize, String.valueOf(orderItem.getQuantity()));
            addContentCell(table,bodyFontSize, orderItem.getUnitPrice() + " €");
            double totalPrice = Double.parseDouble(orderItem.getUnitPrice()) * orderItem.getQuantity();
            addContentCell(table,bodyFontSize, String.valueOf(totalPrice));
        }
    }
}
