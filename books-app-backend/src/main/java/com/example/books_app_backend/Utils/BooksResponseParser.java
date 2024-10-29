package com.example.books_app_backend.Utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.books_app_backend.Exceptions.BookDataParsingException;
import com.example.books_app_backend.Models.Book;
import com.example.books_app_backend.Models.SearchResponse;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Component
public class BooksResponseParser {
    private static final Logger logger = LoggerFactory.getLogger(BooksResponseParser.class);

    // To limit the search results maybe...
    // Keep in mind, they might not be returning all the available results
    // Need to somehow make this work in a sensible way

    public SearchResponse parseBooksResponseData(String jsonPayload) throws BookDataParsingException {
        try(JsonReader jsonReader = Json.createReader(new StringReader(jsonPayload))) {
            JsonObject jsonObject = jsonReader.readObject();
            
            int totalItems = parseTotalItems(jsonObject);
            List<Book> books = parseBooks(jsonObject);

            return new SearchResponse(totalItems, books);
        } catch (JsonException je) {
            // Handle JSON parsing errors
            logger.error("Error parsing JSON: " + je.getMessage(), je);
            throw new BookDataParsingException("Failed to parse JSON response", je);
        } catch (IllegalStateException ise) {
            // Handle unexpected JSON structure
            logger.error("Unexpected JSON structure: " + ise.getMessage(), ise);
            throw new BookDataParsingException("Unexpected structure in JSON response", ise);
        } catch (NullPointerException npe) {
            // Handle null values where they're not expected
            logger.error("Null value encountered: " + npe.getMessage(), npe);
            throw new BookDataParsingException("Null value encountered in JSON response", npe);
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            logger.error("Unexpected error during parsing: " + e.getMessage(), e);
            throw new BookDataParsingException("Unexpected error during parsing", e);
        }
    }

    private int parseTotalItems(JsonObject jsonObject) {
        try {
            return jsonObject.getInt("totalItems", 0);
        } catch (Exception e) {
            logger.warn("Could not parse totalItems, defaulting to 0.");
            return 0;
        }
    }

    private List<Book> parseBooks(JsonObject jsonObject) {
        List<Book> books = new ArrayList<>();

        try {
            JsonArray booksArray = jsonObject.getJsonArray("items");

            if (booksArray == null) {
                logger.warn("No items found in response.");
                return books; // Returns an empty list instead of null.
            }

            for (JsonValue bookValue : booksArray) {
                try {
                    JsonObject bookJson = bookValue.asJsonObject();
                    System.out.println(bookJson);

                    Book book = parseBookDetails(bookJson);
                    books.add(book);
                } catch (Exception e) {
                    logger.warn("Error parsing book: {}", e.getMessage());
                    continue; // Continue processing other books.
                }
            }
        } catch (Exception e) {
            logger.warn("Error parsing books array: {}", e.getMessage());
        }

        return books;
    }

    // Handles individual book parsing.
    private Book parseBookDetails(JsonObject bookJson) {
        boolean isForSale = isForSale(bookJson); 

        return new Book(
            parseId(bookJson),
            parseTitle(bookJson),
            parseAuthors(bookJson),
            parsePublisher(bookJson),
            parsePublishedDate(bookJson),
            parseDescription(bookJson),
            parsePageCount(bookJson),
            parseCategories(bookJson),
            parseLanguage(bookJson),
            parseIsbn(bookJson),
            parseThumbnailUrl(bookJson),
            parseIsEbook(bookJson),
            parsePreviewLink(bookJson),
            parseInfoLink(bookJson),
            parseAverageRating(bookJson),
            parseRatingsCount(bookJson),
            parseSaleability(bookJson),
            // Depends if it is saleable.
            isForSale ? parseListPrice(bookJson) : 0,
            isForSale ? parseRetailPrice(bookJson) : 0,
            isForSale ? parseCurrencyCode(bookJson) : ""
        );
    }

    // Some of the books are not for sale.
    private boolean isForSale(JsonObject bookJson) {
        try {
            JsonObject saleInfo = bookJson.getJsonObject("saleInfo");
            // Return true if for sale, false if not
            if (saleInfo != null) {
                return "FOR_SALE".equals(saleInfo.getString("saleability", "NOT_FOR_SALE"));
            }
            return false;
        } catch (Exception e) {
            logger.warn("Error checking saleability: {}", e.getMessage());
            return false;
        }
    }

    private String parseSaleability(JsonObject bookJson) {
        try {
            JsonObject saleInfo = bookJson.getJsonObject("saleInfo");
            if (saleInfo != null) {
                return saleInfo.getString("saleability", "UNKNOWN");
            }
        } catch (Exception e) {
            logger.warn("Error parsing saleability: {}", e.getMessage());
        }
        return "UNKNOWN";
    }

    private String parseCurrencyCode(JsonObject bookJson) {
        try {
            JsonObject saleInfo = bookJson.getJsonObject("saleInfo");
            if (saleInfo != null) {
                JsonObject retailPrice = saleInfo.getJsonObject("retailPrice");
                if (retailPrice != null) {
                    return retailPrice.getString("currencyCode", "USD");
                }
            }
        } catch (Exception e) {
            logger.warn("Error parsing currency code: {}", e.getMessage());
        }

        return "USD";
    }

    private int parseRetailPrice(JsonObject bookJson) {
        try {
            JsonObject saleInfo = bookJson.getJsonObject("saleInfo");
            if (saleInfo != null) {
                JsonObject retailPrice = saleInfo.getJsonObject("retailPrice");
                if (retailPrice != null) {
                    return retailPrice.getInt("amount", 0);
                }
            }
        } catch (Exception e) {
            logger.warn("Error parsing retail price: {}", e.getMessage());
        }

        return 0;
    }

    private int parseListPrice(JsonObject bookJson) {
        try {
            JsonObject saleInfo = bookJson.getJsonObject("saleInfo");
            if (saleInfo != null) {
                JsonObject listPrice = saleInfo.getJsonObject("listPrice");
                if (listPrice != null) {
                    return listPrice.getInt("amount", 0);
                }
            }
        } catch (Exception e) {
            logger.warn("Error parsing list price: {}", e.getMessage());
        }

        return 0;
    }

    private int parseRatingsCount(JsonObject bookJson) {
        try {
            return bookJson.getJsonObject("volumeInfo").getInt("ratingsCount", 0);
        } catch (Exception e) {
            logger.warn("Error parsing ratings count: {}", e.getMessage());
        }
        
        return 0;
    }

    private double parseAverageRating(JsonObject bookJson) {
        try {
            return bookJson.getJsonObject("volumeInfo").getJsonNumber("averageRating").doubleValue();
        } catch (Exception e) {
            logger.warn("Error parsing average rating: {}", e.getMessage());
        }
        
        return 0.0;
    }

    private String parseInfoLink(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("infoLink", "");
    }

    private String parsePreviewLink(JsonObject bookJson) {
        return bookJson.getJsonObject("volumeInfo").getString("previewLink", "");
    }

    private boolean parseIsEbook(JsonObject bookJson) {
        try {
            return bookJson.getJsonObject("saleInfo").getBoolean("isEbook", false);
        } catch (Exception e) {
            logger.warn("Error parsing isEbook: {}", e.getMessage());
        }
        
        return false;
    }

    private String parseThumbnailUrl(JsonObject bookJson) {
        try {
            JsonObject volumeInfo = bookJson.getJsonObject("volumeInfo");
            if (volumeInfo != null) {
                JsonObject imageLinks = volumeInfo.getJsonObject("imageLinks");
                if (imageLinks != null) {
                    return imageLinks.getString("thumbnail", "");
                }
            }
        } catch (Exception e) {
            logger.warn("Error parsing thumbnail URL: {}", e.getMessage());
        }

        return "";
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
        try {
            JsonObject volumeInfo = bookJson.getJsonObject("volumeInfo");
            if (volumeInfo != null) {
                return volumeInfo.getInt("pageCount", 0);
            }
        } catch (Exception e) {
            logger.warn("Error parsing page count: {}", e.getMessage());
        }

        return 0;
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