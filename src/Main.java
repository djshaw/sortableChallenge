import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// TODO: make sure everything works on a fresh checkout
// TODO: normalize use of _ in identifiers
public class Main
{
    static private Map< String, String > g_stringReplacerNormalizerMap = new HashMap< String, String >();
    static private Map< String, String > g_regexNormalizerMap = new HashMap< String, String >();

    static
    {
	// Assume, for our sanity, that none of these characters are required to 
	// provide uniqueness in any product's identity

	g_stringReplacerNormalizerMap.put( "-", "" );

	// In order of ascii character value
	g_stringReplacerNormalizerMap.put( "!", " " );
	g_stringReplacerNormalizerMap.put( "\"", " " );
	g_stringReplacerNormalizerMap.put( "#", " " );
	g_stringReplacerNormalizerMap.put( "$", " " );
	g_stringReplacerNormalizerMap.put( "%", " " );
	g_stringReplacerNormalizerMap.put( "&", " " );
	g_stringReplacerNormalizerMap.put( "'", " " );
	g_stringReplacerNormalizerMap.put( "(", " " );
	g_stringReplacerNormalizerMap.put( ")", " " );
	g_stringReplacerNormalizerMap.put( "*", " " );
	g_stringReplacerNormalizerMap.put( "+", " " );
	g_stringReplacerNormalizerMap.put( ",", " " );
	// ("-" is removed, not replaced)
	g_stringReplacerNormalizerMap.put( ".", " " );
	g_stringReplacerNormalizerMap.put( "/", " " );
	// 0-9
	g_stringReplacerNormalizerMap.put( ":", " " );
	g_stringReplacerNormalizerMap.put( ";", " " );
	g_stringReplacerNormalizerMap.put( "<", " " );
	g_stringReplacerNormalizerMap.put( "=", " " );
	g_stringReplacerNormalizerMap.put( ">", " " );
	g_stringReplacerNormalizerMap.put( "?", " " );
	g_stringReplacerNormalizerMap.put( "@", " " );
	// A-Z
	g_stringReplacerNormalizerMap.put( "[", " " );
	g_stringReplacerNormalizerMap.put( "\\", " " );
	g_stringReplacerNormalizerMap.put( "]", " " );
	g_stringReplacerNormalizerMap.put( "^", " " );
	g_stringReplacerNormalizerMap.put( "_", " " );
	g_stringReplacerNormalizerMap.put( "`", " " );
	// a-z
	g_stringReplacerNormalizerMap.put( "{", " " );
	g_stringReplacerNormalizerMap.put( "|", " " );
	g_stringReplacerNormalizerMap.put( "}", " " );
	g_stringReplacerNormalizerMap.put( "~", " " );

	g_regexNormalizerMap.put( "\\s+", " " );
    }
    protected static Normalizer g_normalizer = 
	new AccentNormalizer( 
	    new LowercaseNormalizer( 
		new SubstringReplacerNormalizer( 
		    g_stringReplacerNormalizerMap,
		    new RegexNormalizer( 
			g_regexNormalizerMap,
			new TrimNormalizer( 
			    new IdentityNormalizer() ) ) ) ) );

    protected static void Usage()
    {
	System.err.println( "Usage():" );
	System.err.println( "\tjava Main [--debug] $PRODUCTS_FILE $LISTINGS_FILE" );
    }

    protected static Normalizer Get_normalizer()
    {
	return g_normalizer;
    }

    protected static Product ParseProductFromJson( String json )
    	throws Exception
    {
	JSONParser jsonParser = new JSONParser();

	// We could validate the returned object and print out a reasonable 
	// error message when it's not an object
	JSONObject o = (JSONObject) jsonParser.parse( json );

	// Similarly, we could verify that each json object contains a product_name, 
	// manufacturer, model, family, and announced-date fields and that they are all 
	// strings
	return new NormalizedProduct(
	    Get_normalizer(),
	    new ConcreteProduct( (String) o.get( "product_name" ), 
				 (String) o.get( "manufacturer" ), 
				 (String) o.get( "model" ), 
				 (String) o.get( "family" ), // Family is an optional field
				 (String) o.get( "announced-date" ) ) );
    }

    protected static Listing ParseListingFromJson( String json )
    	throws Exception
    {
	JSONParser jsonParser = new JSONParser();

	// We could validate the returned object and print out a reasonable 
	// error message when it's not an object
	JSONObject o = (JSONObject) jsonParser.parse( json );

	// Similarly, we could verify that each json object contains a title, 
	// manufacturer, currency, and price fields and that they are all 
	// strings
	return new NormalizedListing( 
	    Get_normalizer(),
	    new ConcreteListing( (String) o.get( "title" ), 
				 (String) o.get( "manufacturer" ), 
				 (String) o.get( "currency" ), 
				 (String) o.get( "price" ) ) );
    }

    protected static Collection< Product > GetProductsFromFile( File productsFile )
	throws Exception
    {
	Collection< Product > products = new HashSet< Product >();
	BufferedReader productsReader = null;
	try
	{
	    productsReader = 
		new BufferedReader( 
		    new InputStreamReader( 
			new FileInputStream( productsFile ),
			"UTF-8" ) );
	    for( String line = productsReader.readLine(); line != null; line = productsReader.readLine() )
	    {
		products.add( ParseProductFromJson( line ) );
	    }
	} 
	finally
	{
	    if( productsReader != null ) 
	    {
		productsReader.close();
	    }
	    productsReader = null;
	}

	return products;
    }
    
    protected static Collection< Listing > GetListingsFromFile( File listingsFile )
	throws Exception
    {
	Collection< Listing > listings = new HashSet< Listing >();
	BufferedReader listingsReader = null;
	try
	{
	    listingsReader = 
		new BufferedReader( 
		    new InputStreamReader( 
			new FileInputStream( listingsFile ),
			"UTF-8" ) );
	    for( String line = listingsReader.readLine(); line != null; line = listingsReader.readLine() )
	    {
		listings.add( ParseListingFromJson( line ) );
	    }
	}
	finally
	{
	    if( listingsReader != null )
	    {
		listingsReader.close();
	    }
	    listingsReader = null;
	}

	return listings;
    }

    public static void main( String[] args )
	throws Exception
    {
	if( args.length != 2 && args.length != 3 ) 
	{
	    System.err.println( "here" );
	    Usage();
	    return;
	}
	
	String productsFile = null;
	String listingsFile = null;
	boolean debug = false;

	if( args.length == 2 ) 
	{
	    productsFile = args[0];
	    listingsFile = args[1];
	}
	else if( args.length == 3 )
	{
	    if( !args[0].equals( "--debug" ) ) 
	    {
		Usage();
		return;
	    }
	    debug = true;
	    productsFile = args[1];
	    listingsFile = args[2];
	}

	// Thankfully, the set of products and listings can fit in memory

	// Let's assume that the set of products won't contain multiple entries 
	// for the same product.
	Collection< Product > products = GetProductsFromFile( new File( productsFile ) );
	Collection< Listing > listings = GetListingsFromFile( new File( listingsFile ) );

	MatchingEngine engine = new MatchingEngine( new MatcherFactory()
						    {
							public Matcher Get_matcher()
							{
							    return new DeterministicMatcher();
							}
						    } );
	engine.AddListener( debug ? new MatchingProgress( listings ) : null );

	try (
	    InvestigateProductMatches investigateProductMatches = debug ? new InvestigateProductMatches() : null;
	    InvestigateListingMatches investigateListingMatches = debug ? new InvestigateListingMatches() : null;
	    JSONPrinter jsonPrinter = new JSONPrinter( new PrintStream( System.out, true, "UTF-8" ) );
	) 
	{
	    engine.AddListener( investigateProductMatches );
	    engine.AddListener( investigateListingMatches );
	    engine.AddListener( jsonPrinter );

	    Map< Product, Map< Listing, Integer > > matches = engine.Match( products, listings );
	}
    }
}

