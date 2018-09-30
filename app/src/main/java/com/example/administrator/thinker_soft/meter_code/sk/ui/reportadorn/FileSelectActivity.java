package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.FileSelectAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author g
 * @FileName FileSelectActivity
 * @date 2018/9/25 17:43
 */
public class FileSelectActivity extends ListActivity {
    private static final String root = new String(Environment
            .getExternalStorageDirectory().getPath() + File.separator);
    /**显示文件的目录*/
    private TextView tv;
    private File[] files;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        tv = (TextView) findViewById(R.id.currPath);
        getFiles(root);
    }

    /**
     * 返回
     * @param view
     */
    public void OnBack(View view){
        finish();
    }

    public void getFiles(String path) {
        tv.setText(path);
        File f = new File(path);
        // 得到所有子文件和文件夹
        File[] tem = f.listFiles();
        // 如果当前的目录不是在顶层目录，就把父目录要到files数组中的第一个
        if (!path.equals(root)) {
            files = new File[tem.length + 1];
            System.arraycopy(tem, 0, files, 1, tem.length);
            files[0] = f.getParentFile();
        } else {
            files = tem;
        }
        sortFilesByDirectory(files);
        // 为ListActivity设置Adapter
        setListAdapter(new FileSelectAdapter(this, files, files.length == tem.length));
    }

    // 对文件进行排序
    private void sortFilesByDirectory(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.length()).compareTo(f2.length());
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File f = files[position];
        if (!f.canRead()) {
            Toast.makeText(this, "文件不可读", Toast.LENGTH_SHORT).show();
            return;
        }
        if (f.isFile()) {
            // 为文件
            String path = f.getAbsolutePath();
            Intent intent = new Intent();
            intent.putExtra("path", path);
            setResult(1, intent);
            finish();
        } else {
            getFiles(f.getAbsolutePath());
        }
    }


}
