package service;

import model.entities.Quote;
import repository.QuoteRepositoryImpl;

public class QuoteService {

    private QuoteRepositoryImpl quoteRepository;

    public QuoteService(QuoteRepositoryImpl quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Boolean createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }
}
