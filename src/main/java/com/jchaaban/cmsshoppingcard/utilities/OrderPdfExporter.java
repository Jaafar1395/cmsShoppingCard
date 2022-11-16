package com.jchaaban.cmsshoppingcard.utilities;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.jchaaban.cmsshoppingcard.models.data.Order;
import com.jchaaban.cmsshoppingcard.models.data.OrderItem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderPdfExporter {

    private final int IMAGE_HEIGHT = 10;

    public File exportOrderToPdf(Order order, String exportedFileName) throws IOException {

        exportedFileName = exportedFileName + ".pdf";
        PdfWriter writer = new PdfWriter(exportedFileName);

        PdfDocument pdfDoc = new PdfDocument(writer);

        Document document = new Document(pdfDoc);

        setPdfTitle("Ihre Rechnung:",document);

        addText("Bestellungsnummer: " + order.getOrderTrackingNumber(),document);
        addText("Kinde: " + order.getUser().getUsername(),document);
        addText("Email: " + order.getUser().getEmail(),document);
        addText("Addresse: " + order.getUser().getAddress().getFullAddress(),document);
        addText("Bestellt am: " +  getFormattedDateAndTimeString(), document);

        float[] pointColumnWidths = {120f, 120f, 120f, 120f, 120f};
        Table table = new Table(pointColumnWidths);
        table.setMarginTop(15);
        table.setMarginBottom(15);
        writeTableHeader(table);

        for (OrderItem orderItem : order.getOrderItems()) {
            addCell(table, orderItem.getItemName());
            String imagePath = getOrderItemImagePath(orderItem);
            Image image = getImage(imagePath);
            addCell(table,image);
            addCell(table, String.valueOf(orderItem.getQuantity()));
            addCell(table, orderItem.getUnitPrice() + " €");
            double totalPrice = Double.parseDouble(orderItem.getUnitPrice()) * orderItem.getQuantity();
            addCell(table, String.valueOf(totalPrice));
        }

        document.add(table);

        addText("Gesamtpreis: " + Math.round(order.getTotalPrice() * 100) / 100.00 + " €",document);

        document.close();

        return new File(exportedFileName);
    }

    private void setPdfTitle(String title, Document document){
        document.add(new Paragraph(title).setFontSize(17)
                .setPaddingBottom(15)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));
    }

    private void addText(String content, Document document){
        document.add(new Paragraph(content).setFontSize(12)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));
    }

    private String getOrderItemImagePath(OrderItem orderItem){
        String url = orderItem.getImageUrl().substring(1);
        Path uploadPath = Paths.get("media");
        Path filePath = uploadPath.resolve(url.replace("media/", ""));
        return filePath.toString();
    }

    private void addCell(Table table, String content){
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph paragraph = new Paragraph(content);
        paragraph.setFontSize(15);
        cell.add(paragraph);
        cell.setFontColor(ColorConstants.BLACK);
        cell.setBackgroundColor(ColorConstants.WHITE);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }


    private void addCell(Table table, Image image){
        Cell cell = new Cell();
        cell.add(image.setAutoScale(true));
        table.addCell(cell);
    }

    private void writeTableHeader(Table table) {
        String [] pdfHeader = {"Product", "Image", "Quantity", " Price", "Total"};
        Cell cell;
        for (String title : pdfHeader) {
            cell = new Cell();
            cell.setFontColor(ColorConstants.WHITE);
            cell.setBackgroundColor(ColorConstants.DARK_GRAY);
            Paragraph paragraph = new Paragraph(title).setTextAlignment(TextAlignment.CENTER);
            cell.add(paragraph);
            table.addCell(cell);
        }
    }

    private Image getImage(String imageFile) throws MalformedURLException {
        ImageData data = ImageDataFactory.create(imageFile);
        Image image = new Image(data);
        image.setHeight(IMAGE_HEIGHT);
        image.setWidth(IMAGE_HEIGHT);
        return image;
    }

    private String getFormattedDateAndTimeString(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy , HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
