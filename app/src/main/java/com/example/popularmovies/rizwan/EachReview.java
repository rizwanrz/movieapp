package com.example.popularmovies.rizwan;

public class EachReview {

    private String id;
    private String content;
    private String author;
    private String url;

    public EachReview(String id, String author, String content, String url){
        this.id = id;
        this.author = author;
        this.content = content;
        this.url =  url;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
