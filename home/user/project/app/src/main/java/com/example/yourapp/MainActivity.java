package com.example.yourapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private StringBuilder input = new StringBuilder();
    private double firstOperand = 0;
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.most_resultado);

        // Set up button listeners
        setNumberButtonListeners();
        setOperatorButtonListeners();
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
            R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4,
            R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                input.append(button.getText().toString());
                display.setText(input.toString());
            }
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorButtonListeners() {
        findViewById(R.id.sum).setOnClickListener(operatorListener);
        findViewById(R.id.rest).setOnClickListener(operatorListener);
        findViewById(R.id.mult).setOnClickListener(operatorListener);
        findViewById(R.id.div).setOnClickListener(operatorListener);
        findViewById(R.id.igu).setOnClickListener(equalListener);
        findViewById(R.id.DEL).setOnClickListener(deleteListener);
    }

    private View.OnClickListener operatorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (input.length() > 0) {
                firstOperand = Double.parseDouble(input.toString());
                operator = button.getText().toString();
                input.setLength(0);
            }
        }
    };

    private View.OnClickListener equalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (input.length() > 0 && !operator.isEmpty()) {
                double secondOperand = Double.parseDouble(input.toString());
                double result = 0;
                switch (operator) {
                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;
                    case "*":
                        result = firstOperand * secondOperand;
                        break;
                    case "/":
                        if (secondOperand != 0) {
                            result = firstOperand / secondOperand;
                        } else {
                            display.setText("Error");
                            return;
                        }
                        break;
                }
                display.setText(String.valueOf(result));
                input.setLength(0);
                operator = "";
            }
        }
    };

    private View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input.setLength(0);
            display.setText("");
            firstOperand = 0;
            operator = "";
        }
    };
}