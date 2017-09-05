package com.example.android.musenews;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MusicNewsHolder} extends {@link RecyclerView.ViewHolder} enabling the {@link RecyclerView}
 * to display the information pertaining to the {@link MusicNews} in question.
 */

public class MusicNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //view to be populated.
    @BindView(R.id.newsTitle) TextView newsTitle;
    @BindView(R.id.newsAuthor) TextView newsAuthor;
    @BindView(R.id.newsExtract) TextView extract;
    @BindView(R.id.newsDate) TextView newsDate;
    @BindView(R.id.newsSection) TextView newsSection;

    private MusicNews musicNews;
    private Context ctxt;

    public static String LOG_TAG = MusicNewsAdapter.class.getName();

    public MusicNewsHolder(Context context, View itemView) {
        super(itemView);

        this.ctxt = context;

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    public void bindMusicNews(MusicNews musicNews) {
        this.musicNews = musicNews;

        this.newsTitle.setText(musicNews.getNewsTitle()); //set title

        if (musicNews.getNewsAuthor().equals("XXX")) { // set author
            newsAuthor.setVisibility(View.GONE);
        } else {
            String concatenateAuthor = ctxt.getString(R.string.by) + musicNews.getNewsAuthor();
            this.newsAuthor.setText(concatenateAuthor);
        }

        this.extract.setText(musicNews.getNewsExtract()); // set extract

        //// formate and set date - Input format is: YYYY-MM-DDTHH:MM:SSZ
        String inputDate = musicNews.getNewsDate();
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'");
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");//'a' for AM/PM

        Date date = null;
        try {
            date = sourceFormat.parse(inputDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "parsing inputDate: " + inputDate, e);
        }
        String formattedDate = DesiredFormat.format(date.getTime()).substring(0, 10);
        String formattedTime = DesiredFormat.format(date.getTime()).substring(10);

        String concatenateDate = ctxt.getString(R.string.posted) + formattedDate +
                ctxt.getString(R.string.at) + formattedTime;
        newsDate.setText(concatenateDate);

        newsSection.setText(musicNews.getNewsSection());

        //Retrieve the image for the Music News item and set it to its ImageView.
        new MusicNewsAdapter.LoadMusicNewsImage((ImageView) itemView.findViewById(R.id.newsImage))
                .execute(musicNews.getNewsImage());
    }

    @Override
    public void onClick(View v) {

        Intent goToUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(musicNews.getNewsUrl()));
        ctxt.startActivity(goToUrl);
    }
}