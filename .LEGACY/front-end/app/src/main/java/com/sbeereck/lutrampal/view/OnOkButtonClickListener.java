package com.sbeereck.lutrampal.view;

/**
 * Created by lutrampal on 07/01/18 for S'Beer Eck.
 */

public interface OnOkButtonClickListener<T> {
    void onOkButtonClick(T obj, Boolean wasEditing);
}