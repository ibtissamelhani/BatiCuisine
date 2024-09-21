package repository;

import model.entities.Quote;
import repository.interfaces.QuoteRepository;

public class QuoteRepositoryImpl implements QuoteRepository {

    @Override
    public Quote save(Quote quote) {
        return quote;
    }
}
