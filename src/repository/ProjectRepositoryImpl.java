package repository;

import model.entities.*;
import model.enums.ComponentType;
import model.enums.ProjectStatus;
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

    @Override
    public Project findProjectWithDetails(int projectId){

        Project project = null;

        String projectQuery = "SELECT p.id, p.project_name, p.profit_margin, p.total_cost, p.surface_area, " +
                "p.project_status, c.id AS clientId, c.name AS clientName, c.address, c.phone, c.is_professional, " +
                "q.id AS quoteId, q.estimated_amount, q.issue_date, q.validity_date, q.is_accepted " +
                "FROM projects p " +
                "JOIN clients c ON p.client_id = c.id " +
                "JOIN quotes q ON p.id = q.project_id " +
                "WHERE p.id = ?";

        String componentsQuery = "SELECT co.id, co.name, co.component_type, co.tax_rate, " +
                "m.id AS materialId, m.unit_cost, m.quantity, m.transport_cost, m.quality_coefficient, " +
                "l.id AS laborId, l.hourly_rate, l.work_hours, l.worker_productivity " +
                "FROM components co " +
                "LEFT JOIN materials m ON co.id = m.component_id " +
                "LEFT JOIN labors l ON co.id = l.component_id " +
                "WHERE co.project_id = ?";

        try (PreparedStatement projectStmt = connection.prepareStatement(projectQuery);
             PreparedStatement componentStmt = connection.prepareStatement(componentsQuery)) {

            // Récupérer les informations du projet, client et devis

            projectStmt.setInt(1, projectId);
            try (ResultSet projectRs = projectStmt.executeQuery()) {
                if (projectRs.next()) {
                    Client client = new Client(
                            projectRs.getString("clientName"),
                            projectRs.getString("address"),
                            projectRs.getString("phone"),
                            projectRs.getBoolean("is_professional")
                    );

                    project = new Project(
                            projectRs.getString("project_name"),
                            projectRs.getDouble("profit_margin"),
                            projectRs.getDouble("total_cost"),
                            ProjectStatus.valueOf(projectRs.getString("project_status")),
                            projectRs.getDouble("surface_area"),
                            client
                            );

                    Quote quote = new Quote(
                            projectRs.getDouble("estimated_amount"),
                            projectRs.getDate("issue_date").toLocalDate(),
                            projectRs.getDate("validity_date").toLocalDate(),
                            projectRs.getBoolean("is_accepted"),
                            project
                    );


                }
            }

            // Récupérer les composants (matériaux et main-d'œuvre)
            componentStmt.setInt(1, projectId);
            try (ResultSet componentRs = componentStmt.executeQuery()) {
                while (componentRs.next()) {
                    String componentType = componentRs.getString("componentType");

                    if (componentType.equals(ComponentType.MATERIAL.name())) {
                        Material material = new Material(
                                componentRs.getString("name"),
                                ComponentType.valueOf(componentRs.getString("component_type")),
                                componentRs.getDouble("tax_rate"),
                                project,
                                componentRs.getDouble("unit_cost"),
                                componentRs.getDouble("quantity"),
                                componentRs.getDouble("transport_cost"),
                                componentRs.getDouble("quality_coefficient")
                        );
                        project.getComponentList().add(material);
                    } else if (componentType.equals(ComponentType.LABOR.name())) {
                        Labor labor = new Labor(
                                componentRs.getString("name"),
                                ComponentType.valueOf(componentRs.getString("component_type")),
                                componentRs.getDouble("tax_rate"),
                                componentRs.getDouble("hourly_rate"),
                                componentRs.getDouble("work_hours"),
                                componentRs.getDouble("worker_productivity"),
                                project
                        );
                        project.getComponentList().add(labor);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }
}
