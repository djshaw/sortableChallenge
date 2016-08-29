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

    // The json library uses raw type collections internally. Suppress 
    // unchecked warnings, otherwise the compile will not succeed.
    @SuppressWarnings( "unchecked" )
    protected String GetResult( Product product, Map< Listing, Integer > matchingListings )
    {
	JSONArray listingsJsonArray = new JSONArray();
	for( Listing listing : matchingListings.keySet() )
	{
	    while( listing instanceof WrappedListing ) {
		listing = ((WrappedListing) listing).GetParentListing();
	    }
	    JSONObject listingJsonObject = new JSONObject();
	    listingJsonObject.put( "title", listing.GetTitle() );
	    listingJsonObject.put( "manufacturer", listing.GetManufacturer() );
	    listingJsonObject.put( "currency", listing.GetCurrency() );
	    listingJsonObject.put( "price", listing.GetPrice() );

	    listingsJsonArray.add( listingJsonObject );
	}

	JSONObject result = new JSONObject();
	while( product instanceof WrappedProduct ) {
	    product = ((WrappedProduct) product).GetParentProduct();
	}
	result.put( "product_name", product.GetProductName() );
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

