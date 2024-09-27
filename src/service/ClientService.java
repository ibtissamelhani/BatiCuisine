package service;

import model.entities.Client;
import repository.interfaces.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {

    private ClientRepository clientRepositoryImpl;

    public ClientService(ClientRepository clientRepositoryImpl) {
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

    public Optional<Client> getClientById(int id) {
        return clientRepositoryImpl.findById(id);
    }

    public List<Client> getAllClients() {
        return clientRepositoryImpl.findAll();
    }

    public Boolean deleteClient(int id) {
        return clientRepositoryImpl.delete(id);
    }

    public Client updateClient(Client client) {
        return clientRepositoryImpl.update(client);
    }
}
