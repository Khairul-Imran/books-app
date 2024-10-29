package com.example.books_app_backend.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    private String id;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private List<String> categories;
    private String language;
    private String isbn;
    private String thumbnailUrl;
    private boolean isEbook;
    private String previewLink;
    private String infoLink;
    private double averageRating;
    private int ratingsCount;
    private String saleability; // New
    private int listPrice;
    private int retailPrice;
    private String currencyCode;

}
