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
    // Keep in mind, they might not be returning all the available results
    // Need to somehow make this work in a sensible way


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
        return bookJson.getJsonObject("saleInfo").getJsonObject("retailPrice").getString("currencyCode", "");
    }

    private int parseRetailPrice(JsonObject bookJson) {
        return bookJson.getJsonObject("saleInfo").getJsonObject("retailPrice").getInt("amount");
    }

    private int parseListPrice(JsonObject bookJson) {
        return bookJson.getJsonObject("saleInfo").getJsonObject("listPrice").getInt("amount");
    }

    private int parseRatingsCount(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getInt("ratingsCount");
    }

    private double parseAverageRating(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getInt("averageRating");
    }

    private String parseInfoLink(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("infoLink", "");
    }

    private String parsePreviewLink(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("previewLink", "");
    }

    private boolean parseIsEbook(JsonObject bookJson) {
        return bookJson.getJsonObject("saleInfo").getBoolean("isEbook");
    }

    private String parseThumbnailUrl(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getJsonObject("imageLinks").getString("thumbnail", "");
    }

    private String parseIsbn(JsonObject bookJson) {
        JsonArray industryIdentifiersJsonArray = bookJson.getJsonObject("volumeInfo").getJsonArray("industryIdentifiers");

        if (industryIdentifiersJsonArray != null) {
            for (int i = 0; i < industryIdentifiersJsonArray.size(); i++) {
                // Store the current json object in a variable.
                JsonObject identifier = industryIdentifiersJsonArray.getJsonObject(i);
                if ("ISBN_13".equals(identifier.getString("type"))) {
                    return identifier.getString("identifier");
                }
            }
        }

        return null;
    }

    private String parseLanguage(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("language", "");
    }

    private List<String> parseCategories(JsonObject bookJson) {
        List<String> categories = new ArrayList<>();
        JsonArray categoriesJsonArray = bookJson.getJsonObject("volumeInfo").getJsonArray("categories");

        if (categoriesJsonArray != null) {
            for (int i = 0; i < categoriesJsonArray.size(); i++) {
                categories.add(categoriesJsonArray.getString(i));
            }
        }

        return categories;
    }

    private int parsePageCount(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getInt("pageCount");
    }

    private String parseDescription(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("description", "");
    }

    private String parsePublishedDate(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("publishedDate", "");
    }

    private String parsePublisher(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("publisher", "");
    }

    private List<String> parseAuthors(JsonObject bookJson) {
        List<String> authors = new ArrayList<>();
        JsonArray bookAuthorsJsonArray = bookJson.getJsonObject("volumeInfo").getJsonArray("authors");

        if (bookAuthorsJsonArray != null) {
            for (int i = 0; i < bookAuthorsJsonArray.size(); i++) {
                authors.add(bookAuthorsJsonArray.getString(i));
            }
        }

        return authors;
    }

    private String parseTitle(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("title", "");
    }

    private String parseId(JsonObject bookJson) {
        return bookJson.getString("id", "");
    }
}