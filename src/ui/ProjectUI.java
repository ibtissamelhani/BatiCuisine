package ui;

import model.entities.Client;
import model.entities.Component;
import model.entities.Project;
import model.entities.Quote;
import model.enums.ComponentType;
import model.enums.ProjectStatus;
import service.ClientService;
import service.ProjectService;
import utils.InputValidation;

import java.util.Optional;
import java.util.Scanner;

public class ProjectUI {

    private ProjectService projectService;
    private ClientService clientService;
    private MaterialUI materialUI;
    private LaborUI laborUI;
    private QuoteUI quoteUI;
    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";
    final String RED = "\u001B[31m";


    public ProjectUI(ProjectService projectService,MaterialUI materialUI, LaborUI laborUI,ClientService clientService,QuoteUI quoteUI) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.materialUI = materialUI;
        this.laborUI = laborUI;
        this.quoteUI= quoteUI;
    }

    public void createProjectForClient(Client client) {

        System.out.println(YELLOW + "\n--------------------------------------- Creating a New Project ------------------------------------------\n" + RESET);

        String projectName = InputValidation.readString("Enter project name: ");

        double surfaceArea = InputValidation.readDouble("\nEnter the project area (in m²): ");
        Project newProject = new Project(projectName, 0., 0., ProjectStatus.IN_PROGRESS, surfaceArea, client);
        Project savedProject = projectService.createProject(newProject);
        Double  totalMaterialCost= materialUI.addMaterialUI(savedProject);
        Double  totalLaborCost= laborUI.addLaborUI(savedProject);
        calculateTotalCost(savedProject, totalMaterialCost, totalLaborCost);
    }

    public void calculateTotalCost(Project project, Double materialCost, Double laborCost) {
        System.out.println(YELLOW+"\n--------------------------------------- Total Cost Calculation ------------------------------------------\n"+RESET);

        double totalCost = 0.0;
        String applyMargin = InputValidation.readString("\nWould you like to apply a profit margin to the project? (y/n): ");

        double profitMarginPercentage = 0.0;
        if (applyMargin.equalsIgnoreCase("y")) {
            profitMarginPercentage = InputValidation.readDouble("\nEnter the profit margin percentage (%): ");

        }

        totalCost = totalCost + materialCost + laborCost;
        System.out.println(YELLOW+"\n---->Total cost of the project: "+RESET + String.format("%.2f", totalCost) + " €");

        // Add the profit margin to the total cost
        totalCost = totalCost * (1 + (profitMarginPercentage/100));
        System.out.println(YELLOW+"\n---->Total cost of the project after profit margin: "+RESET + String.format("%.2f", totalCost) + " €");

        String applyVAT = InputValidation.readString("\nWould you like to apply VAT to the project? (y/n): ");

        //apply TVA
        double TVAPercentage = 0.0;
        if (applyVAT.equalsIgnoreCase("y")) {
            TVAPercentage = InputValidation.readDouble("Enter the VAT percentage (%): ");


            totalCost =  totalCost * (1 + (TVAPercentage/100));
            System.out.println(YELLOW+"\n---->Total cost of the project after applying VTA: "+RESET + String.format("%.2f", totalCost) + " €");
        }

        //if client is pro apply discount
        Optional<Client> client = clientService.getClientById(project.getClient().getId());
        double discount = 0.0;
        if (client.isPresent() && client.get().getProfessional()) {
            System.out.println(RED+"\nclient : "+client.get().getName()+" is professional client"+RESET);
            String applyDiscount = InputValidation.readString("Would you like to give discount for this client? (y/n): ");
            if (applyDiscount.equalsIgnoreCase("y")) {
                discount = InputValidation.readDouble("Enter the discount percentage (%): ");

            }
            // Add the discount  to the total cost
            totalCost = totalCost * (1 - (discount/100));
            System.out.println(YELLOW+"\n---->Total cost of the project after discount: "+RESET + String.format("%.2f", totalCost) + " €");

        }

        System.out.println(YELLOW+"Total cost of the Project : "+RESET+String.format("%.2f", totalCost) + " €");

        project.setProfitMargin(profitMarginPercentage);
        project.setTotalCost(totalCost);

        boolean success = projectService.addCalculatedCostToProject(project,totalCost,profitMarginPercentage);
        if (success) {
            quoteUI.createQuoteUI(project);
        }


    }

}
