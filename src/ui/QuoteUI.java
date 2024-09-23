package ui;

import model.entities.Client;
import model.entities.Project;
import model.entities.Quote;
import service.ProjectService;
import service.QuoteService;
import utils.DateFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class QuoteUI {

    private QuoteService quoteService;
    private ProjectService projectService;
    private Scanner scanner;
    final String RED = "\u001B[31m";
    final String BLUE = "\u001B[34m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";



    public QuoteUI(QuoteService quoteService, ProjectService projectService) {
        this.quoteService = quoteService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void createQuoteUI(Project project) {
        System.out.println(BLUE+"\n------------------------------------------- Quote Registration -------------------------"+RESET);

        double totalCost = project.getTotalCost();

        System.out.println("\nThe estimated amount for the project is : " + String.format("%.2f", totalCost) + " €");

        System.out.print("\nEnter the issue date of the quote (format: dd/MM/yyyy): ");
        String issueDateStr = scanner.nextLine();
        LocalDate issueDate = DateFormat.parseDate(issueDateStr);
        LocalDate validityDate = LocalDate.now();
        while (true){
            System.out.print("\nEnter the validity date of the quote (format: dd/MM/yyyy): ");
            String validityDateStr = scanner.nextLine();
            validityDate = DateFormat.parseDate(validityDateStr);

            if (!validityDate.isBefore(issueDate)) {
                break;
            } else {
                System.out.println(" \n validate date cannot be before the issue date. Please enter valid dates.\n");
            }
        }


        // Create the quote
        Quote newQuote = new Quote(totalCost,validityDate,issueDate,false,project);

            boolean success = quoteService.createQuote(newQuote);
            if (success) {
                System.out.println(GREEN+" Quote saved successfully!"+RESET);
            } else {
                System.out.println(RED+" Error while saving the quote."+RESET);
            }
    }

    public void deleteQuoteUI() {
       System.out.println("deleting quote");
        Quote quote = showQuoteUI();
        if (quote != null) {
            boolean success = quoteService.delete(quote.getId());
            if (success) {
                System.out.println("Quote deleted successfully!");
            }
        }
   }

    public void updateQuoteUI() {
       System.out.println("updating quote");
        Quote quote = showQuoteUI();
        if (quote != null) {
            System.out.print("Enter new issue date (format: dd/MM/yyyy, or press Enter to keep current): ");
            String issueDateStr = scanner.nextLine();
            if (!issueDateStr.isEmpty()) {
                LocalDate newIssueDate = DateFormat.parseDate(issueDateStr);
                quote.setIssueDate(newIssueDate);
            }

            LocalDate validityDate = quote.getValidityDate();
            while (true) {
                System.out.print("Enter new validity date (format: dd/MM/yyyy, or press Enter to keep current): ");
                String validityDateStr = scanner.nextLine();
                if (validityDateStr.isEmpty()) {
                    break;
                } else {
                    LocalDate newValidityDate = DateFormat.parseDate(validityDateStr);
                    if (!newValidityDate.isBefore(quote.getIssueDate())) {
                        quote.setValidityDate(newValidityDate);
                        break;
                    } else {
                        System.out.println("Validity date cannot be before the issue date. Please enter a valid date.");
                    }
                }
            }

            System.out.print("Is the quote accepted? (y/n, or press Enter to keep current): ");
            String acceptedInput = scanner.nextLine();
            if (!acceptedInput.isEmpty()) {
                quote.setAccepted(acceptedInput.equalsIgnoreCase("y"));
            }

            System.out.print("Do you wish to save the changes? (y/n): ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                boolean success = quoteService.update(quote);
                if (success) {
                    System.out.println("Quote updated successfully!");
                } else {
                    System.out.println("Error while updating the quote.");
                }
            } else {
                System.out.println("Quote update canceled.");
            }


        }

   }

    public Quote showQuoteUI() {
        System.out.print("Enter the client name: ");
        String client_name ;
        try {
            client_name = scanner.nextLine();
        }catch (Exception e){
            System.err.println("\033[0;31mInvalid client name\033[0m");
            return null;
        }

        System.out.print("Enter the project name: ");
        String project_name ;
        try {
            project_name = scanner.nextLine();
        }catch (Exception e){
            System.err.println("\033[0;31mInvalid project name\033[0m");
            return null;
        }

        Optional<Project> optionalProject = projectService.findByNameAndClientName(project_name,client_name);
        if (!optionalProject.isPresent()){
            System.err.println("\033[0;31mProject not found\033[0m");
            return null;
        }
        Project project = optionalProject.get();

        Optional<Quote> OpQuote = quoteService.getQuoteByProjectId(project.getId());

        if (!OpQuote.isPresent()) {
            System.err.println("\033[0;31mQuote not found\033[0m");
            return null;
        }
        Quote quote = OpQuote.get();
        System.out.println("estimation amount : "+ quote.getEstimatedAmount());
        System.out.println("Issue date : "+ quote.getIssueDate());
        System.out.println("Validity date : "+ quote.getValidityDate());
        System.out.println("is quote accepted : "+(quote.getAccepted()?"yes":"no"));
        return quote;
    }

    public void displayAllQuotes() {
        List<Quote> quotes = quoteService.getAllQuotes();

        if (quotes.isEmpty()) {
            System.out.println("No quotes available.");
            return;
        }

        // Table Header
        System.out.printf("%-20s %-25s %-25s %-15s %-10s%n",
                "Client Name", "Project Name", "Estimated Amount", "Issue Date", "Validity Date", "Accepted");

        System.out.println(new String(new char[100]).replace("\0", "-"));

        // Table Rows
        for (Quote quote : quotes) {
            Project project = quote.getProject();
            Client client = project.getClient();

            System.out.printf("%-20s %-25s %-25s %-15s %-10s%n",
                    client.getName(),
                    project.getProjectName(),
                    String.format("%.2f €", quote.getEstimatedAmount()),
                    quote.getIssueDate(),
                    quote.getValidityDate(),
                    quote.getAccepted() ? "Yes" : "No");
        }
    }


}
