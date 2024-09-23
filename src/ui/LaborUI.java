package ui;

import model.entities.Labor;
import model.entities.Project;
import model.enums.ComponentType;
import service.LaborService;
import utils.InputValidation;

import java.util.Scanner;

public class LaborUI {

    private LaborService laborService;

    final String PURPLE = "\033[0;35m";
    final String GREEN = "\u001B[92m";
    final String RESET = "\u001B[0m";
    final String RED = "\033[0;31m";



    public LaborUI(LaborService laborService) {
        this.laborService = laborService;
    }


    public Double addLaborUI(Project project) {

        double totalLaborCost = 0.0;
        String answer;

        System.out.println(PURPLE+"\n------------------------------------------     Add Labors    ---------------------------------------------\n"+RESET);

        do {
        String name = InputValidation.readString("\nEnter the type of labor (e.g., General Worker, Specialist): ");

        Double hourlyRate = InputValidation.readDouble("\nEnter the hourly rate for this labor (€ / h): ");

        Double workHours = InputValidation.readDouble("\nEnter the number of hours worked: ");

        Double workerProductivity = InputValidation.readDouble("\nEnter the productivity factor (1.0 = standard, > 1.0 = high productivity): ");

        Labor labor = new Labor(name,ComponentType.LABOR,0.20,hourlyRate,workHours,workerProductivity,project);


        Boolean success = laborService.addLabor(labor);
            if (success) {
                double laborCost = labor.calculateTotalCost();
                totalLaborCost += laborCost;
                System.out.println(GREEN + "\nLabor added successfully! Cost of this labor: " + laborCost + "€" + RESET);
            } else {
                System.out.println(RED+" Failed to add labor."+RESET);
            }

            answer = InputValidation.readString("\nWould you like to add another labor? (y/n): ");

        }while (answer.equalsIgnoreCase("y"));

        return totalLaborCost;
    }
}
