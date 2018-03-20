package com.collabera.labs.sai;

import android.app.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.hardware.Sensor;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;



import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BluetoothService extends Service 
{

	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public void onCreate()
	{
		super.onCreate();
		//Toast.makeText(this,"Bluetooth Service created ...", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		}
	
}
