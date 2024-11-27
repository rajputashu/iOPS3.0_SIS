package com.sisindia.ai.android.commons.audiorecord;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.models.AudioPlayState;
import com.sisindia.ai.android.models.AudioRecordState;
import com.sisindia.ai.android.utils.FileUtils;
import com.sisindia.ai.android.utils.TimeUtils;

import java.io.IOException;

import javax.inject.Inject;

import timber.log.Timber;

public class AudioRecordingViewModel extends IopsBaseViewModel {

    public ObservableField<String> timerText = new ObservableField<>("00:00:00");

    public ObservableField<AudioRecordState> btnRecordState = new ObservableField<>(AudioRecordState.IDLE);
    public ObservableField<AudioPlayState> btnPlayState = new ObservableField<>(AudioPlayState.PAUSE);
    private AudioTimer audioTimer = new AudioTimer(90000);
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFile;

    @Inject
    public AudioRecordingViewModel(@NonNull Application application) {
        super(application);
    }

    public void onDeleteMedia(View view) {
        Timber.e("");
    }

    public void onPlayMedia(View view) {
        AudioPlayState state = btnPlayState.get();
        if (state != null) {
            switch (state) {

                case PAUSE:
                    btnPlayState.set(AudioPlayState.PLAY);
                    mediaPlayer.start();
                    break;

                case PLAY:
                    btnPlayState.set(AudioPlayState.PAUSE);
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                    break;
            }
        }
    }

    public void onAudioRecordSaveClick(View view) {
        if (TextUtils.isEmpty(audioFile)) {
            showToast("unable to save audio..");
            return;
        }

        message.what = NavigationConstants.ON_AUDIO_RECORDED_FOR_GRIEVANCE;
        message.obj = audioFile;
        liveData.postValue(message);
    }

    public void onRecordMedia(View view) {

        AudioRecordState state = btnRecordState.get();
        if (state != null) {
            switch (state) {

                case IDLE:
                    prepareMediaRecorder(view.getContext());
                    btnRecordState.set(AudioRecordState.RECORDING);
                    audioTimer.start();
                    mediaRecorder.start();
                    break;

                case RECORDING:
                    btnRecordState.set(AudioRecordState.RECORDED);
                    if (mediaRecorder != null) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                    }
                    prepareMediaPlayer();
                    audioTimer.cancel();
                    break;

                case RECORDED:
                    // TODO: 2020-03-26 delete old file
                    mediaRecorder = null;
                    prepareMediaRecorder(view.getContext());
                    btnRecordState.set(AudioRecordState.RECORDING);
                    audioTimer.start();
                    mediaRecorder.start();
                    break;
            }
        }
    }

    private void prepareMediaRecorder(Context context) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /*mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);*/
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        audioFile = FileUtils.createAudioFileForGrievances();
        audioFile = FileUtils.createAudioFileForGrievancesV2(context);
        mediaRecorder.setOutputFile(audioFile);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    private void prepareMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                onPlayMedia(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        if (mediaRecorder != null) {
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onCleared();
    }


    class AudioTimer extends CountDownTimer {

        private static final long INTERVAL_MS = 1000;
        private final long duration;


        public AudioTimer(long durationMs) {
            super(durationMs, INTERVAL_MS);
            this.duration = durationMs;
        }


        @Override
        public void onTick(long msUntilFinished) {
            int sec = (int) ((msUntilFinished) / INTERVAL_MS);
            timerText.set(TimeUtils.convertIntSecondsToHHMMSS(sec));
        }

        @Override
        public void onFinish() {
            onRecordMedia(null);
        }

        public void onPause() {
            onFinish();
            new AudioTimer(duration).start();
        }
    }
}
