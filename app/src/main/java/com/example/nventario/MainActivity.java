package com.example.nventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initializing views
    EditText et;
    RecyclerView recyclerView;
    Button button, excelButton;
    ProgressBar progressBar;

    //Initialiing adapter and recycler
    public ArrayList<Prodotto> productList = new ArrayList<>();
    Adapter myAdapter;

    //Initialize Util
    Util util;

    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect variables with views
        recyclerView = findViewById(R.id.recyclerView);
        et = findViewById(R.id.Ean);
        button = findViewById(R.id.Button);
        excelButton = findViewById(R.id.ExcelButton);
        progressBar = findViewById(R.id.progressBar);

        //creating recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //create util object
        util = new Util();

        excelButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            try {
                util.createExcel(productList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "FIle creato correttamente, controlla nella cartella Download del dispositivo", Toast.LENGTH_SHORT).show();
        });

        util.getProductLiveData().observe(this, new Observer<List<Prodotto>>() {
            @Override
            public void onChanged(List<Prodotto> prodottos) {
                productList.clear();
                productList.addAll(prodottos);
                myAdapter = new Adapter(MainActivity.this, productList);
                recyclerView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }
        });

        button.setOnClickListener(view -> {
            workEan(et.getText().toString());
        });

        et.setOnKeyListener((view, i, keyEvent) -> {
            if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    workEan(et.getText().toString());
                    et.setText("");
                    return true;
            }
            return false;
        });






    }

   
    public void workEan(String ean) {

            util.getProductLiveData().observe(MainActivity.this, new Observer<List<Prodotto>>() {
                @Override
                public void onChanged(List<Prodotto> prodottos) {
                    productList.clear();
                    productList.addAll(prodottos);
                    myAdapter.notifyDataSetChanged();
                }
            });

            //getting index of the eans, if ean not in list then return -1
            int index = util.getIndex(ean, productList);

            //check if index in list, if not then ask the user for the name of the product
            if (index < 0) {
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Nome prodotto");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Prodotto product = new Prodotto(ean,m_Text,util.getDate(),1);
                        productList.add(product);
                        myAdapter.notifyDataSetChanged();
                        util.addItem(product);
                        recyclerView.scrollToPosition(index);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }else{
                //If product already in list simply add 1 to total quantity
                int quantity = productList.get(index).getQuantità();
                productList.get(index).setQuantità(quantity+1);
                util.updateItem(ean,quantity+1,util.getDate());
                myAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }


        }


}

