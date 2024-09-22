package repository;

import model.entities.Project;
import model.entities.Quote;
import repository.interfaces.QuoteRepository;

import java.sql.*;
import java.util.Optional;

public class QuoteRepositoryImpl implements QuoteRepository {

    private Connection connection;
    private ProjectRepositoryImpl projectRepository;

    public QuoteRepositoryImpl(Connection connection, ProjectRepositoryImpl projectRepository) {
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


}
