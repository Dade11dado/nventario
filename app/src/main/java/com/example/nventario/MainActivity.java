package com.example.nventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Prodotto product = new Prodotto("800900313","Banane",4);

    public ArrayList<Prodotto> productList = new ArrayList<>();
    RecyclerView recyclerView;

    Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productList.add(product);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new Adapter(this,productList);
        recyclerView.setAdapter(myAdapter);





    }
}