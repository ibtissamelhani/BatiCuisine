package repository.interfaces;

import model.entities.Quote;

public interface QuoteRepository {

    Quote save(Quote quote);
}
