package ui;

import model.entities.Client;
import model.entities.Project;
import model.enums.ProjectStatus;
import service.ClientService;
import service.ProjectService;

import java.util.Optional;
import java.util.Scanner;

public class ProjectUI {

    private ProjectService projectService;
    private ClientService clientService;
    private MaterialUI materialUI;
    private LaborUI laborUI;
    private Scanner scanner;
    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";

    public ProjectUI(ProjectService projectService,MaterialUI materialUI, LaborUI laborUI,ClientService clientService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.materialUI = materialUI;
        this.laborUI = laborUI;
        this.scanner = new Scanner(System.in);
    }

    public void createProjectForClient(Client client) {

        System.out.println(BLUE + "\n--------------------------------------- Creating a New Project ------------------------------------------\n" + RESET);
        System.out.print("Enter project name: : ");
        String projectName = scanner.nextLine();

        System.out.print("Enter the project area (in m²): ");
        double surfaceArea = scanner.nextDouble();
        scanner.nextLine();


        Project newProject = new Project(projectName, 0., 0., ProjectStatus.IN_PROGRESS, surfaceArea, client);
        Project savedProject = projectService.createProject(newProject);
        Double  totalMaterialCost= materialUI.addMaterialUI(savedProject);
        Double  totalLaborCost= laborUI.addLaborUI(savedProject);
        calculateTotalCost(savedProject, totalMaterialCost, totalLaborCost);
    }

    public void calculateTotalCost(Project project, Double materialCost, Double laborCost) {
        System.out.println(YELLOW+"\n--------------------------------------- Total Cost Calculation ------------------------------------------\n"+RESET);

        double totalCost = 0.0;
        System.out.print("Would you like to apply a profit margin to the project? (y/n): ");
        String applyMargin = scanner.nextLine();

        double profitMarginPercentage = 0.0;
        if (applyMargin.equalsIgnoreCase("y")) {
            System.out.print("Enter the profit margin percentage (%): ");
            profitMarginPercentage = scanner.nextDouble();
            scanner.nextLine();
        }

        totalCost = totalCost + materialCost + laborCost;
        System.out.println("---->Total cost of the project: " + String.format("%.2f", totalCost) + " €");

        // Add the profit margin to the total cost
        totalCost = totalCost * (1 + (profitMarginPercentage/100));
        System.out.println("---->Total cost of the project after profit margin: " + String.format("%.2f", totalCost) + " €");

        System.out.print("Would you like to apply VAT to the project? (y/n): ");
        String applyVAT = scanner.nextLine();

        //apply TVA
        double TVAPercentage = 0.0;
        if (applyVAT.equalsIgnoreCase("y")) {
            System.out.print("Enter the VAT percentage (%): ");
            TVAPercentage = scanner.nextDouble();
            scanner.nextLine();

            totalCost =  totalCost * (1 + (TVAPercentage/100));
            System.out.println("---->Total cost of the project after applying VTA: " + String.format("%.2f", totalCost) + " €");
        }

        //if client is pro apply discount
        Optional<Client> client = clientService.getClientById(project.getClient().getId());
        double discount = 0.0;
        if (client.isPresent() && client.get().getProfessional()) {
            System.out.println("client : "+client.get().getName()+" is professional client");
            System.out.print("Would you like to give discount for this client? (y/n): ");
            String applyDiscount = scanner.nextLine();
            if (applyDiscount.equalsIgnoreCase("y")) {
                System.out.print("Enter the discount percentage (%): ");
                discount = scanner.nextDouble();
                scanner.nextLine();
            }
            // Add the discount  to the total cost
            totalCost = totalCost * (1 - (discount/100));
            System.out.println("---->Total cost of the project after discount: " + String.format("%.2f", totalCost) + " €");

        }

        projectService.addCalculatedCostToProject(project.getId(), totalCost, profitMarginPercentage);
    }

}
