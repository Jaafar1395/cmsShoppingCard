package com.jchaaban.cmsshoppingcard.utilities;

import com.jchaaban.cmsshoppingcard.models.data.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    @Override
    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        setResponseHeader(response,"text/csv",".csv");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String [] csvHeader = {"User ID", "Username", "Email", " Phone number", "Is Admin"};
        String [] fieldMapping = {"id", "username", "email", "phoneNumber", "isAdmin"};
        writeFileContent(userList, csvWriter, csvHeader, fieldMapping);
        csvWriter.close();

    }

    private void writeFileContent(List<User> users, ICsvBeanWriter csvWriter, String[] csvHeader,
                                  String[] objectFields) throws IOException {
        csvWriter.writeHeader(csvHeader);
        for (User user : users)
            csvWriter.write(user, objectFields);
    }
}
