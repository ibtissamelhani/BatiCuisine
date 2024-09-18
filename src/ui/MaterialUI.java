package ui;

import model.entities.Material;
import model.entities.Project;
import model.enums.ComponentType;
import service.MaterialService;

import java.util.Scanner;

public class MaterialUI {

    private MaterialService materialService;
    private Scanner scanner;

    public MaterialUI(MaterialService materialService) {
        this.materialService = materialService;
        this.scanner =  new Scanner(System.in);
    }

    public void addMaterialUI(Project project) {

        System.out.println("--- Add Material ---");

        System.out.print("Enter material name: ");
        String name = scanner.nextLine();

        System.out.print("Enter material quantity (e.g., m² or liters): ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter unit cost of material (€): ");
        double unitCost = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter transport cost of material (€): ");
        double transportCost = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter quality coefficient of material (1.0 = standard, > 1.0 = high quality): ");
        double qualityCoefficient = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter tax rate (%): ");
        double taxRate = scanner.nextDouble();
        scanner.nextLine();

        Material material = new Material(name, ComponentType.MATERIAL, quantity,project, unitCost, transportCost, qualityCoefficient, taxRate);

        boolean success = materialService.addMaterialToProject(material);

        if (success) {
            System.out.println("Material added successfully!");
        } else {
            System.out.println("Failed to add material.");
        }

        System.out.print("Would you like to add another material? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            addMaterialUI(project);  // Recursively add more materials if the user chooses to
        }
    }

}
