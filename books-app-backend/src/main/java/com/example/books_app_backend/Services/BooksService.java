package com.example.books_app_backend.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.books_app_backend.Config.GoogleBooksApiConfig;
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

    


    // Helper methods buildUrl and fetchNews


}
