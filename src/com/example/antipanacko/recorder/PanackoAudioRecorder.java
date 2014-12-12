package com.example.antipanacko.recorder;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.example.antitheft.R;

public class PanackoAudioRecorder extends Activity implements OnTouchListener {
	private static final String LOG_TAG = "PanackoAudioRecorder";
	private static String mFileName = null;

	// private RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;

	// private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;

	private AudioManager mAudioManager = null;
	private int originalVolume = 0;

	private Button btnRecord, btnPlay;

	public PanackoAudioRecorder() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

		/*
		 * LinearLayout ll = new LinearLayout(this);
		 * 
		 * mRecordButton = new RecordButton(this); ll.addView(mRecordButton, new
		 * LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT, 0));
		 * 
		 * mPlayButton = new PlayButton(this); ll.addView(mPlayButton, new
		 * LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT, 0));
		 * 
		 * setContentView(ll);
		 */

		setContentView(R.layout.activity_record);
		btnRecord = (Button) findViewById(R.id.btn_record);
		btnRecord.setOnTouchListener(this);

		btnPlay = (Button) findViewById(R.id.btn_play_sound);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onPlay(true);
			}
		});
	}

	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
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

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

/*	class RecordButton extends Button {
		boolean mStartRecording = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording) {
					setText("Stop recording");
				} else {
					setText("Start recording");
				}
				mStartRecording = !mStartRecording;
			}
		};

		public RecordButton(Context ctx) {
			super(ctx);
			setText("Start recording");
			setOnClickListener(clicker);
		}
	}

	class PlayButton extends Button {
		boolean mStartPlaying = true;

		OnClickListener clicker = new OnClickListener() {
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying) {
					setText("Stop playing");
				} else {
					setText("Start playing");
				}
				mStartPlaying = !mStartPlaying;
			}
		};

		public PlayButton(Context ctx) {
			super(ctx);
			setText("Start playing");
			setOnClickListener(clicker);
		}
	}*/

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			v.setPressed(true);
			btnRecord.setText("Recording ...");
			onRecord(true);
			Log.d("##", "ACTION_DOWN");
			break;
			
		case MotionEvent.ACTION_UP:
			v.setPressed(false);
			onRecord(false);
			btnRecord.setText("Record");
			Log.d("##", "ACTION_UP");
			break;
			
		case MotionEvent.ACTION_OUTSIDE:
			Log.d("##", "ACTION_OUTSIDE");
			break;
			
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.d("##", "ACTION_POINTER_DOWN");
			break;
			
		case MotionEvent.ACTION_POINTER_UP:
			Log.d("##", "ACTION_POINTER_UP");
			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.d("##", "ACTION_MOVE");
			break;
		}
		return true;
	}
}
