package com.example.waqasjutt.androidphpmysql;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static View view;
    private static CheckBox show_hide_password;
    private static Animation shakeAnimation;
    private static LinearLayout loginLayout;

    @Bind(R.id.tvCreateNewAccount) TextView tvCreateNewAccount;
    @Bind(R.id.editTextUsername) EditText username;
    @Bind(R.id.editTextPassword) EditText password;
    @Bind(R.id.btnLogin) Button btnLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        btnLogin = (Button) findViewById(R.id.btnLogin);

        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        btnLogin.setOnClickListener(this);
        tvCreateNewAccount.setOnClickListener(this);

        show_hide_password.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);
                            // change checkbox text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);
                            // change checkbox text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    private void userLogin() {
        final String name = username.getText().toString();
        final String pass = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(name);

        // Check for both field is empty or not
        if (name.equals("") || name.length() == 0 || name.isEmpty()
                || pass.equals("") || pass.length() == 0 || pass.isEmpty()) {
            Toast.makeText(this,"Enter both credentials.",
                    Toast.LENGTH_SHORT).show();
        }
        // Check if email id is valid or not
        if (/*!m.find()*/    name.isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.
                        matcher(name).matches()) {
            username.setError("enter a valid email address");
//            Toast.makeText(this,"Your Email Id is Invalid.",
//                    Toast.LENGTH_SHORT).show();
        }
        else {
            username.setError(null);
        }
        // Else do login and do your stuff
        if (pass.isEmpty() || pass.length() <= 3) {
            password.setError("Password should be " +
                    "greater than or equal to 4");
//            Toast.makeText(this,"Password should be greater than or equal to 4",
//                    Toast.LENGTH_SHORT).show();
        }
        else{
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .userLogin(
                                                    obj.getInt("id"),
                                                    obj.getString("username"),
                                                    obj.getString("email")
                                            );
                                    startActivity(new Intent(getApplicationContext(),
                                            ProfileActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            obj.getString("message"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("username", name);
                    param.put("password", pass);
                    return param;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            userLogin();
        }
        if (view == tvCreateNewAccount) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

//    int i = 0;
//    @Override
//    public void onBackPressed() {
//        i = (i + 1);
//        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//
//        if (i>1) {
//            finishAffinity();
//        }
//    }
}
