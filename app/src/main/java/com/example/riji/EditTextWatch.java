package com.example.riji;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.riji.Day_related.DayDAO;
import com.example.riji.Day_related.DayRepository;

public class EditTextWatch implements TextWatcher {

    private EditText editText;

    protected EditTextWatch(EditText editText, DayDAO mDayDao, DayRepository mDayRepository) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.setText(s.toString());
    }
}
