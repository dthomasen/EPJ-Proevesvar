package dk.iha.itsmap.dpn.epjproevesvar.business;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class PatientsFilterView extends EditText {

	public PatientsFilterView(Context context) {
		super(context);
	}
	
    public PatientsFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PatientsFilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode==KeyEvent.KEYCODE_ENTER) 
		{
			// Just ignore the [Enter] key
			return true;
		}
		// Handle all other keys in the default way
		return super.onKeyDown(keyCode, event);
	}
}
