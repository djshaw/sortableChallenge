import java.util.Map;
import java.util.HashMap;

public class RegexNormalizer implements Normalizer
{
    protected Normalizer m_normalizer;

    protected Map< String, String > m_stringsToReplace = null;

    public RegexNormalizer( Normalizer n )
    {
	m_normalizer = n;
	m_stringsToReplace = new HashMap< String, String >();
    }

    public RegexNormalizer( Map< String, String > stringsToReplace, Normalizer n )
    {
	m_normalizer = n;
	m_stringsToReplace = stringsToReplace;
    }
    
    public String Normalize( String s )
    {
	if( s == null ) {
	    return s;
	}

	for( String remove : m_stringsToReplace.keySet() ) {
	    s = s.replaceAll( remove, m_stringsToReplace.get( remove ) );
	}

	return m_normalizer.Normalize( s );
    }
}

