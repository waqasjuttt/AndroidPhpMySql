package com.example.waqasjutt.androidphpmysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername,tvUserEmail;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        tvUsername=(TextView)findViewById(R.id.tvUsername);
        tvUserEmail=(TextView)findViewById(R.id.tvUserEmail);
        btnLogout=(Button)findViewById(R.id.btnLogout);

        tvUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        tvUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(ProfileActivity.this).logout();
                finish();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.menuSetting:
                Toast.makeText(this,"You clicked settings",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    int i = 0;
    @Override
    public void onBackPressed() {
        i = (i + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (i>1) {
            finishAffinity();
        }
    }
}
