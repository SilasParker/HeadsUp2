package com.silas.headsup;

//Class representing a Deck's representation within the CustomFragment's Deck list
public class ResultDeck {

    private String id,name,author;
    private int downloads,size, year, day;

    //Constructor for a ResultDeck instance
    //id: Icon Id of the Deck
    //name: Name of the Deck
    //author: Author' Name of the Deck
    //downloads: Number of times this ResultDeck has been downloaded
    //size: Size of the Deck this represents
    //year: Year that this Deck was uploaded
    //day: Day of the year that this Deck was uploaded
    public ResultDeck(String id, String name, String author, int downloads, int size, int year, int day) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.downloads = downloads;
        this.size = size;
        this.year = year;
        this.day = day;
    }

    //Getter for this ResultDeck's icon id
    public String getId() {
        return this.id;
    }

    //Getter for this ResultDeck's name
    public String getName() {
        return this.name;
    }

    //Getter for this ResultDeck's author's name
    public String getAuthor() {
        return this.author;
    }

    //Getter for this ResultDeck's number of downloads
    public int getDownloads() {
        return this.downloads;
    }

    //Getter for this ResultDeck's size
    public int getSize() {
        return  this.size;
    }

    //Getter for this ResultDeck's upload year
    public int getYear() { return this.year; }

    //Getter for this ResultDeck's upload day
    public int getDay() { return this.day; }

}
