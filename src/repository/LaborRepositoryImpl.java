package repository;

import model.entities.Labor;
import repository.interfaces.LaborRepository;

import java.sql.*;

public class LaborRepositoryImpl implements LaborRepository {

    private Connection connection;

    public LaborRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Labor labor) {
        String sqlComponent = "INSERT INTO components (name, component_type, tax_rate, project_id) VALUES (?, ?::component_type, ?, ?) RETURNING id";
        String sqlLabor = "INSERT INTO labors (hourly_rate, work_hours, worker_productivity, component_id) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int componentId = 0;
            try (PreparedStatement stmtComponent = connection.prepareStatement(sqlComponent)) {
                stmtComponent.setString(1, labor.getName());
                stmtComponent.setObject(2, labor.getComponentType().name());
                stmtComponent.setDouble(3, labor.getTaxRate());
                stmtComponent.setInt(4, labor.getProject().getId());

                stmtComponent.executeUpdate();
                ResultSet rs = stmtComponent.getGeneratedKeys();
                if (rs.next()) {
                    componentId = rs.getInt(1);
                }
            }


            try (PreparedStatement stmtLabor = connection.prepareStatement(sqlLabor)) {
                stmtLabor.setDouble(1, labor.getHourlyRate());
                stmtLabor.setDouble(2, labor.getWorkHours());
                stmtLabor.setDouble(3, labor.getWorkerProductivity());
                stmtLabor.setInt(4, componentId);

                stmtLabor.executeUpdate();
            }

            connection.commit();
            System.out.println("Labor and component saved successfully.");

        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Transaction failed. Rolled back.");
            } catch (SQLException rollbackEx) {
                System.out.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.out.println(e.getMessage());
        }
    }

}
