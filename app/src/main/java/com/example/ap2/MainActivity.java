package com.example.ap2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Creamos las varibales que vamos a usar para el calculo
    private TextView mostResultado;
    private StringBuilder currentInput;
    private double firstOperand;
    private String operator;
    private boolean isNewNumber;
    private boolean hasResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pal barra de tareas sea transparente
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        
        // INICIALIZAMOS LAS VARIABLES ANTERIORES
        mostResultado = findViewById(R.id.most_resultado);
        currentInput = new StringBuilder();
        isNewNumber = true;
        hasResult = false;
        mostResultado.setText("0");


     //--CONFUIGURACION PARA LOS BOTONES
        //  para botones numéricos
        setNumberButtonListeners();

        //  para botones de operación
        setOperationButtonListeners();

        //  para el botón igual
        Button btnIgual = findViewById(R.id.igu);
        btnIgual.setOnClickListener(v -> calculateResult());

        //  para el botón DEL
        Button btnDel = findViewById(R.id.DEL);
        btnDel.setOnClickListener(v -> deleteLastChar());
    }

    //--CONFIGURAMOS LOS BOTONES NUMERICOS
    private void setNumberButtonListeners() {
        // Creamos un arreglos con todos los id de los botones numericos
        int[] numberIds = {R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9};
        // Recorremos el arreglo y para cada id, le asignamos un listener
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(this::onNumberClick);
        }
    }

    //--CONFIGURAMOS LOS BOTONES DE OPERACION
    private void setOperationButtonListeners() {
        // Creamos un arreglos con todos los id de los botones de operación
        int[] opIds = {R.id.sum, R.id.rest, R.id.mult, R.id.div};
        // Recorremos el arreglo y para cada id, le asignamos un listener
        for (int id : opIds) {
            findViewById(id).setOnClickListener(this::onOperationClick);
        }
    }

    //configuramos el evento para los botones numericos al precionarlos
    private void onNumberClick(View view) {
        // Creamos un boton para que se pueda usar en el evento
        Button button = (Button) view;
        //si es un nuevo numero, se limpia el input
        if (isNewNumber) {
            currentInput.setLength(0);
            isNewNumber = false;
        }
        //si ya hay un resultado, se limpia el input
        if (hasResult) {
            currentInput.setLength(0);
            operator = null;
            firstOperand = 0;
            hasResult = false;
        }
        //se agrega el numero al input
        currentInput.append(button.getText());
        //se muestra el numero en el textview
        mostResultado.setText(currentInput.toString());
    }

    //configuramos el evento para los botones de operación
    private void onOperationClick(View view) {
        Button button = (Button) view;
        String newOperator = button.getText().toString();

        if (currentInput.length() > 0) {
            if (operator != null && !isNewNumber) {
                calculateResult();

            } else {
                firstOperand = Double.parseDouble(currentInput.toString());
            }

            operator = newOperator;
            isNewNumber = true;
            hasResult = false;
            mostResultado.setText(formatNumber(firstOperand) + " " + operator);
            
        } else if (operator != null) {
            // para cambiar operador si no hay nuevo número
            operator = newOperator;
            mostResultado.setText(formatNumber(firstOperand) + " " + operator);
        } else if (firstOperand != 0 || hasResult) {
            operator = newOperator;
            mostResultado.setText(formatNumber(firstOperand) + " " + operator);
        }
    }
 //--CONFIGURAMOS EL EVENTO PARA EL BOTON IGUAL
    private void calculateResult() {
        if (operator != null && currentInput.length() > 0 && !isNewNumber) {
            double secondOperand = Double.parseDouble(currentInput.toString());
            double result = 0;
            boolean validOperation = true;
            // Creamos un switch para realizar cada operación
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
                    //si es diferente a 0, se realiza la operación normalmente
                    if (secondOperand != 0) {
                        result = firstOperand / secondOperand; 
                        //pero si a alguien se le occurre dividir por 0 le mostrara error 
                    } else {
                        mostResultado.setText("Error");
                        validOperation = false;
                        currentInput.setLength(0);
                        operator = null;
                        firstOperand = 0;
                        isNewNumber = true;
                    }
                    break;
            }
            //si la operación es valida, se realiza la operación y se muestra el resultado
            if (validOperation) {
                firstOperand = result;
                currentInput.setLength(0);
                currentInput.append(formatNumber(result));
                mostResultado.setText(formatNumber(result));
                isNewNumber = true;
                hasResult = true;
            }
        }
    }

    //configuramos el evento para el botón DEL que limpia todo
    private void deleteLastChar() {
        currentInput.setLength(0);
        operator = null;
        firstOperand = 0;
        mostResultado.setText("0");
        isNewNumber = true;
        hasResult = false;
    }

    //--CONFIGURAMOS EL FORMATO DE LOS NUMEROS
    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%.2f", number);
        }
    }
}