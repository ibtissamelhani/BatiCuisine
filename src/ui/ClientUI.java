package ui;

import model.entities.Client;
import service.ClientService;

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

        System.out.print("\nEnter client name: ");
        String name = scanner.nextLine();

        System.out.print("\nEnter client address: ");
        String address = scanner.nextLine();

        System.out.print("\nEnter client phone: ");
        String phone = scanner.nextLine();

        System.out.print("\nIs the client a professional? (yes/no): ");
        String isProfessionalInput = scanner.nextLine();
        boolean isProfessional = isProfessionalInput.equalsIgnoreCase("yes");


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
        System.out.print("\nEnter client name to update: ");
        String name = scanner.nextLine();
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
        System.out.print("\nEnter client name: ");
        String name = scanner.nextLine();

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
        System.out.println(RED+"\t\t\t\t List of All Clients "+RESET);
        List<Client> clients = clientService.getAllClients();
        if (clients != null) {
            System.out.println("\n+------+------------------+--------------------------+---------------------+------------------+");
            System.out.printf("| %-4s | %-16s | %-24s |%-20s | %-16s |\n","ID", "Name", "Address", "Phone", "Professional");
            System.out.println("+------+------------------+--------------------------+---------------------+------------------+");
            for (Client client : clients) {
                System.out.printf("| %-4s | %-16s | %-24s |%-20s | %-16s |\n", client.getId(), client.getName(), client.getAddress(), client.getPhone(), client.getProfessional()?"yes":"no");
                System.out.println("+------+------------------+--------------------------+---------------------+------------------+");

            }
        }

    }
}
