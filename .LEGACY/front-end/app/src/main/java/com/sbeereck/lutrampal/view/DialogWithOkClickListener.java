package com.sbeereck.lutrampal.view;

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;

import com.sbeereck.lutrampal.model.Product;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lutrampal on 06/02/18 for S'Beer Eck.
 */

public abstract class DialogWithOkClickListener<T> extends DialogFragment {
    protected OnOkButtonClickListener<T> mOnOkButtonClickListener;
    public void setOnOkButtonClickListener(OnOkButtonClickListener listener) {
        mOnOkButtonClickListener = listener;
    }

    private List<AsyncTask> tasks = new LinkedList<>();

    protected void addTaskToRunningAsyncTasks(AsyncTask task) {
        tasks.add(task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (AsyncTask task: tasks) {
            task.cancel(true);
        }
    }
}
