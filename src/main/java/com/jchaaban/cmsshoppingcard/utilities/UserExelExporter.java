package com.jchaaban.cmsshoppingcard.utilities;

import com.jchaaban.cmsshoppingcard.models.data.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExelExporter extends AbstractExporter {

    final static int HEADER_FONT_SIZE = 16;
    final static int DATA_FONT_SIZE = 14;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExelExporter(){
        workbook = new XSSFWorkbook();
    }

    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        setResponseHeader(response,"application/octet-stream",".xlsx");
        writeHeaderLine();
        writeDataLines(userList);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    private void setCellValue(XSSFCell cell,Object value){
        if (value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if (value instanceof Boolean)
            cell.setCellValue((Boolean) value);
        else
            cell.setCellValue((String) value);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle cellStyle = getCellStyle(true, HEADER_FONT_SIZE);
        writeHeaderLineContent(row, cellStyle);
    }

    private XSSFCellStyle getCellStyle(boolean bold, int fontSize) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeight(fontSize);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void writeHeaderLineContent(XSSFRow row, XSSFCellStyle cellStyle) {
        String [] exelHeader = {"User ID", "Username", "Email", " Phone number", "Is Admin"};
        for (int i = 0; i < exelHeader.length; ++i)
            createCell(row,i,exelHeader[i], cellStyle);
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        setCellValue(cell,value);
        cell.setCellStyle(style);
    }

    private void writeDataLines(List<User> userList) {
        int rowIndex = 1;
        int columnIndex = 0;
        XSSFRow row;
        XSSFCellStyle cellStyle = getCellStyle(false, DATA_FONT_SIZE);
        for (User user : userList) {
            row = sheet.createRow(rowIndex++);
            createCell(row,columnIndex++,user.getId(),cellStyle);
            createCell(row,columnIndex++,user.getUsername(),cellStyle);
            createCell(row,columnIndex++,user.getEmail(),cellStyle);
            createCell(row,columnIndex++,user.getPhoneNumber(),cellStyle);
            createCell(row,columnIndex,user.isAdmin(),cellStyle);
            columnIndex = 0;
        }
    }


}
