import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

public class MatchingProgress
    extends MatchingEngineListener
{
    protected int m_listingCount;
    protected int m_matchProgress;
    protected int m_printedProgress;

    public MatchingProgress( Collection< Listing > listings )
    {
	m_listingCount = listings.size();
	m_matchProgress = 0;

	LogProgress( 0 );
    }

    protected void LogProgress( int progress )
    {
	LogManager.getLogger( this.getClass() ).info( progress + "%" );
    }

    @Override
    public void Matched( Listing listing, Map< Product, Integer > products )
    {
	m_matchProgress += 1;
	int currentProgress = (m_matchProgress * 100) / m_listingCount;
	if( currentProgress > m_printedProgress )
	{
	    m_printedProgress = currentProgress;
	    LogProgress( currentProgress );
	}
    }
}

