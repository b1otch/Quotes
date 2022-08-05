package com.example.quotes;

public class Dataset {
    String author, content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Dataset(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
