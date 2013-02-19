package com.openxc.gaugedriver;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class FuelOdoHandler {
	
	long mDuration;  //This is the time in ns over which we do our calculations.
	List<Long> mTimes = new ArrayList<Long>();
	List<Double> mValues = new ArrayList<Double>();
	boolean mDebug = false;
	
	FuelOdoHandler(long smoothTime, boolean debug) {  //smoothTime is in milliseconds.
		mDuration = smoothTime;  //Convert to nanoseconds.
		mDebug = debug;
	}
	
	void Add(double value, long thisTime) {
		mValues.add(value);
		mTimes.add(thisTime);
		if (mDebug) {
			Log.i("Queue", "Queue total: " + mValues.size() + "  Added " + value + " at " + thisTime);
		}
	}
	
	double Latest() {
		if (mValues.size() > 0)
			return mValues.get(mValues.size()-1);
		else
			return 0.0;
	}
	
	double Recalculate(long thisTime) {
		long expiration = thisTime - mDuration;
		if(mDebug)
			Log.i("Queue", "Dropping everything before " + expiration);
		while ((!mTimes.isEmpty()) && (mTimes.get(0) < expiration)) {
			if(mDebug)
				Log.i("Queue", "Removing " + mTimes.get(0));
			mTimes.remove(0);
			mValues.remove(0);
		}
		if(mDebug)
			Log.i("Queue", "Queue total: " + mValues.size() + "  Returning " + (mValues.get(mValues.size()-1) - mValues.get(0)));
		
		if(mTimes.isEmpty()) {
			return 0.0;
		} else {
			return mValues.get(mValues.size()-1) - mValues.get(0);
		}
	}
}
