package br.com.ftt.betta.mobile.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BettaActivity extends Activity
{  
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    protected void waitingTest( long time )
    {
        try
        {
            Thread.sleep( time ) ;
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }
}
