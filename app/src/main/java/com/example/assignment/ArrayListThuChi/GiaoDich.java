package com.example.assignment.ArrayListThuChi;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GiaoDich implements Serializable {
    private String maGd;
    private String moTaGd;
    private String ngayGd;
    private int soTien;
    private String maKhoan;
    private String ghiChuGd;

    public GiaoDich() {
    }


    public String getMoTaGd() {
        return moTaGd;
    }


    public GiaoDich(String maGd, String moTaGd, String ngayGd, int soTien, String maKhoan, String ghiChuGd) {
        this.maGd = maGd;
        this.moTaGd = moTaGd;
        this.ngayGd = ngayGd;
        this.soTien = soTien;
        this.maKhoan = maKhoan;
        this.ghiChuGd = ghiChuGd;
    }

    public String getMaGd() {
        return maGd;
    }

    public void setMaGd(String maGd) {
        this.maGd = maGd;
    }

    public void setMoTaGd(String moTaGd) {
        this.moTaGd = moTaGd;
    }

    public String getNgayGd() {
        return ngayGd;
    }

    public void setNgayGd(String ngayGd) {
        this.ngayGd = ngayGd;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public String getMaKhoan() {
        return maKhoan;
    }

    public void setMaKhoan(String maKhoan) {
        this.maKhoan = maKhoan;
    }

    public String getGhiChuGd() {
        return ghiChuGd;
    }

    public void setGhiChuGd(String ghiChuGd) {
        this.ghiChuGd = ghiChuGd;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> resuft = new HashMap<>();
        resuft.put("moTaGd", moTaGd);
        resuft.put("ngayGd", ngayGd);
        resuft.put("soTien", soTien);
        resuft.put("maKhoan", maKhoan);
        resuft.put("ghiChuGd", ghiChuGd);
        return resuft;
    }

}


