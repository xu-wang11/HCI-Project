package com.example.zyx.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zyx.position.ActivityInterface;

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

    private int port;

    private Activity activity;

    private ArrayList<String> querys = new ArrayList<String>();

    public NetworkSender(String ip_addr, int _port,  Activity activity)
    {
        this.ipaddr = ip_addr;
        this.activity = activity;
        this.port = _port;
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
        Runnable connectRunnable = new Runnable() {
            public void run()
            {
                while(true){
                    if(querys.size() > 0) {
                        String r = querys.get(querys.size() - 1);
                        querys.clear();
                        try {
                            executeQuery(r);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        };
        //handler.post(connectRunnable);
        new Thread(connectRunnable).start();
    }

    //query example: a=b&c=d
    public void AddQuery(String action, String query)
    {

        //querys.clear();
        querys.add("http://" + ipaddr + ":8888/" + action + "?" + query);
    }

    private void executeQuery(String myurl) throws IOException {
        int len = 500;
        try {
            System.out.println("execute query:" + myurl);
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000 /* milliseconds */);
            conn.setConnectTimeout(3000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            //if view implement the interface
            int response = conn.getResponseCode();
            InputStream is = null;
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            System.out.println(contentAsString);
            if(contentAsString.charAt(0) - '0' != ((ActivityInterface)activity).GetMode())
            {
                ((ActivityInterface) activity).ModeChange(contentAsString.charAt(0) - '0');
            }

        } finally {
            System.out.println("execute query end.");

        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }







}
