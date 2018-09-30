package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * Created by Administrator on 2017/6/20 0020.
 */
public class TesseractOcrActivity extends Activity {
    //训练数据路径，必须包含tesseract文件夹
    //static final String TESSBASE_PATH = "/storage/emulated/0/Download/tesseract/";
    static final String TESSBASE_PATH = Environment.getExternalStorageDirectory() + "/tesseract/";
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";
    //识别语言简体中文
    static final String CHINESE_LANGUAGE = "chi_sim";
    private Button startOcr;
    private ImageView english;
    private TextView englishtext;
    private ImageView simplechinese;
    private TextView simplechinesetext;
    private ImageView opencvImg;
    private TextView opencvOriginal,opencvGray;
    private Bitmap originalBitmap;
    private Bitmap grayBitmap;
    private static boolean flag = true;
    private static boolean isFirst = true;
    static {
        if(!OpenCVLoader.initDebug()){
            Log.i("TesseractOcrActivity", "openCV初始化失败");
        }else{
            System.loadLibrary("opencv_java3");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tesseract_ocr);

        bindView();
        File dir = new File(TESSBASE_PATH + "tessdata/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        defaultSettings();
        setViewClickListener();
    }

    //点击事件
    public void setViewClickListener() {
        startOcr.setOnClickListener(clickListener);
        opencvOriginal.setOnClickListener(clickListener);
        opencvGray.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_ocr:
                    englishOCR();
                    simpleChineseOCR();
                    break;
                case R.id.opencv_original:
                    opencvImg.setImageBitmap(originalBitmap);
                    break;
                case R.id.opencv_gray:
                    opencvImg.setImageBitmap(grayBitmap);
                    break;
            }
        }
    };

    //绑定控件
    private void bindView() {
        startOcr = (Button) findViewById(R.id.start_ocr);
        english = (ImageView) findViewById(R.id.english);
        englishtext = (TextView) findViewById(R.id.english_text);
        simplechinese = (ImageView) findViewById(R.id.simple_chinese);
        simplechinesetext = (TextView) findViewById(R.id.simple_chinese_text);
        opencvImg = (ImageView) findViewById(R.id.opencv_img);
        opencvOriginal = (TextView) findViewById(R.id.opencv_original);
        opencvGray = (TextView) findViewById(R.id.opencv_gray);
    }

    private void defaultSettings(){
        originalToGrayImg();
    }

    //英文识别
    public void englishOCR() {
        //设置图片可以缓存
        english.setDrawingCacheEnabled(true);
        //获取缓存的bitmap
        final Bitmap bmp = english.getDrawingCache();
        final TessBaseAPI baseApi = new TessBaseAPI();
        //初始化OCR的训练数据路径与语言
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        //设置识别模式
        baseApi.setVariable("tessedit_char_whitelist","0123456789");
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bmp);
        english.setImageBitmap(bmp);
        //根据Init的语言，获得ocr后的字符串
        englishtext.setText(baseApi.getUTF8Text());
        //释放bitmap
        baseApi.clear();
        //如果连续ocr多张图片，这个end可以不调用，但每次ocr之后，必须调用clear来对bitmap进行释放
        //释放native内存
        baseApi.end();
    }

    //中文识别
    public void simpleChineseOCR() {
        //设置图片可以缓存
        simplechinese.setDrawingCacheEnabled(true);
        //获取缓存的bitmap
        final Bitmap bmp = simplechinese.getDrawingCache();
        final TessBaseAPI baseApi = new TessBaseAPI();
        //初始化OCR的训练数据路径与语言
        baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bmp);
        simplechinese.setImageBitmap(bmp);
        simplechinesetext.setText(baseApi.getUTF8Text());
        baseApi.clear();
        baseApi.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load OpenCV engine and init OpenCV library
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, getApplicationContext(), mLoaderCallback);
    }

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i("TesseractOcrActivity", "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i("TesseractOcrActivity", "加载失败");
                    break;
            }
        }
    };

    //原图和绘图之间转换
    public void originalToGrayImg(){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.opencv_test);
        grayBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(originalBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        Log.i("TesseractOcrActivity", "图片转换成功！.");
    }
}
