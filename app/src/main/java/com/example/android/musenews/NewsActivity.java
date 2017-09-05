package com.example.android.musenews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MusicNews>> {

    //URL to retrieve the Guardian API.
    private static String GUARDIAN_API = "http://content.guardianapis.com/search?q=music&api-key=28ae7970-8272-4b29-9720-a304ce7a3de2&show-fields=all";

    //Constant value for the earthquake loader ID.
    private static final int MUSICNEWS_LOADER_ID = 1;

    private MusicNewsAdapter musicNewsAdapter;
    private ArrayList<MusicNews> musicNews;
    private RecyclerView.LayoutManager recycleManager;

    @BindView(R.id.recycleview_item) RecyclerView musicNewsView;
    @BindView(R.id.list_empty) TextView emptyState;
    @BindView(R.id.progress_indicator) View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //initialise everything needed.
        musicNews = new ArrayList<>();
        recycleManager = new LinearLayoutManager(this);
        ButterKnife.bind(this);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(MUSICNEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyState.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<ArrayList<MusicNews>> onCreateLoader(int i, Bundle bundle) {

        return new MusicNewsLoader(this, GUARDIAN_API);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MusicNews>> loader, ArrayList<MusicNews> musicNews) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        if (musicNews.isEmpty()) {
            // Set empty state text to display "No music news found."
            emptyState.setText(R.string.no_news);
        } else {
            emptyState.setVisibility(View.GONE);
        }

        // Create a new adapter that takes an empty list of earthquakes as input
        musicNewsAdapter = new MusicNewsAdapter(this, R.layout.news_listitem, musicNews);

        musicNewsView.setLayoutManager(recycleManager);
        // Set the adapter on the {@link ListView} so the list can be populated in the user interface
        musicNewsView.setAdapter(musicNewsAdapter);

        // Clear the adapter of previous earthquake data
        musicNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MusicNews>> loader) {
        // Loader reset, so we can clear out our existing data.
        musicNews.clear();
        musicNewsAdapter.notifyDataSetChanged();
    }
}