package com.jchaaban.cmsshoppingcard.utilities;

import com.jchaaban.cmsshoppingcard.models.data.User;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {

    final static int FONT_SIZE = 10;
    final static int COLUMNS_NUMBER = 5;
    final static float TABLE_WIDTH_PERCENTAGE = 100f;
    final static int TABLE_CELL_PADDING = 6;

    @Override
    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        setResponseHeader(response,"application/pdf",".pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(FONT_SIZE * 2);
        font.setColor(Color.DARK_GRAY);
        Paragraph paragraph = new Paragraph("List of Users",font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(COLUMNS_NUMBER);
        table.setWidthPercentage(TABLE_WIDTH_PERCENTAGE);
        table.setSpacingBefore(TABLE_CELL_PADDING * 2);
        table.setWidths(new float[]{0.7f,1.5f,3.5f,1.5f,1.5f});
        writeTableHeader(table);
        writeTableData(table,userList);
        document.add(table);
        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(TABLE_CELL_PADDING);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(FONT_SIZE);
        font.setColor(Color.WHITE);

        String [] pdfHeader = {"User ID", "Username", "Email", " Phone number", "Is Admin"};
        for (String field : pdfHeader) {
            cell.setPhrase(new Phrase(field, font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table, List<User> userList) {
        for (User user : userList) {
            addCell(table,String.valueOf(user.getId()));
            addCell(table,user.getUsername());
            addCell(table,user.getEmail());
            addCell(table,user.getPhoneNumber());
            addCell(table,String.valueOf(user.isAdmin()));
        }
    }

    private void addCell(PdfPTable table, String cellContent){
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(FONT_SIZE);
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(cellContent, font));
        table.addCell(cell);
    }
}
