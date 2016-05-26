package com.example.zyx.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Xu Wang on 5/26/2016.
 */
public class NetworkSender {

    private String ipaddr;

    private Activity activity;

    private ArrayList<String> querys;

    public NetworkSender(String ip_addr, Activity activity)
    {
        this.ipaddr = ip_addr;
        this.activity = activity;
        ConnectivityManager connMgr = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("network is connected.");
        } else{
            System.out.println("network not connect.");
        }
    }

    //start the background thread.
    public void StartWorking()
    {
        new NetworkSenderTask().execute();
    }

    //query example: a=b&c=d
    public void AddQuery(String query)
    {

        querys.clear();
        querys.add(query);
    }

    private class NetworkSenderTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                while(true)
                {
                    if(querys.size() > 0)
                    {
                        String query = querys.get(0);
                        querys.clear();
                        executeQuery("http://" + ipaddr+ "?" + query);
                    }
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }

        private void executeQuery(String myurl) throws IOException {
            int len = 500;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
            } finally {

            }
        }
    }





}
