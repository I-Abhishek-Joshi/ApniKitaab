package com.example.apnikitaab;

public class Book {
    private String mName;
    private String mAuthor;
    private String mUrl;
    private String mImageUrl;

    public Book(String Name, String Author, String Url, String ImageUrl){
        mName = Name;
        mAuthor = Author;
        mUrl = Url;
        mImageUrl = ImageUrl;
    }
    public String getmName(){
        return mName;
    }
    public String getmAuthor(){
        return mAuthor;
    }
    public String getmUrl(){
        return mUrl;
    }
    public String getmImageUrl(){
        return mImageUrl;
    }
}
