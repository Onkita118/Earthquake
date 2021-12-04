package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

   public  class MainActivity extends AppCompatActivity {
       private EarthAdapter mAdapter;
       private static final int EARTHQUAKE_LOADER_ID = 1;


       /**
        * URL for earthquake data from the dataset
        */
       private static final String USGS_REQUEST_URL =
               "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=20";


       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);


           ListView earthquakeListView = (ListView) findViewById(R.id.list);

           // Create a new adapter that takes an empty list of earthquakes as input
           mAdapter = new EarthAdapter(this, new ArrayList<earthQuake>());

           // Set the adapter on the {@link ListView}
           // so the list can be populated in the user interface
           earthquakeListView.setAdapter(mAdapter);

           // Set an item click listener on the ListView, which sends an intent to a web browser
           // to open a website with more information about the selected earthquake.
           earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                   // Find the current earthquake that was clicked on
                   earthQuake currentEarthquake = mAdapter.getItem(position);

                   // Convert the String URL into a URI object (to pass into the Intent constructor)
                   Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                   // Create a new intent to view the earthquake URI
                   Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                   // Send the intent to launch a new activity
                   startActivity(websiteIntent);
               }
           });
           // Start the AsyncTask to fetch the earthquake data
           EarthquakeAsyncTask task = new EarthquakeAsyncTask();
           task.execute(USGS_REQUEST_URL);

       }

       private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<earthQuake>> {

           /**
            * This method runs on a background thread and performs the network request.
            * We should not update the UI from a background thread, so we return a list of
            * {@link earthQuake}s as the result.
            */
           @Override
           protected List<earthQuake> doInBackground(String... urls) {
               // Don't perform the request if there are no URLs, or the first URL is null
               if (urls.length < 1 || urls[0] == null) {
                   return null;
               }

               List<earthQuake> result = QueryUtils.fetchEarthquakeData(urls[0]);
               return result;
           }

           /**
            * This method runs on the main UI thread after the background work has been
            * completed. This method receives as input, the return value from the doInBackground()
            * method. First we clear out the adapter, to get rid of earthquake data from a previous
            * query to USGS. Then we update the adapter with the new list of earthquakes,
            * which will trigger the ListView to re-populate its list items.
            */
           @Override
           protected void onPostExecute(List<earthQuake> data) {
               // Clear the adapter of previous earthquake data
               mAdapter.clear();

               // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
               // data set. This will trigger the ListView to update.
               if (data != null && !data.isEmpty()) {
                   mAdapter.addAll(data);
               }
           }
       }


   }
