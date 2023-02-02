/*
Author: Aidan Strawder
Date: 12/17/19
Description: This is a program that lets two players play against each other in a game of tic-tac-toe.
The game also has an AI that the player can play against with difficulties level easy and hard.
*/
package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.app.Fragment;

public class MainActivityFragment extends Fragment
implements OnEditorActionListener, OnClickListener {

    // Widgets
    private EditText nameOne;
    private EditText nameTwo;
    private Button enterStartScreen;
    // Instance variables
    private String playerOneName = "";
    private String playerTwoName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startscreen,
                container, false);

        // References to widgets
        nameOne = (EditText) view.findViewById(R.id.editText_Name1);
        nameTwo = (EditText) view.findViewById(R.id.editText_Name2);
        enterStartScreen = (Button) view.findViewById(R.id.button_Enter);

        // Listeners
        nameOne.setOnEditorActionListener(this);
        nameTwo.setOnEditorActionListener(this);
        enterStartScreen.setOnClickListener(this);

        return view;
    }
    // Set names depending on what names are entered in both prompts
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int keyCode = -1;
        if(event != null){
            keyCode = event.getKeyCode();
        }
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_NEXT ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_ENTER){
            playerOneName = nameOne.getText().toString();
            playerTwoName = nameTwo.getText().toString();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // Pass player names with intent and start activity of game board
        if(!playerOneName.isEmpty() && !playerTwoName.isEmpty()){
            Intent intent = new Intent(getActivity(), SecondaryActivity.class);
            intent.putExtra("playerOneName", playerOneName);
            intent.putExtra("playerTwoName", playerTwoName);
            startActivity(intent);
        }
        // Toast error message if names are empty
        else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please enter a name for each player before clicking Enter.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
