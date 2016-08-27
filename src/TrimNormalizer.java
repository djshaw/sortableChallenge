public class TrimNormalizer implements Normalizer
{
    protected Normalizer m_normalizer;

    public TrimNormalizer( Normalizer n )
    {
	m_normalizer = n;
    }
    
    public String Normalize( String s )
    {
	if( s == null ) {
	    return s;
	}

	return m_normalizer.Normalize( s.trim() );
    }
}

