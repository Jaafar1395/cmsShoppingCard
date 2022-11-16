package com.jchaaban.cmsshoppingcard.utilities;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.jchaaban.cmsshoppingcard.models.data.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {

    @Override
    public void export(List<User> userList, HttpServletResponse response) throws IOException {

        PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new com.itextpdf.layout.Document(pdfDoc);

        setResponseHeader(response,"application/pdf",".pdf");
        UserPdfGenerator userPdfGenerator = new UserPdfGenerator();
        Table table = userPdfGenerator.generateUsersPdf(userList,document,"All Users");
        document.add(table);
        document.close();
    }

}
