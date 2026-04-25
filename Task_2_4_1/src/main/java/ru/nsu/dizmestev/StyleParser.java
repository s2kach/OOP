package ru.nsu.dizmestev;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 * Парсер отчетов Checkstyle.
 */
public class StyleParser {

    /**
     * Проверяет наличие ошибок стиля в отчете.
     *
     * @param taskDir Папка задачи.
     * @return Количество ошибок стиля.
     */
    public int countStyleErrors(File taskDir) {
        File reportFile = new File(taskDir, "build/reports/checkstyle/main.xml");

        if (!reportFile.exists()) {
            return 0;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(reportFile);
            return doc.getElementsByTagName("error").getLength();
        } catch (Exception e) {
            System.err.println("Style parse error: " + e.getMessage());
            return -1;
        }
    }
}