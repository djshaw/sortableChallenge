import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NormalizedProductTest
{
    @Test
    public void testNormalizedListing()
    {
	NormalizedProduct np = new NormalizedProduct( new LowercaseNormalizer(), new ConcreteProduct( "A", "B", "C", "D", "2010-01-06T19:00:00.000-05:00" ) );

	// Check that everything is normalized
	assertEquals( "a", np.Get_product_name() );
	assertEquals( "b", np.Get_manufacturer() );
	assertEquals( "c", np.Get_model() );
	assertEquals( "d", np.Get_family() );
	assertEquals( "2010-01-06T19:00:00.000-05:00", np.Get_announced_date() );

	// Make sure we can still get the parent listing
	assertEquals( "A", np.Get_parent_product().Get_product_name() );
	assertEquals( "B", np.Get_parent_product().Get_manufacturer() );
	assertEquals( "C", np.Get_parent_product().Get_model() );
	assertEquals( "D", np.Get_parent_product().Get_family() );
	assertEquals( "2010-01-06T19:00:00.000-05:00", np.Get_parent_product().Get_announced_date() );
    }
}

