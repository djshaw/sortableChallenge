public class LowercaseNormalizer implements Normalizer
{
    protected Normalizer m_normalizer;

    public LowercaseNormalizer()
    {
	m_normalizer = new IdentityNormalizer();
    }

    public LowercaseNormalizer( Normalizer n )
    {
	m_normalizer = n;
    }

    public String Normalize( String s )
    {
	return m_normalizer.Normalize( s.toLowerCase() );
    }
}

