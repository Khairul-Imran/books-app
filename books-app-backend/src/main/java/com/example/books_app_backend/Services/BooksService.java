package com.example.books_app_backend.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.books_app_backend.Config.GoogleBooksApiConfig;
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


    // For searches with single fields only (4)


    // Helper methods buildUrl and fetchNews

    private String buildUrl(String endpoint) {}

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
