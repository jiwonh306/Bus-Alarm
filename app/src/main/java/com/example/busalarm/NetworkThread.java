package com.example.busalarm;

import android.content.res.AssetManager;
import android.util.Log;

import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkThread extends Thread{

    @Override
    public void run() {
        try {

            StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll");
            Log.e("MY_TEST","urlBuilder");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=C1xlYgGuqhzV%2ByBIrUZqZRpEVWsAcp36U%2Fp8W71wN18sSy%2FA3ooEhyrMm0SJu9uR56w0Tl4WQLK7df%2FsPvqenA%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode("100100016", "UTF-8"));
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            Log.e("BUS_API_TEST",sb.toString());
            rd.close();
            conn.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}