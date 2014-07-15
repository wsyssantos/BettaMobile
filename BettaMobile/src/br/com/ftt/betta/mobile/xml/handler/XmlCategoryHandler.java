package br.com.ftt.betta.mobile.xml.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.ftt.betta.mobile.dao.Categoria;

public class XmlCategoryHandler extends DefaultHandler
{
    private Categoria       currentCategory;
    private List<Categoria> categorias;
    private StringBuilder   builder;
    
    public List<Categoria> getCategorias( )
    {
        return categorias ;
    }

    @Override
    public void startDocument( ) throws SAXException
    {
        super.startDocument( );
        builder = new StringBuilder( );
        categorias = new ArrayList<Categoria>( ) ;
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
        if ( localName.equalsIgnoreCase( "categoria" ) )
        {
            this.currentCategory = new Categoria( );
        }
    }
    
    @Override
    public void endElement( String uri, String localName, String qName ) throws SAXException
    {
        super.endElement( uri, localName, qName );
        
        if ( this.currentCategory != null )
        {
            if (localName.equalsIgnoreCase( "nome" ) )
            {
                currentCategory.setNome( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "url" ) )
            {
                currentCategory.setUrl( builder.toString( ) ) ;
            }
            else if ( localName.equalsIgnoreCase( "categoria" ) )
            {
                categorias.add( currentCategory ) ;
            }
            builder.setLength(0);
        }
    }
}
