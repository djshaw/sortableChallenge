import java.util.Set;
import java.util.TreeSet;

public class Levenshtein
{
    public static class LevenshteinMatch
	implements Comparable
    {
	private final int m_score;
	private final int m_start;
	private final int m_end;
	private final String m_image;

	public LevenshteinMatch( String image, int score, int start, int end )
	{
	    m_image = image;
	    m_score = score;
	    m_start = start;
	    m_end = end;
	}

	public int Get_score()
	{
	    return m_score;
	}

	public int Get_start()
	{
	    return m_start;
	}

	public int Get_end()
	{
	    return m_end;
	}

	public String Get_image()
	{
	    return m_image;
	}

	public boolean equals( Object obj )
	{
	    if( !( obj instanceof LevenshteinMatch ) ) {
		return false;
	    }

	    LevenshteinMatch that = (LevenshteinMatch) obj;
	    return m_score == that.m_score
	        && m_start == that.m_start
		&& m_end == that.m_end
		&& m_image == that.m_image;
	}

	public int compareTo( Object o )
	{
	    LevenshteinMatch that = (LevenshteinMatch) o;

	    if( m_score != that.m_score ) 
	    { 
		return that.m_score - m_score;
	    }

	    if( m_start != that.m_start ) 
	    {
		return that.m_start - m_start;
	    }

	    if( m_end != that.m_end ) 
	    {
		return that.m_end - m_end;
	    }

	    return that.m_image.compareTo( m_image );
	}

	public String toString()
	{
	    return "[" + m_image + ", " + m_score + ", " + m_start + ", " + m_end + ", " + m_image.substring( m_start, m_end ) + "]";
	}
    }

    /**
     * Calculates the Levenshtein edit distance between two strings
     */
    public static int editDistance( String a, String b )
    {
	int [][] distances = new int[a.length() + 1][b.length() + 1];
	for( int i = 0; i < distances.length; i += 1 ) {
	    distances[i][0] = i;
	}
	for( int i = 0; i < distances[0].length; i += 1 ) {
	    distances[0][i] = i;
	}
	calculateDistances( a, b, distances );
	return distances[distances.length - 1][distances[0].length - 1];
    }
    
    private static int[][] GetSubstringDistances( String needle, String haystack )
    {
	int[][] distances = new int[needle.length() + 1][haystack.length() + 1];

	// The key difference between the Levenshtein edit distance and the 
	// substring match. The top row of the distance table is all 0s because
	// we don't care how many characters we skip before we start matching.
	for( int i = 0; i < distances.length; i += 1 ) {
	    distances[i][0] = i;
	}
	calculateDistances( needle, haystack, distances );

	return distances;
    }

    /**
     * Perform a fuzzy substring match. Return all substrings that have the
     * minimal Levenshtein edit distance between the needle and the haystack.
     */
    public static Set< LevenshteinMatch > substringMatch( String needle, String haystack )
    {
	if( needle == null || needle.length() == 0 || haystack == null || haystack.length() == 0 ) {
	    return new TreeSet< LevenshteinMatch >();
	}

	int[][] distances = GetSubstringDistances( needle, haystack );

	// Rebuild the matches we need to return. Each match ends as the min 
	// values on the last row of the distance table. Traverse the distance
	// table. Go left if the distance is less than or equal to the
	// current distance. Go up and left as long as the distance up and left
	// is less than the value up, and go up as long as the value up is less
	// than the value left.
        // 
	// Stop at row 0 because everything matches the empty string.
	// 
	// For instance, matching "sx130 is" to "cannon powershot sx130is 12"
	/*
	  c a n o n   p o w e r s h o t   s x 1 3 0 i s   1 2

	  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	                                    
	  1 1 1 1 1 1 1 1 1 1 1 1 0 1 1 1 1 0 1 1 1 1 1 0 1 1 1
	                                     \
	  2 2 2 2 2 2 2 2 2 2 2 2 1 1 2 2 2 1 0 1 2 2 2 1 1 2 2
	                                       \
	  3 3 3 3 3 3 3 3 3 3 3 3 2 2 2 3 3 2 1 0 1 2 3 2 2 1 2
	                                         \
	  4 4 4 4 4 4 4 4 4 4 4 4 3 3 3 3 4 3 2 1 0 1 2 3 3 2 2
	                                           \
	  5 5 5 5 5 5 5 5 5 5 5 5 4 4 4 4 4 4 3 2 1 0 1 2 3 3 3
	                                            |
	  6 6 6 6 6 6 5 6 6 6 6 6 5 5 5 5 4 5 4 3 2 1 1 2 2 3 4
	                                             \
	  7 7 7 7 7 7 6 6 7 7 7 7 6 6 6 6 5 5 5 4 3 2 1 2 3 3 4
	                                               \
	  8 8 8 8 8 8 7 7 7 8 8 8 7 7 7 7 6 5 6 5 4 3 2 1 2 3 4
	                                                ^

	*/
	int minValue = distances[distances.length - 1][0];
	int minOffset = 0;
	for( int j = 1; j < distances[0].length; j += 1 )
	{
	    if( distances[distances.length - 1][j] <= minValue )
	    {
		minValue = distances[distances.length - 1][j];
		minOffset = j;
	    }
	}

	int stringEnd = minOffset;
	int d = distances[distances.length - 1][minOffset];
	int column = minOffset;
	int row = distances.length - 1;
	Set< LevenshteinMatch > results = new java.util.TreeSet< LevenshteinMatch >();
	while( true ) 
	{
	    int up_left = (row != 0 && column != 0) ? distances[row - 1][column - 1] : Integer.MAX_VALUE;
	    int up = row != 0 ? distances[row - 1][column] : Integer.MAX_VALUE;
	    int left = (row != 0 && column != 0) ? distances[row][column - 1] : Integer.MAX_VALUE;

	    boolean moved = false;
	    if( left <= d && d != row )
	    {
		column -= 1;
		moved = true;
	    }
	    else if( up_left <= up && up_left != Integer.MAX_VALUE && up != Integer.MAX_VALUE )
	    {
		row -= 1;
		column -= 1;
		moved = true;
	    }
	    else if( up < left ) 
	    {
		row -= 1;
		moved = true;
	    }

	    d = distances[row][column];
	    if( !moved )
	    {
		LevenshteinMatch match = 
		    new LevenshteinMatch( haystack,
					  minValue, 
					  column,
					  stringEnd );
		results.add( match );

		// Start looking for the next match
		row = distances.length - 1;
		stringEnd = column;
		while( column != 0 )
		{
		    if( distances[row][column] == minValue ) 
		    {
			break;
		    }
		    column -= 1;
		}
		if( column == 0 ) {
		    break;
		}
		d = minValue;
	    }
	}

	return results; 
    }

    protected static void calculateDistances( String needle, String haystack, int[][] distances )
    {
	calculateDistances( needle.toCharArray(), haystack.toCharArray(), distances );
    }

    protected static void calculateDistances( char[] needle, char[] haystack, int[][] distances )
    {
	if( distances.length != needle.length + 1 
	 || distances[0].length != haystack.length + 1 )
	{
	    throw new RuntimeException( "initialized distances table not the correct size!" );
	}

	for( int i = 1; i < distances.length; i += 1 )
	{
	    for( int j = 1; j < distances[0].length; j += 1 ) 
	    {
		int cost = ( needle[i - 1] == haystack[j - 1] ? 0 : 1 );
		distances[i][j] = Math.min( Math.min( distances[i][j - 1] + 1,
						      distances[i - 1][j] + 1) ,
					    distances[i - 1][j - 1] + cost );
	    }
	}
    }

    public static void main( String args[] )
    {
	substringMatch( args[0], args[1] );
    }
}

