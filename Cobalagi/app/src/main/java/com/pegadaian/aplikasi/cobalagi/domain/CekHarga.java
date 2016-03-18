package com.pegadaian.aplikasi.cobalagi.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by opaw on 3/18/16.
 */
public class CekHarga {
    private Date tanggal;
    private BigDecimal harga;


    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }
}
