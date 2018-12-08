package com.udacity.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Step;

import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.bakingapp.utils.Constants.RECIPE_DETAILS_STEPS;
import static com.udacity.bakingapp.utils.Constants.SELECTED_POSITION;
import static com.udacity.bakingapp.utils.Constants.VIDEO;

public class RecipeStepsFragment extends Fragment {

    private static final String TAG = RecipeStepsFragment.class.getSimpleName();

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    Uri videoUri;
    long position;

    @BindView(R.id.tv_step_instruction)
    TextView instruction;
    @BindView(R.id.iv_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.video_player)
    SimpleExoPlayerView videoPlayer;

    public RecipeStepsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_detail_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Step step = null;

        if (getArguments() != null)
            step = getArguments().getParcelable(RECIPE_DETAILS_STEPS);

        videoUri = Uri.parse(step.getVideoURL());

        instruction.setText(step.getDescription());

        if (savedInstanceState != null)
            position = savedInstanceState.getLong(SELECTED_POSITION);

        if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()) {
            if (isVideoFile(step.getThumbnailURL())) {
                initializeVideoPlayer(Uri.parse(step.getThumbnailURL()));
            } else {
                Picasso.get().load(step.getThumbnailURL()).into(thumbnail);
            }
        } else {
            thumbnail.setVisibility(View.GONE);
        }

        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            initializeVideoPlayer(videoUri);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, position);
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith(VIDEO);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null)
            position = exoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (videoUri != null) {
            if (exoPlayer != null) {
                exoPlayer.seekTo(position);
            } else {
                initializeVideoPlayer(videoUri);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
            }
        });
        mediaSession.setActive(true);
    }

    private void initializeVideoPlayer(Uri videoUri) {
        initializeMediaSession();

        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            videoPlayer.setPlayer(exoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null)
            mediaSession.setActive(true);
    }
}
