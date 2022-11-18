package de.ostfalia.i.smartheating;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String... args) {
        // List of contacts to export to Excel file.
        List<Measurement> contacts = new ArrayList<>();
        contacts.add(new Measurement("Raum", "Zählerstand", "Tag", "Monat", "Jahr", "Stunde"));

        // Header text
        String[] headers = new String[] {"Heizkörper", "Zählerstand", "Tag", "Monat", "Jahr", "Stunde"};

        // File name
        String fileName = "contacts.xlsx";

        // Export Excel file
        ExcelFileExporter excelFileExporter = new ExcelFileExporter();
        excelFileExporter.exportExcelFile(contacts, headers, fileName);
    }
}
