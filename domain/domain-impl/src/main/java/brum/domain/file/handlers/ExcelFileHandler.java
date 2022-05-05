package brum.domain.file.handlers;

import brum.model.exception.BRUMGeneralException;
import brum.model.exception.ErrorStatusCode;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelFileHandler extends AbstractFileHandler {

    private int columnsToRead;
    private final Workbook workbook;

    public ExcelFileHandler(byte[] file, Map<FileHeader, Boolean> headersToRead) {
        super(headersToRead);
        try {
            workbook = WorkbookFactory.create(new ByteArrayInputStream(file));
        } catch (IOException e) {
            throw new BRUMGeneralException(ErrorStatusCode.FILE_ERROR, LocalDateTime.now());
        }
    }

    @Override
    public List<String[]> readData() {
        List<String[]> data = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                break;
            }
            String[] rowData = new String[columnsToRead];
            for (int j = 0; j < columnsToRead; j++) {
                Cell cell = row.getCell(j);
                if (cell != null && !cell.getCellType().equals(CellType.BLANK)) {
                    assignValue(rowData, j, cell);
                }
            }
            data.add(rowData);
        }
        return data;
    }

    @Override
    protected String[] readHeaders() {
        String[] headers;
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        columnsToRead = row.getLastCellNum();
        headers = new String[columnsToRead];
        for (int i = 0; i < columnsToRead; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                headers[i] = cell.getStringCellValue();
            } else {
                headers[i] = "";
            }
        }
        return headers;
    }

    @Override
    protected void closeReader() throws IOException {
        workbook.close();
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i < columnsToRead; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !cell.getCellType().equals(CellType.BLANK)) {
                return false;
            }
        }
        return true;
    }

    private void assignValue(String[] row, int index, Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                row[index] = cell.getStringCellValue();
                break;
            case NUMERIC:
                row[index] = Double.toString(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                row[index] = Boolean.toString(cell.getBooleanCellValue());
                break;
            case FORMULA:
                assignFromFormula(row, index, cell);
                break;
            default:
                row[index] = "";
        }
    }

    private void assignFromFormula(String[] row, int index, Cell cell) {
        switch (cell.getCachedFormulaResultType()) {
            case STRING:
                row[index] = cell.getStringCellValue();
                break;
            case NUMERIC:
                row[index] = Double.toString(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                row[index] = Boolean.toString(cell.getBooleanCellValue());
                break;
            default:
                row[index] = "";
        }
    }

}
