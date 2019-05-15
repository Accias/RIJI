package com.example.riji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Type;

import static android.graphics.Typeface.create;

public class TableofYear extends AppCompatActivity {

    float x1, x2, y1, y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableof_year);
    }

    public void tableToday(View view) {
        startActivity(new Intent(TableofYear.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    public void twentyNineteen(View view) {
        startActivity(new Intent(TableofYear.this, YearActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if(x1>x2)
                {
                    Intent i = new Intent(TableofYear.this, YearActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                break;
        }return false;
    }

    public void generateNewYear(View v)
    {
        //the number button
        Button newButton = new Button(this);
        Typeface dosisRegular = ResourcesCompat.getFont(this, R.font.dosis);
        newButton.setTypeface(dosisRegular);
        newButton.setText("2020");
        newButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        newButton.setTextSize(60);
        LinearLayout ll = findViewById(R.id.two);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(newButton, lp);

        //the image button
        ImageButton newImage = new ImageButton(this);
        //newImage.setImageIcon("mouse");
        ll.addView(newImage, lp);
    }
}
