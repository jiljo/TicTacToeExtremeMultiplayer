package com.ajce.blake.tictactoex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

//import android.support.v7.app.AppCompatActivity;

public class FlashScreen extends Activity {

    Button btnPlay;
    EditText edtPlayerX, edtPlayerO;
    SharedPreferences setPlayerData, getPlayerData;
    String playerX, playerO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //go fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash_screen);

        btnPlay = findViewById(R.id.button);
        edtPlayerX = findViewById(R.id.edtPlayerX);
        edtPlayerO = findViewById(R.id.edtPlayerO);
        //get player data
        setPlayerData = getSharedPreferences("playerData", Context.MODE_PRIVATE);
        if (setPlayerData.contains("playerX")) {
            playerX = setPlayerData.getString("playerX", "Player X").toString();
            playerO = setPlayerData.getString("playerO", "Player O").toString();
            edtPlayerO.setText(playerO);
            edtPlayerX.setText(playerX);
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set player data
                getPlayerData = getSharedPreferences("playerData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = getPlayerData.edit();
                editor2.putString("playerX", edtPlayerX.getText().toString());
                editor2.putString("playerO", edtPlayerO.getText().toString());
                editor2.apply();
                //start game play
                Intent gamePlay = new Intent(FlashScreen.this, GamePlay.class);
                startActivity(gamePlay);
                finish();
            }
        });

    }
}
