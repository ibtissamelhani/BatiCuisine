package ui;

import model.entities.Material;
import model.entities.Project;
import model.enums.ComponentType;
import service.MaterialService;

import java.util.Scanner;

public class MaterialUI {

    private MaterialService materialService;
    private Scanner scanner;
    final String YELLOW = "\u001B[33m";
    final String BLEU = "\u001B[94m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";

    public MaterialUI(MaterialService materialService) {
        this.materialService = materialService;
        this.scanner =  new Scanner(System.in);
    }

    public void addMaterialUI(Project project) {

        System.out.println(BLEU+"\n---------------------------------------     Add Material   ------------------------------------------\n"+RESET);

        System.out.print("Enter material name: ");
        String name = scanner.nextLine();

        System.out.print("\nEnter material quantity (e.g., m² or liters): ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("\nEnter unit cost of material (€): ");
        double unitCost = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("\nEnter transport cost of material (€): ");
        double transportCost = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("\nEnter quality coefficient of material (1.0 = standard, > 1.0 = high quality): ");
        double qualityCoefficient = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("\nEnter tax rate (%): ");
        double taxRate = scanner.nextDouble();
        scanner.nextLine();

        Material material = new Material(name, ComponentType.MATERIAL, quantity,project, unitCost, transportCost, qualityCoefficient, taxRate);

        boolean success = materialService.addMaterialToProject(material);

        if (success) {
            System.out.println(GREEN+"Material added successfully!"+RESET);
        } else {
            System.out.println("Failed to add material.");
        }

        System.out.print("\nWould you like to add another material? (y/n): ");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            addMaterialUI(project);
        }
    }

}
