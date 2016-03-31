package com.example.demo;

import android.app.Activity;
import android.os.Bundle;

import com.view.RoundProgressBar;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final RoundProgressBar roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		roundProgressBar.setMax(100);
		roundProgressBar.setProgress(80);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				roundProgressBar.setProgress(50);
			}
		}).start();

	}

}
