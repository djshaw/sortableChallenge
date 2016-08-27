import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

// Improvement: Can probably factor out some stuff with InvestigateListingMatches
public class InvestigateProductMatches
    extends MatchingEngineListener
    implements AutoCloseable
{
    protected File m_outFile;
    protected PrintStream m_out;

    public InvestigateProductMatches()
	throws Exception
    {
	m_outFile = new File( "products.html" );
	Initialize();
    }

    public InvestigateProductMatches( String filename )
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
    public void Matched( Product product, final Map< Listing, Integer > listings )
    {
	TreeMap< Listing, Integer > sorted = new TreeMap< Listing, Integer >(
	    new Comparator< Listing >()
	    {
		public int compare( Listing lhs, Listing rhs )
		{
		    if( listings.get( lhs ) >= listings.get( rhs ) )
		    {
			return 1;
		    }
		    else
		    {
			return -1;
		    }
		}
	    } );
	sorted.putAll( listings );

	m_out.println( "	    <tr>\n"
		     + "		<td>" + (new ProductView( product )).Render() + "</td>\n"
		     + "		<td>\n" );

	int i = 0;
	for( Listing listing : sorted.keySet() )
	{
	    if( listings.get( listing ) > 5 )
	    {
		break;
	    }

	    m_out.println( "<a onClick=\"toggle( 'match_" + m_uniqueMatchingId + "' )\">" + listings.get( listing ) + "</a>"
			 + "<div id=\"match_" + m_uniqueMatchingId + "\" style=\"display: none\">" + (new ListingView( listing )).Render() + "</div>" );
	    i += 1;
	    m_uniqueMatchingId += 1;
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

