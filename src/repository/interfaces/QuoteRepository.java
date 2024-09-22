package repository.interfaces;

import model.entities.Quote;

import java.util.Optional;

public interface QuoteRepository {

    Boolean save(Quote quote);
    Optional<Quote> findByProjectId(int id);
    boolean delete(int id);
    boolean update(Quote quote);
}
