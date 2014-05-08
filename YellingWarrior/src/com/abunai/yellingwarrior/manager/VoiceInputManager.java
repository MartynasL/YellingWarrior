package com.abunai.yellingwarrior.manager;

import java.io.IOException;

import android.media.MediaRecorder;

public class VoiceInputManager {
	private static final VoiceInputManager INSTANCE = new VoiceInputManager();
	private static MediaRecorder recorder;
	private int amp;

	
	/**
	 * Initializes manager. The manager must be prepared before use.
	 */
	public void prepareManager() {
		if (recorder == null) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile("/dev/null");
		}
	}
	/**
	 * Stops recording. Manager must be prepared before use. 
	 */
	public void stopShout(){
		recorder.stop();
		recorder.release();
		recorder = null;
	}
	
	/**
	 * Starts recording. Manager must be prepared before use
	 */
	public void startShout(){
		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Returns the recorder amplitude at the time of use.
	 * @return 
	 */
	public Integer getShoutSound(){
		if (recorder != null)
			amp=recorder.getMaxAmplitude();
		return amp;
	}
	
	/**
	 * Returns an instance of the manager
	 * @return
	 */
	public static VoiceInputManager getInstance() {
		return INSTANCE;
	}

}

