package com.example.antipanacko.system;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.util.Log;

public class PlaySound {

	private static final String LOG_TAG = "PanackoAudioRecorder";
	private static String mFileName = null;
	private MediaPlayer mPlayer = null;
	private AudioManager mAudioManager = null;
	private int originalVolume = 0;
	
	public PlaySound(Context _ctx) {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";
		
		mAudioManager = (AudioManager) _ctx.getSystemService(Context.AUDIO_SERVICE);
		originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
	}
	
	public void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
				}
			});
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}
}
