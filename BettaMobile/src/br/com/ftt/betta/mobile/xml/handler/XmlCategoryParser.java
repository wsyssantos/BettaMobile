package br.com.ftt.betta.mobile.xml.handler;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import android.util.Xml;
import br.com.ftt.betta.mobile.dao.Categoria;

public class XmlCategoryParser extends XmlParser
{

    public XmlCategoryParser( String url, boolean isUrl )
    {
        super( url, isUrl );
    }

    public List<Categoria> parseCategories( )
    {
        XmlCategoryHandler handler = new XmlCategoryHandler( );
        try
        {
            if ( isUrl )
            {
                Xml.parse( getInputStream( ), Xml.Encoding.UTF_8, handler );
            }
            else
            {
                Xml.parse( xmlContent, handler );
            }
        }
        catch ( SAXException e )
        {
            e.printStackTrace( );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return handler.getCategorias( );
    }
}
