package repository;

import model.entities.Quote;
import repository.interfaces.QuoteRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuoteRepositoryImpl implements QuoteRepository {

    private Connection connection;

    public QuoteRepositoryImpl(Connection connection) {
        this.connection = connection;
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
}
