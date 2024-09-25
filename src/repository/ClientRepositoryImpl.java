package repository;

import model.entities.Client;
import model.entities.Project;
import model.enums.ProjectStatus;
import repository.interfaces.ClientRepository;

import java.sql.*;
import java.util.*;

public class ClientRepositoryImpl implements ClientRepository {

    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO clients (name, address, phone, is_professional) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhone());
            stmt.setBoolean(4, client.getProfessional());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public Optional<Client> findById(int id) {

        Client client = new Client();

        String sql = "SELECT * FROM clients WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setAddress(rs.getString("address"));
                client.setPhone(rs.getString("phone"));
                client.setProfessional(rs.getBoolean("is_professional"));
                return Optional.of(client);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByName(String name) {
        String sql = "SELECT * FROM clients WHERE name = ?";
        Client client = new Client();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setAddress(rs.getString("address"));
                client.setPhone(rs.getString("phone"));
                client.setProfessional(rs.getBoolean("is_professional"));
                return Optional.of(client);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, address = ?, phone = ?, is_professional = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getPhone());
            stmt.setBoolean(4, client.getProfessional());
            stmt.setInt(6, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public Boolean delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        boolean isDeleted = false;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            // If rowsAffected > 0, then a row was deleted
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isDeleted;
    }

    @Override
    public List<Client> findAll() {
        Map<Integer,Client> clientMap = new HashMap<>();

        String sql =  "SELECT c.id AS client_id, c.name AS client_name, c.address, c.phone, c.is_professional, " +
                "p.id AS project_id, p.project_name, p.total_cost, p.profit_margin, p.project_status " +
                "FROM clients c " +
                "LEFT JOIN projects p ON c.id = p.client_id " +
                "ORDER BY c.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("client_id");
                Client client = clientMap.get(id);
                if (client == null) {
                    client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setName(rs.getString("client_name"));
                    client.setAddress(rs.getString("address"));
                    client.setPhone(rs.getString("phone"));
                    client.setProfessional(rs.getBoolean("is_professional"));
                    clientMap.put(id, client);
                }
                int project_id = rs.getInt("project_id");
                if (project_id != 0) {
                    Project project = new Project();
                    project.setClient(client);
                    project.setProjectName(rs.getString("project_name"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                    project.setId(project_id);
                    client.getProjects().add(project);
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>(clientMap.values());
    }
}
