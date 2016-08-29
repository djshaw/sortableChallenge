public class NormalizedListing extends WrappedListing
{
    protected Listing m_listing;
    protected String m_normalizedTitle;
    protected String m_normalizedManufacturer;

    public NormalizedListing( Normalizer normalizer,
			      Listing	 listing )
    {
	m_listing = listing;
	m_normalizedTitle = normalizer.Normalize( m_listing.GetTitle() );
	m_normalizedManufacturer = normalizer.Normalize( m_listing.GetManufacturer() );
    }

    public Listing GetParentListing()
    {
	return m_listing;
    }

    public String GetTitle()
    {
	return m_normalizedTitle;
    }

    public String GetManufacturer()
    {
	return m_normalizedManufacturer;
    }

    public String GetCurrency()
    {
	return m_listing.GetCurrency();
    }

    public String GetPrice()
    {
	return m_listing.GetPrice();
    }

    public String toString()
    {
	return m_listing.toString();
    }
}

