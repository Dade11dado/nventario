package com.example.nventario;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    public Context context;
    public ArrayList<Prodotto> list;

    public Adapter(Context context, ArrayList<Prodotto> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0) {
            ObjectAnimator colorFade = ObjectAnimator.ofObject(holder.layout, "backgroundColor", new ArgbEvaluator(), Color.argb(66,255,0,0), 0x66FFD8BE);
            colorFade.setDuration(1000);
            colorFade.start();
            holder.layout.setBackgroundColor(R.drawable.constrain_bg);
        }
        Prodotto product = list.get(position);
        holder.itemNumber.setText(""+product.getQuantità());
        holder.itemName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemEan, itemName, itemNumber;
        ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemNumber = itemView.findViewById(R.id.itemNumber);
            layout= itemView.findViewById(R.id.constraintLayout);
        }
    }
}
