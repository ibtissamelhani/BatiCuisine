package service;

import model.entities.Quote;
import repository.QuoteRepositoryImpl;
import repository.interfaces.QuoteRepository;

import java.util.List;
import java.util.Optional;

public class QuoteService {

    private QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
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

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Quote findProjectWithDetails(int id) {
        return quoteRepository.findProjectWithDetails(id);
    }
}
