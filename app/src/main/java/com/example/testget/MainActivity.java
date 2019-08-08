package com.example.testget;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testget.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.testget.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private static EditText login;
    private static EditText password;
    private Button logInButton;
    private static TextView result;
    private static TextView errorMessage;
    private static ProgressBar loadingIndicator;

    private static void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private static void showErrorTextView(String errorText) {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(errorText);
    }


    public class gScriptGet extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;

            try {
                response = getResponseFromURL(NetworkUtils.setRequest(urls[0], login.getText().toString(), password.getText().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Boolean access = null;
            String resultText = null;
            String nowTime = null;

            if (response != null && response != "") {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    access = jsonResponse.getBoolean("access");
                    resultText = jsonResponse.getString("resultText");
                    nowTime = jsonResponse.getString("nowTime");
                    /*Log.i("MyLogs", access.toString());
                    Log.i("MyLogs", resultText);
                    Log.i("MyLogs", nowTime);*/

                } catch (JSONException e) {
                    e.printStackTrace();

                }
                //result.setText(response);
                System.out.println(response);
                if (access == false || resultText == null || nowTime == null) {
                    showErrorTextView(resultText);
                } else {
                    //result.setText("NOW: " + nowTime + "\naccess: " + access + "\nresultText: " + resultText);
                    nextActivity("NOW: " + nowTime + "\naccess: " + access + "\nresultText: " + resultText);
                    //showResultTextView();
                }
            } else {
                showErrorTextView(resultText);
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        login = findViewById(R.id.et_login);
        password = findViewById(R.id.et_password);
        EditText[] edList = {login, password};
        logInButton = findViewById(R.id.logIn);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);
        logInButton.setEnabled(false);




        CustomTextWatcher textWatcher = new CustomTextWatcher(edList, logInButton);
        for (EditText editText : edList) editText.addTextChangedListener(textWatcher);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                errorMessage.setText("");
                URL generatedUrl = NetworkUtils.generateURL(getString(R.string.url));
                if (generatedUrl != null)
                    new gScriptGet().execute(generatedUrl);
                else
                    Toast.makeText(MainActivity.this, "URL null!", Toast.LENGTH_SHORT).show();

            }
        };

        logInButton.setOnClickListener(onClickListener);
        SetOnForusChangeListener onForusChangeListener = new SetOnForusChangeListener();
        login.setOnFocusChangeListener(onForusChangeListener);
        password.setOnFocusChangeListener(onForusChangeListener);
    }

    public void hideKeyboard(View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public class SetOnForusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }

    public void nextActivity(String response){
        String value="Hello world";
        Intent i = new Intent(MainActivity.this, LoginnedActivity.class);
        i.putExtra("data", response);
        startActivity(i);
    }
}
