package ui;

import database.DataBaseConnection;
import model.entities.Client;
import model.entities.Project;
import repository.*;
import service.*;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private final Connection connection =  DataBaseConnection.getInstance().getConnection();

    private final MaterialRepositoryImpl materialRepository = new MaterialRepositoryImpl(connection);
    private final ClientRepositoryImpl clientRepository = new ClientRepositoryImpl(connection);
    private final ProjectRepositoryImpl projectRepository = new ProjectRepositoryImpl(clientRepository,connection);
    private final LaborRepositoryImpl laborRepository = new LaborRepositoryImpl(connection);
    private final QuoteRepositoryImpl quoteRepository = new QuoteRepositoryImpl(connection, projectRepository);

    private final ClientService clientService = new ClientService(clientRepository);
    private final ProjectService projectService = new ProjectService(projectRepository);
    private final MaterialService materialService = new MaterialService(materialRepository);
    private final LaborService laborService = new LaborService(laborRepository);
    private final QuoteService quoteService = new QuoteService(quoteRepository);

    private final MaterialUI materialUI = new MaterialUI(materialService);
    private final LaborUI laborUI = new LaborUI(laborService);
    private final ClientUI clientUI = new ClientUI(clientService);
    private final QuoteUI quoteUI = new QuoteUI(quoteService, projectService);
    private final ProjectUI projectUI = new ProjectUI(projectService,materialUI,laborUI,clientService,quoteUI);

    private final Scanner scanner = new Scanner(System.in);
    private boolean quit = false;

    final String YELLOW = "\u001B[33m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";
    final String RED = "\033[0;31m";


    public void start(){

        while (true){
            quit = false;
            System.out.println("\n********************************************************************************");
            System.out.println("*                                  Principal Menu                              *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Client Management                                                        *");
            System.out.println("*  2. Project Management                                                       *");
            System.out.println("*  3. Quote Management                                                         *");
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
                    quoteMenu();
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
            System.out.println("\n********************************************************************************");
            System.out.println("*                                  Client Management                           *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Add new client                                                           *");
            System.out.println("*  2. Update a client                                                          *");
            System.out.println("*  3. Delete a client                                                          *");
            System.out.println("*  4. Show all clients                                                         *");
            System.out.println("*  5. Return to Main Menu                                                      *");
            System.out.println("*  6. Exit                                                                     *");
            System.out.println("********************************************************************************\n");

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    clientUI.createClientUI();
                    break;
                case "2":
                    clientUI.updateClientUI();
                    break;
                case "3":
                    clientUI.deleteClientUI();
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
            System.out.println("\n\n********************************************************************************");
            System.out.println("*                           PROJECT MANAGEMENT                                 *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Search for an existing client                                            *");
            System.out.println("*  2. Add a new client                                                         *");
            System.out.println("*  3. Show Project                                                             *");
            System.out.println("*  4. Return to Main Menu                                                      *");
            System.out.println("*  5. Exit                                                                     *");
            System.out.println("********************************************************************************\n"+RESET);

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();
            Client client = null;
            Project project = null;
            switch(choice){
                case "1":
                    client = clientUI.searchClientUI();
                    break;
                case "2":
                    client = clientUI.createClientUI();
                    break;
                case "3":
                    project = projectUI.findProjectWithDetailsUI();
                    break;
                case "4":
                    quit = true;
                    System.out.println("return to principal menu");
                    break;
                case "5":
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

    public void quoteMenu() {
        while(!quit) {
            System.out.println("\n********************************************************************************");
            System.out.println("*                                  Quote Management                            *");
            System.out.println("********************************************************************************");
            System.out.println("*  1. Update a Quote                                                           *");
            System.out.println("*  2. Delete a Quote                                                           *");
            System.out.println("*  3. Show all Quotes                                                          *");
            System.out.println("*  4. Return to Main Menu                                                      *");
            System.out.println("*  5. Exit                                                                     *");
            System.out.println("********************************************************************************\n");

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    quoteUI.updateQuoteUI();
                    break;
                case "2":
                    quoteUI.deleteQuoteUI();
                    break;
                case "3":
                    quoteUI.displayAllQuotes();
                    break;
                case "4":
                    quit = true;
                    System.out.println("return to principal menu");
                    break;
                case "5":
                    System.out.println("exit");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }

        }
    }

}
