package com.example.loginlayout;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection extends AsyncTask<String, String, JSONObject> {

    private Interfaz inter;

    public Connection (Interfaz inter){
        this.inter = inter;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        JSONObject result = new JSONObject();
        URL url = null;
        String Method = null;
        String datos_env = null;
        try {
            url = new URL(strings[0]);
            Method = strings[1];
            if (strings[2] != null) datos_env = strings[2];
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(Method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            if (datos_env != null) {
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(datos_env);
                wr.flush();
            }

            Integer code = connection.getResponseCode();

            if (code == 200) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String resp;

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder res = new StringBuilder();
                String output;
                try {
                    while ((output = br.readLine()) != null) {
                        res.append(output);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resp = res.toString();
                if (resp.length() != 0) {
                    Object json = new JSONTokener(resp).nextValue();
                    if (json instanceof JSONArray) {
                        JSONArray array = (JSONArray) json;
                        result.accumulate("array", array);
                    }
                    else if (json instanceof JSONObject) {
                        result = (JSONObject) json;
                    }
                }
            }
            result.put("codigo",code);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    @Override

    public void onPostExecute(JSONObject Result) {
        this.inter.Respuesta(Result);
    }
}
