package ui;

import model.entities.Material;
import model.entities.Project;
import model.enums.ComponentType;
import service.MaterialService;
import utils.InputValidation;

import java.util.Scanner;

public class MaterialUI {

    private MaterialService materialService;
    final String CYAN = "\033[0;36m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";
    final String RED = "\033[0;31m";

    public MaterialUI(MaterialService materialService) {
        this.materialService = materialService;
    }

    public Double addMaterialUI(Project project) {

        String answer;
        double totalMaterialCost = 0.0;
        System.out.println(CYAN+"\n-----------------------------------------     Add Materials   --------------------------------------------\n"+RESET);

        do{

        String name = InputValidation.readString("Enter material name: ");

        double quantity = InputValidation.readDouble("\nEnter material quantity (e.g., m² or liters): ");

        double unitCost = InputValidation.readDouble("\nEnter unit cost of material (€): ");

        double transportCost = InputValidation.readDouble("\nEnter transport cost of material (€): ");

        double qualityCoefficient = InputValidation.readDouble("\nEnter quality coefficient of material (1.0 = standard, > 1.0 = high quality): ");

        double taxRate = InputValidation.readDouble("\nEnter tax rate (%): ");

        Material material = new Material(name, ComponentType.MATERIAL, quantity,project, unitCost, transportCost, qualityCoefficient, taxRate);

        boolean success = materialService.addMaterial(material);

        if (success) {
            double materialCost = material.calculateTotalCost();
            totalMaterialCost += materialCost;
            System.out.println(GREEN+"\nMaterial added successfully! Cost of this material : "+String.format("%.2f", materialCost) + " €"+RESET);
        } else {
            System.out.println(RED+" Failed to add material."+RESET);
        }

          answer = InputValidation.readString("\n Would you like to add another material? (y/n): ");


        }while (answer.equalsIgnoreCase("y"));

        return totalMaterialCost;
    }

}
