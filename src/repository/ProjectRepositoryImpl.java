package repository;

import database.DataBaseConnection;
import model.entities.Project;
import repository.interfaces.ProjectRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectRepositoryImpl implements ProjectRepository {

    Connection connection;
    public ProjectRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Project project) {
        String sql = "INSERT INTO Projects (project_name, profit_margin, total_cost, project_status, surface_area, client_id) VALUES (?, ?, ?, ?::project_status, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, project.getProjectName());
            statement.setDouble(2, project.getProfitMargin());
            statement.setDouble(3, project.getTotalCost());
            statement.setObject(4, project.getProjectStatus().name());
            statement.setDouble(5, project.getSurfaceArea());
            statement.setInt(6, project.getClient().getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
