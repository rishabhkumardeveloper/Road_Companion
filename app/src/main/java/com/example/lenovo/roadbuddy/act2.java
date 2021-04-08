package com.example.lenovo.roadbuddy;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class act2 extends FragmentActivity {
    TextView tv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banateh);
        tv1=findViewById(R.id.asd);
//getdata2();
    }
    public void getdata(){
        URL url = null;
        Toast.makeText(this, "Exec", Toast.LENGTH_SHORT).show();
        try {
url = new URL("http://www.opencellid.org/cell/getInArea?key=be03723dca638c&BBOX=12.978,77.656,12.979,77.658&limit=30&format=json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            tv1.setText( ""+response);
            int responseCode = con.getResponseCode();
            Toast.makeText(this, ""+responseCode, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "data", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getdata2(){ thread.start();
    }
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                   /////////////////

                    URL url = null;
                    Toast.makeText(getApplicationContext(), "Exec", Toast.LENGTH_SHORT).show();
                    try {
                        url = new URL("http://www.opencellid.org/cell/getInArea?key=be03723dca638c&BBOX=12.978,77.656,12.979,77.658&limit=30&format=json");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        tv1.setText( ""+response);
                        int responseCode = con.getResponseCode();
                        Toast.makeText(getApplicationContext(), ""+responseCode, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "data", Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    /////////////////

                    //Your code goes here
                } catch (Exception e) {
                    e.printStackTrace();
                   tv1.setText(""+e.toString());
                }
            }
        });


}
