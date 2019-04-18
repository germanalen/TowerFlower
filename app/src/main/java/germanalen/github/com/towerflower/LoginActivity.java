package germanalen.github.com.towerflower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserData.clearData();
    }

    public void startMainActivity(View view) {
        EditText name = findViewById(R.id.editText);
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name should not be empty", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("TOWER_FLOWER", "name: " + name.getText());
            UserData.setUsername(name.getText().toString());

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}
