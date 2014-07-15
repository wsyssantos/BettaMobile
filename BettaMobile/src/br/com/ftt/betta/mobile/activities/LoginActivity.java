package br.com.ftt.betta.mobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ViewSwitcher;
import br.com.ftt.betta.mobile.constants.StringConstants;
import br.com.ftt.betta.moblie.util.CriptographyUtil;
import br.com.ftt.betta.moblie.util.StringUtil;

public class LoginActivity extends BettaActivity 
{
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );     
        new LoadViewTask( ).execute( ) ;
    }
    
    private void initTabActivity( )
    {
    	try
    	{
    	    this.finalize( ) ;
	        Intent it = new Intent( this, TabContentActivity.class ) ;
	        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivityForResult(it, StringConstants.FINALIZE_RESULT);
	        startActivity( it ) ;
    	}
    	catch( Exception e )
    	{
    		e.printStackTrace() ;
    	}
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult( requestCode, resultCode, data );
        if( requestCode == StringConstants.FINALIZE_RESULT )
        {
            if(resultCode == RESULT_CANCELED)
            {
                this.finish( ) ;
            }
        }
    }
    
    @Override
    protected void onResume( )
    {
        super.onResume( );
        try
        {
            this.finalize( ) ;
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
    }
    
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>   
    {
        private boolean loginValid = false ;
        private ViewSwitcher viewSwitcher ;
        @Override
        protected void onPreExecute( )
        {
            viewSwitcher = new ViewSwitcher(LoginActivity.this);
            viewSwitcher.addView( ViewSwitcher.inflate( LoginActivity.this, R.layout.loading_screen, null ) );
            
            setContentView(viewSwitcher) ;
        }

        @Override
        protected Void doInBackground( Void... params )
        {
            SharedPreferences loginPreferences = getSharedPreferences( StringConstants.USER_PREFERENCES, 0 ) ;
            
            String userName = loginPreferences.getString( StringConstants.USER_NAME, "" ) ;
            String userPass = loginPreferences.getString( StringConstants.USER_PASSWORD, "" ) ;
            
           // waitingTest( 5000 );
            
            if( StringUtil.isEmpty( userName ) || StringUtil.isEmpty( userPass ) )
            {
                loginValid = false ;
            }
            else
            {
                // Realiza Login Automático
                loginValid = true ;
            } 
            return null;
        }
        
        
        
        @Override
        protected void onPostExecute( Void result )
        {
            if( !loginValid )
            {
                viewSwitcher.addView(ViewSwitcher.inflate(LoginActivity.this, R.layout.login, null));
                viewSwitcher.showNext();
                Button botaoCadastro = (Button) findViewById( R.id.btnIniciaCadastro ) ;
                botaoCadastro.setOnClickListener( new OnClickListener( )
                {
                    public void onClick( View v )
                    {
                        
                    }
                } ) ;
                
                Button botaoLogin = (Button) findViewById( R.id.btnLogin ) ;
                botaoLogin.setOnClickListener( new OnClickListener( )
                {
                    public void onClick( View v )
                    {
                        String userName = "" ;
                        String userPass = "" ;
                        
                        EditText user = (EditText) findViewById( R.id.userName ) ;
                        EditText pass = (EditText) findViewById( R.id.userPassword ) ;
                        
                        userName = user.getText( ).toString( ) ;
                        userPass = pass.getText( ).toString( ) ;
                        
                        String cripttedPassword = CriptographyUtil.crypt( userPass ) ;
                        
                        boolean login = true ;
                        
                        //TODO: Fazer login
                        
                        if( login )
                        {
                            CheckBox checkUser = (CheckBox) findViewById( R.id.saveUserInformation ) ;
                            
                            if( checkUser.isChecked( ) )
                            {
                                SharedPreferences preferences = getSharedPreferences( StringConstants.USER_PREFERENCES, MODE_PRIVATE ) ;
                                
                                Editor preferencesEditor = preferences.edit( ) ;
                                preferencesEditor.putString( StringConstants.USER_NAME, userName ) ;
                                preferencesEditor.putString( StringConstants.USER_PASSWORD, cripttedPassword ) ;
                                preferencesEditor.commit( ) ;
                            }
                            initTabActivity( ) ;
                        }
                    }
                } ) ;
            }
            else
            {
                initTabActivity( ) ;
            }
        }
        
    }    
}
