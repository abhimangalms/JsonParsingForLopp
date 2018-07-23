package com.abhimangalms.digital.jsonparsingforloop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView mtvJsonItem;
    Button mbtnHit;
    String line = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtvJsonItem = (TextView) findViewById(R.id.tvJsonItem);
        mbtnHit = (Button) findViewById(R.id.btnHit);


        mbtnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  new JsonTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
                new JsonTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");

            }
        });

    }

    public class JsonTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {

                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                while ((line = reader.readLine()) != null){

                    buffer.append(line);
                }

                //returning fetched data

                String finalJson = buffer.toString();

                //filtering the Objects -> Array -> Objects -> Value from keys
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");


                StringBuffer finalBufferedData = new StringBuffer();
                //Fetching all the objects in the array
                for (Integer i = 0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String movieName = finalObject.getString("movie");
                    Integer year = finalObject.getInt("year");

                    finalBufferedData.append(movieName + " - " + year + "\n");

                }

                //returning filtered data
                return finalBufferedData.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {

                    connection.disconnect();

                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //return NULL if failed to fetch data
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mtvJsonItem.setText(result);
        }
    }
}

