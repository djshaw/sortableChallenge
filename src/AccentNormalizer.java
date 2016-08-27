public class AccentNormalizer implements Normalizer
{
    protected Normalizer m_normalizer;

    public AccentNormalizer()
    {
	m_normalizer = new IdentityNormalizer();
    }

    public AccentNormalizer( Normalizer normalize )
    {
	m_normalizer = normalize;
    }

    public String Normalize( String s )
    {
	if( s == null ) {
	    return s;
	}

	return m_normalizer.Normalize( 
	    java.text.Normalizer.normalize( s, java.text.Normalizer.Form.NFD ).replaceAll( "[\\p{InCombiningDiacriticalMarks}]", "") );
    }
}

