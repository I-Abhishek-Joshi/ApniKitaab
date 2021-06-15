package com.example.apnikitaab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String QUERY = "https://www.googleapis.com/books/v1/volumes?q=";
    String userInput = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringUrl = Search();
                if(stringUrl == null || stringUrl.length() == 0){
                    Toast.makeText(MainActivity.this, "Enter Book Details", Toast.LENGTH_SHORT).show();
                }else {
                    stringUrl = QUERY + stringUrl.replaceAll(" ", "+");
                    Intent intent =new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Search", stringUrl);
                    startActivity(intent);
                }
            }
        });

    }
    private String Search(){
        EditText text = (EditText) findViewById(R.id.search_bar);
        String output = text.getText().toString();
        return output;
    }
}