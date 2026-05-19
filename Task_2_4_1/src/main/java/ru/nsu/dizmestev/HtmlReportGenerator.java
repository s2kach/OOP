package ru.nsu.dizmestev;

import java.util.List;

/**
 * Класс, отвечающий за формирование визуального представления результатов.
 * Генерирует HTML-код отчета на основе собранных данных.
 */
public class HtmlReportGenerator {

    /**
     * Генерирует полный HTML-документ отчета.
     * Включает таблицу результатов студентов и таблицу контрольных точек.
     *
     * @param results     список результатов проверок
     * @param checkpoints список контрольных точек курса
     * @return строка, содержащая HTML-код
     */
    public String generate(List<CheckResult> results, List<Checkpoint> checkpoints) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        sb.append("<title>OOP Report</title></head><body>");
        sb.append("<h2>Отчет о проверке репозиториев</h2>");

        sb.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
        sb.append("<tr style='background-color: #f2f2f2;'>")
                .append("<th>Студент</th><th>Задача</th><th>Статус</th><th>Тесты (П/Ф/С)</th>")
                .append("<th>Стиль</th><th>Дата сдачи</th><th>Итоговый балл</th></tr>");

        for (CheckResult res : results) {
            sb.append("<tr>")
                    .append("<td>").append(res.getStudent().getName()).append("</td>")
                    .append("<td>").append(res.getTaskId()).append("</td>")
                    .append("<td style='color: ")
                    .append(res.isBuildSuccess() ? "green" : "red").append(";'>")
                    .append(res.isBuildSuccess() ? "BUILD SUCCESS" : "BUILD FAILED")
                    .append("</td>").append("<td>")
                    .append(res.getTestResult().toString()).append("</td>")
                    .append("<td>").append(res.getStyleErrors() == 0 ? "OK" : "Errors: "
                            + res.getStyleErrors()).append("</td>")
                    .append("<td>").append(res.getSubmissionDate()).append("</td>")
                    .append("<td><b>").append(res.getTotalScore()).append("</b></td>")
                    .append("</tr>");
        }

        sb.append("</table><br><h2>Контрольные точки и итоговая аттестация</h2>");
        sb.append("<table border='1' style='border-collapse: collapse;'>");
        sb.append("<tr style='background-color: #f2f2f2;'>");
        sb.append("<th>Контрольная точка</th><th>Дата</th></tr>");

        for (Checkpoint cp : checkpoints) {
            sb.append("<tr><td>").append(cp.getName()).append("</td>")
                    .append("<td>").append(cp.getDate()).append("</td></tr>");
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }
}