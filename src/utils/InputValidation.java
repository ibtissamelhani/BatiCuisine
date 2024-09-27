package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputValidation {

    static Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        int value = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                value = scanner.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : La valeur doit être un nombre entier.");
                scanner.next();
            }
        }
        return value;
    }

    public static String readString(String prompt) {
        String value = " ";
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            value = scanner.nextLine();
            if (!value.trim().isEmpty()) {
                valid = true;
            } else {
                System.out.println("Error: Please enter a valid text.");
            }
        }
        return value;
    }



    public static Double readDouble(String prompt) {
        double value = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                value = scanner.nextDouble();
                scanner.nextLine();
                if(value > 0){
                    valid = true;
                }else {
                    System.out.println("Error: Please enter a valid number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: The value must be a number.");
                scanner.next();
            }
        }
        return value;
    }
}
