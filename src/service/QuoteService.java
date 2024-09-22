package service;

import model.entities.Quote;
import repository.QuoteRepositoryImpl;

import java.util.Optional;

public class QuoteService {

    private QuoteRepositoryImpl quoteRepository;

    public QuoteService(QuoteRepositoryImpl quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Boolean createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Optional<Quote> getQuoteByProjectId(int id) {
        return quoteRepository.findByProjectId(id);
    }

    public boolean delete(int id) {
        return quoteRepository.delete(id);
    }

    public boolean update(Quote quote) {
        return quoteRepository.update(quote);
    }
}
