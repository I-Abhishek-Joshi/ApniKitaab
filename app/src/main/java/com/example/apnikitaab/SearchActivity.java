package com.example.apnikitaab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final int LOADER_ID = 1;
    private BookAdapter mAdapter;
    private TextView EmptyTextView;
    private ArrayList<Book> MainBookList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.list_view_type);
        mAdapter = new BookAdapter(this, new ArrayList<>());
        listView.setAdapter(mAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Book curr = mAdapter.getItem(position);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(curr.getmUrl()));
//                startActivity(intent);
//            }
//        });

        EmptyTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(EmptyTextView);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        }else{
            EmptyTextView.setText("R.string.no_network");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search Book");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == null || newText.length() == 0){
                    mAdapter.clear();
                    mAdapter.addAll(MainBookList);
                } else{

                    ArrayList<Book> result = new ArrayList<>();

                    for(Book x: MainBookList){
                        String Author = x.getmAuthor();
                        String BookName = x.getmName();

                        if(BookName.contains(newText) || Author.contains(newText)){
                            result.add(x);
                        }
                    }
                    mAdapter.clear();
                    mAdapter.addAll(result);
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        String stringUrl = getIntent().getStringExtra("Search");
        return new Query(this, stringUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        mAdapter.clear();
        EmptyTextView.setText(R.string.no_books);

        if(data != null && !data.isEmpty()){
            MainBookList = data;
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        mAdapter.clear();

    }
}