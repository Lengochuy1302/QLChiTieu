package com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.DatabaseSQLite.Database;

import java.util.ArrayList;

public class chucnangloaithuchi {
    Database dtb;
    SQLiteDatabase dtTC;

    public chucnangloaithuchi(Context context) {
        dtb = new Database(context);
        dtTC = dtb.getReadableDatabase();
    }


    public ArrayList<ThuChi> getTC(String sql, String... a) {
        ArrayList<ThuChi> list = new ArrayList<>();
        Cursor cs = dtTC.rawQuery(sql, a);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int maTc = Integer.parseInt(cs.getString(0));
            String tenTc = cs.getString(1);
            int loaiTc = Integer.parseInt(cs.getString(2));

//            ThuChi tc = new ThuChi(maTc, tenTc, loaiTc);
//            list.add(tc);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }

    //Thêm các khoản thu chi
    public boolean themTC(ThuChi tc) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenKhoan", tc.getTenKhoan());
        contentValues.put("loaiKhoan", tc.isLoaiKhoan());
        long r = db.insert("THUCHI", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa khoản thu chi theo mã, khi xóa khoản thu chi, các dữ liệu bên giao dịch thuộc khoản thu chi cũng phải xóa theo
    public boolean xoaTC(ThuChi tc) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        int r = db.delete("THUCHI", "maKhoan=?", new String[]{String.valueOf(tc.getMaKhoan())});
        int s = db.delete("GIAODICH", "maKhoan=?", new String[]{String.valueOf(tc.getMaKhoan())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Sửa khoản thu chi theo mã thu chi
    public boolean suaTC(ThuChi tc) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenKhoan", tc.getTenKhoan());
        contentValues.put("loaiKhoan", tc.isLoaiKhoan());
        int r = db.update("THUCHI", contentValues, "maKhoan=?", new String[]{String.valueOf(tc.getMaKhoan())});

        if (r <= 0) {
            return false;
        }
        return true;
    }

    //lấy toàn bộ ds thu chi
    public ArrayList<ThuChi> getAll() {
        String sql = "SELECT * FROM THUCHI";
        return getTC(sql);
    }

    //show danh sách theo thu hoăc chi 0 là thu 1 là chi
    public ArrayList<ThuChi> getThuChi(int loaiKhoan) {
        String sql = "SELECT * FROM THUCHI WHERE loaiKhoan=?";
        ArrayList<ThuChi> list = getTC(sql, String.valueOf(loaiKhoan));
        return list;
    }


    //Lấy tên theo mã khoản
    public String getTen(int maKhoan) {
        String tenKhoan = "";

        String sql = "SELECT * FROM THUCHI WHERE maKhoan=?";
        ArrayList<ThuChi> list = getTC(sql, String.valueOf(maKhoan));
        return list.get(0).getTenKhoan();
    }

    //Tim kiem ten loai thu chi

    public ArrayList<ThuChi> Timkiem(String tenKhoan , int loaiKhoan) {
        String ten = "%"+ tenKhoan +"%";
        String sql = "SELECT * FROM THUCHI WHERE tenKhoan LIKE ? and loaiKhoan = ?";
        ArrayList<ThuChi> list = getTC(sql, ten, String.valueOf(loaiKhoan));
        return list;
    }

    //Loc loai thu chi

    public ArrayList<ThuChi> Kiemtratontai(String tenKhoan , int loaiKhoan) {
        String ten = "%"+ tenKhoan +"%";
        String sql = "SELECT * FROM THUCHI WHERE tenKhoan LIKE ? and loaiKhoan = ?";
        ArrayList<ThuChi> list = getTC(sql, ten, String.valueOf(loaiKhoan));
        return list;
    }
}
