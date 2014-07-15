package br.com.ftt.betta.moblie.util;

public class StringUtil
{
    public static boolean isEmpty( String value )
    {
        return ( value == null || value.trim( ).length( ) == 0 ) ;
    }
    
    public static boolean isNotEmpty( String value )
    {
        return ( !isEmpty( value ) ) ;
    }
    
    public static String formatIntoHHMMSS(int secsIn)
    {
        int hours = secsIn / 3600,
        remainder = secsIn % 3600,
        minutes = remainder / 60,
        seconds = remainder % 60;
    
        return ( (hours < 10 ? "0" : "") + hours
        + ":" + (minutes < 10 ? "0" : "") + minutes
        + ":" + (seconds< 10 ? "0" : "") + seconds );
    }
}
