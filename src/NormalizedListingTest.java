import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NormalizedListingTest
{
    @Test
    public void testNormalizedListing()
    {
	NormalizedListing nl = new NormalizedListing( new LowercaseNormalizer(), new ConcreteListing( "A", "B", "CAD", "0.00" ) );

	// Check that everything is normalized
	assertEquals( "a", nl.GetTitle() );
	assertEquals( "b", nl.GetManufacturer() );
	assertEquals( "CAD", nl.GetCurrency() );
	assertEquals( "0.00", nl.GetPrice() );

	// Make sure we can still get the parent listing
	assertEquals( "A", nl.GetParentListing().GetTitle() );
	assertEquals( "B", nl.GetParentListing().GetManufacturer() );
	assertEquals( "CAD", nl.GetParentListing().GetCurrency() );
	assertEquals( "0.00", nl.GetParentListing().GetPrice() );
    }
}

