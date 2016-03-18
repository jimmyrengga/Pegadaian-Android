package com.pegadaian.aplikasi.cobalagi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pegadaian.aplikasi.cobalagi.constant.Constant;
import com.pegadaian.aplikasi.cobalagi.service.HttpService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends ActivityHelper {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        final Button btn = (Button) findViewById(R.id.btn);

        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                hideKeyboard();

                String username = usernameWrapper.getEditText().getText().toString();
                String password = usernameWrapper.getEditText().getText().toString();
                if (!validateEmail(username)) {
                    usernameWrapper.setError("Not a valid email address!");
                } else if (!validatePassword(password)) {

                    passwordWrapper.setError("Not a valid password!");
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    new Login(username, password).execute();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Constant.PREFS, MODE_PRIVATE);
        if(sp.contains(Constant.KEY_USER)){
            goToDashboard("logged");
        }
    }

    public void goToDashboard(String msg) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", msg);
        startActivity(intent);
    }

    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {


            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class Login extends AsyncTask<Void, Void, ResponseEntity>{

        private String username, password;

        public Login (String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog("Checking user, please wait ....");
        }

        @Override
        protected ResponseEntity doInBackground(Void... params) {
            try {
                return service.login(username, password);
            } catch (HttpStatusCodeException e){
                if(e.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
                    Log.i("500", e.getResponseBodyAsString());
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String,Object> map = null;
                    String msg = "";
                    try {
                        map = mapper.readValue(e.getResponseBodyAsString(), Map.class);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    msg = (String) map.get("message");
                    return new ResponseEntity(msg, e.getStatusCode());
                } else {
                    return new ResponseEntity(e.getStatusCode().getReasonPhrase(), e.getStatusCode());
                }
            } catch (ResourceAccessException e){
                Log.i("LOGIN ERROR", e.getLocalizedMessage());
                return null;
            } catch (Exception e){
                Log.i("LOGIN ERROR", e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity responseEntity) {
            super.onPostExecute(responseEntity);
            dismissLoadingDialog();
            if(responseEntity != null) {
                if(responseEntity.getStatusCode().equals(HttpStatus.FOUND)) {
                    if(!responseEntity.getHeaders().getFirst("Set-Cookie").isEmpty()) {
                        HttpHeaders responseHeaders = responseEntity.getHeaders();
                        String jsession = responseHeaders.getFirst("Set-Cookie");
                        jsession = jsession.substring(0, jsession.indexOf(";") + 1);
                        Log.i("Cookies : ", jsession);
                        goToDashboard("login");
                    } else {
                        showMessageDialog("Tidak mendapatkan cookie dari server");
                    }
                } else {
                    showMessageDialog((String) responseEntity.getBody());
                }
            } else {
                showMessageDialog("Tidak mendapat balasan dari server");
            }
        }
    }

}
