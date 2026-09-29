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

    public float evaluateFormula(String formula) {
        String symbols = "()÷×−+";
        String newFormula =  formula;
        String innerFormula = formula;

        // Get the index of the last ( and the first ) after that. This
        // locates the innermost operation
        if (newFormula.contains("(")) {
            int openIdx = -1;
            int closedIdx = -1;
            for (int i = 0; i < newFormula.length(); i++) {
                if (newFormula.charAt(i) == '(') openIdx = i;
            }
            for (int i = openIdx; i < newFormula.length(); i++) {
                if (newFormula.charAt(i) == ')') {
                    closedIdx = i;
                    break;
                }
            }

            if (openIdx < 0 || closedIdx < 0) {
                return (float)(Double.NaN);
            }

            innerFormula = newFormula.substring(openIdx + 1, closedIdx);
            //screen.setText(innerFormula);
        }

        if (innerFormula.contains("÷")) {
            String firstValue = "";
            String secondValue = "";
            int divIdx = -1;

            // Locate the first division sign
            for (int i = 0; i < innerFormula.length(); i++) {
                if (innerFormula.charAt(i) == '÷') {
                    divIdx = i;
                    break;
                }
            }

            // Work backwards from the division symbol; prepend characters
            // preceding the symbol to firstValue until another symbol is
            // found or the start of the formula is reached
            for (int i = divIdx; i >= 0; i--) {
                String currentCharacter = Character.toString(innerFormula.charAt(i));
                if (symbols.contains(currentCharacter)) {
                    break;
                }
                firstValue = currentCharacter + firstValue;
            }
            //
            for (int i = divIdx; i < innerFormula.length(); i++) {
                String currentCharacter = Character.toString(innerFormula.charAt(i));
                if (symbols.contains(currentCharacter)) {
                    break;
                }
                secondValue += currentCharacter;
            }
        }





        return 0;
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

        if (currentText.length() > 0) {
            // Clear the screen if currentText is alphabetic (i.e. an error or storage message)
            if (!isStoreable(currentText.charAt(0))) {
                screen.setText("Error: cannot store");
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

    //
    public void solveFormula(View button) {
        String currentText = screen.getText().toString();
        float solution = evaluateFormula(currentText);

        //if (isNaN(solution)) screen.setText("Error: syntax error");
        //else screen.setText(Float.toString(solution));
    }
}