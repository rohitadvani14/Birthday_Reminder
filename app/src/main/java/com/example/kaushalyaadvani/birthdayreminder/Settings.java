package com.example.kaushalyaadvani.birthdayreminder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by kaushalyaadvani on 29/06/16.
 */
public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar)findViewById(R.id.settings_Toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab= getSupportActionBar();

        //enable up button

        ab.setDisplayHomeAsUpEnabled(true);

    }
}
