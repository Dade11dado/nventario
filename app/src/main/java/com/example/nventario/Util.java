package com.example.nventario;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Util {

    MutableLiveData<List<Prodotto>> productMutableLiveData;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public Util(){
        this.productMutableLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance("https://inventario-1a2fb-default-rtdb.europe-west1.firebasedatabase.app");
        myRef = database.getReference("Inventario");
    }

    public int getIndex(String ean, ArrayList<Prodotto> list) {
        int i = 0;
        for (Prodotto product : list) {
            if(Objects.equals(product.getEan(), ean)){
                return i;
            }
            i++;
        }
        return -1;
    }

    public MutableLiveData<List<Prodotto>> getProductLiveData(){

        List<Prodotto> chatMessages = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Prodotto product = dataSnapshot.getValue(Prodotto.class);
                    chatMessages.add(product);
                }
                productMutableLiveData.postValue(chatMessages);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  productMutableLiveData;

    }

    public void addItem(Prodotto product){
            myRef.child(product.getEan()).setValue(product);
        }




}
