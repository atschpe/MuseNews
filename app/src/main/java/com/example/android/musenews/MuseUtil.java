package com.example.android.musenews;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

// Helper method to communicate with the API.
public class MuseUtil {

    //Tag for the log messages
    private static final String LOG_TAG = MuseUtil.class.getSimpleName();

    private MuseUtil() {
    }

    /**
     * Query the API and return a list of {@link MusicNews} objects.
     */
    public static ArrayList<MusicNews> fetchMusicNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link MusicNews}s
        ArrayList<MusicNews> musicNewsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link MusicNews}s
        return musicNewsList;
    }

    /**
     * Returns new URL object from the given string URL.
     *
     * @param stringUrl is the url of the API
     * @return returns the object.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url is API to be accessed.
     * @return the json response.
     * @throws IOException catches any issues whilst querying the url.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the MusicListItem JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from the server.
     *
     * @param inputStream is the input to be handled
     * @return outputs the string for further use
     * @throws IOException handles possible issues.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link MusicNews} objects that has been built up from parsing the given JSON response.
     *
     * @param musicNewsJSON is the json to access the items in the API.
     * @return list of music news items.
     */
    private static ArrayList<MusicNews> extractFeatureFromJson(String musicNewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(musicNewsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding musicNewsItem to
        ArrayList<MusicNews> musicNewsList = new ArrayList<>();

        // Try to parse the JSON response string. Problems are caught by the JSONException object.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(musicNewsJSON);

            // Extract the JSONArray associated with the key called "response",
            // which represents a list of features (or musicNewsList).
            JSONObject musicNewsObject = baseJsonResponse.getJSONObject("response");
            JSONArray results = musicNewsObject.getJSONArray("results");

            // For each musicListItem in the musicNewsArray, create an {@link MusicNews} object
            for (int i = 0; i < results.length(); i++) {

                JSONObject currentMusicNews = results.getJSONObject(i); // Get item at position i
                JSONObject fields = currentMusicNews.getJSONObject("fields");//extract "fields" key
                String newsImage = fields.getString("thumbnail"); // Extract the image
                String newsTitle = currentMusicNews.getString("webTitle"); // extract the title

                String newsAuthor; // extract the author if available
                if (fields.has("byline")) {
                    newsAuthor = fields.getString("byline");
                } else {
                    newsAuthor = "XXX";
                }

                String newsDate = currentMusicNews.getString("webPublicationDate"); // extract date
                String newsSection = currentMusicNews.getString("sectionName"); // extract section
                String newsExtract = fields.getString("trailText"); // extract trail text.
                String newsUrl = currentMusicNews.getString("webUrl"); // extract url

                // Create new MusicNews object with the newsImage, NewsTitle, NewsDate, newsSection,
                // newsExtract, and newsUrl from the JSON response & add to musicNewsList.
                MusicNews musicNewsItem = new MusicNews(newsImage, newsTitle, newsAuthor, newsDate,
                        newsSection, newsExtract, newsUrl);
                musicNewsList.add(musicNewsItem);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the musicNewsList JSON results", e);
        }

        // Return the list of musicNewsList
        return musicNewsList;
    }
}