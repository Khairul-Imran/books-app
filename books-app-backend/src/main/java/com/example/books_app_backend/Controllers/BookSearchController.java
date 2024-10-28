package com.example.books_app_backend.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.books_app_backend.Exceptions.BookServiceException;
import com.example.books_app_backend.Models.SearchCriteria;
import com.example.books_app_backend.Models.SearchResponse;
import com.example.books_app_backend.Services.BooksService;

@RestController
@RequestMapping("/api/books")
public class BookSearchController {

    private final BooksService booksService;
    private static final Logger logger = LoggerFactory.getLogger(BookSearchController.class);

    @Autowired
    public BookSearchController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) String general,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn) {

                SearchCriteria searchCriteria = new SearchCriteria()
                    .setQ(general)
                    .setTitle(title)
                    .setAuthor(author)
                    .setPublisher(publisher)
                    .setIsbn(isbn);
                
                logger.info("Received request to search for books: {}", searchCriteria);

                try {
                    SearchResponse searchResponse = booksService.search(searchCriteria);
                    logger.info("Successfully retrieved books data based on criteria: {}", searchCriteria);
                    return ResponseEntity.ok(searchResponse);
                } catch (BookServiceException bse) {
                    logger.error("Error fetching books data: {}", searchCriteria);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching books data: " + bse.getMessage());
                }
    }
}
