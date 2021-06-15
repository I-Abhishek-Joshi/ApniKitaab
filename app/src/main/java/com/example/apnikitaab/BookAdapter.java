package com.example.apnikitaab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public ArrayList<Book> mBooks = new ArrayList<>();
    public BookAdapter(Context context, ArrayList<Book> books){
        super(context,0 ,books);
        mBooks = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View myView = convertView;
        if(myView == null){
            myView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        Book curr = getItem(position);
        TextView book = (TextView)myView.findViewById(R.id.name);
        TextView author = (TextView)myView.findViewById(R.id.author);
        ImageView image = (ImageView)myView.findViewById(R.id.image_view);

        book.setText(curr.getmName());
        author.setText(curr.getmAuthor());

        try{
            URL url = new URL(curr.getmImageUrl());
            Glide.with(getContext()).load(url).into(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return myView;
    }
    public ArrayList<Book> getArrayList(){
        return mBooks;
    }
}
