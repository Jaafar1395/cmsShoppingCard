package com.jchaaban.cmsshoppingcard.utilities;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.Document;
import com.jchaaban.cmsshoppingcard.models.data.User;

import java.util.List;

public class UserPdfGenerator extends PDFTableMaker {

    final int TABLE_CONTENT_FONT_SIZE = 10;

    public Table generateUsersPdf(List<User> userList, Document document, String title){
        setPdfTitle(title,document);
        String [] tableHeaderTitles = {"User ID", "Username", "Email", " Phone number", "Is Admin"};
        Table table = createTableHavingTitles(tableHeaderTitles.length);
        writeTableHeader(table,tableHeaderTitles);
        writeTableData(table,TABLE_CONTENT_FONT_SIZE,userList);

        return table;
    }

    private void writeTableData(Table table, int fontsize, List<User> userList) {
        for (User user : userList) {
            addContentCell(table,fontsize,String.valueOf(user.getId()));
            addContentCell(table,fontsize,user.getUsername());
            addContentCell(table,fontsize,user.getEmail());
            addContentCell(table,fontsize,user.getPhoneNumber());
            addContentCell(table,fontsize,String.valueOf(user.isAdmin()));
        }
    }
}
