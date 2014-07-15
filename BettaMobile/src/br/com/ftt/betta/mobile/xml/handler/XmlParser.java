package br.com.ftt.betta.mobile.xml.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class XmlParser
{
    protected URL xmlUrl ;
    protected String xmlContent ;
    protected boolean isUrl ;
    
    public XmlParser( String url, boolean isUrl )
    {
        this.isUrl = isUrl ;
        try
        {
            if( this.isUrl )
            {
                xmlUrl = new URL( url ) ;
            }
            else
            {
                xmlContent = url ;
            }
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }
    }
    
    public InputStream getInputStream( )
    {
        try
        {
            return xmlUrl.openConnection( ).getInputStream( ) ;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return null ;
    }
    
}
