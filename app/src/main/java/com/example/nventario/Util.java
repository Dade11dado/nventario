package com.example.nventario;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


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

        public void  updateItem(String ean, int quantity,String date){
        myRef.child(ean).child("quantità").setValue(quantity);
        myRef.child(ean).child("date").setValue(date);
        }

    public String getDate(){
        DateTimeFormatter dtf = null;
        LocalDateTime now;
        String date = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           dtf  = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            now = LocalDateTime.now();
            date = dtf.format(now);
        }

        return date;
    }

    public ArrayList<Prodotto> sorteList(ArrayList<Prodotto> prduct) {
        ArrayList<Prodotto> sort = new ArrayList<Prodotto>();

        Collections.sort(sort, new Comparator<Prodotto>() {
            @Override
            public int compare(Prodotto prodotto, Prodotto t1) {
                return prodotto.getDate().compareTo(t1.getDate());
            }
        });
        return sort;
    }

    public void createExcel(ArrayList<Prodotto> list) throws IOException {
        int rowP = 1;
        Cell cell = null;
        Sheet sheet = null;
        String EXCEL_SHEET_NAME = "Inventario";

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);

        sheet = workbook.createSheet(EXCEL_SHEET_NAME);

        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Ean");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Prodotto");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Quantità");
        cell.setCellStyle(cellStyle);

        for(Prodotto product : list){
            Row rowProduct = sheet.createRow(rowP);

            cell = rowProduct.createCell(0);
            cell.setCellValue(product.getEan());

            cell = rowProduct.createCell(1);
            cell.setCellValue(product.getName());

            cell = rowProduct.createCell(2);
            cell.setCellValue(product.getQuantità());

            rowP += 1;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS),"Inventario.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);

        workbook.write(outputStream);
    }


}
