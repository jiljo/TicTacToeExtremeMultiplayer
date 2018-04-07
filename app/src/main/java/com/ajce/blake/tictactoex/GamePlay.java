package com.ajce.blake.tictactoex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GamePlay extends Activity implements View.OnClickListener {

    Button btnReset, btnRestart, btnScore, btnXScore, btnOScore;
    TextView txtPlayer1, txtPlayer2;
    SharedPreferences getPlayerData;
    String player1, player2;
    MediaPlayer playTone, playScore;
    //
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private boolean firstMoveX = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private int draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      //go fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_play);
        playTone = MediaPlayer.create(GamePlay.this, R.raw.play_bg);

        /*initialize button obj array*/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        //score card init
        player1Points = 0;
        player2Points = 0;
        draw = 0;
        //player data
        //get player data
        getPlayerData = getSharedPreferences("playerData", Context.MODE_PRIVATE);
        player1 = getPlayerData.getString("playerX", "Player X");
        player2 = getPlayerData.getString("playerO", "Player O");
        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);

        btnXScore = findViewById(R.id.btnXScore);
        btnOScore = findViewById(R.id.btnOScore);

        //player points and player names
        btnXScore.setText(String.valueOf(player1Points));
        btnOScore.setText(String.valueOf(player2Points));
        txtPlayer2.setText(player2);
        txtPlayer1.setText(player1);

        //play controls
        btnReset = findViewById(R.id.btnReset);
        btnRestart = findViewById(R.id.btnRestart);
        btnScore = findViewById(R.id.btnScoreCard);

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreCard();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPlay();
            }
        });


    }


    //play button click handler
    @Override
    public void onClick(View v) {

        playTone.start();

        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
            v.setBackground(getDrawable(R.drawable.playx));
            v.setBackground(getDrawable(R.drawable.playx));
        } else {
            ((Button) v).setText("O");
            v.setBackground(getDrawable(R.drawable.playo));
        }


        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Points++;
                player1Wins();
                player1Turn = true;
            } else {
                player2Points++;
                player2Wins();
                player1Turn = false;
            }
        } else if (roundCount == 9) {
            draw++;
            draw();
            player1Turn = !firstMoveX;
            firstMoveX = false;
        } else {
            player1Turn = !player1Turn;
        }
    }


    //game functions
    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }//row

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }//column

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }//diagonally left to right

        return field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("");

    }

    private void player1Wins() {
//        Toast.makeText(this, "Player 1 Wins\nScore: " + player1Points, Toast.LENGTH_SHORT).show();
//        resetPlay();
        userAlert(player1 + " Wins\nScore: " + player1Points);
        btnXScore.setText(String.valueOf(player1Points));
    }

    private void player2Wins() {
//        Toast.makeText(this, "Player 2 Wins\nScore: " + player2Points, Toast.LENGTH_SHORT).show();
//        resetPlay();
        userAlert(player2 + " Wins\nScore: " + player2Points);
        btnOScore.setText(String.valueOf(player2Points));

    }

    private void draw() {
//        Toast.makeText(this, "It's a Draw!", Toast.LENGTH_SHORT).show();
        userAlert("It's a Draw!");


    }

    private void userAlert(String alert) {

        playScore = MediaPlayer.create(GamePlay.this, R.raw.score);
        playScore.start();

        AlertDialog.Builder userAlert = new AlertDialog.Builder(this);
        userAlert.setMessage(alert);
        userAlert.setTitle("TIC TAC TOE EXTREME!");
        userAlert.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });

        AlertDialog alertDialog = userAlert.create();
        alertDialog.show();
        resetPlay();

    }

    //game controls
    private void resetPlay() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(getDrawable(R.drawable.btn_bg));
            }
        }

        //moves in a play
        roundCount = 0;
    }

    private void restartGame() {
        Intent gameFlash = new Intent(GamePlay.this, FlashScreen.class);
        startActivity(gameFlash);
        finish();
    }

    private void scoreCard() {
        int totalPlay = player1Points + player2Points + draw;
        String score = "Total Plays : " + totalPlay + "\n" + player1 + " : " + player1Points + "\n" + player2 + " : " + player2Points + "\nDraw : " + draw;


        userAlert(score);
    }
}
