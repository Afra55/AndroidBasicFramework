package com.afra55.commontutils.http;

/**
 * @author Afra55
 * @date 2018/4/16
 * A smile is the best business card.
 */
public interface UploadProgressListener {
    /**
     * 上传进度
     *
     * @param currentBytesCount long
     * @param totalBytesCount   long
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
