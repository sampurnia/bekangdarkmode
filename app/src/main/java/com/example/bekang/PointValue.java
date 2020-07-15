package com.example.bekang;

/*
HALAMAN UNTUK MENYIMPAN POINT VALUE DARI SI X DAN Y, DIGUNAKAN PADA PERCOBAAN GRAFIK PERTAMA
 HALAMAN MONITORING(calonfragment)*/
public class PointValue {
    long xValue;
    int yValue;

    public PointValue() {
    }

    public PointValue(long xValue, int yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public long getxValue() {
        return xValue;
    }

    public int getyValue() {
        return yValue;
    }

}