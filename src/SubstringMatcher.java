public class SubstringMatcher
    extends Matcher
{
    public int Match( Product product, Listing listing )
    {
	return (listing.GetTitle().indexOf( product.GetProductName() ) != -1 ? 0 : 1)
	     + (product.GetFamily() == null ? 0 : (listing.GetTitle().indexOf( product.GetFamily()) != -1 ? 0 : 1))
	     + (Math.max( listing.GetTitle().indexOf( product.GetManufacturer() ),
			  listing.GetManufacturer().indexOf( product.GetManufacturer() ) ) != -1 ? 0 : 1);
    }
}

