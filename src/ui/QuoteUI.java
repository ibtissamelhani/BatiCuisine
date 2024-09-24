package ui;

import model.entities.*;
import model.enums.ProjectStatus;
import service.ProjectService;
import service.QuoteService;
import utils.DateFormat;
import utils.InputValidation;

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

        LocalDate issueDate = DateFormat.readDate("\nEnter the issue date of the quote (format: dd/MM/yyyy): ");

        LocalDate validityDate = DateFormat.readAndValidateValidityDate(issueDate);

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

            String acceptedInput = InputValidation.readString("Is the quote accepted? (y/n, or press Enter to keep current): ");
            if (!acceptedInput.isEmpty()) {
                quote.setAccepted(acceptedInput.equalsIgnoreCase("y"));
                if (!quote.getAccepted() || quote.getValidityDate().isBefore(LocalDate.now())) {
                    Project project = quote.getProject();
                    project.setProjectStatus(ProjectStatus.CANCELED);
                    projectService.update(project);
                    System.out.println("The project status has been updated to CANCELED due to the quote being refused or invalid.");
                }
            }

            String saveChoice = InputValidation.readString("Do you wish to save the changes? (y/n): ");
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

    public void findProjectWithDetailsUI(){

        String client_name ;
        try {
            client_name = InputValidation.readString("Enter the client name: ");
        }catch (Exception e){
            System.err.println("\033[0;31mInvalid client name\033[0m");
            return;
        }

        String project_name ;
        try {
            project_name = InputValidation.readString("Enter the project name: ");
        }catch (Exception e){
            System.err.println("\033[0;31mInvalid project name\033[0m");
            return;
        }

        Optional<Project> optionalProject = projectService.findByNameAndClientName(project_name,client_name);
        if (!optionalProject.isPresent()){
            System.err.println("\033[0;31mProject not found\033[0m");
            return;
        }
        Quote quote = quoteService.findProjectWithDetails(optionalProject.get().getId());

        // Header
        System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-10s %-15s%n",
                "Project Name", "Client Name", "Profit Margin",
                "Total Cost", "Surface Area", "Quote Amount", "Accepted?","status" );
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        // Display project info
        System.out.printf("%-20s %-15s %-15.2f %-15.2f %-15.2f %-15.2f %-10s %-15s%n",
                quote.getProject().getProjectName(),
                quote.getProject().getClient().getName(),
                quote.getProject().getProfitMargin(),
                quote.getProject().getTotalCost(),
                quote.getProject().getSurfaceArea(),
                quote.getEstimatedAmount(),
                quote.getAccepted() ? "Yes" : "No",
                quote.getProject().getProjectStatus());


        // Display Components (Materials and Labor)
        System.out.println("\nComponents:");
        System.out.printf("%-15s %-15s %-10s %-15s %-15s%n",
                "Component Name", "Component Type", "Cost", "Quantity/Hours", "Tax Rate");
        System.out.println("---------------------------------------------------------------------------");

        for (Component component : quote.getProject().getComponentList()) {
            if (component instanceof Material) {
                Material material = (Material) component;
                System.out.printf("%-15s %-15s %-10.2f %-15.2f %-15.2f%n",
                        material.getName(), "Material", material.calculateTotalCost(),
                        material.getQuantity(), material.getTaxRate());
            } else if (component instanceof Labor) {
                Labor labor = (Labor) component;
                System.out.printf("%-15s %-15s %-10.2f %-15.2f %-15.2f%n",
                        labor.getName(), "Labor", labor.calculateTotalCost(),
                        labor.getWorkHours(), labor.getTaxRate());
            }
        }

    }


}
