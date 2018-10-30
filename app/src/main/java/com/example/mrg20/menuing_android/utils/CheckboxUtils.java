package com.example.mrg20.menuing_android.utils;

import android.app.ActionBar;
import android.content.Context;
import android.widget.CheckBox;

import com.example.mrg20.menuing_android.R;

public class CheckboxUtils {
    public static CheckBox createNewCheckBox(String text, Context context) {
        final ActionBar.LayoutParams lparams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        final CheckBox checkBox = new CheckBox(context);
        checkBox.setLayoutParams(lparams);
        checkBox.setText(text);
        checkBox.setChecked(true);
        checkBox.setTextAppearance(context, R.style.SmallText);
        return checkBox;
    }
}
