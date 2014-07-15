package br.com.ftt.betta.mobile.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class TabContentActivity extends TabActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        Resources res = getResources();
        final TabHost tabHost = getTabHost( );

        tabHost.addTab( tabHost.newTabSpec( "tab1" )
                .setIndicator( getText( R.string.categorias ), res.getDrawable( R.drawable.ic_tab_category ) )
                .setContent( new Intent( this, ListCategoriesActivity.class ) ) );
        
        tabHost.addTab( tabHost.newTabSpec( "tab2" )
                .setIndicator( getText( R.string.busca ), res.getDrawable( R.drawable.ic_tab_search ) )
                .setContent( new Intent( this, SearchActivity.class ) ) );
        
        tabHost.addTab( tabHost.newTabSpec( "tab3" )
                .setIndicator( getText( R.string.listas ), res.getDrawable( R.drawable.ic_tab_playlist ) )
                .setContent( new Intent( this, PlaylistActivity.class ) ) );
        
        tabHost.setCurrentTab(0);
    }
    
    @Override
    protected void onDestroy( )
    {
        super.onDestroy( ) ;
    }
}
