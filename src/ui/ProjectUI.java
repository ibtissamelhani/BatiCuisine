package ui;

import model.entities.Client;
import model.entities.Project;
import model.enums.ProjectStatus;
import service.ProjectService;

import java.util.Scanner;

public class ProjectUI {

    private ProjectService projectService;
    private Scanner scanner;

    public ProjectUI(ProjectService projectService) {
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void createProjectForClient(Client client) {

        System.out.println("--- Creating a New Project ---");
        System.out.print("Enter project name: : ");
        String projectName = scanner.nextLine();

        System.out.print("Enter the project area (in m²): ");
        double surfaceArea = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter the profit margin (in %): ");
        double profitMargin = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Entrez le coût total du projet : ");
        double totalCost = scanner.nextDouble();
        scanner.nextLine();

        Project newProject = new Project(projectName, profitMargin, totalCost, ProjectStatus.IN_PROGRESS, surfaceArea, client);
        boolean success = projectService.createProject(newProject);

        if (success) {
            System.out.println("Projet créé avec succès !");
        } else {
            System.out.println("Échec de la création du projet.");
        }
    }
}
