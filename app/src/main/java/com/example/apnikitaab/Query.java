package com.example.apnikitaab;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class Query extends AsyncTaskLoader<ArrayList<Book>> {

    String mUrl;
    public Query(Context context, String stringUrl){
        super(context);
        mUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Book> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        ArrayList<Book> books = getData(mUrl);
        return  books;
    }

    private static ArrayList<Book> getData(String mUrl) {
        URL url = createUrl(mUrl);
        String response = null;
        try {
            response = makeHttpConnection(url);
        } catch (Exception e) {
            Log.e("LOG_TAG", "Error getting data", e);
        }

        ArrayList<Book> books = getJson(response);
        return books;
    }

    private static ArrayList<Book> getJson(String response) {
        ArrayList<Book> books = new ArrayList<Book>();

        if(response == null || response.length() == 0)
            return null;

        try {
            JSONObject root = new JSONObject(response);
            JSONArray items = root.getJSONArray("items");

            for(int i=0; i<items.length(); i++){
                JSONObject book = items.getJSONObject(i);
                JSONObject details = book.getJSONObject("volumeInfo");
                String title = details.getString("title");
                JSONArray author = details.getJSONArray("authors");
                String Author = author.getString(0);
                String link = details.getString("infoLink");
                Author = "By- " + Author;
                JSONObject imageLinks = details.getJSONObject("imageLinks");
                String ImageUrl = imageLinks.getString("smallThumbnail");
                books.add(new Book(title, Author, link, ImageUrl));
            }
        } catch (JSONException e) {
            Log.e("Query", "Problem parsing the Book JSON results", e);
        }
        return books;
    }

    private static String makeHttpConnection(URL url) throws IOException {
        String resposne = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                resposne = readStream(inputStream);
            }

        } catch (Exception e) {
            Log.e("LOG_TAG", "Error connecting " + urlConnection.getResponseCode(), e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return resposne;
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String Line = reader.readLine();
        while (Line != null){
            output.append(Line);
            Line = reader.readLine();
        }
        return output.toString();
    }

    private static URL createUrl(String mUrl) {
        URL url;
        try{
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            Log.e("LOG_TAG", "Error creating data", e);
            return null;
        }
        return url;
    }
}
