package dk.iha.itsmap.dpn.epjproevesvar.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.AddUpdateFavoritePatient;
import dk.iha.itsmap.dpn.epjproevesvar.business.Color;
import dk.iha.itsmap.dpn.epjproevesvar.business.PatientInformation;
import dk.iha.itsmap.dpn.epjproevesvar.services.DownloadService;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable.Factory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class AddFavorite extends Activity implements OnClickListener {

	private EditText CPRInput;
	private Button FindPatientButton;
	private String authorization;
	private static final String TAG="AddFavorite";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_favorite);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		CPRInput = (EditText) findViewById(R.id.cprTextInput);
		authorization = getIntent().getExtras().getString("Authorization");
		FindPatientButton = (Button) findViewById(R.id.submitcprbutton);
		FindPatientButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_favorite, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitcprbutton:
			try {
				String result = new DownloadService().execute("http://130.225.184.205:8082/","pfpas-mobile/rest/patientinformation/patients/"+CPRInput.getText(),"Authorization","Basic "+authorization).get();
				
				if(result.equals("notFound")){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							this);

					alertDialogBuilder.setTitle("CPR Not Found");

					alertDialogBuilder
					.setMessage("CPR " + CPRInput.getText()+" couldn't be found")
					.setCancelable(false)
					.setNeutralButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();
				}else if(result.equals("badRequest")){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							this);

					alertDialogBuilder.setTitle("Bad CPR");

					alertDialogBuilder
					.setMessage(CPRInput.getText()+" is not a CPR number")
					.setCancelable(false)
					.setNeutralButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog = alertDialogBuilder.create();

					alertDialog.show();
				}else{
				PatientInformation favorites = new Gson().fromJson(result, PatientInformation.class);
				buildDialog(favorites.getCpr(), favorites.getName());
				}
			} catch (InterruptedException e) {
			} catch (ExecutionException e) { }
			break;
		}

	}

	public void buildDialog(final String cpr, final String name){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		LayoutInflater factory = LayoutInflater.from(this);
		View customDialog = factory.inflate(R.layout.custom_add_patient_dialog, null);
		
		alertDialogBuilder.setTitle("Are you sure?");

		String alert1 = "Adding following patient to favorites: ";
		String alert2 = "Name: " + name;
		String alert3 = "CPR: " + cpr;
		
		//Populating color dropdown
		final Spinner colorDropdown = (Spinner) customDialog.findViewById(R.id.ColorChooser);
		final EditText noteInput = (EditText) customDialog.findViewById(R.id.noteInput);
		List<String> list = new ArrayList<String>();
		for(Color color : Color.values()){
			list.add(color.name());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Log.d(TAG,"Data adapter: "+dataAdapter);
		Log.d(TAG,"Colordropdown: "+colorDropdown);
		colorDropdown.setAdapter(dataAdapter);
		
		
		alertDialogBuilder
		.setMessage(alert1 +"\n"+ alert2 +"\n"+ alert3)
		.setCancelable(false)
		.setView(customDialog)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				new Thread(){
					public void run(){
						Log.d(TAG,"Color choosed: "+colorDropdown.getSelectedItem());
						Log.d(TAG,"Note entered: "+noteInput.getText().toString());
						addFavoriteToServer(cpr, name, (String) colorDropdown.getSelectedItem(), noteInput.getText().toString());
					}
				}.start();
				AddFavorite.this.finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	public void addFavoriteToServer(String cpr, String name, String color, String patientNote) {
		Log.d(TAG, "Before json");

		Gson gson = new Gson();
		String json = gson.toJson(new AddUpdateFavoritePatient(patientNote,Color.valueOf(color))); 

		Log.d(TAG, "Json: "+json);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);
	    try {
	        HttpPost request = new HttpPost("http://130.225.184.205:8082/pfpas-mobile/rest/favorites/"+cpr);

	        Log.d(TAG, "Authorization: "+authorization);
	        request.addHeader("Authorization","Basic "+authorization);
	        StringEntity se = new StringEntity(json);
	        se.setContentType("application/json"); 
	        request.setEntity(se);
	        
	        HttpResponse response = client.execute(request);
	        int responseCode = response.getStatusLine().getStatusCode();
	        
	        if(responseCode == 409){
	        	 NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        	 Notification note=new Notification(R.drawable.ic_launcher,"EPJ Error",System.currentTimeMillis());
	     	    PendingIntent i=PendingIntent.getActivity(this, 0,new Intent(this, AddFavorite.class),0);
	     	    note.setLatestEventInfo(this, "Couldn't add favorite",cpr+" already a favorite", i);
	     	    
	     	    mgr.notify(1, note);
	        }else if(responseCode == 500){
	        	 NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        	 Notification note=new Notification(R.drawable.ic_launcher,"EPJ Error",System.currentTimeMillis());
	     	    PendingIntent i=PendingIntent.getActivity(this, 0,new Intent(this, AddFavorite.class),0);
	     	    note.setLatestEventInfo(this, "Server Error","And unknown server error occured", i);
	     	    
	     	    mgr.notify(1, note);
	        }else if(responseCode == 400){
	        	 NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        	 Notification note=new Notification(R.drawable.ic_launcher,"EPJ Error",System.currentTimeMillis());
	     	    PendingIntent i=PendingIntent.getActivity(this, 0,new Intent(this, AddFavorite.class),0);
	     	    note.setLatestEventInfo(this, "Unknown patient","Patient with CPR "+cpr+" couldn't be found", i);
	     	    
	     	    mgr.notify(1, note);
	        }
	        
	        Log.d(TAG, Integer.toString(responseCode));
	        // handle response here...
	    }catch (Exception ex) {
	        // handle exception here
	    } finally {
	        client.getConnectionManager().shutdown();
	    }
	} 
}
