package repository;

import model.entities.Project;
import repository.interfaces.ClientRepository;
import repository.interfaces.ProjectRepository;

import java.sql.*;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private ClientRepository clientRepository;
    Connection connection;
    public ProjectRepositoryImpl(ClientRepository clientRepository, Connection connection) {
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
                project.setClient(clientRepository.findById(rs.getInt("client_id")).get());
                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public  Boolean updateTotalCost(Project project) {
        String query = "update projects set total_cost = ?, profit_margin = ? where id = ?";
        boolean isUpdated = false;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, project.getTotalCost());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setInt(3, project.getId());
            int rowsAffected = stmt.executeUpdate();
            isUpdated = rowsAffected > 0;
//            connection.commit();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isUpdated;
    }
}
