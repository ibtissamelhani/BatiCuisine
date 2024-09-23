package ui;

import model.entities.Material;
import model.entities.Project;
import model.enums.ComponentType;
import service.MaterialService;

import java.util.Scanner;

public class MaterialUI {

    private MaterialService materialService;
    private Scanner scanner;
    final String CYAN = "\033[0;36m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";
    final String RED = "\033[0;31m";

    public MaterialUI(MaterialService materialService) {
        this.materialService = materialService;
        this.scanner =  new Scanner(System.in);
    }

    public Double addMaterialUI(Project project) {

        double totalMaterialCost = 0.0;
        System.out.println(CYAN+"\n-----------------------------------------     Add Materials   --------------------------------------------\n"+RESET);

        do{
        System.out.print("\nEnter material name: ");
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

        boolean success = materialService.addMaterial(material);

        if (success) {
            double materialCost = material.calculateTotalCost();
            totalMaterialCost += materialCost;
            System.out.println(GREEN+"\nMaterial added successfully! Cost of this material : "+String.format("%.2f", materialCost) + " €"+RESET);
        } else {
            System.out.println(RED+" Failed to add material."+RESET);
        }

        System.out.print("\n Would you like to add another material? (y/n): ");
        }while (scanner.nextLine().equalsIgnoreCase("y"));

        return totalMaterialCost;
    }

}
