package com.example.stockapiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView stockRecyclerView;
    private Button addSymbolButton;
    private EditText symbolEditText;

    private DatabaseHelper db;

    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        //inicjalizcaja widoku
        stockRecyclerView = findViewById(R.id.stockRecyclerView);
        addSymbolButton = findViewById(R.id.addSymbolButton);
        symbolEditText = findViewById(R.id.symbolEditText);

        addSymbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = symbolEditText.getText().toString().trim();
                if(!symbol.isEmpty()){
                    long id = db.insertRecord(symbol);
                    loadStocks();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStocks(); //przeładowanie listy symboli
    }

    private void loadStocks(){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,
                db.getAllRecords());

        stockRecyclerView.setAdapter(customAdapter);
    }


    private void searchRecords(String query){
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,
                db.searchRecords(query));

        stockRecyclerView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecords(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecords(newText);
                return true;
            }
        });
        searchView.setQueryHint("Szukaj");
        return super.onCreateOptionsMenu(menu);
    }
}