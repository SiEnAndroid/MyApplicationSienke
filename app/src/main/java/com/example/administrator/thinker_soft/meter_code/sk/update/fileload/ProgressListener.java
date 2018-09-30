package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;

/**
 * @author g
 * @FileName ProgressListener
 * @date 2018/9/25 15:49
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
