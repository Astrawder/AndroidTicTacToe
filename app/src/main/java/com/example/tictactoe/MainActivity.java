package com.example.tictactoe;

import android.app.Activity;
import android.os.Bundle;
// Main Activity
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content to main activity layout
        setContentView(R.layout.activity_main);
    }
}
