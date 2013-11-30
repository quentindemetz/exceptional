package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.utils.ContactsUtils;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class EmailPickerActivity extends ListActivity {

	private static final String TAG = "EmailPickerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate()");			
		super.onCreate(savedInstanceState);
	
		Set<String> emails_set = new HashSet<String>(ContactsUtils.userEmailAddresses(this));
		ArrayList<String> emails = new ArrayList<String>(emails_set);
//		ArrayList<String> emails = ContactsUtils.userEmailAddressesTEST(this);

//      Removing this for demo, we want this screen to be displayed		
//		if(emails.size() == 1) {
//			moveToNextIntent(emails.get(0));
//		}
		
		setContentView(R.layout.email_picker);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, emails);
		
		setListAdapter(adapter); 	    
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		moveToNextIntent(item);
	}	    

	private void moveToNextIntent(String selectedEmailAddress) {
		Log.v(TAG,"selected email: " + selectedEmailAddress);
		SharedPreferencesUtil.setCurrentEmailAddress(this, selectedEmailAddress);
		
		Log.v(TAG, "executing FindOrRegisterUserTask background task");
		new FindOrRegisterUserTask().execute("");				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class FindOrRegisterUserTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			String email = SharedPreferencesUtil.getCurrentEmailAddress(EmailPickerActivity.this, "");

			Log.v(TAG, "find_or_register user " + email);
			try {
				return HTTPUtils.sendPost(EmailPickerActivity.this, "http://semat.herokuapp.com/api/v1/users/find_or_register.json", "email=" + email);
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
				return "EmailPickerActivity: Unable to retrieve web page. URL may be invalid. " + e.getMessage();
			} catch (Exception e) {
				Log.v(TAG, e.getMessage());
				e.printStackTrace();
				return "EmailPickerActivity: Unable to retrieve web page. URL may be invalid. " + e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Log.v(TAG, "find_or_register user performing fetch callback");
			if(result == null){
				Toast.makeText(EmailPickerActivity.this, "Cannot login - device offline", Toast.LENGTH_LONG).show();
				super.onPostExecute(result);
				return;
			}
			Log.v(TAG, result);
			
			if(result.equals("{\"response\":true}")) {
					Intent intent = new Intent(EmailPickerActivity.this, LoginActivity.class);
					startActivity(intent);							
			} else { //User has not confirmed account
				Intent intent = new Intent(EmailPickerActivity.this, WaitForRegistrationEmailActivity.class);
				startActivity(intent);			
			}
		}
	}	
}
