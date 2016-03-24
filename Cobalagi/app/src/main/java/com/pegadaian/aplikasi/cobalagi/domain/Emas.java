package com.pegadaian.aplikasi.cobalagi.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by teddy on 23/03/16.
 */
public class Emas {

    private int berat;
    private BigDecimal harga;
    private BigDecimal hargaJual;
    private BigDecimal hargaBeli;
    private Date tanggal;

    public BigDecimal getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(BigDecimal hargaJual) {
        this.hargaJual = hargaJual;
    }

    public BigDecimal getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(BigDecimal hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
}
