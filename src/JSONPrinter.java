import java.io.PrintStream;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONPrinter
    extends MatchingEngineListener
    implements AutoCloseable
{
    protected PrintStream m_out;

    public JSONPrinter( PrintStream out )
    {
	m_out = out;
    }

    protected String GetResult( Product product, Map< Listing, Integer > matchingListings )
    {
	JSONArray listingsJsonArray = new JSONArray();
	for( Listing listing : matchingListings.keySet() )
	{
	    while( listing instanceof WrappedListing ) {
		listing = ((WrappedListing) listing).Get_parent_listing();
	    }
	    JSONObject listingJsonObject = new JSONObject();
	    listingJsonObject.put( "title", listing.Get_title() );
	    listingJsonObject.put( "manufacturer", listing.Get_manufacturer() );
	    listingJsonObject.put( "currency", listing.Get_currency() );
	    listingJsonObject.put( "price", listing.Get_price() );

	    listingsJsonArray.add( listingJsonObject );
	}

	JSONObject result = new JSONObject();
	while( product instanceof WrappedProduct ) {
	    product = ((WrappedProduct) product).Get_parent_product();
	}
	result.put( "product_name", product.Get_product_name() );
	result.put( "listings", listingsJsonArray );

	return result.toJSONString();
    }

    @Override
    public void Matched( Product product, Map< Listing, Integer > listings )
    {
	m_out.println( GetResult( product, listings ) );
    }

    public void close()
    {
	m_out.close();
    }
}

