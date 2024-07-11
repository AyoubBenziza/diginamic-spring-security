package fr.diginamic.springdemo.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.springdemo.annotations.csv.CSVField;
import fr.diginamic.springdemo.annotations.pdf.PDFList;
import fr.diginamic.springdemo.annotations.pdf.PDFValue;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * Utility class to export data to files
 * @see CSVPrinter
 * @see CSVFormat
 * @see HttpServletResponse
 * @author AyoubBenziza
 */
public class ExportsUtils {
    /**
     * Export a set of data to a CSV file
     * @param data the data to export
     * @param filename the name of the file
     * @param response the HttpServletResponse
     */
    public static void toCSVFile(Set<?> data, String filename, HttpServletResponse response) throws IOException, IllegalAccessException {
        if (data.isEmpty()) return;

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");

        // Assuming all objects in the set are of the same type
        Object firstObj = data.iterator().next();
        List<String> headers = new ArrayList<>();
        List<Field> annotatedFields = new ArrayList<>();

        // Collect headers and annotated fields
        for (Field field : firstObj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(CSVField.class)) {
                headers.add(field.getAnnotation(CSVField.class).name());
                annotatedFields.add(field);
            }
        }

        CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader(headers.toArray(new String[0])).build();

        try (CSVPrinter printer = new CSVPrinter(response.getWriter(), csvFormat)) {
            for (Object obj : data) {
                List<Object> values = new ArrayList<>();
                for (Field field : annotatedFields) {
                    field.setAccessible(true);
                    values.add(field.get(obj));
                }
                printer.printRecord(values);
            }
        }
    }

    public static void toPDFFile(Set<?> data, String filename, HttpServletResponse response) throws DocumentException, IOException, IllegalAccessException {
        if (data.isEmpty()) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".pdf");

        Document document = new Document(PageSize.A4, 36, 36, 36, 36); // Reduced margins
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Object firstObj = data.iterator().next();
        List<String> headers = getStrings(firstObj);

        PdfPTable mainTable = new PdfPTable(headers.size());
        mainTable.setWidthPercentage(100); // Use full page width

        // Add headers to the table
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setBorderWidth(1);
            headerCell.setPadding(4);
            headerCell.setPhrase(new Phrase(header));
            mainTable.addCell(headerCell);
        }

        // Process each object in the data set
        for (Object obj : data) {
            processFieldForTable(obj, mainTable); // This method needs to be adjusted to not add headers again
        }

        document.add(mainTable);
        document.close();
    }

    private static void processFieldForTable(Object obj, PdfPTable table) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(PDFValue.class)) {
                // Handle primitive fields and strings
                String value = field.get(obj).toString();
                table.addCell(new PdfPCell(new Phrase(value)));
            } else if (field.isAnnotationPresent(PDFList.class)) {
                // Handle collections
                Object listValue = field.get(obj);
                if (listValue instanceof Collection<?> collection) {
                    if (!collection.isEmpty()) {
                        Object firstItem = collection.iterator().next();
                        List<String> headers = getStrings(firstItem); // Reuse getStrings to prepare headers for subtable
                        PdfPTable subTable = new PdfPTable(headers.size());
                        // Add headers to the subtable
                        headers.forEach(header -> {
                            PdfPCell headerCell = new PdfPCell(new Phrase(header));
                            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            headerCell.setBorderWidth(1);
                            subTable.addCell(headerCell);
                        });
                        // Add values to the subtable
                        for (Object item : collection) {
                            processFieldForTable(item, subTable); // Recursive call
                        }
                        PdfPCell cell = new PdfPCell(subTable);
                        cell.setColspan(headers.size()); // Span across the number of headers
                        table.addCell(cell);
                    }
                }
            }
        }
    }


    private static List<String> getStrings(Object firstObj) {
        Class<?> clazz = firstObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Prepare headers
        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PDFValue.class)) {
                headers.add(field.getAnnotation(PDFValue.class).name());
            } else if (field.isAnnotationPresent(PDFList.class)) {
                headers.add(field.getAnnotation(PDFList.class).name());
            }
        }
        return headers;
    }
}
