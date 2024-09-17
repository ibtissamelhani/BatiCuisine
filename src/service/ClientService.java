package service;

import model.entities.Client;
import repository.ClientRepositoryImpl;
import repository.interfaces.ClientRepository;

import java.util.Optional;

public class ClientService {

    private ClientRepositoryImpl clientRepositoryImpl;

    public ClientService(ClientRepositoryImpl clientRepositoryImpl) {
        this.clientRepositoryImpl = clientRepositoryImpl;
    }

    public Client creatClient(String name, String address, String phone, boolean isProfessional) {

        Optional<Client> existingClient = getClientByName(name);
        if (existingClient.isPresent()) {
            System.out.println("Client already exists. Please use a different Name.");
            return null;
        }
        Client client = new Client(name, address, phone, isProfessional);
        clientRepositoryImpl.save(client);
        return client;
    }

    public Optional<Client> getClientByName(String name) {
        return clientRepositoryImpl.findByName(name);
    }
}
