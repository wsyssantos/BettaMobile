package br.com.ftt.betta.mobile.activities;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.ftt.betta.mobile.dao.Filme;
import br.com.ftt.betta.mobile.xml.handler.XmlFilmeParser;

public class DetalhesFilmes extends BettaActivity
{
    private Runnable carregaDetalhes;
    private ProgressDialog loadingDetalhes = null; 
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState ) ;
        setContentView( R.layout.detalhes_filmes ) ;
        
        carregaDetalhes = new Runnable( )
        {
            public void run( )
            {
                carregaDetalhes( ) ;
            }
        };
        Thread thread =  new Thread(null, carregaDetalhes, "MagentoBackground");
        thread.start();
        loadingDetalhes = ProgressDialog.show(DetalhesFilmes.this, "Aguarde...", "Carregando...", true);
    }
    
    private Runnable returnRes = new Runnable( ) 
    {
        public void run( ) 
        {
            loadingDetalhes.dismiss( ) ;
        }
    };
    
    private void carregaDetalhes( )
    {
        String xml = "<categoria numFilmes=\"1\"><categoriaNome>Ação</categoriaNome><categoriaUrl>http://localhost:8080/MobileRequest/buscaFilmesCategoria?categoriaId=1</categoriaUrl><filme><id>7</id><nome>Os Vingadores</nome><urlImagem>http://localhost:48080/Videos/A Origem/inception_32.jpg</urlImagem><urlFilme>http://localhost:8080/MobileRequest/procuraPlaylistPadrao?idFilme=7</urlFilme><duracao>8520</duracao><diretores>Joss Whedon</diretores><atores>Robert Downey Jr., Chris Evans e Scarlett Johansson</atores><sinopse>Em 'Os Vingadores', quando um inimigo inesperado surge ameaçando a segurança global, Nick Fury (Samuel L. Jackson), diretor da agência internacional de paz conhecido como SHIELD, recruta uma equipe para livrar o mundo de uma possível destruição: Homem de Ferro (Robert Downey Jr.), Capitão América (Chris Evans), Thor (Chris Hemsworth), Hulk (Mark Ruffalo), Gavião Arqueiro (Jeremy Renner) e Viúva Negra (Scarlett Johansson). Baseado na popular série de revistas em quadrinhos da Marvel \"The Avengers\", publicada pela primeira vez em 1963, e desde então uma instituição.</sinopse><ano>2012</ano><avaliacao>5</avaliacao><idiomas><codigo>br</codigo><id>1</id><nome>Portugês</nome><urlIdioma>http://localhost:8080/MobileRequest/procuraPlaylist?idFilme=7&#38;idIdioma=1</urlIdioma></idiomas></filme></categoria>";
        XmlFilmeParser parser = new XmlFilmeParser( xml, false );
        try
        {
            List<Filme> filmes = parser.parseFilmes( ) ;
            if( filmes != null && !filmes.isEmpty( ) )
            {
                Filme filme = filmes.get( 0 );
                
                if( filme != null )
                {
                    TextView nomeFilme = (TextView) findViewById( R.id.nomeDetalheFilme ) ;
                    RatingBar avaliacao = (RatingBar) findViewById( R.id.avaliacaoDetalheFilme ) ;
                    TextView ano = (TextView) findViewById( R.id.anoDetalheFilme ) ;
                    TextView duracao = (TextView) findViewById( R.id.duracaoDetalheFilme ) ;
                    TextView direcao = (TextView) findViewById( R.id.labelDiretores ) ;
                    TextView elenco = (TextView) findViewById( R.id.labelElenco ) ;
                    TextView sinopse = (TextView) findViewById( R.id.labelSinopse ) ;
                    ImageView imagem = (ImageView) findViewById( R.id.imagemDetalheFilme ) ;
                    
                    nomeFilme.setText( filme.getNome( ) ) ;
                    avaliacao.setRating( filme.getAvaliacao( ) ) ;
                    ano.setText( String.valueOf( filme.getAno( ) ) ) ;
                    duracao.setText( filme.getStrDuracao( ) ) ;
                    direcao.setText( filme.getDiretores( ) ) ;
                    elenco.setText( filme.getAtores( ) ) ;
                    sinopse.setText( filme.getSinopse( ) ) ;
                    
                    imagem.setImageResource( R.drawable.vingadores ) ;
                    //imagem.setImageURI( new Uri( filme.getUrlImagem( ) ) ) ;
                    
                    
                }
            }
            
        }
        catch( Exception e )
        {
            e.printStackTrace( ) ;
        }

        runOnUiThread(returnRes);
    }
}
