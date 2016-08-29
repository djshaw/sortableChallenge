import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

class SubstringMatcherFactory
    extends MatcherFactory
{
    public Matcher GetMatcher()
    {
	return new SubstringMatcher();
    }
}

public class MatchingEngineTest
{
    @Test
    public void test()
    {
	MatchingEngine engine = new MatchingEngine( new SubstringMatcherFactory(), 1 );

	Product product_a = new ConcreteProduct( "a", "b", "", "", "" );
	Product product_c = new ConcreteProduct( "c", "d", "", "", "" );

	Listing listing_a = new ConcreteListing( "a b", "", "", "" );
	Listing listing_c = new ConcreteListing( "c d", "", "", "" );
	HashMap< Product, Map< Listing, Integer > > expected = new HashMap< Product, Map< Listing, Integer > >();
	HashMap< Listing, Integer > expected_listing_a = new HashMap< Listing, Integer >();
	expected_listing_a.put( listing_a, 0 );
	HashMap< Listing, Integer > expected_listing_c = new HashMap< Listing, Integer >();
	expected_listing_c.put( listing_c, 0 );
	expected.put( product_a, expected_listing_a );
	expected.put( product_c, expected_listing_c );

	Map< Product, Map< Listing, Integer > > actual = 
	    engine.Match( new HashSet< Product >( Arrays.asList( product_a, product_c ) ), 
			  new HashSet< Listing >( Arrays.asList( listing_a, listing_c ) ) );
	assertEquals( expected, actual );
    }

    @Test
    public void testNoMatches()
    {
	MatchingEngine engine = new MatchingEngine( new SubstringMatcherFactory(), 1 );
	Product product = new ConcreteProduct( "a", "b", "", "", "" );

	HashMap< Product, Map< Listing, Integer > > expected = new HashMap< Product, Map< Listing, Integer > >();
	expected.put( product, new HashMap< Listing, Integer >() );
	assertEquals( expected, engine.Match( new HashSet< Product >( Arrays.asList( product ) ),
					      new HashSet< Listing >() ) );
    }
}

