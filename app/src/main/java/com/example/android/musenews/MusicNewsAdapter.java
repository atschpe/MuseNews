package com.example.android.musenews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * {@link MusicNewsAdapter} is a {@link RecyclerView.Adapter} which uses the {@link MusicNewsHolder}
 * to display the information in the layout of the {@link MusicNews} in question.
 */

public class MusicNewsAdapter extends RecyclerView.Adapter<MusicNewsHolder> {

    private final ArrayList<MusicNews> musicNews;
    private int resourceId;
    private Context ctxt;

    /**
     * Create a new {@link MusicNewsAdapter)
     *
     * @param context is the activity within the adapter is called
     * @param musicNews is the list containing the items
     */
    public static String LOG_TAG = MusicNewsAdapter.class.getName();

    public MusicNewsAdapter(Context context, int resourceId, ArrayList<MusicNews> musicNews) {
        this.ctxt = context;
        this.resourceId = resourceId;
        this.musicNews = musicNews;
    }

    @Override
    public MusicNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
        return new MusicNewsHolder(this.ctxt, view);
    }

    @Override
    public void onBindViewHolder(MusicNewsHolder holder, int position) {
        MusicNews musicnews = this.musicNews.get(position);
        holder.bindMusicNews(musicnews);
    }

    @Override
    public int getItemCount() {
        return this.musicNews.size();
    }

    protected static class LoadMusicNewsImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public LoadMusicNewsImage(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}