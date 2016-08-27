public class NormalizedListing extends WrappedListing
{
    protected Listing m_listing;
    protected String m_normalizedTitle;
    protected String m_normalizedManufacturer;

    public NormalizedListing( Normalizer normalizer,
			      Listing	 listing )
    {
	m_listing = listing;
	m_normalizedTitle = normalizer.Normalize( m_listing.Get_title() );
	m_normalizedManufacturer = normalizer.Normalize( m_listing.Get_manufacturer() );
    }

    public Listing Get_parent_listing()
    {
	return m_listing;
    }

    public String Get_title()
    {
	return m_normalizedTitle;
    }

    public String Get_manufacturer()
    {
	return m_normalizedManufacturer;
    }

    public String Get_currency()
    {
	return m_listing.Get_currency();
    }

    public String Get_price()
    {
	return m_listing.Get_price();
    }

    public String toString()
    {
	return m_listing.toString();
    }
}

