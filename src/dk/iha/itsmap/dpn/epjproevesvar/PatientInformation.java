package dk.iha.itsmap.dpn.epjproevesvar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PatientInformation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_information);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_patient_information, menu);
		return true;
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	}

}
