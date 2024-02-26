package com.example.datatest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SearchView searchView;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("customerDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS customer(meter_no VARCHAR, name VARCHAR);");


        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                listView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void search(String keyword) {
        adapter.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM customer WHERE meter_no LIKE '%" + keyword + "%' OR name LIKE '%" + keyword + "%'", null);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String meterNo = cursor.getString(0);
                String name = cursor.getString(1);
                adapter.add("" + meterNo + ",  " + name);
            }
        }
        cursor.close();
    }
}
