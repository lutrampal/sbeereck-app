package com.sbeereck.lutrampal.view;

import android.app.DialogFragment;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lutrampal on 13/02/18 for S'Beer Eck.
 */

public abstract class DialogFragmentWithAsyncTasks extends DialogFragment {
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
