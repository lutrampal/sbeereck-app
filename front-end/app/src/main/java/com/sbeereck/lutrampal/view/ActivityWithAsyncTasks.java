package com.sbeereck.lutrampal.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lutrampal on 13/02/18 for S'Beer Eck.
 */

public abstract class ActivityWithAsyncTasks extends AppCompatActivity {
    private List<AsyncTask> tasks = new LinkedList<>();

    protected void addTaskToRunningAsyncTasks(AsyncTask task) {
        tasks.add(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (AsyncTask task: tasks) {
            task.cancel(true);
        }
    }
}
