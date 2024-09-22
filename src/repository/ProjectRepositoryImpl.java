package repository;

import model.entities.Client;
import model.entities.Project;
import model.enums.ProjectStatus;
import repository.interfaces.ClientRepository;
import repository.interfaces.ProjectRepository;

import java.sql.*;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private ClientRepositoryImpl clientRepository;
    Connection connection;
    public ProjectRepositoryImpl(ClientRepositoryImpl clientRepository, Connection connection) {
        this.connection = connection;
        this.clientRepository = clientRepository;
    }

    @Override
    public Project save(Project project) {
        String sql = "INSERT INTO Projects (project_name, profit_margin, total_cost, project_status, surface_area, client_id) VALUES (?, ?, ?, ?::project_status, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, project.getProjectName());
            statement.setDouble(2, project.getProfitMargin());
            statement.setDouble(3, project.getTotalCost());
            statement.setObject(4, project.getProjectStatus().name());
            statement.setDouble(5, project.getSurfaceArea());
            statement.setInt(6, project.getClient().getId());
            statement.executeUpdate() ;

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public Optional<Project> findById(int id) {

        Project project = new Project();

        String sql = "SELECT * FROM projects WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                project.setId(rs.getInt("id"));
                project.setProjectName(rs.getString("project_name"));
                project.setProfitMargin(rs.getDouble("profit_margin"));
                project.setTotalCost(rs.getDouble("total_cost"));
                project.setSurfaceArea(rs.getDouble("surface_area"));
                project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                project.setClient(clientRepository.findById(rs.getInt("client_id")).get());
                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Project> findByNameAndClient(String projectName, String clientName) {
        Optional<Client> client = clientRepository.findByName(clientName);
        if (!client.isPresent()) {
            return Optional.empty();
        }
        int client_id = client.get().getId();

        String query = "SELECT * FROM Projects WHERE project_name = ? AND client_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, projectName);
            ps.setInt(2, client_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setSurfaceArea(rs.getDouble("surface_area"));
                    project.setProjectStatus(ProjectStatus.valueOf(rs.getString("project_status")));
                    project.setClient(clientRepository.findById(rs.getInt("client_id")).get());
                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    @Override
    public  Boolean update(Project project) {
        String query = "UPDATE Projects SET project_name = ?, profit_margin = ?, total_cost = ?, project_status = ?::project_status, surface_area = ?, client_id = ? WHERE id = ?";
        boolean isUpdated = false;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, project.getProjectName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setObject(4, project.getProjectStatus().name());
            stmt.setDouble(5, project.getSurfaceArea());
            stmt.setInt(6, project.getClient().getId());
            stmt.setInt(7, project.getId());
            int rowsAffected = stmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }
}
