package repository.interfaces;

import model.entities.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository {

    Boolean save(Quote quote);
    Optional<Quote> findByProjectId(int id);
    boolean delete(int id);
    boolean update(Quote quote);
    List<Quote> findAll();
    Quote findProjectWithDetails(int projectId);
}
