package repository;

import model.entities.Material;
import repository.interfaces.MaterialRepository;

import java.sql.*;

public class MaterialRepositoryImpl implements MaterialRepository {

    Connection connection;

    public MaterialRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Material material) {
        String sqlComponent = "INSERT INTO components (name, component_type, tax_rate, project_id) VALUES (?, ?, ?, ?)";
        String sqlMaterial = "INSERT INTO materials (unit_cost, quantity, transport_cost, quality_coefficient, component_id) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtComponent = connection.prepareStatement(sqlComponent)){
                stmtComponent.setString(1, material.getName());
                stmtComponent.setString(2, material.getComponentType().toString());
                stmtComponent.setDouble(3, material.getTaxRate());
                stmtComponent.setInt(4, material.getProject().getId());

                stmtComponent.executeUpdate();
                ResultSet rs = stmtComponent.getGeneratedKeys();
                if (rs.next()) {
                    material.setId(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtMaterial = connection.prepareStatement(sqlMaterial)) {
                stmtMaterial.setDouble(1, material.getUnitCost());
                stmtMaterial.setDouble(2, material.getQuantity());
                stmtMaterial.setDouble(3, material.getTransportCost());
                stmtMaterial.setDouble(4, material.getQualityCoefficient());
                stmtMaterial.setInt(5, material.getId());  // Use the generated component ID

                stmtMaterial.executeUpdate();
            }

            connection.commit();
            System.out.println("Material and component saved successfully.");

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
