package com.example.android.musenews;

// A class of variables to retrieve information on news articles about music from the API.

public class MusicNews {

    //Title image of the article.
    private String mNewsImage;

    //Title of the article.
    private String mNewsTitle;

    //Author of the article.
    private String mNewsAuthor;

    //Date of publishing.
    private String mNewsDate;

    //Author of the article.
    private String mNewsSection;

    private String mNewsExtract;

    //URl of the article.
    private String mNewsUrl;

    /**
     * Create an object for {@link MusicNews) with
     *
     * @param newsImage   is the title image of the article.
     * @param newsTitle   is the title of the article.
     * @param newsDate    is the date the article was published.
     * @param newsAuthor  is the author of the article.
     * @param newsExtract is the first paragrpah of the article.
     * @param newsUrl     is the url to view the full article.
     */

    public MusicNews(String newsImage, String newsTitle, String newsAuthor, String newsDate,
                     String newsSection, String newsExtract, String newsUrl) {
        mNewsImage = newsImage;
        mNewsTitle = newsTitle;
        mNewsAuthor = newsAuthor;
        mNewsDate = newsDate;
        mNewsSection = newsSection;
        mNewsExtract = newsExtract;
        mNewsUrl = newsUrl;
    }

    public String getNewsImage() {
        return mNewsImage;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsAuthor() {
        return mNewsAuthor;
    }

    public String getNewsDate() {
        return mNewsDate;
    }

    public String getNewsSection() {
        return mNewsSection;
    }

    public String getNewsExtract() {
        return mNewsExtract;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }
}