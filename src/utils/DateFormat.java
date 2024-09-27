package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateFormat {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Scanner scanner = new Scanner(System.in);

    public static LocalDate readDate(String prompt) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            try {
                date = LocalDate.parse(dateStr, dateFormat);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Invalid date");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please enter the date in the format dd/MM/yyyy.");
            }
        }
        return date;
    }

    public static LocalDate readAndValidateValidityDate(LocalDate issueDate) {
        LocalDate validityDate = null;
        while (validityDate == null || validityDate.isBefore(issueDate)) {
            validityDate = readDate("\nEnter the validity date of the quote (format: dd/MM/yyyy): ");
            if (validityDate.isBefore(issueDate)) {
                System.out.println("\nError: The validity date cannot be before the issue date. Please enter a valid date.\n");
            }
        }
        return validityDate;
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, dateFormat);
    }
}
