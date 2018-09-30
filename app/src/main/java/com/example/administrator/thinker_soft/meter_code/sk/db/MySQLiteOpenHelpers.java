package com.example.administrator.thinker_soft.meter_code.sk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/5/18.
 */

public class MySQLiteOpenHelpers extends SQLiteOpenHelper {

    private static final String DB_NAME = "thinker_softs.db";
    private static final int VERSION = 1;

    public MySQLiteOpenHelpers(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelpers(Context context) {
       // super(context, getMyDatabaseName(context), null, VERSION);
        super(context, DB_NAME, null, VERSION);
    }

    public MySQLiteOpenHelpers(Context context,int i) {
        super(context, DB_NAME, null, VERSION);
    }

    private static String getMyDatabaseName(Context context){
        String databasename = DB_NAME;
        boolean isSdcardEnable =false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){//SDCard是否插入
            isSdcardEnable = true;
        }
        String dbPath = null;
        if(isSdcardEnable){
            dbPath =Environment.getExternalStorageDirectory().getPath() +"/ThinkerSoftData/";
        }else{//未插入SDCard，建在内存中
            dbPath =context.getFilesDir().getPath() + "/ThinkerSoftData/";
        }
        File dbp = new File(dbPath);
        if(!dbp.exists()){
            dbp.mkdirs();
        }
        databasename = dbPath +DB_NAME;
        return databasename;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
//        db.execSQL("create table person (_id integer primary key autoincrement, " +
//                "name char(10), phone char(20), money integer(20))");

        db.execSQL("CREATE TABLE MeterUsers" +
                "(id integer primary key AUTOINCREMENT,login_user_id varchar(200),meter_reader_id varchar(200),meter_reader_name varchar(200),meter_date varchar(200),user_amount varchar(200),meter_degrees varchar(200),meter_number varchar(200)," +
                "arrearage_months varchar(200),mix_state varchar(200),meter_order_number varchar(200),arrearage_amount varchar(200),area_id varchar(200),area_name varchar(200),user_name varchar(200),user_phone varchar(200)," +
                "last_month_dosage varchar(200),property_id varchar(200),property_name varchar(200),user_id varchar(200),book_id varchar(200),float_range varchar(200),meterState varchar(200),locationAddress varchar(200)," +
                "dosage_change varchar(200),user_address varchar(200),start_dosage varchar(200),old_user_id varchar(200),book_name varchar(200),meter_model varchar(200),n_state_id varchar(200),n_state_remark varchar(200),opened_remark varchar(200)," +
                "rubbish_cost varchar(200),remission varchar(200),this_month_dosage varchar(200),this_month_end_degree varchar(200),n_jw_x varchar(200),n_jw_y varchar(200),d_jw_time varchar(200),file_name varchar(200),uploadState varchar(200)," +
                "user_remark varchar(200),n_user_state varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}
