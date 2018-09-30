package com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.util;



import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.PickerManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileEntity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileType;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    /**
     * 根据路径获取file的集合
     *
     * @param path
     * @param filter
     * @return
     */
    public static List<FileEntity> getFileListByDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);

        if (files == null) {
            return new ArrayList<>();
        }

        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());

        List<FileEntity> entities = new ArrayList<>();
        for (File f : result) {
            String absolutePath = f.getAbsolutePath();
            FileEntity e;
            if (checkExits(absolutePath)) {
                e = new FileEntity(absolutePath, f, true);
            } else {
                e = new FileEntity(absolutePath, f, false);
            }
            FileType fileType = getFileTypeNoFolder(PickerManager.getInstance().mFileTypes, absolutePath);
            e.setFileType(fileType);
            if (PickerManager.getInstance().files.contains(e)) {
                e.setSelected(true);
            }
            entities.add(e);
        }
        return entities;
    }
    /**
     * 根据路径获取file的集合
     *
     * @param path
     * @param filter
     * @return
     */
    public static ArrayList<FileEntity> getFileDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);

        if (files == null) {
            return new ArrayList<>();
        }

        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());

        ArrayList<FileEntity> entities = new ArrayList<>();
        for (File f : result) {
            String absolutePath = f.getAbsolutePath();
            FileEntity e;
            if (checkExits(absolutePath)) {
                e = new FileEntity(absolutePath, f, true);
            } else {
                e = new FileEntity(absolutePath, f, false);
            }
            FileType fileType = getFileTypeNoFolder(PickerManager.getInstance().mFileTypes, absolutePath);
            e.setFileType(fileType);
            if (PickerManager.getInstance().files.contains(e)) {
                e.setSelected(true);
            }
            entities.add(e);
        }
        return entities;
    }
    private static boolean checkExits(String path) {
        for (FileEntity entity : PickerManager.getInstance().files) {
            if (entity.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static FileType getFileType(ArrayList<FileType> fileTypes, String path) {
        for (String str : PickerManager.getInstance().mFilterFolder) {//按文件夹筛选
            if (path.contains(str)) {
                for (int index = 0; index < fileTypes.size(); index++) {//按照文件类型筛选
                    for (String string : fileTypes.get(index).filterType) {
                        if (path.endsWith(string))
                            return fileTypes.get(index);
                    }
                }
            }
        }
        return null;
    }

    //不包含文件夹
    public static FileType getFileTypeNoFolder(ArrayList<FileType> fileTypes, String path) {
        for (int index = 0; index < fileTypes.size(); index++) {
            //按照文件类型筛选
            for (String string : fileTypes.get(index).filterType) {
                if (path.endsWith(string))
                    return fileTypes.get(index);
            }
        }
        return null;
    }
}
