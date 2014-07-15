package br.com.ftt.betta.mobile.xml.handler;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.ftt.betta.mobile.dao.Filme;
import br.com.ftt.betta.mobile.dao.Idioma;

public class XmlFilmeHandler extends DefaultHandler
{
    private Filme         currentMovie;
    private Idioma        currentIdioma;
    private List<Filme>   filmes;
    private StringBuilder builder;
    private boolean       checkingIdioma;

    public List<Filme> getFilmes( )
    {
        return filmes;
    }

    @Override
    public void startDocument( ) throws SAXException
    {
        super.startDocument( );
        builder = new StringBuilder( );
        filmes = new LinkedList<Filme>( );
    }

    @Override
    public void characters( char[ ] ch, int start, int length )  throws SAXException
    {
        super.characters( ch, start, length );
        builder.append( ch, start, length );
    }

    @Override
    public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
    {
        super.startElement( uri, localName, qName, attributes );
        if ( localName.equalsIgnoreCase( "filme" ) )
        {
            this.currentMovie = new Filme( );
            this.currentMovie.setIdiomas( new LinkedList<Idioma>( ) ) ;
        }
        else if ( localName.equalsIgnoreCase( "idiomas" ) )
        {
            checkingIdioma = true ;
            this.currentIdioma = new Idioma( ) ;
        }
    }
    
    @Override
    public void endElement( String uri, String localName, String qName ) throws SAXException
    {
        super.endElement( uri, localName, qName );
        
        if( this.currentMovie != null )
        {
            if ( localName.equalsIgnoreCase( "id" ) )
            {
                if( !checkingIdioma )
                {
                    currentMovie.setId( Integer.valueOf( builder.toString( ) ) ) ;
                }
                else
                {    
                    currentIdioma.setId( Integer.valueOf( builder.toString( ) ) ) ;
                }
            }
            else if ( localName.equalsIgnoreCase( "nome" ) )
            {
                if( !checkingIdioma )
                {
                    currentMovie.setNome( builder.toString( ) ) ;
                }
                else
                {    
                    currentIdioma.setNome( builder.toString( ) ) ;
                }
            }
            else if ( localName.equalsIgnoreCase( "urlImagem" ) )
            {
                currentMovie.setUrlImagem( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "urlFilme" ) )
            {
                currentMovie.setUrlFilme( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "duracao" ) )
            {
                currentMovie.setDuracao( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "diretores" ) )
            {
                currentMovie.setDiretores( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "atores" ) )
            {
                currentMovie.setAtores( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "sinopse" ) )
            {
                currentMovie.setSinopse( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "ano" ) )
            {
                currentMovie.setAno( Integer.parseInt( builder.toString( ) ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "avaliacao" ) )
            {
                currentMovie.setAvaliacao( Integer.parseInt( builder.toString( ) ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "codigo" ) )
            {
                currentIdioma.setCodigo( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "urlIdioma" ) )
            {
                currentIdioma.setUrlIdioma( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "idiomas" ) )
            {
                checkingIdioma = false ;
                currentMovie.getIdiomas( ).add( currentIdioma ) ;
            }
            else if ( localName.equalsIgnoreCase( "filme" ) )
            {
                filmes.add( currentMovie ) ;
            }
        }

        builder.setLength(0);
    }
}
