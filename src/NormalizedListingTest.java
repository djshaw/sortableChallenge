import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NormalizedListingTest
{
    @Test
    public void testNormalizedListing()
    {
	NormalizedListing nl = new NormalizedListing( new LowercaseNormalizer(), new ConcreteListing( "A", "B", "CAD", "0.00" ) );

	// Check that everything is normalized
	assertEquals( "a", nl.Get_title() );
	assertEquals( "b", nl.Get_manufacturer() );
	assertEquals( "CAD", nl.Get_currency() );
	assertEquals( "0.00", nl.Get_price() );

	// Make sure we can still get the parent listing
	assertEquals( "A", nl.Get_parent_listing().Get_title() );
	assertEquals( "B", nl.Get_parent_listing().Get_manufacturer() );
	assertEquals( "CAD", nl.Get_parent_listing().Get_currency() );
	assertEquals( "0.00", nl.Get_parent_listing().Get_price() );
    }
}

