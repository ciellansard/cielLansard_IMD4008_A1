package com.example.ciellansard_imd4008_a1;

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
        return 0; // Detail this later
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
}