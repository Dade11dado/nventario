package com.example.nventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initializing views
    EditText et;
    RecyclerView recyclerView;
    Button button;

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

        //creating recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //create util object
        util = new Util();

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
                    myAdapter = new Adapter(MainActivity.this, productList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            });

            //getting index of the eans, if ean not in list then return -1
            int index = util.getIndex(ean, productList);

            //check if index in list, if not then ask the user for the name of the product
            if (index < 0) {
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
                        Prodotto product = new Prodotto(ean,m_Text,1);
                        productList.add(product);
                        myAdapter.notifyDataSetChanged();
                        util.addItem(product);
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
                myAdapter.notifyDataSetChanged();
            }


        }


}

