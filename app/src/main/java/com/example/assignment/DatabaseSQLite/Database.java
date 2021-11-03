package com.example.assignment.DatabaseSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "QLCTHUY", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE THUCHI(" +
                "maKhoan integer PRIMARY KEY AUTOINCREMENT," +
                "tenKhoan text," +
                "loaiKhoan integer)";
        db.execSQL(sql);
        //0 là thu, 1 là chi
//        sql = "INSERT INTO THUCHI VALUES(null,'Lương',0)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Lãi ngân hàng',0)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Xếp thưởng',0)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Tiền sinh hoạt',1)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Tiền ốm đau',1)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Tiền con học',1)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Mua sắm',1)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'May mắn',0)";
//        db.execSQL(sql);
//        sql = "INSERT INTO THUCHI VALUES(null,'Xui xẻo',1)";
//        db.execSQL(sql);
        //Tạo bảng giao dịch, cho maGd tự tăng lên
        sql = "CREATE TABLE GIAODICH(" +
                "maGd integer PRIMARY KEY AUTOINCREMENT," +
                "moTaGd text," +
                "ngayGd date," +
                "soTien integer, " +
                "ghiChu text," +
                "maKhoan integer REFERENCES thuchi(maKhoan))";
        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Lương tháng 1','27/04/2020',2000000,1)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Mua Quần áo','27/04/2020',200000,7)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Đóng học phí cho con','30/04/2020',2000000,6)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Thưởng doanh thu','01/05/2020',5000000,3)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Lãi tháng 5','01/05/2020',7000000,2)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Mua bàn ủi','01/05/2020',7000000,4)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Bị tai nạn','04/05/2020',2000000,5)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Mua tủ lạnh','10/05/2020',7000000,7)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Trúng vé số','10/05/2020',70000000,8)";
//        db.execSQL(sql);
//        sql = "INSERT INTO GIAODICH VALUES(null,'Bị cướp giật','11/05/2020',4000000,9)";
//        db.execSQL(sql);


//        sql = "CREATE TABLE taiKhoan(tenTaiKhoan text primary key, matKhau text)";
//        db.execSQL(sql);
//        sql = "INSERT INTO taiKhoan VALUES('admin','admin')";
//        db.execSQL(sql);
//        sql = "INSERT INTO taiKhoan VALUES('admin1','admin1')";
//        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS thuchi");
        db.execSQL("DROP TABLE IF EXISTS giaodich");
        onCreate(db);
    }
}
