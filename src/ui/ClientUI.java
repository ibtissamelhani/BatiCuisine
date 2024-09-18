package ui;

import model.entities.Client;
import service.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientUI {

    final String GREEN = "\u001B[32m";
    final String RED = "\u001B[31m";
    final String RESET = "\u001B[0m";

    private ClientService clientService;
    Scanner scanner;

    public ClientUI(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public Client createClientUI() {

        System.out.println("Create a New Client");

        System.out.print("Enter client name: ");
        String name = scanner.nextLine();

        System.out.print("Enter client address: ");
        String address = scanner.nextLine();

        System.out.print("Enter client phone: ");
        String phone = scanner.nextLine();

        System.out.print("Is the client a professional? (yes/no): ");
        String isProfessionalInput = scanner.nextLine();
        boolean isProfessional = isProfessionalInput.equalsIgnoreCase("yes");

        double discountPercentage = 0.0;
        if (isProfessional) {
            System.out.println("enter discount percentage: ");
            discountPercentage = scanner.nextDouble();
        }

        Client newClient = clientService.creatClient(name, address, phone, isProfessional, discountPercentage);

        if (newClient != null) {
            System.out.println("\n Client created successfully! \n");
            System.out.println(GREEN+"- Name: "+RESET + newClient.getName());
            System.out.println(GREEN+"- Address: "+RESET + newClient.getAddress());
            System.out.println(GREEN+"- Phone: "+RESET + newClient.getPhone());
            System.out.println(GREEN+"- Professional: "+RESET + (newClient.getProfessional() ? "Yes" : "No"));
            System.out.println( (newClient.getProfessional() ? GREEN+"- discount Percentage: "+RESET + newClient.getDiscountPercentage() +" %" : ""));
            return newClient;
        } else {
            System.out.println("Failed to create client. The name might already be in use.");
        }
        return null;
    }

    public Client searchClientUI() {
        System.out.print("Enter client name: ");
        String name = scanner.nextLine();

        Optional<Client> client = clientService.getClientByName(name);
        if (client.isPresent()) {
            System.out.println("\n Client trouvé !\n");
            System.out.println(GREEN+"- Name: "+RESET + client.get().getName());
            System.out.println(GREEN+"- Address: "+RESET + client.get().getAddress());
            System.out.println(GREEN+"- Phone: "+RESET + client.get().getPhone());
            return client.get();
        }else {
            System.out.println("Failed to get client with name " + name);
            return null;
        }
    }

    public void showAllClients() {
        System.out.println(RED+"List of All Clients "+RESET);
        List<Client> clients = clientService.getAllClients();
        if (clients != null) {
            System.out.println("\n+--------------------+--------------------+--------------------+-------------------+-------------------+-------------------+");
            System.out.printf("| %-18s | %-18s | %-18s |%-18s | %-18s | %-18s |\n","ID", "Name", "Address", "Phone", "IsProfessional", "DiscountPercentage");
            System.out.println("+--------------------+--------------------+--------------------+-------------------+-------------------+-------------------+");
            for (Client client : clients) {
                System.out.printf("| %-18s | %-18s | %-18s |%-18s | %-18s |%-18s |%-18s |\n", client.getId(), client.getName(), client.getAddress(), client.getPhone(), client.getProfessional()?"yes":"no", client.getProfessional()? client.getDiscountPercentage(): "-");
                System.out.println("+--------------------+--------------------+--------------------+-------------------+");

            }
        }

    }
}
