package com.example.app;

/**
 * Created by Christopher Lee on 2/18/14.
 */

        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Intent;
        import android.view.*;
        import android.widget.TextView;

public class SuccessActivity extends Activity {

    private TextView success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String u = intent.getStringExtra(MainActivity.USER);
        int i = intent.getIntExtra(MainActivity.COUNT, 0);
        String s_msg = String.format("Welcome %s.\n  You have logged in %d times", u, i);
        setContentView(R.layout.s_activity);
        success = (TextView) findViewById(R.id.s_msg);
        success.setText(s_msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.l_success, menu);
        return true;
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
