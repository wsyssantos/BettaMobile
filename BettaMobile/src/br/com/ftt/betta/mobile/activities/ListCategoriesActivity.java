package br.com.ftt.betta.mobile.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.com.ftt.betta.mobile.dao.Categoria;
import br.com.ftt.betta.mobile.xml.handler.XmlCategoryParser;

public class ListCategoriesActivity extends ListActivity
{
    private ProgressDialog loadingCategories = null; 
    private List<Categoria> categorias ;
    private CategoryListAdapter listAdapter ;
    private Runnable carregaCategorias;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_categories );
        categorias = new ArrayList<Categoria>( ) ;
        listAdapter = new CategoryListAdapter( this, R.layout.category_list_row, categorias ) ;
        setListAdapter( listAdapter ) ;
        
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(300);
        set.addAnimation(animation);
        
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        ListView listView = getListView();        
        listView.setLayoutAnimation(controller);
        
        carregaCategorias = new Runnable( )
        {
            public void run( )
            {
                carregaCategorias( ) ;
            }
        };
        Thread thread =  new Thread(null, carregaCategorias, "MagentoBackground");
        thread.start();
        loadingCategories = ProgressDialog.show(ListCategoriesActivity.this, "Aguarde...", "Carregando Gêneros ...", true);
    }
    
    private Runnable returnRes = new Runnable( ) 
    {
        public void run( ) 
        {
            if ( categorias != null && categorias.size( ) > 0 )
            {
                listAdapter.notifyDataSetChanged( ) ;
                for(int i=0 ; i<categorias.size( ) ; i++ )
                {
                    listAdapter.add( categorias.get( i ) ) ;
                }
            }
            loadingCategories.dismiss( ) ;
            listAdapter.notifyDataSetChanged( ) ;
        }
    };

    private void carregaCategorias( )
    {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><categorias numCategorias=\"10\"><categoria><nome>Ação</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=1</url><isVisible>true</isVisible></categoria><categoria><nome>Terror</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=2</url><isVisible>true</isVisible></categoria><categoria><nome>Animação</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=3</url><isVisible>true</isVisible></categoria><categoria><nome>Romance</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=4</url><isVisible>true</isVisible></categoria><categoria><nome>Aventura</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=5</url><isVisible>true</isVisible></categoria><categoria><nome>Suspense</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=6</url><isVisible>true</isVisible></categoria><categoria><nome>Infantil</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=7</url><isVisible>true</isVisible></categoria><categoria><nome>Documentário</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=8</url><isVisible>true</isVisible></categoria><categoria><nome>Comédia</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=9</url><isVisible>true</isVisible></categoria><categoria><nome>Comédia Romântica</nome><url>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=10</url><isVisible>true</isVisible></categoria></categorias>";
        XmlCategoryParser parser = new XmlCategoryParser( xml, false );
        categorias = parser.parseCategories( ) ;
        /*try
        {
            Thread.sleep(5000);
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }*/
        runOnUiThread(returnRes);
    }

    private class CategoryListAdapter extends ArrayAdapter<Categoria>
    {
        private List<Categoria> items;

        public CategoryListAdapter( Context context, int textViewResourceId, List<Categoria> items )
        {
            super( context, textViewResourceId, items );
            this.items = items;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            View v = convertView;

            if ( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                v = vi.inflate( R.layout.category_list_row, null );
            }

            Categoria cat = items.get(position) ;
            
            if( cat != null )
            {
                TextView label = (TextView) v.findViewById( R.id.nomeCategoria ) ;
                TextView url = (TextView) v.findViewById( R.id.urlCategoria ) ;
                
                if( label != null )
                {
                    label.setText( cat.getNome( ) ) ;
                }
                if( url != null )
                {
                    url.setText( cat.getUrl( ) ) ;
                }
            }
            
            return v;
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        super.onBackPressed( );
        try
        {
            setResult(RESULT_CANCELED);
            finish();
        }
        catch ( Throwable e )
        {
            e.printStackTrace();
        }
    }
    
    public static void closeAllBelowActivities(Activity current) 
    {
        boolean flag = true;
        Activity below = current.getParent( ) ;
        if (below == null)
        {
            return;
        }
        System.out.println("Below Parent: " + below.getClass());
        while (flag) 
        {
            Activity temp = below;
            try 
            {
                below = temp.getParent();
                temp.finish();
            } 
            catch (Exception e) 
            {
                flag = false;
            }
        }
    }
    
    @Override
    protected void onListItemClick( ListView l, View v, int position, long id )
    {
        super.onListItemClick( l, v, position, id );
        
        Object o = this.getListAdapter( ).getItem( position ) ;
        
        Categoria cat = (Categoria) o ;
        
        Intent it = new Intent(this, ListMoviesActivity.class);
        Bundle params = new Bundle( );
        params.putString( "categoryName", cat.getNome( ) );
        params.putString( "categoryUrl", cat.getUrl( ) );
        it.putExtras( params ) ;
        startActivity( it ) ;
    }
}
