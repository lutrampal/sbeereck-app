package com.sbeereck.lutrampal.view;

import android.support.v4.app.DialogFragment;

import com.sbeereck.lutrampal.model.Product;

/**
 * Created by lutrampal on 06/02/18 for S'Beer Eck.
 */

public abstract class DialogWithOkClickListener<T> extends DialogFragment {
    protected OnOkButtonClickListener<T> mOnOkButtonClickListener;
    public void setOnOkButtonClickListener(OnOkButtonClickListener listener) {
        mOnOkButtonClickListener = listener;
    }
}
