package com.example.oppai.top10;

public class Article {
    private String name;
    private String author;
    private String title;
    private String url;
    private String imageUrl;
    private String publishDate;
    private String content;
    private String description;

    public Article() {
    }

    public Article(String name, String author, String title,
                   String url, String imageUrl,
                   String publishDate, String content, String description) {

        this.name = name;
        this.author = author;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.publishDate = publishDate;
        this.content = content;
        this.description = description;
    }

    //getters

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

}
