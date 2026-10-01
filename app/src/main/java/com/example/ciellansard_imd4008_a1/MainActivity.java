package com.example.ciellansard_imd4008_a1;

import static java.lang.Float.isNaN;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView screen;
    // All keys that type exactly what's on the key
    // Nums 0 - 9, ., (), and operands (excluding =)
    TextView[] btnType = new TextView[17];
    TextView btnBack;
    TextView btnClear;
    TextView btnStore;
    TextView btnRecall;
    TextView btnSign;
    TextView btnHistory;
    TextView btnEqual;

    String storedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        screen = findViewById(R.id.screen);

        btnEqual  = findViewById(R.id.buttonEqual);
    }

    // Return true if character is a letter of the alphabet
    public boolean isAlpha(Character character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }

    // Return true if character can be stored (numbers, (, -)
    public boolean isStoreable(Character character) {
        if (character == '-' || character == '(' || (character >= '0' && character <= '9')) {
            // perform more checks here
            return true;
        }
        return false;
    }

    public String doOperation(String formula, String operator) {
        String symbols = "()÷×−+";
        String firstValue = "";
        String secondValue = "";
        int startIdx = -1;
        int endIdx = -1;

        if (formula.contains(operator)) {
            int divIdx = -1;

            // Locate the first division sign
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == operator.charAt(0)) {
                    divIdx = i;
                    break;
                }
            }

            // Work backwards from the division symbol; prepend characters
            // preceding the symbol to firstValue until another symbol is
            // found or the start of the formula is reached
            for (int i = divIdx - 1; i >= 0; i--) {
                startIdx = i;
                String currentCharacter = Character.toString(formula.charAt(i));
                if (symbols.contains(currentCharacter)) {
                    startIdx++;
                    break;
                }
                firstValue = currentCharacter + firstValue;
            }
            //
            for (int i = divIdx + 1; i < formula.length(); i++) {
                endIdx = i + 1;
                String currentCharacter = Character.toString(formula.charAt(i));
                if (symbols.contains(currentCharacter)) {
                    endIdx--;
                    break;
                }
                secondValue += currentCharacter;
            }
        }
        else return Float.toString((float)(0.0/0.0));

        float result = 0;

        if (operator.equals("÷")) result = Float.parseFloat(firstValue) / Float.parseFloat(secondValue);
        else if (operator.equals("×")) result = Float.parseFloat(firstValue) * Float.parseFloat(secondValue);
        else if (operator.equals("−")) result = Float.parseFloat(firstValue) - Float.parseFloat(secondValue);
        else if (operator.equals("+")) result = Float.parseFloat(firstValue) + Float.parseFloat(secondValue);

        //String e = "FIRST: [" + firstValue + "], Second: [" + secondValue + "], Result: [" + result + "]";
        String e = formula.substring(0, startIdx) + result + formula.substring(endIdx);

        screen.setText(e);

        return e;
    }
    public void evaluateFormula(String formula) {
        String newFormula =  formula;
        String innerFormula = formula;
        while(newFormula.matches(".*[()÷×−+].*")) {
            if ((Character.toString(newFormula.charAt(0))).matches(".*[÷×−+].*") || (Character.toString(newFormula.charAt(newFormula.length() - 1))).matches(".*[÷×−+].*")) {
                screen.setText("Error: syntax error");
                return;
            }

            int openIdx = -1;
            int closedIdx = -1;
            // Get the index of the last ( and the first ) after that. This
            // locates the innermost operation
            if (newFormula.contains("(") || newFormula.contains(")")) {
                for (int i = 0; i < newFormula.length(); i++) {
                    if (newFormula.charAt(i) == '(') openIdx = i;
                }
                if (openIdx < 0) {
                    screen.setText("Error: missing opening bracket");
                    return;
                }

                for (int i = openIdx; i < newFormula.length(); i++) {
                    if (newFormula.charAt(i) == ')') {
                        closedIdx = i;
                        break;
                    }
                }
                if (closedIdx < 0) {
                    screen.setText("Error: missing closing bracket");
                    return;
                }

                innerFormula = newFormula.substring(openIdx + 1, closedIdx);
            }

            while (innerFormula.contains("÷")) innerFormula = doOperation(innerFormula, "÷");
            while (innerFormula.contains("×")) innerFormula = doOperation(innerFormula, "×");
            while (innerFormula.contains("−")) innerFormula = doOperation(innerFormula, "−");
            while (innerFormula.contains("+")) innerFormula = doOperation(innerFormula, "+");

            //String e = "";
            if (openIdx > -1 && closedIdx > 0) {
                screen.setText(newFormula.substring(0, openIdx) + innerFormula + newFormula.substring(closedIdx + 1));
            } else screen.setText(innerFormula);

            newFormula = screen.getText().toString();
            innerFormula = newFormula;
        }
        return;
    }

    // Clear the screen (only used for buttonClear's onClick)
    public void clearScreen(View button) {
        screen.setText("");
    }

    // When a button is pressed, add the character from the button to the
    // end of the current expression (screen)
    // Example: pressing button 7 adds a '7' to the screen
    public void appendCharacterToScreen(View button) {
        String currentText = screen.getText().toString();

        int textLength = currentText.length();

        if (textLength > 0) {
            // Clear the screen if currentText is alphabetic (i.e. an error or storage message)
            if (isAlpha(currentText.charAt(0))) {
                currentText = "";
            }
        }

        currentText += ((TextView) button).getText().toString();
        screen.setText(currentText);
    }

    // Delete the last character from the screen
    public void deleteLastCharacter(View button) {
        String currentText = screen.getText().toString();

        int textLength = currentText.length();
        if (textLength > 0) {
            // Clear the screen if currentText is alphabetic (i.e. an error or storage message)
            if (isAlpha(currentText.charAt(0))) {
                screen.setText("");
                return;
            }
            screen.setText(currentText.substring(0, textLength - 1));
        }
    }

    //
    public void storeValue(View button) {
        String currentText = screen.getText().toString();
        evaluateFormula(currentText);
        currentText = screen.getText().toString();
        if (currentText.length() > 0) {
            // Clear the screen if currentText is alphabetic (i.e. an error or storage message)
            if (!isStoreable(currentText.charAt(0))) {
                screen.setText("Error: result could not be stored");
                return;
            }
            //storedValue = evaluateFormula(currentText).toString();
            storedValue = currentText;
            screen.setText("Value (" + storedValue + ") stored");
            return;
        }

        screen.setText("Error: cannot store");
    }

    //
    public void recallValue(View button) {
        if (storedValue != null) {
            String currentText = screen.getText().toString();

            int textLength = currentText.length();

            if (textLength > 0) {
                // Clear the screen if currentText is alphabetic (i.e. an error or storage message)
                if (isAlpha(currentText.charAt(0))) {
                    currentText = "";
                }
            }

            currentText += storedValue;
            screen.setText(currentText);
        }
    }

    //
    public void solveFormula(View button) {
        String currentText = screen.getText().toString();
        evaluateFormula(currentText);
    }
}