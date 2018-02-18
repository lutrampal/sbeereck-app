package com.sbeereck.lutrampal.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.WindowManager;

/**
 * Created by lutrampal on 17/02/18 for S'Beer Eck.
 */

public abstract class AsyncTaskWithLoadAnimation<T, U, V> extends AsyncTask<T, U, V> {
    private ProgressDialog progDialog;
    private Context context;

    public AsyncTaskWithLoadAnimation(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show( context, null, null, true, false);
        progDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        progDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        progDialog.setContentView(R.layout.progress_bar);
    }

    @Override
    protected void onPostExecute(V v) {
        super.onPostExecute(v);
        progDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progDialog.dismiss();
    }

    @Override
    protected void onCancelled(V v) {
        super.onCancelled(v);
        progDialog.dismiss();
    }
    
}

