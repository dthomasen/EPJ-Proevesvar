package dk.iha.itsmap.dpn.epjproevesvar.services;

import java.util.ArrayList;

import dk.iha.itsmap.dpn.epjproevesvar.R;
import dk.iha.itsmap.dpn.epjproevesvar.business.Favorite;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PatientBaseAdapter extends BaseAdapter {

	private static ArrayList<Favorite> searchArrayList;
    private LayoutInflater mInflater;
    private static final String TAG="PatientBaseAdapter";
 
    public PatientBaseAdapter(Context context, ArrayList<Favorite> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return searchArrayList.size();
    }
 
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public void sendList(ArrayList<Favorite> results){
    	searchArrayList = results;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.d(TAG,"GETVIEW called!");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_patient_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.line_name);
            holder.txtCpr = (TextView) convertView
                    .findViewById(R.id.line_cpr);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtCpr.setText(searchArrayList.get(position).getCpr());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtName;
        TextView txtCpr;
    }
}
