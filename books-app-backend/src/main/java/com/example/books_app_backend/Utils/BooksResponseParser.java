package com.example.books_app_backend.Utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.books_app_backend.Models.Book;
import com.example.books_app_backend.Models.SearchResponse;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Component
public class BooksResponseParser {
    private static final Logger logger = LoggerFactory.getLogger(BooksResponseParser.class);

    // To limit the search results maybe...
    public SearchResponse parseBooksResponseData(String jsonPayload) {
        try(JsonReader jsonReader = Json.createReader(new StringReader(jsonPayload))) {
            JsonObject jsonObject = jsonReader.readObject();
            
            int totalItems = parseTotalItems(jsonObject);
            List<Book> books = parseBooks(jsonObject);

            return new SearchResponse(totalItems, books);
        } catch () {

        } catch () {

        }
    }

    private int parseTotalItems(JsonObject jsonObject) {
        return jsonObject.getInt("totalItems");
    }

    private List<Book> parseBooks(JsonObject jsonObject) {
        List<Book> books = new ArrayList<>();

        JsonArray booksArray = jsonObject.getJsonArray("items");
        for (JsonValue bookValue : booksArray) {
            JsonObject bookJson = bookValue.asJsonObject();

            String id = parseId(bookJson);
            String title = parseTitle(bookJson);
            List<String> authors = parseAuthors(bookJson);
            String publisher = parsePublisher(bookJson);
            String publishedDate = parsePublishedDate(bookJson);
            String description = parseDescription(bookJson);
            int pageCount = parsePageCount(bookJson);
            List<String> categories = parseCategories(bookJson);
            String language = parseLanguage(bookJson);
            String isbn = parseIsbn(bookJson);
            String thumbnailUrl = parseThumbnailUrl(bookJson);
            boolean isEbook = parseIsEbook(bookJson);
            String previewLink = parsePreviewLink(bookJson);
            String infoLink = parseInfoLink(bookJson);
            double averageRating = parseAverageRating(bookJson);
            int ratingsCount = parseRatingsCount(bookJson);
            int listPrice = parseListPrice(bookJson);
            int retailPrice = parseRetailPrice(bookJson);
            String currencyCode = parseCurrencyCode(bookJson);

            books.add(new Book(id, title, authors, publisher, publishedDate, description, pageCount, categories, language,isbn, thumbnailUrl, isEbook, previewLink, infoLink, averageRating, ratingsCount, listPrice, retailPrice, currencyCode));
        }

        return books;
    }

    private String parseCurrencyCode(JsonObject bookJson) {
        
    }

    private int parseRetailPrice(JsonObject bookJson) {
        
    }

    private int parseListPrice(JsonObject bookJson) {
        
    }

    private int parseRatingsCount(JsonObject bookJson) {
        
    }

    private double parseAverageRating(JsonObject bookJson) {
        
    }

    private String parseInfoLink(JsonObject bookJson) {
        
    }

    private String parsePreviewLink(JsonObject bookJson) {
        
    }

    private boolean parseIsEbook(JsonObject bookJson) {
        
    }

    private String parseThumbnailUrl(JsonObject bookJson) {
        
    }

    private String parseIsbn(JsonObject bookJson) {
        
    }

    private String parseLanguage(JsonObject bookJson) {
        
    }

    private List<String> parseCategories(JsonObject bookJson) {
        
    }

    private int parsePageCount(JsonObject bookJson) {
        
    }

    private String parseDescription(JsonObject bookJson) {
        
    }

    private String parsePublishedDate(JsonObject bookJson) {
        
    }

    private String parsePublisher(JsonObject bookJson) {
        
    }

    private List<String> parseAuthors(JsonObject bookJson) {
        
    }

    private String parseTitle(JsonObject bookJson) {
        
    }

    private String parseId(JsonObject bookJson) {
        
    }

}
