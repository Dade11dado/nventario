package com.example.nventario;

public class Prodotto {

    String ean, name;
    int quantità;

    public Prodotto(String ean, String name, int quantità) {
        this.ean = ean;
        this.name = name;
        this.quantità = quantità;
    }

    public Prodotto() {
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }
}
