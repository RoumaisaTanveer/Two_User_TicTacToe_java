package com.example.usertictac;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true;  // true = user, false = AI
    private int moveCount = 0;
    private TextView status;
    private Random random = new Random();
    private boolean gameOver = false; // To prevent further moves after a win

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        initializeBoard();

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                final int finalI = i, finalJ = j;
                buttons[i][j].setOnClickListener(v -> makeMove(finalI, finalJ));
            }
        }
    }

    private void makeMove(int i, int j) {
        if (!buttons[i][j].getText().toString().equals("") || !playerTurn || gameOver) return;

        buttons[i][j].setText("X");
        buttons[i][j].setTextColor(Color.BLUE);
        moveCount++;

        if (checkWin("X")) {
            status.setText("You Win! 🎉");
            gameOver = true;
            disableBoard();
            return;
        }

        if (moveCount < 9) {
            playerTurn = false;
            status.setText("System's Turn...");
            systemMove();
        } else {
            status.setText("It's a Draw! 🤝");
            gameOver = true;
        }
    }

    private void systemMove() {
        if (moveCount >= 9 || gameOver) return;

        int[] bestMove = getBestMove();
        int i = bestMove[0], j = bestMove[1];

        buttons[i][j].setText("O");
        buttons[i][j].setTextColor(Color.RED);
        moveCount++;

        if (checkWin("O")) {
            status.setText("System Wins! 😞");
            gameOver = true;
            disableBoard();
            return;
        }

        if (moveCount < 9) {
            playerTurn = true;
            status.setText("Your Turn!");
        } else {
            status.setText("It's a Draw! 🤝");
            gameOver = true;
        }
    }

    private int[] getBestMove() {
        // First, check if AI can win in the next move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("O");
                    if (checkWin("O")) {
                        buttons[i][j].setText("");
                        return new int[]{i, j};
                    }
                    buttons[i][j].setText("");
                }
            }
        }

        // Second, block the player if they are about to win
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("X");
                    if (checkWin("X")) {
                        buttons[i][j].setText("");
                        return new int[]{i, j};
                    }
                    buttons[i][j].setText("");
                }
            }
        }

        // Otherwise, play a random move
        int i, j;
        do {
            i = random.nextInt(3);
            j = random.nextInt(3);
        } while (!buttons[i][j].getText().toString().equals(""));
        return new int[]{i, j};
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(symbol) &&
                    buttons[i][1].getText().toString().equals(symbol) &&
                    buttons[i][2].getText().toString().equals(symbol)) return true;

            if (buttons[0][i].getText().toString().equals(symbol) &&
                    buttons[1][i].getText().toString().equals(symbol) &&
                    buttons[2][i].getText().toString().equals(symbol)) return true;
        }

        if (buttons[0][0].getText().toString().equals(symbol) &&
                buttons[1][1].getText().toString().equals(symbol) &&
                buttons[2][2].getText().toString().equals(symbol)) return true;

        if (buttons[0][2].getText().toString().equals(symbol) &&
                buttons[1][1].getText().toString().equals(symbol) &&
                buttons[2][0].getText().toString().equals(symbol)) return true;

        return false;
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        moveCount = 0;
        playerTurn = true;
        gameOver = false;
        status.setText("Your Turn!");
    }
}
