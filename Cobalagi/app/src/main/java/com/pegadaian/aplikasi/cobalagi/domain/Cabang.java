package com.pegadaian.aplikasi.cobalagi.domain;

/**
 * Created by ari on 23/03/16.
 */
public class Cabang {
    private Integer kodeBaru;
    private Integer kodeLama;
    private String namaPegadain;
    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String provinsi;
    private String telepon;
    private String status;

    public Integer getKodeBaru() {
        return kodeBaru;
    }

    public void setKodeBaru(Integer kodeBaru) {
        this.kodeBaru = kodeBaru;
    }

    public Integer getKodeLama() {
        return kodeLama;
    }

    public void setKodeLama(Integer kodeLama) {
        this.kodeLama = kodeLama;
    }

    public String getNamaPegadain() {

        return namaPegadain;
    }

    public void setNamaPegadain(String namaPegadain) {

        this.namaPegadain = namaPegadain;
    }

    public String getKelurahan() {

        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {

        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {

        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {

        this.kecamatan = kecamatan;
    }

    public String getKota() {

        return kota;
    }

    public void setKota(String kota) {

        this.kota = kota;
    }

    public String getProvinsi() {

        return provinsi;
    }

    public void setProvinsi(String provinsi) {

        this.provinsi = provinsi;
    }

    public String getTelepon() {

        return telepon;
    }

    public void setTelepon(String telepon) {

        this.telepon = telepon;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }
}
