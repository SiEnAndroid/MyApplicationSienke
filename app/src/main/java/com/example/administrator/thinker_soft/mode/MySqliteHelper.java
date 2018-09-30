package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * 对于抽象类SQLiteOpenHelper的继承，需要重写：1）constructor，2）onCreate()和onUpgrade()
 * Created by Administrator on 2017/3/20 0020.
 */
public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "thinkersoft.db";//数据库名称
    private static MySqliteHelper instance;
    /**
     * 移动安检模块表
     */
    //安检用户表
    final String CREATE_TABLE_SQL_USER = "CREATE TABLE User" +
            "(user_id integer primary key AUTOINCREMENT,securityNumber varchar(200),userName varchar(200),meterNumber varchar(200),userPhone varchar(200)," +
            "securityType varchar(200),oldUserId varchar(200),newUserId varchar(200),userAddress varchar(200),taskId varchar(200),ifChecked varchar(200)," +
            "security_case varchar(200),security_case_id varchar(200),remarks varchar(200),security_hidden varchar(200),security_hidden_id varchar(200),security_hidden_reason varchar(200)," +
            "security_hidden_reason_id varchar(200),photoNumber varchar(200),ifUpload varchar(200),currentTime varchar(200),ifPass varchar(200),loginName varchar(200),loginUserId varchar(200),security_state varchar(200)," +
            "userProperty varchar(200),lastCheckTime varchar(200),sum_dosage varchar(200))";

    //安检任务表
    final String CREATE_TABLE_SQL_TASK = "CREATE TABLE Task" +
            "(task_id integer primary key AUTOINCREMENT,taskName varchar(200),taskId varchar(200),securityType varchar(200),totalCount varchar(200)," +
            "endTime varchar(200),loginName varchar(200),loginUserId varchar(200),restCount varchar(200))";
    //安检图片表
    final String CREATE_TABLE_SQL_SECURITY_PHOTO_INFO = "CREATE TABLE security_photo" +
            "(id integer primary key AUTOINCREMENT,photoPath varchar(200),newUserId varchar(200),loginName varchar(200),loginUserId varchar(200))";

    //安检状态表（安检类型）
    final String CREATE_TABLE_SQL_SECURITY_STATE = "CREATE TABLE SecurityState" +
            "(id integer primary key AUTOINCREMENT,securityId varchar(200),securityName varchar(200))";

    //安全情况表
    final String CREATE_TABLE_SQL_SECURITY_CONTENT = "CREATE TABLE security_content" +
            "(id integer primary key AUTOINCREMENT,securityId varchar(200),securityName varchar(200))";

    //安全隐患类型表
    final String CREATE_TABLE_SQL_SECURITY_HIDDEEN = "CREATE TABLE security_hidden" +
            "(id integer primary key AUTOINCREMENT,n_safety_hidden_id varchar(200),n_safety_hidden_name varchar(200))";

    //安全隐患原因表
    final String CREATE_TABLE_SQL_SECURITY_HIDDEEN_REASON = "CREATE TABLE security_hidden_reason" +
            "(id integer primary key AUTOINCREMENT,n_safety_hidden_reason_id varchar(200),n_safety_hidden_id varchar(200),n_safety_hidden_reason_name varchar(200))";

    //安全信息与照片关联表
    final String CREATE_TABLE_SQL_SECURITY_INFO_PHOTO = "CREATE TABLE security_info_photo" +
            "(id integer primary key AUTOINCREMENT,name varchar(200),chengji varchar(200),loginName varchar(200))";

    /**arrearage_amount
     * 移动抄表模块表
     *
     */
    //抄表用户表
    final String CREATE_TABLE_SQL_METER_USER = "CREATE TABLE MeterUser" +
            "(id integer primary key AUTOINCREMENT,login_user_id varchar(200),meter_reader_id varchar(200),meter_reader_name varchar(200),meter_date varchar(200),user_amount varchar(200),meter_degrees varchar(200),meter_number varchar(200)," +
            "arrearage_months varchar(200),mix_state varchar(200),meter_order_number integer,arrearage_amount varchar(200),area_id varchar(200),area_name varchar(200),user_name varchar(200),user_phone varchar(200)," +
            "last_month_dosage varchar(200),property_id varchar(200),property_name varchar(200),user_id varchar(200),book_id varchar(200),float_range varchar(200),meterState varchar(200),locationAddress varchar(200)," +
            "dosage_change varchar(200),user_address varchar(200),start_dosage varchar(200),old_user_id varchar(200),book_name varchar(200),meter_model varchar(200),n_state_id varchar(200),n_state_remark varchar(200),opened_remark varchar(200)," +
            "rubbish_cost varchar(200),remission varchar(200),this_month_dosage varchar(200),this_month_end_degree varchar(200),n_jw_x varchar(200),n_jw_y varchar(200),d_jw_time varchar(200),file_name varchar(200),uploadState varchar(200)," +
            "user_remark varchar(200),n_user_state varchar(200),mt_number integer)";
    //抄表文件表
    final String CREATE_TABLE_SQL_METER_FILE = "CREATE TABLE MeterFile" +
            "(id integer primary key AUTOINCREMENT,fileName varchar(200),login_user_id varchar(200),login_user_name varchar(200))";
    //文件和抄表本表
    final String CREATE_TABLE_SQL_METER_BOOK = "CREATE TABLE MeterBook" +
            "(id integer primary key AUTOINCREMENT,bookName varchar(200),bookId varchar(200),fileName varchar(200),login_user_id varchar(200),login_user_name varchar(200))";

    //抄表序号
    final String CREATE_TABLE_SQL_METER_NUMERICAL = "CREATE TABLE MeterNumerical" +
            "(id integer primary key AUTOINCREMENT,login_user_id varchar(200),book_name varchar(200),file_name varchar(200),book_id varchar(200),numerical_id varchar(200))";


    //    String sql = "CREATE TABLE IF NOT EXISTS " + "gd_account" +
//            "(" + "_id" + " INTEGER PRIMARY KEY NOT NULL," +
//            "username" + " VARCHAR(50)," +
//            "password" + " VARCHAR(50)," +
//            "remember" + " BYTE," +
//            "automate" + " BYTE" +
//            ")";
    //移动抄表图片表
    final String CREATE_TABLE_SQL_METER_PHOTO = "CREATE TABLE MeterPhoto" +
//            "(" + "id" + "INTEGER PRIMARY KEY AUTOINCREMENT,"+
//            "photoPath" + "VARCHAR(200)," +
//            "newUserId" + "VARCHAR(200)," +
//            "loginName" + "VARCHAR(200)," +
//            "loginUserId" +"VARCHAR(200))";
            "(id integer primary key AUTOINCREMENT,photoPath varchar(200),newUserId varchar(200),loginName varchar(200),loginUserId varchar(200),fileName varchar(200))";


    /**
     * OA模块表
     */
    //OA用户基础信息表
    final String CREATE_TABLE_SQL_OA_USER = "CREATE TABLE OaUser" +
            "(id integer primary key AUTOINCREMENT,userName varchar(200),userId varchar(200),userPhone varchar(200),late varchar(200),early varchar(200),noCheckMor varchar(200),isShake varchar(200),isRing varchar(200),isNewMessage varchar(200),noCheckEve varchar(200),outWork varchar(200))";
    //OA用户外勤信息表
    final String CREATE_TABLE_SQL_OA_USER_OUT_WORK = "CREATE TABLE OaUserOutWork" +
            "(id integer primary key AUTOINCREMENT,userId varchar(200),checkTime varchar(200),checkAddress varchar(200),contactType varchar(200),customerName varchar(200),photo varchar(200),customerPhoneNumber varchar(200))";
    //OA图片表
    final String CREATE_TABLE_SQL_OA_PHOTO_INFO = "CREATE TABLE oaPhoto" +
            "(id integer primary key AUTOINCREMENT,photoPath varchar(200),userId varchar(200))";
    //OA日程安排表
    final String CREATE_TABLE_SQL_OA_CALENDAR = "CREATE TABLE OaCalendar" +
            "(id integer primary key AUTOINCREMENT,userId varchar(200),userName varchar(200),title varchar(200),isAllDay varchar(200),beginTime varchar(200),endTime varchar(200),participant varchar(200),address varchar(200),details varchar(200))";

    //OA工作汇报表
    final String CREATE_TABLE_SQL_OA_REPORT = "CREATE TABLE oaReport" +
            "(id integer primary key AUTOINCREMENT,userPhoto varchar(200),userName varchar(200),userId varchar(200),time varchar(200),createReport varchar(200),summarizeReport varchar(200),nextReport varchar(200))";

    //OA日程安排表
    final String CREATE_TABLE_SQL_OA_ANNOUNCE = "CREATE TABLE OaAnnounce" +
            "(id integer primary key AUTOINCREMENT,userId varchar(200),userName varchar(200),type varchar(200),time varchar(200),content varchar(200))";

    //OA邮件表
    final String CREATE_TABLE_SQL_OA_EMAIL = "CREATE TABLE OaEmail" +
            "(id integer primary key AUTOINCREMENT,userId varchar(200),sendName varchar(200),recipients varchar(200),copyTo varchar(200),type varchar(200),content varchar(200),inboxTitle varchar(200),inboxAddress varchar(200),time varchar(200))";

    //OA邮件表
    final String CREATE_TABLE_SQL_FLOW = "CREATE TABLE Flow" +
            "(id integer primary key AUTOINCREMENT,userId varchar(200),userName varchar(200),type varchar(200),section varchar(200),date varchar(200),content varchar(200),startDate varchar(200),endDate varchar(200))";

    //构造器
    public MySqliteHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    public MySqliteHelper(Context context,//上下文
                          String name,//数据库的名字
                          SQLiteDatabase.CursorFactory factory,//游标对象
                          int version) {//版本号
        super(context, name, factory, version);
    }

    public MySqliteHelper(Context context,//上下文
                          String name,//数据库的名字
                          SQLiteDatabase.CursorFactory factory,//游标对象
                          int version,//版本号
                          DatabaseErrorHandler errorHandler) {//异常handler
        super(context, name, factory, version, errorHandler);
    }
    /**
     * 单实列
     * @param context
     * @return
     */
    public synchronized static MySqliteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySqliteHelper(context, MyApplication.DATA_BASE_VERSION);
        }
        return instance;
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 移动安检
         */
        db.execSQL(CREATE_TABLE_SQL_USER);                           //安检用户表
        db.execSQL(CREATE_TABLE_SQL_TASK);                           //安检任务表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_PHOTO_INFO);            //用户安检图片关联表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_STATE);                 //安全状态表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_CONTENT);               //安全情况表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_HIDDEEN);               //安全隐患表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_HIDDEEN_REASON);        //安全隐患原因表
        db.execSQL(CREATE_TABLE_SQL_SECURITY_INFO_PHOTO);            //安全信息与照片关联表
        /**
         * 移动抄表
         */
        db.execSQL(CREATE_TABLE_SQL_METER_USER);                      //抄表用户表
        db.execSQL(CREATE_TABLE_SQL_METER_BOOK);                      //抄表本表
        db.execSQL(CREATE_TABLE_SQL_METER_FILE);                      //抄表文件表
        db.execSQL(CREATE_TABLE_SQL_METER_PHOTO);                     //抄表图片
        db.execSQL(CREATE_TABLE_SQL_METER_NUMERICAL);                   //抄表序号
        /**
         * OA表
         */
        db.execSQL(CREATE_TABLE_SQL_OA_USER);                        //OA用户基础信息表
        db.execSQL(CREATE_TABLE_SQL_OA_USER_OUT_WORK);               //OA用户外勤信息表
        db.execSQL(CREATE_TABLE_SQL_OA_PHOTO_INFO);                  //OA照片表
        db.execSQL(CREATE_TABLE_SQL_OA_CALENDAR);                    //OA日程表
        db.execSQL(CREATE_TABLE_SQL_OA_REPORT);                      //OA工作汇报表
        db.execSQL(CREATE_TABLE_SQL_OA_ANNOUNCE);                    //OA发布公告
        db.execSQL(CREATE_TABLE_SQL_OA_EMAIL);                       //OA邮件
        db.execSQL(CREATE_TABLE_SQL_FLOW);                           //OA审批流程
    }

    //SQLiteDatabase 数据库操作类
    //execSQL 直接执行sql语句
    //sqLiteDatabase.execSQL("select table Student where name='王老五'")
    //sqLiteDatabase.execSQL(String str,Object[] objs);//直接执行sql语句，并把里面的？替换成后面的对象
    //  {"select table Student where name=? and chengji=?",new String[]{"王老五","50"}

    //insert 插入方法 【android封装好的插入数据的方法】
    //update 更新方法 【更新数据的方法，封装好的】
    //query() 查询方法
    //rawQuery 未加工的查询方法
    //delete 删除方法

    //版本更新回调函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (oldVersion == 1) {
                db.execSQL("ALTER TABLE MeterUser ADD COLUMN n_state_remark VARCHAR(200)");
                db.execSQL("ALTER TABLE MeterUser ADD COLUMN opened_remark VARCHAR(200)");
            } else if (oldVersion == 2) {
                db.execSQL("ALTER TABLE MeterUser ADD COLUMN opened_remark VARCHAR(200)");
            }else if (oldVersion==3){
                db.execSQL("ALTER TABLE MeterUser ADD COLUMN user_remark VARCHAR(200)");
            }else if (oldVersion==4){
                db.execSQL("ALTER TABLE MeterUser ADD COLUMN n_user_state VARCHAR(200)");
            }else if (oldVersion==5){
//                db.execSQL("ALTER TABLE MeterUser ADD COLUMN mt_number integer");//序号
//                db.execSQL(CREATE_TABLE_SQL_METER_NUMERICAL);                   //抄表序号
            //    db.execSQL("ALTER TABLE User ADD COLUMN lastCheckTime VARCHAR(200)");
                db.execSQL("ALTER TABLE User ADD COLUMN sum_dosage VARCHAR(200)");//购气量
            }else if (oldVersion==6){
                db.execSQL("ALTER TABLE MeterPhoto ADD COLUMN fileName varchar(200)");
        
            }

        }
    }
}
