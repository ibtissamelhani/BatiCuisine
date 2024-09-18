package repository.interfaces;

import model.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    Client save(Client client);
    Optional<Client> findById(int id);
    Optional<Client> findByName(String name);
    List<Client> findAll();
    Client update(Client client);
    Boolean delete(int id);
}
