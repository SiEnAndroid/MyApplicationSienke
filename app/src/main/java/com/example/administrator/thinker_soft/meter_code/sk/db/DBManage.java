package com.example.administrator.thinker_soft.meter_code.sk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/8/1.
 */

public class DBManage {
    private MySqliteHelper dbHelper;
    private SQLiteDatabase db;
    public static DBManage instance = null;

    public DBManage(Context context) {
        this.dbHelper = new MySqliteHelper(context, MyApplication.DATA_BASE_VERSION);
        this.db = dbHelper.getWritableDatabase();
        //   this.db = MySqliteHelper.getInstance(context).getWritableDatabase();

    }

//    /**
//     * 初始化
//     *
//     * @param context
//     * @return
//     */
//    public static final DBManage getInstance(Context context) {
//        if (instance == null)
//            instance = new DBManage(context);
//        return instance;
//    }

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public static synchronized DBManage getInstance(Context context) {

        //就可以判断  如果为空 就创建一个， 如果不为空就还用原来的  这样整个应用程序中就只能获的一个实例
        if (instance == null) {
            instance = new DBManage(context);

        }
        return instance;
    }


    /**
     * 读取安全情况列表
     */
    public List<PopupwindowListItem> getSecurityCheckCase() {
        List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
        //查询并获得游标
        Cursor cursor = db.query("security_content", null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            return securityCaseItemList;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("securityId")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("securityName")));
            securityCaseItemList.add(item);
        }
        close();
        return securityCaseItemList;
    }

    /**
     * 读取安全隐患原因
     */

    public List<PopupwindowListItem> getSecurityHiddenReason() {
        List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from security_hidden_reason", null);
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return securityCaseItemList;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_id")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_name")));
            securityCaseItemList.add(item);
        }
        close();
        return securityCaseItemList;

    }

    /**
     * 读取安全隐患原因
     *
     * @param list
     * @return
     */
    public List<PopupwindowListItem> getSecurityHiddenReason(List<PopupwindowListItem> list) {
        List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //查询并获得游标
            Cursor cursor = db.rawQuery("select * from security_hidden_reason where n_safety_hidden_id=?", new String[]{list.get(i).getItemId()});
            //如果游标为空，则返回空
            if (cursor.getCount() == 0) {
                return securityCaseItemList;
            }
            while (cursor.moveToNext()) {
                PopupwindowListItem item = new PopupwindowListItem();
                item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_id")));
                item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_name")));
                securityCaseItemList.add(item);
            }
        }
        close();
        return securityCaseItemList;
    }

    /**
     * 读取安全隐患类型列表
     */
    public List<PopupwindowListItem> getSecurityHiddenType() {
        List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
        //查询并获得游标
        Cursor cursor = db.query("security_hidden", null, null, null, null, null, null);
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return securityCaseItemList;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_id")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_name")));
            securityCaseItemList.add(item);
        }
        close();
        return securityCaseItemList;
    }

    /**
     * 读取下载到本地的任务数据
     *
     * @param loginUserId
     * @return
     */

    public List<TaskChoose> getTaskData(String loginUserId) {
        List<TaskChoose> taskChooseList = new ArrayList<>();
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return taskChooseList;
        }
        while (cursor.moveToNext()) {
            TaskChoose taskChoose = new TaskChoose();
            taskChoose.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
            taskChoose.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
            taskChoose.setCheckType(cursor.getString(cursor.getColumnIndex("securityType")));
            taskChoose.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
            taskChoose.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            taskChoose.setRestCount("(" + cursor.getString(cursor.getColumnIndex("restCount")) + ")");
            taskChooseList.add(taskChoose);
        }
        //cursor游标操作完成以后,一定要关闭
        close();
        return taskChooseList;
    }

    /**
     * 插入安检用户数据
     */
    public void insertUser(JSONObject object, String taskId, String login_name, String userId) {
        ContentValues values = new ContentValues();
        values.put("securityNumber", String.valueOf(object.optInt("safetyInspectionId", 0)));
        if (!"null".equals(object.optString("userName", ""))) {
            values.put("userName", object.optString("userName", ""));
        } else {
            values.put("userName", "暂无");
        }
        if (!"null".equals(object.optString("meterNumber", ""))) {
            values.put("meterNumber", object.optString("meterNumber", ""));
        } else {
            values.put("meterNumber", "暂无");
        }
        if (!"null".equals(object.optString("userPhone", ""))) {
            values.put("userPhone", object.optString("userPhone", ""));
        } else {
            values.put("userPhone", "暂无");
        }
        if (!"null".equals(object.optString("securityName", ""))) {
            values.put("securityType", object.optString("securityName", ""));
        } else {
            values.put("securityType", "暂无");
        }
        if (!"null".equals(object.optString("oldUserId", ""))) {
            values.put("oldUserId", object.optString("oldUserId", ""));
        } else {
            values.put("oldUserId", "暂无");
        }
        if (!"null".equals(object.optString("userId", ""))) {
            values.put("newUserId", object.optString("userId", ""));
        } else {
            values.put("newUserId", "暂无");
        }
        if (!"null".equals(object.optString("userAdress", ""))) {
            values.put("userAddress", object.optString("userAdress", ""));
        } else {
            values.put("userAddress", "暂无");
        }
        values.put("taskId", taskId);
        values.put("ifChecked", "false");
        values.put("security_case", "");
        values.put("remarks", "");
        values.put("security_hidden", "");
        values.put("security_hidden_reason", "");
        values.put("security_case_id", "");
        values.put("security_hidden_id", "");
        values.put("security_hidden_reason_id", "");
        values.put("photoNumber", "0");
        values.put("ifUpload", "false");
        values.put("currentTime", "");
        values.put("ifPass", "");
        values.put("loginName", login_name);
        values.put("loginUserId", userId);
        values.put("security_state", "0");
        if (!"null".equals(object.optString("c_properties_name", ""))) {
            //用户性质
            values.put("userProperty", object.optString("c_properties_name", ""));
        } else {
            values.put("userProperty", "暂无");
        }
        // 新增的
        if (!"null".equals(object.optString("d_safety_inspection_date", ""))) {
            values.put("lastCheckTime", object.optString("d_safety_inspection_date", ""));
        } else {
            values.put("lastCheckTime", "1980-08-08 11:11:11");
        }
        db.insert("User", null, values);
        close();
    }


    /**
     * 更新任务Id
     *
     * @param taskId
     * @param loginUserId
     */
    public void updateTaskInfo(String taskId, String loginUserId, int tkId) {
        ContentValues values = new ContentValues();
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from Task where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            values.put("restCount", String.valueOf(Integer.parseInt(cursor.getString(cursor.getColumnIndex("restCount"))) + tkId));
            values.put("totalCount", String.valueOf(Integer.parseInt(cursor.getString(cursor.getColumnIndex("totalCount"))) + tkId));

        }
        db.update("Task", values, "taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        cursor.close();
        close();
    }


    
    Flowable<List<PopupwindowListItem>> getAllTask() {
        return Flowable.create(new FlowableOnSubscribe<List<PopupwindowListItem>>() {
            @Override
            public void subscribe(FlowableEmitter<List<PopupwindowListItem>> e) throws Exception {
                List<PopupwindowListItem> taskList = new ArrayList<>();
                
                Cursor cursor = db.rawQuery("", null);
                if (cursor.moveToFirst()) {
                    int count = cursor.getCount();
                    for (int a = 0; a < count; a++) {
//                        TaskItem item = new TaskItem();
//                        item.setTid(cursor.getInt(1));
//                        item.setStartts(cursor.getInt(2));
//                        item.setEndts(cursor.getInt(3));
//                        taskList.add(item);
//                        cursor.move(1);
                    }
                }
                cursor.close();
                db.close();
                e.onNext(taskList);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 获取安检状态
     *
     * @return
     */
    public Observable<List<PopupwindowListItem>> getSyState() {
       return HttpRetrofit.getOnSubscrib(new ObservableOnSubscribe<List<PopupwindowListItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PopupwindowListItem>> emitter) throws Exception {
                Cursor cursor = null;
                try {
                     cursor = db.rawQuery("select * from SecurityState", new String[]{});
                    List<PopupwindowListItem> result = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        PopupwindowListItem item = new PopupwindowListItem();
                        item.setItemId(cursor.getString(cursor.getColumnIndex("securityId")));
                        item.setItemName(cursor.getString(cursor.getColumnIndex("securityName")));
                        result.add(item);
                    }
                    emitter.onNext(result);
                    emitter.onComplete();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });

    }

    /**
     * 读取下载到本地的任务数据
     * @param loginUserId
     * @return
     */
    
    public Observable<List<TaskChoose>> getTaskDataList(final String loginUserId) {
        
        return HttpRetrofit.getOnSubscrib(new ObservableOnSubscribe<List<TaskChoose>>() {
            @Override
            public void subscribe(ObservableEmitter<List<TaskChoose>> emitter) throws Exception {
                Cursor cursor = null;
                try {
                    //查询并获得游标
                     cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});
                    //如果游标为空，则显示没有数据图片
                    List<TaskChoose> result = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        TaskChoose taskChoose = new TaskChoose();
                        taskChoose.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
                        taskChoose.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
                        taskChoose.setCheckType(cursor.getString(cursor.getColumnIndex("securityType")));
                        taskChoose.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
                        taskChoose.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                        taskChoose.setRestCount("(" + cursor.getString(cursor.getColumnIndex("restCount")) + ")");
                        result.add(taskChoose);
                    }
                    emitter.onNext(result);
                    emitter.onComplete();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }
    /**
     * 读取下载到本地的任务数据
     * @param taskId
     * @param loginUserId
     */
    
    public boolean getTaskData(String taskId, String loginUserId) {
        Cursor cursor = db.rawQuery("select * from Task where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return false;
        }else {
            return true;
        }
    }


    /**
     * 关闭
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (instance != null) {
            instance = null;
        }

    }
}
