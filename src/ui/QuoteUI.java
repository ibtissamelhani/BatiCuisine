package ui;

import model.entities.Project;
import model.entities.Quote;
import service.QuoteService;
import utils.DateFormat;

import java.time.LocalDate;
import java.util.Scanner;

public class QuoteUI {

    private QuoteService quoteService;
    private Scanner scanner;

    public QuoteUI(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.scanner = new Scanner(System.in);
    }

    public void createQuoteUI(Project project) {
        System.out.println("--- Quote Registration ---");

        double totalCost = project.getTotalCost();

        System.out.println("The estimated amount for the project is : " + String.format("%.2f", totalCost) + " €");

        System.out.print("Enter the issue date of the quote (format: dd/MM/yyyy): ");
        String issueDateStr = scanner.nextLine();
        LocalDate issueDate = DateFormat.parseDate(issueDateStr);
        LocalDate validityDate = LocalDate.now();
        while (true){
            System.out.print("Enter the validity date of the quote (format: dd/MM/yyyy): ");
            String validityDateStr = scanner.nextLine();
            validityDate = DateFormat.parseDate(validityDateStr);

            if (!validityDate.isBefore(issueDate)) {
                break;
            } else {
                System.out.println(" \n validate date cannot be before the issue date. Please enter valid dates.\n");
            }
        }


        // Create the quote
        Quote newQuote = new Quote();
        newQuote.setEstimatedAmount(totalCost);
        newQuote.setIssueDate(issueDate);
        newQuote.setValidityDate(validityDate);
        newQuote.setAccepted(false);
        newQuote.setProject(project);

        System.out.print("Do you wish to save the quote? (y/n):");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            boolean success = quoteService.createQuote(newQuote);
            if (success) {
                System.out.println("Quote saved successfully!");
            } else {
                System.out.println("Error while saving the quote.");
            }
        } else {
            System.out.println("Quote registration canceled.");
        }
    }

}
