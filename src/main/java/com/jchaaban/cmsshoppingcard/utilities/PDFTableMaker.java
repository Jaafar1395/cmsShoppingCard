package com.jchaaban.cmsshoppingcard.utilities;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFTableMaker {

    final int TITLE_MARGIN_BOTTOM = 15;
    final int TITLE_FONT_SIZE = 17;
    final int TABLE_MAX_WIDTH = 600;

    public Table createTableHavingTitles(int numOfColumns){
        int colWidth = TABLE_MAX_WIDTH / numOfColumns;
        float[] pointColumnWidths = new float[numOfColumns];
        for (int i = 0; i < numOfColumns; ++i)
            pointColumnWidths[i] = colWidth;

        return new Table(pointColumnWidths);
    }

    public void writeTableHeader(Table table, String [] titles) {

        Cell cell;
        for (String title : titles) {
            cell = new Cell();
            cell.setFontColor(ColorConstants.WHITE);
            cell.setBackgroundColor(ColorConstants.DARK_GRAY);
            Paragraph paragraph = new Paragraph(title).setTextAlignment(TextAlignment.CENTER);
            cell.add(paragraph);
            table.addCell(cell);
        }
    }

    public void setPdfTitle(String title, Document document){
        document.add(new Paragraph(title).setFontSize(TITLE_FONT_SIZE)
                .setMarginBottom(TITLE_MARGIN_BOTTOM)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));
    }

    public void addContentCell(Table table, int fontSize, String content){
        addCell(table,fontSize,String.valueOf(content));
    }

    public void addCell(Table table, int fontSize, String content){
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph paragraph = new Paragraph(content);
        paragraph.setFontSize(fontSize);
        cell.add(paragraph);
        cell.setFontColor(ColorConstants.BLACK);
        cell.setBackgroundColor(ColorConstants.WHITE);
        cell.setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }

    public String getFormattedDateAndTimeString(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy , HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }


}
