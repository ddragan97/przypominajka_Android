package pl.wsiz.przypominajka;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isUserCreated())
        {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),HelloActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        else
        {
            Button create = findViewById(R.id.mainButton);
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText userName = findViewById(R.id.mainNazwaU2);

                    if (userName.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Uzupełnij nazwę użytkownika", Toast.LENGTH_LONG).show();
                    }
                    else if (userName.getText().toString().length() > 20) {
                        Toast.makeText(getApplicationContext(), "Zbyt długa nazwa użytkownika (max 20 znaków)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        SharedPreferences sharedPrefs = getSharedPreferences("sharedName",0);
                        sharedPrefs.edit().putString("userName", userName.getText().toString()).apply();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), HelloActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
            });
        }
    }

    protected boolean isUserCreated() {
        SharedPreferences sharedPrefs = getSharedPreferences("sharedName",0);
        String name = sharedPrefs.getString("userName","");
        return name.length() != 0;
    }
}
