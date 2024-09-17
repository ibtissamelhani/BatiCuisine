package ui;

import repository.ClientRepositoryImpl;
import service.ClientService;

import java.util.Scanner;

public class Menu {

    private ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
    private ClientService clientService = new ClientService(clientRepository);
    private ClientUI clientUI = new ClientUI(clientService);

    private final Scanner scanner = new Scanner(System.in);
    private boolean quit = false;

    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";

    public void start(){

        while (true){
            quit = false;
            System.out.println("\n********************************************************************************");
            System.out.println("*                                  Principal Menu                              *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Create a new project                                                     *");
            System.out.println("*  2. Display existing projects                                                *");
            System.out.println("*  3. Calculate the cost of a project                                          *");
            System.out.println("*  4. Exit                                                                     *");
            System.out.println("********************************************************************************\n");

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();
            switch(choice){
                case "1":
                    projectMenu();
                    break;
                case "2":

                    break;
                case "3":

                    break;
                case "4":
                    System.out.println("exit ...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public void projectMenu(){
        while(!quit){
            System.out.println(BLUE+"\n\n*     Would you like to search for an existing client or add a new one?        *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Search for an existing client                                            *");
            System.out.println("*  2. Add a new client                                                         *");
            System.out.println("*  3. Return to Main Menu                                                      *");
            System.out.println("*  4. Exit                                                                     *");
            System.out.println("********************************************************************************\n"+RESET);

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();

            switch(choice){
                case "2":
                    clientUI.createClientUI();
                    break;
                case "3":
                    quit = true;
                    System.out.println("return to principal menu");
                    break;
                case "4":
                    System.out.println("exit");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }

        }
    }



}
