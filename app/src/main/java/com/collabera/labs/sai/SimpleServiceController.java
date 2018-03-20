package com.collabera.labs.sai;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SimpleServiceController extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

	private LinearLayout timerBox;
	long startTime = 0;

	MediaRecorder mediaRecorder;


	SurfaceHolder surfaceHolder;
	boolean recording;
	WakeLock wl;

	Intent sensor = null;
	long timestamp = 0;

	int gpsCount;
	float gpsData[][] = new float[180000][5];
	long gpstimestamp;
	boolean logGPS = false;
	EditText txtFileName;

	String file_name;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensor = new Intent(SimpleServiceController.this, SimpleService.class);

		recording = false;

		String fSaveName = Environment.getExternalStorageDirectory().toString();
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Drive");

		File folder = new File(fSaveName, "DriveCapture");


		folder.mkdir();

		mediaRecorder = new MediaRecorder();
		setContentView(R.layout.main);

//		SurfaceView myVideoView = (SurfaceView) findViewById(R.id.videoView);
//		surfaceHolder = myVideoView.getHolder();
//		surfaceHolder.addCallback(this);
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Button start = (Button) findViewById(R.id.btnStart);
		Button stop = (Button) findViewById(R.id.btnStop);
		Button lapTime = (Button) findViewById(R.id.btnLapTime);

		txtFileName= (EditText)findViewById(R.id.txtFileName);

		timerBox = (LinearLayout)findViewById(R.id.timerBox);

		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		lapTime.setOnClickListener(this);



		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		//Toast.makeText(getApplicationContext(), "GPS Working", Toast.LENGTH_SHORT).show();
		wl.acquire();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (wl.isHeld())
			wl.release();


	}

	private void initMediaRecorder() {
//		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//
//		// Specify the audio and video encoding
//		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
//		// Specify the output file
//		mediaRecorder.setOrientationHint(90);
//		mediaRecorder.setMaxDuration(3600000);
//		//mediaRecorder.setOutputFile("/sdcard/DriveCapture/test.mp4");
		//mediaRecorder.setVideoFrameRate(10);
		timestamp = System.currentTimeMillis();
		//mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/DriveCapture/" + timestamp + ".mp4");

	}

	private void prepareMediaRecorder() {
		mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		///////TODO initMediaRecorder();
		///////TODO prepareMediaRecorder();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnStart:
				Start();
				break;
			case R.id.btnStop:
				Stop();
				break;
			case R.id.btnLapTime:
				lapTimes();
				break;
			default:
				break;
		}
	}
	ArrayList<String> lapTimes = new ArrayList<String>();
	private void lapTimes() {
		long millis = System.currentTimeMillis()- startTime ;
		int s = (int) (millis/1000);
		int sadom = (int) (millis%1000);
		TextView tv;
		tv = createNewTextView(String.format("%d.%02d", s, sadom));
		tv.setTextSize(10);

		timerBox.addView(tv);
		lapTimes.add(String.format("%d.%02d", s, sadom));
		Toast.makeText(this,"time lapped", Toast.LENGTH_SHORT).show();



	}

	private TextView createNewTextView(String text) {
		final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final TextView textView = new TextView(this);
		textView.setLayoutParams(lparams);

		textView.setText( text);
		textView.setTextColor(Color.parseColor("#030c5f"));
		textView.setTextSize(24);

		return textView;
	}
	private void Start() {
		lapTimes.clear();

		timerBox.removeAllViews();
		if(txtFileName.getText().toString().trim().length() == 0) {
			Toast.makeText(getApplicationContext(), "Enter a name for the files.", Toast.LENGTH_SHORT).show();
			return;
		}

		file_name= txtFileName.getText().toString();
		//if (recording == false) {
			//////TODO mediaRecorder.reset();
		  // timestamp = System.currentTimeMillis();
			//////TODO prepareMediaRecorder();
			//sensor.putExtra("time", timestamp + "");
			/////TODO mediaRecorder.start();
			sensor.putExtra("filename", file_name + "");
			startService(sensor);
		    startTime= System.currentTimeMillis();
			recording = true;
			logGPS = true;
		//}
	}

	private void Stop() {

		long millis = System.currentTimeMillis()- startTime ;
		int s = (int) (millis/1000);
		int sadom = (int) (millis%1000);
		TextView tv;
		tv = createNewTextView(String.format("%d.%02d", s, sadom));
		tv.setTextSize(10);
		timerBox.addView(tv);
		lapTimes.add(String.format("%d.%02d", s, sadom));


		Toast.makeText(getApplicationContext(), "-- STOOOOOOOP --", Toast.LENGTH_SHORT).show();
		if (recording == false) return;

		if (recording) {
			stopService(sensor);
			////////todo mediaRecorder.stop();

			recording = false;
			logGPS = false;


			String file_name = txtFileName.getText().toString();
			//file_name = "gpsFileName";//intent.getStringExtra("filename");

			PrintWriter captureFile1 = null;
			PrintWriter captureFile_timer =null;

			long fileTimestamp = System.currentTimeMillis();
		    String gspFN = String.valueOf(fileTimestamp);
			//file_name = file_name+"_" + gspFN;

			File captureAccFile = new File("/sdcard/DriveCapture", file_name + "_GPSData.csv");
			File captureTimer = new File("/sdcard/DriveCapture", file_name + "_Timer.csv");

			try {
				captureFile1 = new PrintWriter(new FileWriter(captureAccFile, false));
				captureFile_timer = new PrintWriter(new FileWriter(captureTimer, false));

				for (int i = 0; i < gpsCount; i++)
					captureFile1.println(gpsData[i][0] + "," + gpsData[i][1] + "," + gpsData[i][2] + "," + gpsData[i][3] + "," + gpsData[i][4]);

				for(int i=0; i<lapTimes.size();i++)
					captureFile_timer.println(lapTimes.get(i));


				Toast.makeText(getApplicationContext(), "-- GPS and Timer Files writen --", Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				Log.i("SensorService: ", e.getMessage());
			} finally {
				captureFile1.close();
				captureFile_timer.close();
			}
			txtFileName.getText().clear();
		}
	}




	/* ******************************  */
	    /* Class My Location Listener */
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			loc.getLatitude();
			loc.getLongitude();
			loc.getAltitude();
			loc.getAccuracy();
			loc.getSpeed();
			loc.getTime();

	      /*  String Text = "My current location is: " +
	        "Latitud = " + loc.getLatitude() +
	        "Longitud = " + loc.getLongitude() +
	        "Altitude = " + loc.getAltitude();
	      */
			//*********************
			if (logGPS == true) {
				gpsData[gpsCount][0] = (float) loc.getLatitude();
				gpsData[gpsCount][1] = (float) loc.getLongitude();
				gpsData[gpsCount][2] = (float) loc.getAltitude();
				gpsData[gpsCount][3] = (float) loc.getSpeed();
				gpsData[gpsCount][4] = (float) System.nanoTime();//   .currentTimeMillis();//loc.getTime();
				gpstimestamp = loc.getTime();
				gpsCount++;
				//****************************
				String Text = "";
				Text = "[" + loc.getTime() + "]  [" + loc.getSpeed() + "]";// +   gpsData[gpsCount-1][4];
				//Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			//Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			//Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//Toast.makeText(getApplicationContext(), "Status changed ", Toast.LENGTH_SHORT).show();

		}
	}
}

