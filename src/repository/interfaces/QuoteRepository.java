package repository.interfaces;

import model.entities.Quote;

public interface QuoteRepository {

    Boolean save(Quote quote);
}
