public class ListingView
    extends HTMLView
{
    protected Listing m_listing;

    public ListingView( Listing listing )
    {
	m_listing = listing;
    }

    @Override
    public String Render()
    {
	return "<table><tr><td><b>title</b>:</td><td>" + m_listing.GetTitle() + "</td></tr>"
	            + "<tr><td><b>manufacturer</b>:</td><td>" + m_listing.GetManufacturer() + "</td></tr>"
	     + "</table>";
    }
}

