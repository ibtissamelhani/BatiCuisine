package ui;

import model.entities.Labor;
import model.entities.Project;
import model.enums.ComponentType;
import service.LaborService;

import java.util.Scanner;

public class LaborUI {

    private LaborService laborService;
    private Scanner scanner;
    final String YELLOW = "\u001B[33m";
    final String BLEU = "\u001B[94m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";

    public LaborUI(LaborService laborService) {
        this.laborService = laborService;
        this.scanner = new Scanner(System.in);
    }


    public Double addLaborUI(Project project) {

        double totalLaborCost = 0.0;

        System.out.println(BLEU+"\n---------------------------------------     Add Labors    ------------------------------------------\n"+RESET);

        do {
        System.out.print("Enter the type of labor (e.g., General Worker, Specialist): ");
        String name = scanner.nextLine();

        System.out.print("Enter the hourly rate for this labor (€ / h): ");
        Double hourlyRate = scanner.nextDouble();

        System.out.print("Enter the number of hours worked: ");
        Double workHours = scanner.nextDouble();

        System.out.print("Enter the productivity factor (1.0 = standard, > 1.0 = high productivity): ");
        Double workerProductivity = scanner.nextDouble();
        scanner.nextLine();

        Labor labor = new Labor(name,ComponentType.LABOR,0.20,hourlyRate,workHours,workerProductivity,project);


        Boolean success = laborService.addLabor(labor);
            if (success) {
                double laborCost = labor.calculateTotalCost();
                totalLaborCost += laborCost;
                System.out.println(GREEN + "Labor added successfully! Cost of this labor: " + laborCost + "€" + RESET);
            } else {
                System.out.println("Failed to add labor.");
            }


        System.out.print("Would you like to add another labor? (y/n): ");
        }while (scanner.nextLine().equalsIgnoreCase("y"));

        return totalLaborCost;
    }
}
