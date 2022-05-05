package brum.domain.file.writers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public abstract class ExcelFileWriter {

    protected ExcelFileWriter() {}

    protected <T> void writeToSheet(Sheet sheet, List<T> data, List<Column<T>> columns) {
        Row headersRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headersRow.createCell(i);
            cell.setCellValue(columns.get(i).title);
        }
        int rowIndex = 1;
        for (T object : data) {
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;
            for (Column<T> column : columns) {
                Cell cell = row.createCell(cellIndex);
                Object value = column.action.apply(object);
                setCellValue(cell, value);
                cellIndex++;
            }
            rowIndex++;
        }
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            return;
        }
        if (value instanceof Boolean) {
            cell.setCellValue(Boolean.TRUE.equals(value));
            return;
        }
        cell.setCellValue(String.valueOf(value));
    }

    protected byte[] getBytesFromWorkbook(Workbook workbook) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            return new byte[0];
        }
        return baos.toByteArray();
    }

    protected static class Column<T> {
        String title;
        Function<T, Object> action;

        public Column(String title, Function<T, Object> action) {
            this.title = title;
            this.action = action;
        }
    }
}
