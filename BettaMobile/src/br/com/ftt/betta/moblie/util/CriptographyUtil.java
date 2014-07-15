package br.com.ftt.betta.moblie.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptographyUtil
{
    public static String crypt( String senha )
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "MD5" );
            digest.update( senha.getBytes( ) );

            byte messageDigest[] = digest.digest( );
            StringBuffer hexString = new StringBuffer( );
            
            for ( int i = 0 ; i < messageDigest.length ; i++ )
            {
                hexString.append( Integer.toHexString( 0xFF & messageDigest[ i ] ) );
            }
            return hexString.toString( );
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace( );
            return senha;
        }
    }
}
