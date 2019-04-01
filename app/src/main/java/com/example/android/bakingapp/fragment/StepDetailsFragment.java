package com.example.android.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.object.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String STEP_LIST = "step_list";
    public static final String STEP_POSITION = "step_position";
    private static final String EXO_PLAYER_POSITION = "exo_player_position";
    private static final String EXO_PLAYER_WINDOW_INDEX = "exo_player_window_index";
    private static final String EXO_PLAYER_WHEN_READY = "exo_player_when_ready";
    
    @BindView(R.id.tv_step_description)
    TextView stepDescription;
    @BindView(R.id.next_step_button)
    Button nextStepButton;
    @BindView(R.id.previous_step_button) Button previousStepButton;
    @BindView(R.id.step_video_view)
    SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean hasVideoUrl;
    private long exoPlayerPosition = -1;
    private int exoPlayerWindowIndex;
    private boolean exoPlayerReady;

    private ArrayList<Step> mStepList;
    private int mStepPosition;

    OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onNextPressed(int position);
        void onPreviousPressed(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    public StepDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
            exoPlayerPosition = savedInstanceState.getLong(EXO_PLAYER_POSITION);
            exoPlayerWindowIndex = savedInstanceState.getInt(EXO_PLAYER_WINDOW_INDEX);
            exoPlayerReady = savedInstanceState.getBoolean(EXO_PLAYER_WHEN_READY);
        }

        String mediaUrl = mStepList.get(mStepPosition).getMediaUrl();
        hasVideoUrl = !TextUtils.isEmpty(mediaUrl);
        if (hasVideoUrl) {
            initializeMediaSession();
            initializePlayer(Uri.parse(mediaUrl));
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
        stepDescription.setText(mStepList.get(mStepPosition).getDescription());

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mStepPosition;
                if (++position >= mStepList.size())
                    mCallback.onNextPressed(0);
                else
                    mCallback.onNextPressed(position);
            }
        });

        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mStepPosition;
                if (--position < 0)
                    mCallback.onPreviousPressed(mStepList.size() - 1);
                else
                    mCallback.onPreviousPressed(position);
            }
        });

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            if (exoPlayerPosition != -1) {
                mExoPlayer.seekTo(exoPlayerPosition);
                mExoPlayer.setPlayWhenReady(exoPlayerReady);
            } else {
                mExoPlayer.setPlayWhenReady(true);
            }
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.mStepList = stepList;
    }

    public void setStepPosition(int stepPosition) {
        this.mStepPosition = stepPosition;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STEP_LIST, mStepList);
        outState.putInt(STEP_POSITION, mStepPosition);
        if(mExoPlayer != null) {
            exoPlayerPosition = mExoPlayer.getCurrentPosition();
            exoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
            exoPlayerReady = mExoPlayer.getPlayWhenReady();
            outState.putLong(EXO_PLAYER_POSITION, exoPlayerPosition);
            outState.putInt(EXO_PLAYER_WINDOW_INDEX, exoPlayerWindowIndex);
            outState.putBoolean(EXO_PLAYER_WHEN_READY, exoPlayerReady);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (hasVideoUrl) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }
}
