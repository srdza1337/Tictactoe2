package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int spinner;
    int n = 3;
    private Button[][] buttons = new Button[n][n];

    private boolean player1Turn = true;

    private int roundCount;

    private MediaPlayer clockVoice; //novo
    private CountDownTimer countDownTimer;//novo
    private int sec = 10000;//novo
    public TextView timer;//novo

    private int player1Points;
    private int player2Points;

    public TextView textViewPlayer1;
    public TextView textViewPlayer2;
    String pl1, pl2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timer = (TextView) findViewById(R.id.timer);
        clockVoice = MediaPlayer.create(MainActivity.this, R.raw.music);
        clockVoice.start();//novo

        Intent intent = this.getIntent();
        if (intent != null) {
            pl1 = intent.getStringExtra("pl1");
            pl2 = intent.getStringExtra("pl2");

        }
        textViewPlayer1 = (TextView) findViewById(R.id.text_view_p1);
        textViewPlayer2 = (TextView) findViewById(R.id.text_view_p2);

        textViewPlayer1.setText(pl1 + ": 0");
        textViewPlayer2.setText(pl2 + ": 0");

        Time();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);

            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                timer.setText("10");

            }
        });

        Button buttonpa = (Button) findViewById(R.id.buttonpa);//novo
        buttonpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();

                timer.setText("10");

            }
        });

        Button buttonbtm = (Button) findViewById(R.id.buttonbtm);//novo
        buttonbtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, First_Page.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        countDownTimer.start();
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            textViewPlayer1.setTextColor(Color.parseColor("gray"));//novo
            textViewPlayer2.setTextColor(Color.parseColor("green"));//novo
            ((Button) v).setText("X");
            ((Button) v).setBackgroundColor(getResources().getColor(R.color.blue));

        } else {
            ((Button) v).setText("O");
            textViewPlayer1.setTextColor(Color.parseColor("blue"));//novo
            textViewPlayer2.setTextColor(Color.parseColor("gray"));//novo
            ((Button) v).setBackgroundColor(getResources().getColor(R.color.green));


        }
        roundCount++;

        if (checkForWin(n)) {

            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }

        } else if (roundCount == Math.pow(n,2)) {
            draw();
        } else {


            player1Turn = !player1Turn;
        }
    }


    private boolean checkForWin(int n) {
        ArrayList<List<String>> field1 = new ArrayList<>();

        for (int i = 0; i < n ; i++)
            field1.add(Arrays.asList("", "", "", "", "", ""));


        for (int x = 0; x < n; x++)
            for (int y = 0; y < n; y++)
                if (!(field1.get(x).get(y).equals(buttons[x][y].getText().toString())) && !(buttons[x][y].getText().toString().equals(""))) {

                    field1.get(x).set(y, buttons[x][y].getText().toString());
                    String flag = buttons[x][y].getText().toString();


                    for (int i = 0; i < n; i++) {
                        if (!(field1.get(x).get(i).equals(flag)))
                            break;
                        if (i == n - 1)
                            return true;


                    }

                    for (int i = 0; i < n; i++) {
                        if (!(field1.get(i).get(y).equals(flag))) {

                            break;
                        }
                        if (i == n - 1)
                            return true;


                    }

                    if (x == y) {
                        for (int i = 0; i < n; i++) {
                            if (!(field1.get(i).get(i).equals(flag)))
                                break;
                            if (i == n - 1)
                                return true;

                        }
                    }

                    if (x + y == n - 1) {
                        for (int i = 0; i < n; i++) {
                            if (!(field1.get(i).get((n - 1) - i).equals(flag)))
                                break;
                            if (i == n - 1) {
                                return true;
                            }
                        }
                    }
                }
        return false;
    }


    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, pl1 + " wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        countDownTimer.cancel();//novo

    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, pl2 + " wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        countDownTimer.cancel();//novo

    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        countDownTimer.cancel();//novo
    }

    private void updatePointsText() {
        textViewPlayer1.setText(pl1 + ": " + player1Points);
        textViewPlayer2.setText(pl2 + ": " + player2Points);
        textViewPlayer1.setTextColor(Color.parseColor("blue"));//novo
        textViewPlayer2.setTextColor(Color.parseColor("gray"));//novo


    }

    private void resetBoard() {
        countDownTimer.start();//novo
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(getResources().getColor(R.color.gray));//novo

            }
        }
        roundCount = 0;
        player1Turn = true;
        countDownTimer.cancel();//novo
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();

    }

    private void Time() {                //novo
        countDownTimer = new CountDownTimer(sec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                player1Turn = !player1Turn;
                if (player1Turn) {
                    Toast.makeText(MainActivity.this, pl1 + "'s turn.", Toast.LENGTH_SHORT).show();
                    textViewPlayer1.setTextColor(Color.parseColor("blue"));
                    textViewPlayer2.setTextColor(Color.parseColor("gray"));
                } else {
                    Toast.makeText(MainActivity.this, pl2 + "'s turn.", Toast.LENGTH_SHORT).show();
                    textViewPlayer1.setTextColor(Color.parseColor("gray"));
                    textViewPlayer2.setTextColor(Color.parseColor("green"));
                }
                countDownTimer.start();
                clockVoice.start();

            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}