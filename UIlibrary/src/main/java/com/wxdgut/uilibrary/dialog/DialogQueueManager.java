package com.wxdgut.uilibrary.dialog;

import android.app.Dialog;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Dialog弹窗队列管理类
 * 先添加的先显示，显示顺序取决于弹窗添加的时间
 *
 * @param <T>
 */
public class DialogQueueManager<T extends Dialog> {
    private Queue<T> dialogQueue = new LinkedList<>();
    private boolean isShowingDialog = false;

    public void addDialog(T dialog) {
        if (dialog != null) {
            dialogQueue.offer(dialog);
            showNextDialog();
        }
    }

    private void showNextDialog() {
        if (!isShowingDialog && !dialogQueue.isEmpty()) {
            isShowingDialog = true;
            T nextDialog = dialogQueue.poll();
            nextDialog.setOnDismissListener(dialog -> {
                isShowingDialog = false;
                showNextDialog();
            });
            nextDialog.show();
        }
    }

    public void clearQueue() {
        dialogQueue.clear();
    }
}