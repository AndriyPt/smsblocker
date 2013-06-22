package org.sms.blocker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ApplicationSettings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.application_settings, menu);
        return true;
    }
    
}
