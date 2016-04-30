package com.example.zyx.position;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = ((TextView)findViewById(R.id.editText)).getText().toString();
                String port = ((TextView)findViewById(R.id.editText2)).getText().toString();
                String IPADDRESS_PATTERN =
                        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
                if(ip.matches(IPADDRESS_PATTERN)&& port.matches("^\\d+$"))
                {
                    Intent intent = new Intent(MainActivity.this,
                            GetPosition.class);
                    intent.putExtra("ip", ip);
                    intent.putExtra("port", port);
                    startActivity(intent);
                }


            }
        });



    }

}
