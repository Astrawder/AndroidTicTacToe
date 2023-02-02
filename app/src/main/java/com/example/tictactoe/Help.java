package com.example.tictactoe;

import android.app.Activity;
import android.os.Bundle;
// Help Activity
public class Help extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content to help xml layout
        setContentView(R.layout.activity_help);
    }
}
