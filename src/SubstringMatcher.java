public class SubstringMatcher
    extends Matcher
{
    public int Match( Product product, Listing listing )
    {
	return (listing.Get_title().indexOf( product.Get_product_name() ) != -1 ? 0 : 1)
	     + (product.Get_family() == null ? 0 : (listing.Get_title().indexOf( product.Get_family()) != -1 ? 0 : 1))
	     + (Math.max( listing.Get_title().indexOf( product.Get_manufacturer() ),
			  listing.Get_manufacturer().indexOf( product.Get_manufacturer() ) ) != -1 ? 0 : 1);
    }
}

