package test.repository;

import model.entities.Client;
import org.junit.jupiter.api.Test;
import repository.ClientRepositoryImpl;
import repository.interfaces.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientRepositoryImplTest {

    Connection mockConnection = mock(Connection.class);
    PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    @Test
    void save() {
    }

    @Test
    void findById() throws SQLException {
        when(mockConnection.prepareStatement("SELECT * FROM clients WHERE id = ?"))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        when(mockResultSet.next());
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("James");
        when(mockResultSet.getString("address")).thenReturn("123 Main St");
        when(mockResultSet.getString("phone")).thenReturn("123456789");

        ClientRepository clientRepository = new ClientRepositoryImpl(mockConnection);
        Optional<Client> client = clientRepository.findById(1);

        //Then
        assertTrue(client.isPresent());
        Client presentClient = client.get();

        assertEquals(1, presentClient.getId());
        assertEquals("James", presentClient.getName());
        assertEquals("123 Main St", presentClient.getAddress());
        assertEquals("123456789", presentClient.getPhone());

        verify(mockPreparedStatement.executeQuery());
        verify(clientRepository.findById(1));
    }

    @Test
    void findByName() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
    }
}