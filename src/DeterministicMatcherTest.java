import java.lang.reflect.Method;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DeterministicMatcherTest
{
    @Test
    public void testExpandMatchToWordBoundary()
	throws Exception
    {	
	Method method = DeterministicMatcher.class.getDeclaredMethod( "expandMatchToWordBoundary", Levenshtein.LevenshteinMatch.class );

	assertEquals( new Levenshtein.LevenshteinMatch( "foo", 0, 0, 3 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( "foo", 0, 0, 3 ) ) );

	// space on the left
	assertEquals( new Levenshtein.LevenshteinMatch( " foo", 0, 1, 4 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( " foo", 0, 1, 4 ) ) );

	// push left 1
	assertEquals( new Levenshtein.LevenshteinMatch( "foo", 1, 0, 3 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( "foo", 0, 1, 3 ) ) );

	// push left 1, space on the left
	assertEquals( new Levenshtein.LevenshteinMatch( " foo", 1, 1, 4 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( " foo", 0, 2, 4 ) ) );


	// space on the right
	assertEquals( new Levenshtein.LevenshteinMatch( "foo ", 0, 0, 3 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( "foo ", 0, 0, 3 ) ) );
	
	// push right 1
	assertEquals( new Levenshtein.LevenshteinMatch( "abc", 1, 0, 3 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( "abc", 0, 0, 2 ) ) );

	// push left 1, right 1
	assertEquals( new Levenshtein.LevenshteinMatch( " foo ", 2, 1, 4 ), 
		      (Levenshtein.LevenshteinMatch) method.invoke( null, new Levenshtein.LevenshteinMatch( " foo ", 0, 2, 3 ) ) );
    }

    @Test
    public void testSimpleMatch()
    {
	DeterministicMatcher matcher = new DeterministicMatcher();

	// Exact match on manufacturer, model, and family (manufacturer in the title of the listing)
	assertEquals( 0, matcher.Match( new ConcreteProduct( "NAME", "foo", "bar", "baz", "" ), 
					new ConcreteListing( "foo bar baz",  "", "", "" ) ) );

	// Exact match on manufacturer, model, and faily (manufacturer in manufacturer field of the listing)
	assertEquals( 0, matcher.Match( new ConcreteProduct( "NAME",	"foo", "bar", "baz", "" ), 
					new ConcreteListing( "bar baz", "foo", "", "" ) ) );

	// null family
	assertEquals( 0, matcher.Match( new ConcreteProduct( "NAME",	"foo", "bar", null, "" ), 
					new ConcreteListing( "foo bar baz", "", "", "" ) ) );

	// Nothing matches
	assertEquals( 9, matcher.Match( new ConcreteProduct( "NAME",	"foo", "bar", "baz", "" ), 
					new ConcreteListing( "ccc", "", "", "" ) ) );
    }

    @Test
    public void fujifilmTest()
    {
	DeterministicMatcher matcher = new DeterministicMatcher();
	
	Normalizer normalizer = new AccentNormalizer( new LowercaseNormalizer() );
	Listing listing = new NormalizedListing(
	    normalizer,
	    new ConcreteListing( "Fujifilm - FinePix XP10 - Appareil photo num??rique - 12 Mpix - Noir",
				 "FUJIFILM",
				 "EUR",
				 "104.99" ) );

	Product product = new NormalizedProduct(
	    normalizer,
	    new ConcreteProduct( "Fujifilm_FinePix_XP10",
				 "Fujifilm",
				 "XP10",
				 "FinePix",
				 "2010-02-01T19:00:00.000-05:00" ) );

	assertEquals( 0, matcher.Match( product, listing ) );
    }
    
    @Test
    public void canonTest()
    {
	DeterministicMatcher matcher = new DeterministicMatcher();

	Normalizer normalizer = new IdentityNormalizer();
	Listing listing = new ConcreteListing( "canon powershot sx130is 12 1 mp digital camera with 12x wide angle optical image stabilized zoom with 3 0inch lcd",
					       "canon",
					       "FOO",
					       "0.00" );
	Product product = new ConcreteProduct( "FOO",
					       "canon",
					       "sx130 is",
					       "powershot",
					       "2010-02-01T19:00:00.000-05:00" );

	assertEquals( 1, matcher.Match( product, listing ) );
    }
}

