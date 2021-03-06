package khs.study.youtubeexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.YouTube;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadFactory;

public class MainActivity extends YouTubeBaseActivity {
    private final String TAG = "JYP/"+getClass().getSimpleName();
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    YouTube youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess: ");
                youTubePlayer.loadVideo("ar88lOiwz58");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: ");
            }
        };

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyBnSJPhyFwzMbOxaccpK7jYeZNXmyEgtu0", onInitializedListener);
            }
        });

        searchThread.start();

    }

    Thread searchThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Search.search("성시경");
        }
    });
}
