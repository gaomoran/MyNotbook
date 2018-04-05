package com.mrg.mynotbook.presenter;

import android.view.View;

/**
 * Created by MrG on 2017-05-10.
 */
public interface EditDiaryListenter {
    void onClickListener(View view);

    /**
     * 开始录音0
     */
    void startRecordAudio();

    /**
     * 保存录音
     */
    void saveRecordAudio();

    /**
     * 取消录音
     */
    void cancelRucordAudio();

    /**
     * 关闭底部工具
     */
    void closeViewUtils();
}
