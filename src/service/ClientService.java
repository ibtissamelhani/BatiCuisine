package service;

import model.entities.Client;
import repository.ClientRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class ClientService {

    private ClientRepositoryImpl clientRepositoryImpl;

    public ClientService(ClientRepositoryImpl clientRepositoryImpl) {
        this.clientRepositoryImpl = clientRepositoryImpl;
    }

    public Client creatClient(String name, String address, String phone, boolean isProfessional, Double discountPercentage) {

        Optional<Client> existingClient = getClientByName(name);
        if (existingClient.isPresent()) {
            System.out.println("Client already exists. Please use a different Name.");
            return null;
        }
        Client client = new Client(name, address, phone, isProfessional,discountPercentage);
        clientRepositoryImpl.save(client);
        return client;
    }

    public Optional<Client> getClientByName(String name) {
        return clientRepositoryImpl.findByName(name);
    }

    public List<Client> getAllClients() {
        return clientRepositoryImpl.findAll();
    }

    public void deleteClient(int id) {
        clientRepositoryImpl.delete(id);
    }

    public Client updateClient(Client client) {
        return clientRepositoryImpl.update(client);
    }
}
