package com.example.riji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Year extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        yearBackTable();
    }

    public void yearBackTable()
    {
        Button backButton = (Button) findViewById(R.id.TableofYear);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Year.this, TableofYear.class));
                finish();
            }
        });

    }
}
