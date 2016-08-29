import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NormalizedProductTest
{
    @Test
    public void testNormalizedListing()
    {
	NormalizedProduct np = new NormalizedProduct( new LowercaseNormalizer(), new ConcreteProduct( "A", "B", "C", "D", "2010-01-06T19:00:00.000-05:00" ) );

	// Check that everything is normalized
	assertEquals( "a", np.GetProductName() );
	assertEquals( "b", np.GetManufacturer() );
	assertEquals( "c", np.GetModel() );
	assertEquals( "d", np.GetFamily() );
	assertEquals( "2010-01-06T19:00:00.000-05:00", np.GetAnnouncedDate() );

	// Make sure we can still get the parent listing
	assertEquals( "A", np.GetParentProduct().GetProductName() );
	assertEquals( "B", np.GetParentProduct().GetManufacturer() );
	assertEquals( "C", np.GetParentProduct().GetModel() );
	assertEquals( "D", np.GetParentProduct().GetFamily() );
	assertEquals( "2010-01-06T19:00:00.000-05:00", np.GetParentProduct().GetAnnouncedDate() );
    }
}

