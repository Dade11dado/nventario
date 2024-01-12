package com.example.nventario;

import java.util.ArrayList;

public class Util {

    public static boolean isYet(String ean, ArrayList<Prodotto> list){
        for (Prodotto product : list) {
            if (product.ean == ean){
                return true;
            }
        }
        return false;
    }
}
