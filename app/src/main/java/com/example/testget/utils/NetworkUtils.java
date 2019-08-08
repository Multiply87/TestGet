package com.example.testget.utils;

import android.content.res.Resources;

import com.example.testget.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    private static final String login = "login";
    private static final String password = "password";

    public static URL generateURL(String url) {
        URL generatedURL = null;
        try {
            generatedURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return generatedURL;
    }

    public static String getResponseFromURL(HttpURLConnection urlConnection) throws IOException {


        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch (UnknownHostException ex){
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }

    public static HttpURLConnection setRequest(URL generatedURL, String login, String password) throws IOException {

        JSONObject data = new JSONObject();
        try {
            data.accumulate("login", login);
            data.accumulate("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        HttpURLConnection urlConnection = (HttpURLConnection) generatedURL.openConnection();
        urlConnection.setRequestMethod("POST");

        BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

        writer.write(String.valueOf(data));

        writer.flush();

        writer.close();

        out.close();

        urlConnection.connect();
        return urlConnection;

    }



}
