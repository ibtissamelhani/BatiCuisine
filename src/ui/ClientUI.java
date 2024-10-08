package ui;

import model.entities.Client;
import service.ClientService;
import utils.InputValidation;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientUI {

    final String GREEN = "\u001B[32m";
    final String RED = "\u001B[31m";
    final String BLUE = "\u001B[34m";
    final String RESET = "\u001B[0m";

    private ClientService clientService;
    Scanner scanner;

    public ClientUI(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public Client createClientUI() {

        System.out.println(BLUE+"\n--------------------------------------- Create a New Client ---------------------------------------"+RESET);

        String name = InputValidation.readString("\nEnter client name: ");

        String address = InputValidation.readString("\nEnter client address: ");

        String phone = InputValidation.readString("\nEnter client phone number: ");

        String isProfessionalInput = InputValidation.readString("\nIs the client a professional? (y/n): ");
        boolean isProfessional = isProfessionalInput.equalsIgnoreCase("y");


        Client newClient = clientService.creatClient(name, address, phone, isProfessional);

        if (newClient != null) {
            System.out.println(GREEN+"\n         Client created successfully! \n"+RESET);
            System.out.println(GREEN+"- Name : "+RESET + newClient.getName());
            System.out.println(GREEN+"- Address : "+RESET + newClient.getAddress());
            System.out.println(GREEN+"- Phone : "+RESET + newClient.getPhone());
            System.out.println(GREEN+"- Professional : "+RESET + (newClient.getProfessional() ? "Yes" : "No"));
        } else {
            System.out.println(RED+" Failed to create client. The name might already be in use."+RESET);
        }
        return newClient;
    }

    public void updateClientUI() {
        System.out.println(BLUE+"\n------------------------------------------- Update Client -------------------------------------------"+RESET);
        String name = InputValidation.readString("\nEnter client name to update: ");
        Optional<Client> existingClient = clientService.getClientByName(name);

        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            System.out.println(GREEN+"Client found ! "+RESET);
            System.out.println(GREEN + "- Name: " + RESET + client.getName()+ GREEN +"- Address: "  + RESET+ client.getAddress()+ GREEN +"- Phone: " + RESET + client.getPhone()+GREEN + "- Professional: " + RESET + (client.getProfessional() ? "Yes" : "No")  );


            System.out.print("\nEnter new Name (or press Enter to keep the old one): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                client.setName(newName);
            }

            System.out.print("\nEnter new Address (or press Enter to keep the old one): ");
            String newAddress = scanner.nextLine();
            if (!newAddress.trim().isEmpty()) {
                client.setAddress(newAddress);
            }

            System.out.print("\nEnter new Phone (or press Enter to keep the old one): ");
            String newPhone = scanner.nextLine();
            if (!newPhone.trim().isEmpty()) {
                client.setPhone(newPhone);
            }

            System.out.print("\nIs the client professional? (yes/no or press Enter to keep the old value): ");
            String isProfessionalInput = scanner.nextLine();
            if (!isProfessionalInput.trim().isEmpty()) {
                boolean isProfessional = isProfessionalInput.equalsIgnoreCase("yes");
                client.setProfessional(isProfessional);

            }

            Client updatedClient = clientService.updateClient(client);
            System.out.println(GREEN+"\n          Client updated successfully! \n"+RESET);
            System.out.println(BLUE+"- Name: "+RESET + updatedClient.getName());
            System.out.println(BLUE+"- Address: "+RESET + updatedClient.getAddress());
            System.out.println(BLUE+"- Phone: "+RESET + updatedClient.getPhone());
            System.out.println(BLUE+"- Professional: "+RESET + (updatedClient.getProfessional() ? "Yes" : "No"));

        } else {
            System.out.println(RED + " Client not found!" + RESET);
        }
    }

    public void deleteClientUI() {
        System.out.println(BLUE+"\n------------------------------------------- Delete Client -------------------------------------------"+RESET);
        System.out.print("\n Enter client name to delete: ");
        String name = scanner.nextLine();
        Optional<Client> existingClient = clientService.getClientByName(name);
        if (existingClient.isPresent()) {
            boolean isDeleted = clientService.deleteClient(existingClient.get().getId());
            if (isDeleted) {
                System.out.println(RED+ "\n  Client deleted successfully! \n"+RESET);
            }else{
                System.out.println(RED+"\n problem while deleting client" + RESET);
            }
        }else {
            System.out.println(RED + " Client not found!" + RESET);
        }
    }

    public Client searchClientUI() {
        String name = InputValidation.readString("\nEnter client name: ");

        Optional<Client> client = clientService.getClientByName(name);
        if (client.isPresent()) {
            System.out.println(GREEN+"\n        Client found !\n"+RESET);
            System.out.println(GREEN+"- Name: "+RESET + client.get().getName());
            System.out.println(GREEN+"- Address: "+RESET + client.get().getAddress());
            System.out.println(GREEN+"- Phone: "+RESET + client.get().getPhone());
            System.out.println(GREEN+"- Professional: "+RESET + (client.get().getProfessional() ? "Yes" : "No"));
            return client.get();
        }else {
            System.out.println(RED+" Failed to get client with name " + name+RESET);
            return null;
        }
    }

    public void showAllClients() {
        System.out.println(RED + "\t\t\t\t List of All Clients " + RESET);
        List<Client> clients = clientService.getAllClients();

        if (clients != null && !clients.isEmpty()) {
            clients.stream().forEach(client -> {
                System.out.println("\n+------+------------------+--------------------------+---------------------+------------------+");
                System.out.printf("| %-4s | %-16s | %-24s | %-20s | %-16s |\n", client.getId(), client.getName(), client.getAddress(), client.getPhone(), client.getProfessional() ? "yes" : "no");
                System.out.println("+------+------------------+--------------------------+---------------------+------------------+");

                // Display project info using Streams
                if (client.getProjects() != null && !client.getProjects().isEmpty()) {
                    System.out.println("\t\t\t Projects for " + client.getName() + ":");
                    System.out.println("\t\t\t+--------+----------------------+---------------------+-------------------+");
                    System.out.printf("\t\t\t| %-6s | %-20s | %-19s | %-17s |\n", "ID", "Project Name", "Total Cost (€)", "Profit Margin (%)");
                    System.out.println("\t\t\t+--------+----------------------+---------------------+-------------------+");

                    client.getProjects().stream().forEach(project -> {
                        System.out.printf("\t\t\t| %-6d | %-20s | %-19.2f | %-17.2f |\n", project.getId(), project.getProjectName(), project.getTotalCost(), project.getProfitMargin());
                        System.out.println("\t\t\t+--------+----------------------+---------------------+-------------------+");
                    });
                } else {
                    // If no projects for the client
                    System.out.println("\t\t\tNo projects for " + client.getName());
                }
            });
        } else {
            System.out.println("No clients found.");
        }
    }

}
