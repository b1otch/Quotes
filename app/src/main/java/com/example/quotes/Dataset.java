package com.example.quotes;
public class Dataset {

    String author;

    String content;

    String[] tags;

    String id;
    boolean isFav;

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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsFav() {
        return isFav;
    }

    public void setFav(boolean isFav) {
        this.isFav = isFav;
    }

    public Dataset(String author, String content, String[] tags, String id, boolean isFav) {
        this.author = author;
        this.content = content;
        this.tags = tags;
        this.id = id;
        this.isFav = isFav;
    }
}
