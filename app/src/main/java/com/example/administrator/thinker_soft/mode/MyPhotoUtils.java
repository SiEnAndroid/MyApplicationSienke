package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
public class MyPhotoUtils {
    private  String userNewIdString;
    private File cropDir;  //裁剪后的图片路径
    private File tempDir;    //未裁剪的临时图片
    private SharedPreferences sharedPreferences_login;
    private Context mContext;
    private static String userPaht;

    public MyPhotoUtils(Context context, String userNewIdString) {
        this.userNewIdString = userNewIdString;
        this.mContext=context;
        sharedPreferences_login = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        File external = Environment.getExternalStorageDirectory();
        String rootDir = "/ThinkerSoft_APP/"  + sharedPreferences_login.getString("login_name", "");
        userPaht="ThinkerSoft_APP/" + sharedPreferences_login.getString("login_name", "");//用户信息
        tempDir = new File(external, rootDir + "/icon");
        cropDir = new File(external, rootDir + "/crop");
        if (!tempDir.getParentFile().exists()) {
            tempDir.getParentFile().mkdirs();
        }
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        if (!cropDir.getParentFile().exists()) {
            cropDir.getParentFile().mkdirs();
        }
        if (!cropDir.exists()) {
            cropDir.mkdirs();
        }
    }

    public File createTempFile() {
        String fileName = "";
        if (tempDir != null) {
            //fileName = UUID.randomUUID().toString() + ".png";
            fileName = "Icon_IMG_" + userNewIdString + ".jpg";
        }
        return new File(tempDir, fileName);
    }

    public File createCropFile() {
        String timeFlag = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "";
        if (cropDir != null) {
            fileName = "Crop_IMG_" + timeFlag + "_" + userNewIdString + ".jpg";
        }
        return new File(cropDir, fileName);
    }

    /*public MyPhotoUtils(int type,String securityId) {
        this.TYPE_FILE_CROP_IMAGE = type;
        this.securityId = securityId;
    }

    //得到输出文件的URI
    public Uri getOutFileUri(int fileType) {
        return Uri.fromFile(getOutFile(fileType));
    }

    //生成输出文件
    public File getOutFile(int fileType) {
        if (!Tools.hasSdcard()) {
            return null;
        }
        Log.i("MyPhotoUtils", "有SD卡");
        File mediaStorageDir = null;
        if(fileType == TYPE_FILE_CROP_IMAGE){
            //mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"SiEnKe_Crop");
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"SiEnKe_Crop");
        }

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
            if (!mediaStorageDir.mkdirs()) {
                Log.i("MyPictures", "创建图片存储路径目录失败");
                Log.i("MyPictures", "mediaStorageDir : " + mediaStorageDir.getPath());
                return null;
            }
        }
        return new File(getFilePath(mediaStorageDir, fileType));
    }

    //生成输出文件路径
    public String getFilePath(File mediaStorageDir, int fileType) {
        String timeFlag = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = mediaStorageDir.getPath() + File.separator;
        if(fileType == TYPE_FILE_CROP_IMAGE){
            filePath += ("Crop_IMG_" + timeFlag + "_" + securityId + ".jpg");
        } else {
            return null;
        }
        return filePath;
    }*/


    private static final String ICON_DIR = "Icon_image";



    //判断SD卡是否挂载

    private static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取app在外置SD卡的路径
     *
     * @param name
     * @return
     */
    private static String getAppDir(Context context, String name) {

        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getAppExternalStoragePath());
        } else {
            sb.append(getCachePath(context));
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    //获取SD下当前APP的目录

    private static String getAppExternalStoragePath() {

        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        // sb.append(APP_STORAGE_ROOT);
        sb.append(userPaht);
        sb.append(File.separator);
        return sb.toString();
    }

    //获取应用的cache目录
    private static String getCachePath(Context context) {
        File f = context.getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    //创建文件夹
    private static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    //产生图片的路径，带文件夹和文件名，文件名为当前毫秒数

    public  String generateImgePath() {
       // return getAppDir(context, ICON_DIR) +getDate()+String.valueOf(System.currentTimeMillis()) + ".jpg";
        return getAppDir(mContext, ICON_DIR) +getDate()+ userNewIdString+ ".jpg";
    }

    /**
     * 日期
     * @return
     */
    private static  String getDate(){
        String timeFlag = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeFlag+"_";
    }



private void getFilePath(){
    //调用系统相机
    File dir = new File(Environment.getExternalStorageDirectory(), "myimage");//在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
    if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
        dir.mkdirs();
    }
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
    String filename = df.format(new Date());
    File  currentImageFile = new File(dir, filename + ".jpg");
    if (!currentImageFile.exists()) {
        try {
            currentImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




    /**
     * 根据uri返回bitmap
     *
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            // 先通过getContentResolver方法获得一个ContentResolver实例，
            // 调用openInputStream(Uri)方法获得uri关联的数据流stream
            // 把上一步获得的数据流解析成为bitmap
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 返回一张压缩后的图片
     *
     * @param image 原图片
     * @param size  裁剪之后的大小
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //在自定义目录创建图片
    public   File outputIamge(Bitmap bitmap) {

        File outputIamge = new File(generateImgePath());

        //创建
        try {
            outputIamge.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(outputIamge);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputIamge;
    }

    /**
     * 保存压缩后的图片
     * @param bitmap
     * @param filePath2
     * @throws IOException
     */
    public void saveFile(Bitmap bitmap, String filePath2) throws Exception {
        // TODO Auto-generated method stub
        File testFile = new File(filePath2);
        if (testFile.exists()) {
            testFile.delete();
        }

        File myCaptureFile = new File(filePath2);
        System.out.println("------filePath2==" + filePath2);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        bos.flush();
        bos.close();
    }


//        if (outputIamge.exists()) {
//        outputIamge.delete();
//    }

//    File myCaptureFile = new File(generateImgePath());
//        System.out.println("------filePath2==" + generateImgePath());
//    BufferedOutputStream bos = null;
//        try {
//        bos = new BufferedOutputStream(
//                new FileOutputStream(myCaptureFile));
//    } catch (FileNotFoundException e) {
//        e.printStackTrace();
//    }
//    // 100表示不进行压缩，70表示压缩率为30%
//    // bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                try {
//        bos.flush();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//        try {
//        bos.close();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//
//
//        return myCaptureFile;


}
