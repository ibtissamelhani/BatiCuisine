package ui;

import model.entities.Labor;
import model.entities.Project;
import model.enums.ComponentType;
import service.LaborService;

import java.util.Scanner;

public class LaborUI {

    private LaborService laborService;
    private Scanner scanner;

    final String PURPLE = "\033[0;35m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";
    final String RED = "\033[0;31m";



    public LaborUI(LaborService laborService) {
        this.laborService = laborService;
        this.scanner = new Scanner(System.in);
    }


    public Double addLaborUI(Project project) {

        double totalLaborCost = 0.0;

        System.out.println(PURPLE+"\n------------------------------------------     Add Labors    ---------------------------------------------\n"+RESET);

        do {
        System.out.print("\nEnter the type of labor (e.g., General Worker, Specialist): ");
        String name = scanner.nextLine();

        System.out.print("\nEnter the hourly rate for this labor (€ / h): ");
        Double hourlyRate = scanner.nextDouble();

        System.out.print("\nEnter the number of hours worked: ");
        Double workHours = scanner.nextDouble();

        System.out.print("\nEnter the productivity factor (1.0 = standard, > 1.0 = high productivity): ");
        Double workerProductivity = scanner.nextDouble();
        scanner.nextLine();

        Labor labor = new Labor(name,ComponentType.LABOR,0.20,hourlyRate,workHours,workerProductivity,project);


        Boolean success = laborService.addLabor(labor);
            if (success) {
                double laborCost = labor.calculateTotalCost();
                totalLaborCost += laborCost;
                System.out.println(GREEN + "\nLabor added successfully! Cost of this labor: " + laborCost + "€" + RESET);
            } else {
                System.out.println(RED+" Failed to add labor."+RESET);
            }


        System.out.print("\nWould you like to add another labor? (y/n): ");
        }while (scanner.nextLine().equalsIgnoreCase("y"));

        return totalLaborCost;
    }
}
