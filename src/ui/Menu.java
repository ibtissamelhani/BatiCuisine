package ui;

import database.DataBaseConnection;
import model.entities.Client;
import model.entities.Project;
import repository.*;
import repository.interfaces.*;
import service.*;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private final Connection connection =  DataBaseConnection.getInstance().getConnection();

    private final MaterialRepository materialRepository = new MaterialRepositoryImpl(connection);
    private final ClientRepository clientRepository = new ClientRepositoryImpl(connection);
    private final ProjectRepository projectRepository = new ProjectRepositoryImpl(clientRepository,connection);
    private final LaborRepository laborRepository = new LaborRepositoryImpl(connection);
    private final QuoteRepository quoteRepository = new QuoteRepositoryImpl(connection, projectRepository);

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
    String space = "\t\t\t\t\t";


    public void start(){

        while (true){
            quit = false;
            System.out.println("\n"+space+"********************************************************************************");
            System.out.println(space+"*                                  Principal Menu                              *");
            System.out.println(space+"********************************************************************************");
            System.out.println(space+"*  1. Client Management                                                        *");
            System.out.println(space+"*  2. Project Management                                                       *");
            System.out.println(space+"*  3. Quote Management                                                         *");
            System.out.println(space+"*  4. Exit                                                                     *");
            System.out.println(space+"********************************************************************************\n");

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
            System.out.println(BLUE+"\n"+space+"********************************************************************************");
            System.out.println(space+"*                                  Client Management                           *");
            System.out.println(space+"********************************************************************************");
            System.out.println(space+"*  1. Add new client                                                           *");
            System.out.println(space+"*  2. Update a client                                                          *");
            System.out.println(space+"*  3. Delete a client                                                          *");
            System.out.println(space+"*  4. Show all clients                                                         *");
            System.out.println(space+"*  5. Return to Main Menu                                                      *");
            System.out.println(space+"*  6. Exit                                                                     *");
            System.out.println(space+"********************************************************************************\n"+RESET);

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
            System.out.println("\n\n"+space+"********************************************************************************");
            System.out.println(space+"*                           PROJECT MANAGEMENT                                 *");
            System.out.println(space+"********************************************************************************");
            System.out.println(space+"*  1. Search for an existing client                                            *");
            System.out.println(space+"*  2. Add a new client                                                         *");
            System.out.println(space+"*  3. Show Project                                                             *");
            System.out.println(space+"*  4. Return to Main Menu                                                      *");
            System.out.println(space+"*  5. Exit                                                                     *");
            System.out.println(space+"********************************************************************************\n"+RESET);

            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();
            Client client = null;
            switch(choice){
                case "1":
                    client = clientUI.searchClientUI();
                    if (client != null) {
                        projectUI.createProjectForClient(client);
                    } else {
                        System.out.println("Client not found");
                    }
                    break;
                case "2":
                    client = clientUI.createClientUI();
                    if (client != null) {
                        projectUI.createProjectForClient(client);
                    } else {
                        System.out.println("Client not found");
                    }
                    break;
                case "3":
                    quoteUI.findProjectWithDetailsUI();
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

    public void quoteMenu() {
        while(!quit) {
            System.out.println(RED+"\n"+space+"********************************************************************************");
            System.out.println(space+"*                                  Quote Management                            *");
            System.out.println(space+"********************************************************************************");
            System.out.println(space+"*  1. Update a Quote                                                           *");
            System.out.println(space+"*  2. Delete a Quote                                                           *");
            System.out.println(space+"*  3. Show all Quotes                                                          *");
            System.out.println(space+"*  4. Return to Main Menu                                                      *");
            System.out.println(space+"*  5. Exit                                                                     *");
            System.out.println(space+"********************************************************************************\n"+RESET);

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
