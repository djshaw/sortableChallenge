import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

class WorkerThread 
    extends Thread
{
    private static int MATCH_THRESHOLD = 3; // (arbitrarily chosen, I'm sorry to say)

    protected MatchingEngine m_engine;
    protected Matcher m_matcher;
    protected Collection< Product > m_products;
    protected Collection< Listing > m_listings;

    protected Map< Product, Map< Listing, Integer > > m_results = new HashMap< Product, Map< Listing, Integer > >();

    WorkerThread( MatchingEngine engine, 
		  Matcher matcher, 
		  Collection< Product > products, 
		  Collection< Listing > listings )
    {
	m_engine = engine;
	m_matcher = matcher;
	m_products = products;
	m_listings = listings;
    }

    public void run()
    {
	for( Listing listing : m_listings ) 
	{
	    int matched_score = Integer.MAX_VALUE;
	    Vector< Product > matched_products = new Vector< Product >();
	    Map< Product, Integer > scores = new HashMap< Product, Integer >();
	    for( Product product : m_products )
	    {
		int score = m_matcher.Match( product, listing );
		scores.put( product, score );
		if( score < matched_score )
		{
		    matched_products.clear();
		    matched_score = score;
		}

		if( score == matched_score )
		{
		    matched_products.add( product );
		}
	    }

	    if( matched_products.size() == 1 
	     && matched_score <= MATCH_THRESHOLD )  // Potential improvement: This 
						    // threshold needs to be configurable,
						    // perhaps by passing in a filter
	    {
		for( Product product : matched_products )
		{
		    Map< Listing, Integer > listings = m_results.get( product );
		    if( listings == null ) {
			listings = new HashMap< Listing, Integer >();
			m_results.put( product, listings );
		    }
		    listings.put( listing, matched_score );
		}
	    }

	    m_engine.MatchedEvent( listing, scores );
	}
    }

    public Map< Product, Map< Listing, Integer > > GetResults()
    {
	return m_results;
    }
}

public class MatchingEngine
{
    protected int m_maxThreadCount;
    protected MatcherFactory m_matcherFactory;
    protected Vector< MatchingEngineListener > listeners = new Vector< MatchingEngineListener >();

    public MatchingEngine( MatcherFactory matcherFactory, int maxThreads )
    {
	if( maxThreads == 0 )
	{
	    throw new RuntimeException( "must have at least 1 thread" );
	}
	m_matcherFactory = matcherFactory;
	m_maxThreadCount = maxThreads;
    }

    public MatchingEngine( MatcherFactory matcherFactory ) 
    {
	m_matcherFactory = matcherFactory;
	m_maxThreadCount = 8; // This could be more reasonable. 2 * number of cores, for instance
    }

    public void AddListener( MatchingEngineListener listener )
    {
	if( listener != null ) {
	    listeners.add( listener );
	}
    }

    // This is probably a significant performance bottle neck
    /**
     * Called by worker threads after a listing is scored against the set of 
     * products.
     */
    synchronized void MatchedEvent( Listing listing, Map< Product, Integer > scores )
    {
	for( MatchingEngineListener listener : listeners )
	{
	    listener.Matched( listing, scores );
	}
    }

    void MatchedEvent( Product product, Map< Listing, Integer > listings )
    {
	for( MatchingEngineListener listener : listeners )
	{
	    listener.Matched( product, listings );
	}
    }

    public Map< Product, Map< Listing, Integer > > Match( 
	Collection< Product > products, 
	Collection< Listing > listings )
    {	
	Map< Product, Map< Listing, Integer > > results = new HashMap< Product, Map< Listing, Integer > >();

	for( Product product : products )
	{
	    results.put( product, new HashMap< Listing, Integer >() );
	}

	// The threading architecture is rather crude: dividing work evenly 
	// amoung the threads and dispatching. With the current model, if a
	// thread happens to finish its work early because of the nature of the 
	// data it was given, the CPU resources sit idle. It would be better 
	// to hand out smaller work units of roughly equal size, 1000 listings
	// per unit, for instance. When a thread finishes a work unit, it can 
	// check in for more. 
	Vector< WorkerThread > threads = new Vector< WorkerThread >();
	int i = 0;
	Iterator< Listing > iter = listings.iterator();
	for( int threadCount = 0; threadCount < m_maxThreadCount; threadCount += 1 ) 
	{
	    Collection< Listing > workUnit = new Vector< Listing >();
	    for( ; iter.hasNext() 
		&& i < Math.max( 1, listings.size() * (threadCount + 1) / m_maxThreadCount ); 
		 i += 1 )
	    {
		workUnit.add( iter.next() );
	    }
	    if( threadCount + 1 == m_maxThreadCount ) {
		while( iter.hasNext() ) 
		{
		    workUnit.add( iter.next() );
		}
	    }

	    WorkerThread workerThread = new WorkerThread( this, m_matcherFactory.GetMatcher(), products, workUnit );
	    threads.add( workerThread );
	    workerThread.start();
	}

	// Coalesce the results of all the worker threads. This could be 
	// improved by coalescing the results as they come in. That way, 
	// if thread 2 finishes before thread 1, the coalescing can be 
	// performed while we're waiting for the works to complete. This would
	// also the matching engine to work online and take a stream of 
	// listings.
	for( WorkerThread thread : threads )
	{
	    try
	    {
		thread.join();
	    }
	    catch( InterruptedException e )
	    {
		throw new RuntimeException( e );
	    }

	    Map< Product, Map< Listing, Integer > > threadResults = thread.GetResults();
	    for( Product product : threadResults.keySet() )
	    {
		results.get( product ).putAll( threadResults.get( product ) );
	    }
	}
	
	for( Product product : results.keySet() )
	{
	    MatchedEvent( product, results.get( product ) );
	}

	return results;
    }
}

