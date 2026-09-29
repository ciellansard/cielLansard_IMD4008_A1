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

    // When a button is pressed, add the character from the button to the
    // end of the current expression (screen)
    // Example: pressing button 7 adds a '7' to the screen
    public void appendCharacterToScreen(View button) {
        String currentText = screen.getText().toString();
        currentText += ((TextView) button).getText().toString();
        screen.setText(currentText);
    }
}