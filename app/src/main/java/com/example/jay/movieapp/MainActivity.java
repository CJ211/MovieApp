package com.example.jay.movieapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.jay.movieapp.R.id.lv;

public class MainActivity extends AppCompatActivity {

    TextView textView1;
    private String TAG = MainActivity.class.getSimpleName();
    ListView mList;
    ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 =(TextView) findViewById(R.id.name);
         movieList = new ArrayList<>();
         mList = (ListView) findViewById(lv);

        new GetMovie().execute();
    }

    private class GetMovie extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            // String url
            String url = "http://api.themoviedb.org/3/tv/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON movie node
                    JSONArray movies = jsonObj.getJSONArray("movies");

                    // looping through All weathers
                    for (int i = 0; i < movies.length(); i++) {

                        JSONObject m = movies.getJSONObject(i);
                        String name = m.getString("name");
                        String vote_count = m.getString("vote_count");
                        String id = m.getString("id");

                        // tmp hash map for single contact
                        HashMap<String, String> movie = new HashMap<>();

                        // adding each child node to HashMap key => value
                        movie.put("name :", name);
                        movie.put("vote_count :", vote_count);
                        movie.put("id :", id);

                        // adding movies to movie list
                        movieList.add(movie);


                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, movieList,
                    R.layout.list_row, new String[]{"name", "vote_count","id"}, new int[]{R.id.name,R.id.Votes, R.id.id});

            mList.setAdapter(adapter);   }
    }
}