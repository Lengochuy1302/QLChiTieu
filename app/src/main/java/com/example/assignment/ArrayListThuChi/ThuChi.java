package com.example.assignment.ArrayListThuChi;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ThuChi implements Serializable {
    private String maKhoan;
    private String tenKhoan;
    //loaiKhoan = true sẽ là thu, false là chi
    private int loaiKhoan;

    public ThuChi() {
    }

    public ThuChi(String maKhoan, String tenKhoan, int loaiKhoan) {
        this.maKhoan = maKhoan;
        this.tenKhoan = tenKhoan;
        this.loaiKhoan = loaiKhoan;
    }

    public String getMaKhoan() {
        return maKhoan;
    }

    public void setMaKhoan(String maKhoan) {
        this.maKhoan = maKhoan;
    }

    public String getTenKhoan() {
        return tenKhoan;
    }

    public void setTenKhoan(String tenKhoan) {
        this.tenKhoan = tenKhoan;
    }

    public int isLoaiKhoan() {
        return loaiKhoan;
    }

    public void setLoaiKhoan(int loaiKhoan) {
        this.loaiKhoan = loaiKhoan;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> resuft = new HashMap<>();
        resuft.put("tenKhoan", tenKhoan);
        return resuft;
    }
}
