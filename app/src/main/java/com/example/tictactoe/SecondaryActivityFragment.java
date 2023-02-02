package com.example.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;
import java.util.ArrayList;
import java.util.Random;

public class SecondaryActivityFragment extends Fragment
implements OnClickListener {
    // Widgets
    private TextView playerOne;
    private TextView playerTwo;
    private TextView textViewAI;
    private Button[][] buttons = new Button[3][3];
    private Button reset;
    private Button playerVsAI;
    // variables
    private String playerOneNameWithScore;
    private String playerTwoNameWithScore;
    private String onlyPlayerOneName;
    private String onlyPlayerTwoName;
    private String AI = "AI" ;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private int aiScore = 0;
    private int roundCounter = 0;
    private boolean playerOneTurn = true;
    private boolean isPlayerVSAI = false;
    // Constants
    private final int EASYMODE = 0;
    private final int HARDMODE = 1;
    // preferences
    private SharedPreferences preferences;
    private int difficultyValue = EASYMODE;
    private boolean difficulty;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // set variables of name and scores
        playerOneNameWithScore = ((SecondaryActivity) getActivity()).getNameOne() + ": " + playerOneScore;
        playerTwoNameWithScore = ((SecondaryActivity) getActivity()).getNameTwo() + ": " + playerTwoScore;
        onlyPlayerOneName = ((SecondaryActivity) getActivity()).getNameOne();
        onlyPlayerTwoName = ((SecondaryActivity) getActivity()).getNameTwo();

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(getActivity(),
                R.xml.preferences, false);

        // get the default SharedPreferences object
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_gameboard,
                container, false);

        // TextViews for players
        playerOne = (TextView) view.findViewById(R.id.textView_player1);
        playerTwo = (TextView) view.findViewById(R.id.textView_player2);
        textViewAI = (TextView) view.findViewById(R.id.textView_AI);
        // Set Names
        playerOne.setText(playerOneNameWithScore);
        playerTwo.setText(playerTwoNameWithScore);
        //  Dynamically create button listeners in matrix for board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String button = "button_" + i + j;
                int ID = getResources().getIdentifier(button, "id", getActivity().getPackageName());
                buttons[i][j] = view.findViewById(ID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        // Reset button will reset everything
        reset = view.findViewById(R.id.button_Reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlayerVSAI = false;
                playerOneScore = 0;
                playerTwoScore = 0;
                aiScore = 0;
                resetBoard();
                String nameOneAndScore = onlyPlayerOneName + " :" + playerOneScore;
                String nameTwoAndScore = onlyPlayerTwoName + " :" + playerTwoScore;
                String nameAiAndScore = AI + " :" + aiScore;
                playerOne.setText(nameOneAndScore);
                playerTwo.setText(nameTwoAndScore);
                textViewAI.setText(nameAiAndScore);
            }
        });
        // Button to enable AI. Also resets everything
        playerVsAI = view.findViewById(R.id.button_Player_vs_AI);
        playerVsAI.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.performClick();
                isPlayerVSAI = true;
            }
        });
        return view;
    }
    public void onResume() {
        super.onResume();

        // Get difficulty AI value that was set in settings
        difficultyValue = Integer.parseInt(preferences.getString("pref_AI_difficulty", "0"));
        playerVsAI.performClick();
        if(difficultyValue == 0){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "AI difficulty is set to Easy", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "AI difficulty is set to Hard", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    // This method will operate the game rounds and operate AI moves if AI has been enabled
    @Override
    public void onClick(View v) {
        // If space is occupied return
        if(!((Button) v).getText().toString().equals("")){
            return;
        }
        // Increment after every turn
        roundCounter++;

        // Regulate player and AI turns
        if(playerOneTurn){
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.RED);
            // If AI is enabled then AI move
            if(isPlayerVSAI){
                artificialMove();
            }
        }
        else{
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.BLUE);
        }
        // Displays who has won. If AI is enabled it will check to see if AI has won
        if(checkWinner()){
            if(playerOneTurn){
                if(isPlayerVSAI){
                    if(checkIfWinnerAI()){
                        aiWins();
                    }
                    else{
                        playerOneWins();
                    }
                }
                else{
                    playerOneWins();
                }
            }
            else{
                playerTwoWins();
            }
            // Displays draw between two players
        }else if(roundCounter == 9){
            draw();
            return;
        }
        // Displays draw between AI and player1 on Easy mode and Hard mode
        else if(roundCounter == 5 && isPlayerVSAI && difficulty){
            draw();
            return;
        }
        else if(roundCounter == 5 && isPlayerVSAI && !difficulty){
            draw();
            return;
        }
        else{
            if(playerOneTurn){
                if(!isPlayerVSAI){
                    playerOneTurn = false;
                }
            }
            else{
                playerOneTurn = true;
            }
        }
    }
    // Creates options menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager()
                .findFragmentById(R.id.settings_fragment);


        inflater.inflate(R.menu.fragment_startscreen_menu, menu);

    }
    // Start activity depending which option from menu is selected
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.menu_help:
                startActivity(new Intent(getActivity(), Help.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Check if player one or player two won the game
    private boolean checkWinner(){
        String[][] board = new String[3][3];
        // Create board from strings
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }
        // Check Rows for a win
        for(int i = 0; i < 3; i++){
            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])
                                    && !board[i][0].equals("")){
                return true;
            }
        }
        // Check columns for a win
        for(int i = 0; i < 3; i++){
            if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])
                    && !board[0][i].equals("")){
                return true;
            }
        }
        // Check first diagonal
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])
                && !board[0][0].equals("")){
            return true;
        }
        // Check second diagonal
        if(board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])
                && !board[0][2].equals("")){
            return true;
        }

        return false;
    }
    // Check if AI won the game
    private boolean checkIfWinnerAI(){
        String[][] board = new String[3][3];
        String symbolAI = "AI";
        // Create board from strings
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }
        // Check Rows for a win and if AI symbol was used
        for(int i = 0; i < 3; i++){
            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])
                    && !board[i][0].equals("") && board[i][0].equals(symbolAI)){
                return true;
            }
        }
        // Check columns for a win and if AI symbol was used
        for(int i = 0; i < 3; i++){
            if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])
                    && !board[0][i].equals("") && board[0][i].equals(symbolAI)){
                return true;
            }
        }
        // Check first diagonal and if AI symbol was used
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])
                && !board[0][0].equals("") && board[0][0].equals(symbolAI)){
            return true;
        }
        // Check second diagonal and if AI symbol was used
        if(board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])
                && !board[0][2].equals("") && board[0][2].equals(symbolAI)){
            return true;
        }

        return false;
    }
    // Displays winner and increments their game score
    private void playerOneWins(){
        playerOneScore++;
        String nameAndScore = onlyPlayerOneName + " :" + playerOneScore;
        playerOne.setText(nameAndScore);
        // Display toast showing player1 has won the game
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), onlyPlayerOneName + " Wins!!!", Toast.LENGTH_SHORT);
        toast.show();
        resetBoard();
    }
    // Displays winner and increments their game score
    private void playerTwoWins(){
        playerTwoScore++;
        String nameAndScore = onlyPlayerTwoName + " :" + playerTwoScore;
        playerTwo.setText(nameAndScore);
        // Display toast showing player2 has won the game
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), onlyPlayerTwoName + " Wins!!!", Toast.LENGTH_SHORT);
        toast.show();
        resetBoard();
    }
    // Displays winner and increments their game score
    private void aiWins(){
        aiScore++;
        String nameAndScore = AI + " :" + aiScore;
        textViewAI.setText(nameAndScore);
        // Display toast showing AI has won the game
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),  "AI Wins!!!", Toast.LENGTH_SHORT);
        toast.show();
        resetBoard();
    }
    // Displays if game came to a draw
    private void draw(){
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),  "Draw!!!", Toast.LENGTH_SHORT);
        toast.show();
        resetBoard();
    }
    // Only reset board and rounds
    private void resetBoard(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText("");
            }
        }
        roundCounter = 0;
        playerOneTurn = true;
    }
    // Finds move for AI. Move is different based on the AI difficulty
    private void artificialMove() {
        Random rand = new Random();
        String[][] board = new String[3][3];
        class Move {
            int row, col;
        }
        if (difficultyValue == EASYMODE) {
            difficulty = true;
        }
        if (difficultyValue == HARDMODE) {
            difficulty = false;
        }
        // Easy mode will find a random open space and move there
        if (difficulty) {
            int i, j;
            ArrayList<Move> moveList = new ArrayList<>();
            // Create board from strings
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    board[i][j] = buttons[i][j].getText().toString();
                }
            }
            // Gather coordinates for all open moves
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    if (board[i][j].equals("")) {
                        Move move = new Move();
                        move.row = i;
                        move.col = j;
                        moveList.add(move);
                    }
                }
            }
            // If no moves are left return for game to either end or draw
            if (moveList.isEmpty()) {
                return;
            }
            // Place AI move on board
            int randomInteger = rand.nextInt(moveList.size());
            Move randomMove = new Move();
            randomMove.row = moveList.get(randomInteger).row;
            randomMove.col = moveList.get(randomInteger).col;
            buttons[randomMove.row][randomMove.col].setText(AI);
            buttons[randomMove.row][randomMove.col].setTextColor(Color.BLACK);
        }
        // Hard mode will use the minimax algorithm to find the best possible move on the board
        else {
            // Create board with Strings and replace empty places with underscored for minimax to know which spaces are empty
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = buttons[i][j].getText().toString();
                    if(board[i][j].equals("")){
                        board[i][j] = "_";
                    }
                }
            }
            // Create array and call findMove to find the best possible move using minimax and place AI on board after move is found
            if(roundCounter == 5){
                return;
            }
            int[] moves = findMove(board);
            buttons[moves[0]][moves[1]].setText(AI);
            buttons[moves[0]][moves[1]].setTextColor(Color.BLACK);
        }
    }
    // Methods for Minimax algorithm below
    // Return if board has moves left that can be played
    private Boolean movesLeftOnBoard(String[][] board){
        // Return true if there is a move left to be played. Return false if no moves are left
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
               if(board[i][j].equals("_")){
                   return true;
               }
            }
        }
        return false;
    }
    // Checks if any moves are winning moves or not
    private int checkWinnerMinimax(String[][] board){
        // Check Rows for a win
        for(int i = 0; i < 3; i++){
            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])
                    && !board[i][0].equals("_")){
                if(board[i][0].equals(AI)){
                    return +10;
                }
                else if (board[i][0].equals("X")){
                    return -10;
                }
            }
        }
        // Check columns for a win
        for(int i = 0; i < 3; i++){
            if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])
                    && !board[0][i].equals("_")){
                if(board[0][i].equals(AI)){
                    return +10;
                }
                else if(board[0][i].equals("X")){
                    return -10;
                }
            }
        }
        // Check first diagonal
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])
                && !board[0][0].equals("_")){
            if(board[0][0].equals(AI)){
                return +10;
            }
            else if(board[0][0].equals("X")){
                return -10;
            }
        }
        // Check second diagonal
        if(board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])
                && !board[0][2].equals("_")){
            if(board[0][2].equals(AI)){
                return +10;
            }
            else if(board[0][2].equals("X")){
                return -10;
            }
        }
        // Return 0 if no moves result in a win
        return 0;
    }
    /* The minimax function will figure out all the possible ways
    the game can go and will return a score based on the outcome */
    private int minimax(String[][] board, int depth, boolean isMax){
        int score = checkWinnerMinimax(board);
        // Return good score based on move
        if(score == 10){
            return score;
        }
        // Return bad score based on move
        if(score == -10){
            return score;
        }
        // Return 0 if no moves are left on board
        if(!movesLeftOnBoard(board)){
            return 0;
        }
        // Maximizer's move
        if(isMax){
            int bestScore = -1000;
            // Traverse every cell in matrix
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board[i][j].equals("_")){
                        board[i][j] = AI;
                        // Recursive call to minimax and find maximum value
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, !isMax));
                        // Reset move
                        board[i][j] = "_";
                    }
                }
            }
            return bestScore;
        }
        // Minimizer's move
        else{
            int bestScore = 1000;
            // Traverse every cell in matrix
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board[i][j].equals("_")){
                        board[i][j] = "X";
                        // Recursive call to minimax and find minimum value
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, !isMax));
                        // Reset move
                        board[i][j] = "_";
                    }
                }
            }
            return bestScore;
        }
    }
    // This function will call minimax and return the best move
    private int[] findMove(String[][] board){
        // Create array to hold coordinates for best move
        int[] moves = new int[2];

        int bestValue = -1000;

        moves[0] = -1;
        moves[1] = -1;
        // Traverse every cell in matrix
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                // Once an empty move is found place that move on board
                if(board[i][j].equals("_")){
                    board[i][j] = AI;
                    // Check move with minimax
                    int moveValue = minimax(board, 0, false);
                    // Reset move
                    board[i][j] = "_";
                    // If the current value of the move made is greater then, assign best value to be equal to the current move value
                    if(moveValue > bestValue){
                        bestValue = moveValue;
                        moves[0] = i;
                        moves[1] = j;
                    }
                }
            }
        }
        // Return the best move coordinates
        return moves;
    }
}
