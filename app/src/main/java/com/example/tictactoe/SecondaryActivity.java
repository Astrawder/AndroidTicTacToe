package com.example.tictactoe;

import android.app.Activity;
import android.os.Bundle;
// Secondary Activity
public class SecondaryActivity extends Activity {
    // Variables
    private String nameOne;
    private String nameTwo;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Get the names passed from Main Activity
        nameOne = getIntent().getStringExtra("playerOneName");
        nameTwo = getIntent().getStringExtra("playerTwoName");
        // Set content to xml secondary layout
        setContentView(R.layout.activity_secondary);
    }
    // Two getter methods that return the player names
    public String getNameOne(){
        return nameOne;
    }

    public String getNameTwo(){
        return nameTwo;
    }
}
