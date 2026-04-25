package ru.nsu.dizmestev;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Класс для парсинга XML отчетов JUnit.
 */
public class TestParser {

    /**
     * Собирает общую статистику из всех XML файлов в папке результатов.
     *
     * @param taskDir Директория задачи.
     * @return Объект с результатами тестов.
     * @throws Exception В случае ошибки чтения XML.
     */
    public TestResult parse(File taskDir) throws Exception {
        File resultsDir = new File(taskDir, "build/test-results/test");
        int total = 0, failures = 0, errors = 0, skipped = 0;

        if (!resultsDir.exists() || !resultsDir.isDirectory()) {
            return new TestResult(0, 0, 0, 0);
        }

        File[] xmlFiles = resultsDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (xmlFiles == null) return new TestResult(0, 0, 0, 0);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        for (File file : xmlFiles) {
            try {
                Document doc = builder.parse(file);
                NodeList nodes = doc.getElementsByTagName("testsuite");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    total += Integer.parseInt(element.getAttribute("tests"));
                    failures += Integer.parseInt(element.getAttribute("failures"));
                    errors += Integer.parseInt(element.getAttribute("errors"));
                    skipped += Integer.parseInt(element.getAttribute("skipped"));
                }
            } catch (Exception e) {
                throw new Exception("Ошибка парсинга файла: " + file.getName(), e);
            }
        }

        return new TestResult(total, failures, errors, skipped);
    }
}
