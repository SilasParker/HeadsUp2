package com.silas.headsup;

public class ResultDeck {
    private String id,name,author;
    private int downloads,size;

    public ResultDeck(String id, String name, String author, int downloads, int size) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.downloads = downloads;
        this.size = size;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getDownloads() {
        return this.downloads;
    }

    public int getSize() {
        return  this.size;
    }
}
