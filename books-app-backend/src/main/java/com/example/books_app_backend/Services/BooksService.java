package com.example.books_app_backend.Services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.books_app_backend.Config.GoogleBooksApiConfig;
import com.example.books_app_backend.Models.Book;
import com.example.books_app_backend.Models.SearchCriteria;
import com.example.books_app_backend.Models.SearchResponse;
import com.example.books_app_backend.Utils.BooksResponseParser;

@Service
public class BooksService {
    
    private final RestTemplate restTemplate;
    private final GoogleBooksApiConfig config;
    private final BooksResponseParser booksResponseParser;
    private final Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    public BooksService(RestTemplate restTemplate, GoogleBooksApiConfig config, BooksResponseParser booksResponseParser) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.booksResponseParser = booksResponseParser;
    }

    // For general searches


    // For searches with multiple fields
    public List<Book> search(SearchCriteria searchCriteria) {

    }

    // For searches with single fields only / Convenience methods
    // TODO: To update these methods to use the helper methods
    public List<Book> searchByTitle(String title) {
        return search(new SearchCriteria().setTitle(title));
    }

    public List<Book> searchByAuthor(String author) {
        return search(new SearchCriteria().setAuthor(author));
    }    

    public List<Book> searchByPublisher(String publisher) {
        return search(new SearchCriteria().setPublisher(publisher));
    }

    public List<Book> searchByIsbn(String isbn) {
        return search(new SearchCriteria().setIsbn(isbn));
    }

    // Helper methods buildUrl and fetchBooks
    // To double check the documentation to ensure the params are supposed to be done this way....
    private String buildUrl(String endpoint, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder
                                        .fromHttpUrl(config.getApiUrl() + endpoint)
                                        .queryParam("apiKey", config.getApiKey());
        queryParams.forEach(builder::queryParam);
        return builder.toUriString();
    }

    private SearchResponse fetchBooks(String url) {
        try {
            RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return booksResponseParser.parseBooksResponseData(response.getBody());
            } else {
                logger.error("Error response from Google Books API. Status: {}", response.getStatusCode());
                // TODO: To throw an exception here

            }

        } catch (Exception e) {
            logger.error("Error fetching news from API, e");
            // TODO: To throw an exception here

        }
    }
}
