package ui;

import model.entities.Client;
import model.entities.Project;
import model.enums.ProjectStatus;
import service.ProjectService;

import java.util.Scanner;

public class ProjectUI {

    private ProjectService projectService;
    private MaterialUI materialUI;
    private LaborUI laborUI;
    private Scanner scanner;
    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";

    public ProjectUI(ProjectService projectService,MaterialUI materialUI, LaborUI laborUI) {
        this.projectService = projectService;
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

        System.out.print("Would you like to apply a profit margin to the project? (y/n): ");
        String applyMargin = scanner.nextLine();

        double profitMarginPercentage = 0.0;
        if (applyMargin.equalsIgnoreCase("y")) {
            System.out.print("Enter the profit margin percentage (%): ");
            profitMarginPercentage = scanner.nextDouble();
            scanner.nextLine();
        }
        projectService.calculateCost(project.getId(), materialCost, laborCost, profitMarginPercentage);
    }

}
