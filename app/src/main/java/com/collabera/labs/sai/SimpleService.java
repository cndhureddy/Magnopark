package com.collabera.labs.sai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class SimpleService extends Service implements SensorEventListener{
	
	String file_name;
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		file_name = intent.getStringExtra("filename");
	}
	
	
	SensorManager sensorManager = null;
	int accCount;
	int oriCount;
	int gyroCount;
	int compassCount;
	float accData [][] = new float[180000][4];
	float oriData [][] =  new float[180000][4];
	float gyroData [][] = new float[180000][4];
	float compassData [][] = new float[180000][4];
	long acctimestamp,gyrotimestamp,oritimestamp,compasstimestamp;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		compassCount=accCount=oriCount=gyroCount=0;   
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
	    
		Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();

	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)); 
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
		
		PrintWriter captureFile1=null; 
		PrintWriter captureFile2=null; 
		PrintWriter captureFile3=null;
		PrintWriter captureFile4=null; 
		
		File captureAccFile = new File( "/sdcard/DriveCapture", file_name+"_accData.csv" );
		File captureOriFile = new File( "/sdcard/DriveCapture", file_name+"_oriData.csv" );
		File captureGyroFile = new File( "/sdcard/DriveCapture", file_name+"_gyroData.csv" );
		File captureCompassFile = new File( "/sdcard/DriveCapture", file_name+"_compassData.csv" );
		
		try{
		captureFile1 = new PrintWriter( new FileWriter( captureAccFile, false ) );
		captureFile2 = new PrintWriter( new FileWriter( captureOriFile, false ) );
		captureFile3 = new PrintWriter( new FileWriter( captureGyroFile, false ) );
		captureFile4 = new PrintWriter( new FileWriter( captureCompassFile, false ) );
		
		for(int i=0;i<accCount;i++)
		   captureFile1.println(accData[i][0]+","+accData[i][1]+","+accData[i][2]+","+accData[i][3]);
		
		
		for(int i=0;i<oriCount;i++)
		   captureFile2.println(oriData[i][0]+","+oriData[i][1]+","+oriData[i][2]+","+oriData[i][3]);
			
			
	    for(int i=0;i<gyroCount;i++)
		   captureFile3.println(gyroData[i][0]+","+gyroData[i][1]+","+gyroData[i][2]+","+gyroData[i][3]);
	    
	    for(int i=0;i<compassCount;i++)
	    	captureFile4.println(compassData[i][0]+","+compassData[i][1]+","+compassData[i][2]+","+compassData[i][3]);
		    	    
	   
	    Toast.makeText(this,"Files written .. .. ", Toast.LENGTH_LONG).show();
		
		}   
		catch(IOException e){
			Log.i("SensorService: ", e.getMessage());
		}
		finally{
			captureFile1.close();
			captureFile2.close();
			captureFile3.close();
			captureFile4.close();
			
		}
		Toast.makeText(this, "Completed.", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized(this){
			if(accCount>=180000||gyroCount>=180000||oriCount>=180000)
				return;
			switch(event.sensor.getType()){
			
			case Sensor.TYPE_ACCELEROMETER:
				accData[accCount][0]=event.values[0]; 
				accData[accCount][1]=event.values[1]; 
				accData[accCount][2]=event.values[2]; 
				acctimestamp=event.timestamp;
				accData[accCount][3]=event.timestamp;
				accCount++;
				break;
			case Sensor.TYPE_ORIENTATION:
				oriData[oriCount][0] = event.values[0];
				oriData[oriCount][1] = event.values[1];
				oriData[oriCount][2] = event.values[2];
				oritimestamp = event.timestamp;
				oriData[oriCount][3]=event.timestamp;
				oriCount++;
				break;
			case Sensor.TYPE_GYROSCOPE:
				gyroData[gyroCount][0] = event.values[0];
				gyroData[gyroCount][1] = event.values[1];
				gyroData[gyroCount][2] = event.values[2];
				gyrotimestamp = event.timestamp;
				gyroData[gyroCount][3] = event.timestamp;
				gyroCount++;
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				compassData[compassCount][0] = event.values[0];
				compassData[compassCount][1] = event.values[1];
				compassData[compassCount][2] = event.values[2];
				compasstimestamp = event.timestamp;
				compassData[compassCount][3] = event.timestamp;
				compassCount++;
				break;
			}
		}
	}
}
