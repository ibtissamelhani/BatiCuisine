package ui;

import database.DataBaseConnection;
import model.entities.Client;
import repository.ClientRepositoryImpl;
import repository.ProjectRepositoryImpl;
import service.ClientService;
import service.ProjectService;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private Connection connection =  DataBaseConnection.getInstance().getConnection();
    private ClientRepositoryImpl clientRepository = new ClientRepositoryImpl(connection);
    private ClientService clientService = new ClientService(clientRepository);
    private ProjectRepositoryImpl projectRepository = new ProjectRepositoryImpl(connection);
    private ProjectService projectService = new ProjectService(projectRepository);
    private ClientUI clientUI = new ClientUI(clientService);
    private ProjectUI projectUI = new ProjectUI(projectService);

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
            System.out.println("*  1. Client Management                                                        *");
            System.out.println("*  2. Project Management                                                       *");
            System.out.println("*  3. Calculate the cost of a project                                          *");
            System.out.println("*  4. Exit                                                                     *");
            System.out.println("********************************************************************************\n");

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();
            switch(choice){
                case "1":
                    clientMenu();
                    break;
                case "2":
                    projectMenu();
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

    public void clientMenu() {
        while(!quit) {
            System.out.println(YELLOW+"\n********************************************************************************");
            System.out.println("*                                  Client Management                           *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Add new client                                                           *");
            System.out.println("*  2. Update a client                                                          *");
            System.out.println("*  3. Delete a client                                                          *");
            System.out.println("*  4. Show all clients                                                         *");
            System.out.println("*  5. Return to Main Menu                                                      *");
            System.out.println("*  6. Exit                                                                     *");
            System.out.println("********************************************************************************\n" + RESET);

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    clientUI.createClientUI();
                    break;
                case "4":
                    clientUI.showAllClients();
                    break;
                case "5":
                    quit = true;
                    System.out.println("return to principal menu");
                    break;
                case "6":
                    System.out.println("exit");
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
            Client client = null;

            switch(choice){
                case "1":
                    client = clientUI.searchClientUI();
                    break;
                case "2":
                    client = clientUI.createClientUI();
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
            if (client != null) {
                projectUI.createProjectForClient(client);
            } else {
                System.out.println("Client non trouvé ou création échouée.");
            }

        }
    }



}
