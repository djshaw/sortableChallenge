import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;

public class InvestigateListingMatches
    extends MatchingEngineListener
    implements AutoCloseable
{
    protected File m_outFile;
    protected PrintStream m_out;

    public InvestigateListingMatches()
	throws Exception
    {
	m_outFile = new File( "listings.html" );
	Initialize();
    }
    
    public InvestigateListingMatches( String filename )
	throws Exception
    {
	m_outFile = new File( filename );
	Initialize();
    }

    private void Initialize()
	throws Exception
    {
	if( m_outFile.exists() ) 
	{
	    if( !m_outFile.delete() ) 
	    {
		throw new RuntimeException( "Couldn't delete " + m_outFile.getName() );
	    }
	}

	// Improvement: better handing of IO errors

	m_out = new PrintStream( new FileOutputStream( m_outFile ), true, "UTF-8" );

	m_out.println( "<html>\n"
		     + "    <head>\n"
		     + "	<script>\n"
		     + "	    function toggle( id )\n"
		     + "	    {\n"
		     + "		element = document.getElementById( id );\n"
		     + "		if( element.style.display == 'block' )\n"
		     + "		{\n"
		     + "		    element.style.display = 'none';\n"
		     + "		}\n"
		     + "		else\n"
		     + "		{\n"
		     + "		    element.style.display = 'block';\n"
		     + "		}\n"
		     + "	    }\n"
		     + "	</script>\n"
		     + "    </head>\n"
		     + "    <body>\n" 
		     + "	<table border=\"1\">\n" );
    }

    private int m_uniqueMatchingId = 0;

    @Override
    public void Matched( Listing listing, final Map< Product, Integer > products )
    {
	TreeMap< Product, Integer > sorted = new TreeMap< Product, Integer >( 
	    new Comparator< Product >()
	    {
		public int compare( Product lhs, Product rhs )
		{
		    if( products.get( lhs ) >= products.get( rhs ) )
		    {
			return 1;
		    }
		    else
		    {
			return -1;
		    }
		}
	    } );
	sorted.putAll( products );

	m_out.println( "	    <tr>\n"
		     + "		<td>" + (new ListingView( listing )).Render() + "</td>\n"
		     + "		<td>\n" );

	int i = 0;
	int displayingScore = 0;
	for( Iterator< Product > iter = sorted.keySet().iterator(); iter.hasNext(); )
	{
	    Product product = iter.next();
	    int score = products.get( product );
	    if( i < 5 || score == displayingScore )
	    {
		displayingScore = score;
		m_out.println( "<a onClick=\"toggle( 'match_" + m_uniqueMatchingId + "' )\">" + score + "</a>" 
			     + "<div id=\"match_" + m_uniqueMatchingId + "\" style=\"display: none\">" + (new ProductView( product )).Render() + "</div>" );
		m_uniqueMatchingId += 1;
	    }

	    i += 1;
	}

	m_out.println( "		</td>\n"
		     + "	    </tr>\n" );
    }

    public void close()
    {
	m_out.println( "	</table>\n" 
		     + "    </body>\n"
		     + "</html>\n" );
	m_out.close();
    }
}

