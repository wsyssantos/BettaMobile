package br.com.ftt.betta.mobile.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.ftt.betta.mobile.dao.Filme;
import br.com.ftt.betta.mobile.xml.handler.XmlFilmeParser;

public class PlaylistActivity extends ListActivity
{
    private ProgressDialog   loadingMovies = null;
    private List<Filme>      filmes        = null;
    private Runnable         carregaFilmes;
    private MovieListAdapter listAdapter ;
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_categories );
        filmes = new LinkedList<Filme>( ) ;
        listAdapter = new MovieListAdapter( this, R.layout.movies_list_row, filmes ) ;
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
        
        carregaFilmes = new Runnable( )
        {
            public void run( )
            {
                carregaFilmes( ) ;
            }
        };
        Thread thread =  new Thread(null, carregaFilmes, "MagentoBackground");
        thread.start();
        loadingMovies = ProgressDialog.show(PlaylistActivity.this, "Aguarde...", "Carregando Lista ...", true);
        this.setTitle( getResources( ).getString( R.string.myList ) ) ;
    }
    
    private void carregaFilmes( )
    {
        String xml = "<categoria numFilmes=\"1\"><categoriaNome>Ação</categoriaNome><categoriaUrl>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=1</categoriaUrl><filme><id>7</id><nome>Os Mercenários</nome><urlImagem>http://localhost:48080/Videos/A Origem/inception_32.jpg</urlImagem><urlFilme>http://localhost:8080/MobileRequest/procuraPlaylistPadrao?idFilme=7</urlFilme><duracao>8430</duracao><diretores>cqwdqwd</diretores><atores>wdwsd</atores><sinopse>qwdqw</sinopse><ano>2010</ano><avaliacao>4</avaliacao><idiomas><codigo>br</codigo><id>1</id><nome>Portugês</nome><urlIdioma>http://localhost:8080/MobileRequest/procuraPlaylist?idFilme=7&#38;idIdioma=1</urlIdioma></idiomas></filme><filme><id>8</id><nome>Os Vingadores</nome><urlImagem>http://localhost:48080/Videos/A Origem/inception_32.jpg</urlImagem><urlFilme>http://localhost:8080/MobileRequest/procuraPlaylistPadrao?idFilme=7</urlFilme><duracao>8520</duracao><diretores>cqwdqwd</diretores><atores>wdwsd</atores><sinopse>qwdqw</sinopse><ano>2010</ano><avaliacao>4</avaliacao><idiomas><codigo>br</codigo><id>1</id><nome>Portugês</nome><urlIdioma>http://localhost:8080/MobileRequest/procuraPlaylist?idFilme=7&#38;idIdioma=1</urlIdioma></idiomas></filme></categoria>";
        XmlFilmeParser parser = new XmlFilmeParser( xml, false );
        try
        {
            filmes = parser.parseFilmes( ) ;
            
            for( Filme film : filmes )
            {
                Log.w( "Filmes", film.getNome( ) ) ;
            }
        }
        catch( Exception e )
        {
            e.printStackTrace( ) ;
        }
        runOnUiThread(returnRes);
    }
    
    private Runnable returnRes = new Runnable( ) 
    {
        public void run( ) 
        {
            if ( filmes != null && filmes.size( ) > 0 )
            {
                listAdapter.notifyDataSetChanged( ) ;
                for(int i=0 ; i<filmes.size( ) ; i++ )
                {
                    listAdapter.add( filmes.get( i ) ) ;
                }
            }
            loadingMovies.dismiss( ) ;
            listAdapter.notifyDataSetChanged( ) ;
        }
    };
    
    private class MovieListAdapter extends ArrayAdapter<Filme>
    {
        private List<Filme> items;
        
        public MovieListAdapter( Context context, int textViewResourceId, List<Filme> items )
        {
            super( context, textViewResourceId, items ) ;
            this.items = items ;
        }
        
        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            View v = convertView;

            if ( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                v = vi.inflate( R.layout.movies_list_row, null );
            }
            
            final Filme filme = items.get( position ) ;
            
            if( filme != null )
            {
                TextView nomeFilme = (TextView) v.findViewById( R.id.nomeFilme ) ;
                TextView duracaoFilme = (TextView) v.findViewById( R.id.duracaoFilme ) ;
                TextView anoFilme = (TextView) v.findViewById( R.id.anoFilme ) ;
                RatingBar avaliacaoFilme = (RatingBar) v.findViewById( R.id.avaliacaoFilme );
                ImageView image = (ImageView) v.findViewById( R.id.imagemFilme );
                //Button assistir = (Button) v.findViewById( R.id.buttonPlay ) ;
                
                int avaliacao = filme.getAvaliacao( ) ;
                
                try
                {
                
                    if( nomeFilme != null )
                        nomeFilme.setText( filme.getNome( ) ) ;
                    if( duracaoFilme != null )
                        duracaoFilme.setText( filme.getStrDuracao( ) ) ;
                    if( anoFilme != null )
                        anoFilme.setText( String.valueOf( filme.getAno( ) ) ) ;
                    if( avaliacaoFilme != null )
                        avaliacaoFilme.setProgress( avaliacao ) ;
                    
                    switch ( position )
                    {
                        case 0 : image.setImageResource( R.drawable.mercenarios ) ; break;
                        case 1 : image.setImageResource( R.drawable.vingadores ) ; break;
                        default : image.setImageResource( R.drawable.origem ) ; break;
                    }
                    
                    
                   /* assistir.setOnClickListener( new OnClickListener( )
                    {
                        public void onClick( View v )
                        {
                            Toast.makeText( ListMoviesActivity.this, "Assisti: " + filme.getNome( ), Toast.LENGTH_LONG ).show( ) ;
                        }
                    } ) ;*/
                }
                catch(Exception e )
                {
                    e.printStackTrace( );
                }
            }
            
            return v ;
        }
    }
    
    @Override
    protected void onListItemClick( ListView l, View v, int position, long id )
    {
        super.onListItemClick( l, v, position, id );
        
        Object o = this.getListAdapter( ).getItem( position ) ;
        
        Filme filme = (Filme) o;
        
        Intent it = new Intent( this, DetalhesFilmes.class ) ;
        it.putExtra( "filmeId", filme.getId( ) ) ;
        startActivity( it ) ;
    }
}
