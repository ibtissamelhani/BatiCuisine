package ui;

import model.entities.Project;
import model.entities.Quote;
import service.ProjectService;
import service.QuoteService;
import utils.DateFormat;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class QuoteUI {

    private QuoteService quoteService;
    private ProjectService projectService;
    private Scanner scanner;

    public QuoteUI(QuoteService quoteService, ProjectService projectService) {
        this.quoteService = quoteService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void createQuoteUI(Project project) {
        System.out.println("------------------------------------------- Quote Registration -------------------------");

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
        Quote newQuote = new Quote(totalCost,validityDate,issueDate,false,project);
//        newQuote.setEstimatedAmount(totalCost);
//        newQuote.setIssueDate(issueDate);
//        newQuote.setValidityDate(validityDate);
//        newQuote.setAccepted(false);
//        newQuote.setProject(project);

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

   public void deleteQuoteUI() {
        Quote quote = showQuoteUI();
        if (quote != null) {
            boolean success = quoteService.delete(quote.getId());
            if (success) {
                System.out.println("Quote deleted successfully!");
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
}
