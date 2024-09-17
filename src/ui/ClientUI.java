package ui;

import model.entities.Client;
import service.ClientService;

import java.util.Scanner;

public class ClientUI {

    private ClientService clientService;
    Scanner scanner;

    public ClientUI(ClientService clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public void createClientUI() {

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

        Client newClient = clientService.creatClient(name, address, phone, isProfessional);

        if (newClient != null) {
            System.out.println("\n Client created successfully! \n");
            System.out.println("Name: " + newClient.getName());
            System.out.println("Address: " + newClient.getAddress());
            System.out.println("Phone: " + newClient.getPhone());
            System.out.println("Professional: " + (newClient.getProfessional() ? "Yes" : "No"));
        } else {
            System.out.println("Failed to create client. The name might already be in use.");
        }

    }
}
