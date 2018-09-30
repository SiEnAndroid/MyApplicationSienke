package com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter.CommonFileAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter.OnFileItemClickListener;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
public class FileCommonFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    private CommonFileAdapter mCommonFileAdapter;
    private OnUpdateDataListener mOnUpdateDataListener;

    public void setOnUpdateDataListener(OnUpdateDataListener onUpdateDataListener) {
        mOnUpdateDataListener = onUpdateDataListener;
    }

    public static FileCommonFragment newInstance(){
        return new FileCommonFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_normal,null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_normal_file);
        mRecyclerView.setLayoutManager(layoutManager);
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void initData() {
        new FileScannerTask(getContext(), new FileScannerTask.FileScannerListener() {
            @Override
            public void scannerResult(List<FileEntity> entities) {
                mProgressBar.setVisibility(View.GONE);
                if(entities.size()>0){
                    mEmptyView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                mCommonFileAdapter = new CommonFileAdapter(getContext(),entities);
                mRecyclerView.setAdapter(mCommonFileAdapter);
                iniEvent(entities);
            }
        }).execute();
    }

    private void iniEvent(final List<FileEntity> entities) {
        mCommonFileAdapter.setOnItemClickListener(new OnFileItemClickListener() {
            @Override
            public void click(int position) {
                FileEntity entity = entities.get(position);
                String absolutePath = entity.getPath();
                ArrayList<FileEntity> files = PickerManager.getInstance().files;
                if(files.contains(entity)){
                    files.remove(entity);
                    if(mOnUpdateDataListener!=null){
                        mOnUpdateDataListener.update(-Long.parseLong(entity.getSize()));
                    }
                    entity.setSelected(!entity.isSelected());
                    mCommonFileAdapter.notifyDataSetChanged();
                }else {
                    if(PickerManager.getInstance().files.size()<PickerManager.getInstance().maxCount){
                        files.add(entity);
                        if(mOnUpdateDataListener!=null){
                            mOnUpdateDataListener.update(Long.parseLong(entity.getSize()));
                        }
                        entity.setSelected(!entity.isSelected());
                        mCommonFileAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getContext(),getString(R.string.file_select_max,PickerManager.getInstance().maxCount), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
