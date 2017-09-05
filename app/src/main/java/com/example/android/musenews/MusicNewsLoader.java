package com.example.android.musenews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Create {@link AsyncTaskLoader} to load {@link MusicNews} received from the API into the the app.
 */
public class MusicNewsLoader extends AsyncTaskLoader<ArrayList<MusicNews>> {
    /**
     * Construct a new {@link MusicNewsLoader}.
     *
     * @param context of the activity.
     * @param url     of the API:
     */
    private String mUrl;

    public MusicNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<MusicNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<MusicNews> musicNewsList = MuseUtil.fetchMusicNewsData(mUrl);
        return musicNewsList;
    }
}
