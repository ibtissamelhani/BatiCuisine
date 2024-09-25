package repository;

import model.entities.*;
import model.enums.ComponentType;
import model.enums.ProjectStatus;
import repository.interfaces.ProjectRepository;
import repository.interfaces.QuoteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepositoryImpl implements QuoteRepository {

    private Connection connection;
    private ProjectRepository projectRepository;

    public QuoteRepositoryImpl(Connection connection, ProjectRepository projectRepository) {
        this.connection = connection;
        this.projectRepository = projectRepository;
    }

    @Override
    public Boolean save(Quote quote) {
            String query = "INSERT INTO quotes (estimated_amount, validity_date, issue_date, is_accepted, project_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setDouble(1, quote.getEstimatedAmount());
                stmt.setDate(2, Date.valueOf(quote.getValidityDate()));
                stmt.setDate(3, Date.valueOf(quote.getIssueDate()));
                stmt.setBoolean(4, quote.getAccepted());
                stmt.setInt(5, quote.getProject().getId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Error while saving quote: " + e.getMessage());
                return false;
            }
    }

    @Override
    public Optional<Quote> findByProjectId(int projectId) {
        String query = "SELECT id, estimated_amount, validity_date, issue_date, is_accepted, project_id FROM quotes WHERE project_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Build the Quote object from the result set
                Quote quote = new Quote();
                quote.setId(rs.getInt("id"));
                quote.setEstimatedAmount(rs.getDouble("estimated_amount"));
                quote.setValidityDate(rs.getDate("validity_date").toLocalDate());
                quote.setIssueDate(rs.getDate("issue_date").toLocalDate());
                quote.setAccepted(rs.getBoolean("is_accepted"));

                // Assuming you have a method to get a Project by its ID
                Optional<Project> project = projectRepository.findById(rs.getInt("project_id"));
                quote.setProject(project.get());

                return Optional.of(quote);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quote by projectId: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM quotes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error while deleting quote: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Quote quote) {
        String query = "UPDATE quotes SET validity_date = ?, issue_date = ?, is_accepted = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(quote.getValidityDate()));
            stmt.setDate(2, Date.valueOf(quote.getIssueDate()));
            stmt.setBoolean(3, quote.getAccepted());
            stmt.setInt(4, quote.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error while updating quote: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Quote> findAll() {
        String query = "SELECT q.id, q.estimated_amount, q.issue_date, q.validity_date ,q.is_accepted, q.project_id, " +
                "p.project_name, p.profit_margin,p.surface_area ,p.total_cost, p.project_status," +
                "c.id AS client_id, c.name, c.address, c.phone, c.is_professional " +
                "FROM quotes q " +
                "JOIN projects p ON q.project_id = p.id " +
                "JOIN clients c ON p.client_id = c.id";

        List<Quote> devisList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"),
                        resultSet.getBoolean("is_professional")
                );

                Project project = new Project(
                        resultSet.getString("project_name"),
                        resultSet.getDouble("profit_margin"),
                        resultSet.getDouble("total_cost"),
                        ProjectStatus.valueOf(resultSet.getString("project_status").toUpperCase()),
                        resultSet.getDouble("surface_area"),
                        client
                );

                Quote quote = new Quote(
                        resultSet.getDouble("estimated_amount"),
                        resultSet.getDate("issue_date").toLocalDate(),
                        resultSet.getDate("validity_date").toLocalDate(),
                        resultSet.getBoolean("is_accepted"),
                        project
                );

                devisList.add(quote);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return devisList;
    }

    @Override
    public Quote findProjectWithDetails(int projectId){

        Project project = null;
        Quote quote = null;

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

                    quote = new Quote(
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
                    String componentType = componentRs.getString("component_type");

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

        return quote;
    }


}
