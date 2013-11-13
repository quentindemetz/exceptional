package edu.cmu.semat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class IntroductionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.introduction);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("onTouch 2");
		SharedPreferencesUtil.setSplashScreenSeenByUser(this, true);

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
