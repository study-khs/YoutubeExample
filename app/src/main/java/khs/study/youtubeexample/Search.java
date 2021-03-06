package khs.study.youtubeexample;

import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by jaeyoung on 2017. 4. 1..
 */

public class Search {
    private final static String TAG = "JYP/"+"Search";
    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Global instance of the max number of videos we want returned (50 = upper limit per page).
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Global instance of Youtube object to make all API requests.
     */
    private static YouTube youtube;

    private static final String apiKey = "AIzaSyAIXC2N6hn4djpAeZCWYYG7hKoXROH00tM";

    public static void search(String queryTerm) {

        try {
      /*
       * The YouTube object is used to make all API requests. The last argument is required, but
       * because we don't need anything initialized when the HttpRequest is initialized, we override
       * the interface and provide a no-op function.
       */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Get query term from user.
            // queryTerm;

            YouTube.Search.List search = youtube.search().list("id,snippet");
      /*
       * It is important to set your developer key from the Google Developer Console for
       * non-authenticated requests (found under the API Access tab at this link:
       * code.google.com/apis/). This is good practice and increased your quota.
       */
            search.setKey(apiKey);
            search.setQ(queryTerm);
      /*
       * We are only searching for videos (not playlists or channels). If we were searching for
       * more, we would add them as a string like this: "video,playlist,channel".
       */
            search.setType("video");
      /*
       * This method reduces the info returned to only the fields we need and makes calls more
       * efficient.
       */
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
     * thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        Log.d(TAG, "prettyPrint: " + "\n=============================================================");
        Log.d(TAG, "prettyPrint: " +
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        Log.d(TAG, "prettyPrint: " + "=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            Log.d(TAG, "prettyPrint: " + " There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail)singleVideo.getSnippet().getThumbnails().get("default");

                Log.d(TAG, "prettyPrint: " + " Video Id" + rId.getVideoId());
                Log.d(TAG, "prettyPrint: " + " Title: " + singleVideo.getSnippet().getTitle());
                Log.d(TAG, "prettyPrint: " + " Thumbnail: " + thumbnail.getUrl());
                Log.d(TAG, "prettyPrint: " + "\n-------------------------------------------------------------\n");
            }
        }
    }
}
