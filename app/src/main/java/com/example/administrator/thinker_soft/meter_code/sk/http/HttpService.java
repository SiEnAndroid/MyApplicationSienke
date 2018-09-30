package com.example.administrator.thinker_soft.meter_code.sk.http;

import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastContentBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastHiddenBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastLoginBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastReasonBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastStateBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastTaskBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastUserBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.UpDateBean;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * class
 *
 * @author g
 * @date 2018/8/21:15:00]
 */
public interface HttpService {
    /**
     * @return 下载apk
     * @Streaming
     */
//
    @GET
    Observable<ResponseBody> loadFile(@NonNull @Url String url);
//    @GET("thinkerSoft_移动安检.apk")
//    Call<ResponseBody> loadFile();

    /**
     * 获取apk信息
     *
     * @return
     */
    @GET("getVersion.do")
    Observable<UpDateBean> getUpdate();

    /**
     * 获取数据
     */
    @FormUrlEncoded
    @POST
    Observable<String> getRemoteData(@Url String url, @FieldMap LinkedHashMap<String, String> map);

    /**
     * 上传文件
     */
    @Multipart
    @FormUrlEncoded
    @POST
    Observable<String> upLoadFile(@Url String url, @Part List<MultipartBody.Part> file, @FieldMap LinkedHashMap<String, String> map);

    /**
     * 文件下载
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url() String url);

    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     *
     * @param parts 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @POST("users/image")
    Observable<ResponseBody> uploadFilesWithParts(@Part() List<MultipartBody.Part> parts);

    @Multipart
    @POST("/rest/services/file")
    Observable<ResponseBody> uploadFileWithRequestBody(@Part("json") RequestBody jsonBody,
                                                       @Part List<MultipartBody.Part> parts);

    @GET("getYwMsg.do")
    Observable<ResponseBody> getYwMsg(@Query("n_stop_water_type") int start);


//  -------------------- 东渝 -------------------------------------------

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @return
     */
    @GET("login.do")
    Observable<SyEastLoginBean> getDyLogin(@Query("username") String username, @Query("password") String password);

    /**
     * 安检状态
     *
     * @return
     */
    @GET("findSecurityState.do")
    Observable<SyEastStateBean> getSecurityState();

    /**
     * 安检内容
     *
     * @return
     */
    @GET("findSecurityContent.do")
    Observable<SyEastContentBean> getSecurityContent();

    /**
     * 安检原因类型
     *
     * @return
     */
    @GET("findSafetyHidden.do")
    Observable<SyEastHiddenBean> getSecurityHidden();

    /**
     * 安检原因
     *
     * @return
     */
    @GET("findSafetyReason.do")
    Observable<SyEastReasonBean> getSecurityReason();

    /**
     * 安检计划下载
     *
     * @return
     */
    @GET("getDySjXz.do")
    Observable<SyEastTaskBean> getSecurityTask(
            @Query("n_company_id") String n_company_id
            , @Query("c_anjian_staff") String c_anjian_staff
            , @Query("startTime") String startTime
            , @Query("endTime") String endTime);


    /**
     * 下载安检用户数据
     * @param safetyPlan
     * @return
     */
   @GET("getDyUserCheck.do")
    Observable<SyEastUserBean> getUserCheck(@Query("safetyPlan") String safetyPlan
            , @Query("safetyState")String safetyState );

    
}