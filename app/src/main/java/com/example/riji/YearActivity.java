package com.example.riji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class YearActivity extends AppCompatActivity {

    float x1, x2, y1, y2;
    //Button myButton = new Button(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        yearBackTable();
       // myButton.setText("Push Me");

       // RelativeLayout ll = (RelativeLayout)findViewById(R.id.year);
       // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // ll.addView(myButton, lp);
    }

    public void yearToday(View view) {
        startActivity(new Intent(YearActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
        finish();
    }

    public void january(View view){

        startActivity(new Intent(YearActivity.this, Month_swipe.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void yearBackTable()
    {
        Button backButton =  findViewById(R.id.TableofYear);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(YearActivity.this, TableofYear.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

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
                    Intent i = new Intent(YearActivity.this, Month_swipe.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                if(x1<x2)
                {
                    Intent j = new Intent(YearActivity.this, TableofYear.class);
                    startActivity(j);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                break;
        }return false;
    }
}
