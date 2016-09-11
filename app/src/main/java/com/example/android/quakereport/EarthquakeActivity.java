/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_QUERY_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=10";

    private EarthquakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(findViewById(R.id.text_empty_list));

        // Create a new {@link ArrayAdapter} of earthquakes

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake earthquake = mAdapter.getItem(i);
                Uri uri = Uri.parse(earthquake.getUrl());
                Intent openWebsite = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openWebsite);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            ProgressBar pbLoading = (ProgressBar) findViewById(R.id.loading_spinner);
            pbLoading.setVisibility(View.GONE);
            TextView tvEmptyList = (TextView) findViewById(R.id.text_empty_list);
            tvEmptyList.setText(getText(R.string.no_internet));
        }


    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this, USGS_QUERY_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        TextView tvEmptyList = (TextView) findViewById(R.id.text_empty_list);
        tvEmptyList.setText(getText(R.string.no_earthquake));
        ProgressBar pbLoading = (ProgressBar) findViewById(R.id.loading_spinner);
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }

    private static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        private String mUrl;

        public EarthquakeLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        public List<Earthquake> loadInBackground() {
            if (mUrl == null) {
                return null;
            }
            List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeList(mUrl);
            return earthquakes;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

    }


}
