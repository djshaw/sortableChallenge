import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LevenshteinTest
{
    @Test
    public void testEditDistance() 
    {
	assertEquals( 0, Levenshtein.editDistance( "", "" ) );	    // exact match
	assertEquals( 1, Levenshtein.editDistance( "a", "" ) );	    // empty string
	assertEquals( 1, Levenshtein.editDistance( "", "a" ) );	    // empty string
	assertEquals( 0, Levenshtein.editDistance( "a", "a" ) );    // exact match
	assertEquals( 1, Levenshtein.editDistance( "a", "b" ) ); 

	assertEquals( 1, Levenshtein.editDistance( "aa", "ab" ) );  // one modification
	assertEquals( 1, Levenshtein.editDistance( "aa", "aba" ) ); // one deletion
	assertEquals( 1, Levenshtein.editDistance( "aa", "a" ) );   // one addition
    }

    @Test
    public void testSubstringMatch()
    {
	assertEquals( Levenshtein.substringMatch( null, null ), new TreeSet() );
	assertEquals( Levenshtein.substringMatch( null, "" ), new TreeSet() );
	assertEquals( Levenshtein.substringMatch( "", null ), new TreeSet() );
	assertEquals( Levenshtein.substringMatch( "", "" ), new TreeSet() );

	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "a", 0, 0, 1 ) ) ),
		      Levenshtein.substringMatch( "a", "a" ) );

	// Repetition breaks substring matching because we have [aa]aa, a[aa]a, aa[aa]
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "aaaa", 0, 0, 4 ) ) ),  // aaaa
		      Levenshtein.substringMatch( "aa", "aaaa" ) );

	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "abab", 0, 0, 2 ),
						  new Levenshtein.LevenshteinMatch( "abab", 0, 2, 4 ) ) ),
		      Levenshtein.substringMatch( "ab", "abab" ) );

	// In the middle
	assertEquals( "abba", "c abba c".substring( 2, 6 ) ); // to sanity check my expection of substring
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "c abba c", 1, 2, 6 ) ) ), // abba
		      Levenshtein.substringMatch( "aba", "c abba c" ) ); 

	// Two identical approximate matches
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "abbcabbc", 1, 0, 4 ), // abbc
						  new Levenshtein.LevenshteinMatch( "abbcabbc", 1, 4, 8 ) ) ), // abbc 
		      Levenshtein.substringMatch( "abc", "abbcabbc" ) );

	// Common suffix
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "xxx abbb xxx", 1, 4, 8 ) ) ), // abbb
		      Levenshtein.substringMatch( "cbbb", "xxx abbb xxx" ) );

	// Common prefix 
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "xxx bbba xxx", 1, 4, 8 ) ) ), // bbbc
		      Levenshtein.substringMatch( "bbbc", "xxx bbba xxx" ) );

	
	// No character matches
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "b", 3, 0, 1 ) ) ), // b
	              Levenshtein.substringMatch( "foo", "b" ) );
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "ba", 3, 0, 2 ) ) ), // ba
		      Levenshtein.substringMatch( "foo", "ba" ) );
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "bar", 3, 0, 3 ) ) ), // bar
		      Levenshtein.substringMatch( "foo", "bar" ) );

	// Random test cases of strings I was investigating from the challenge 
	// data
	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "canon powershot sx130is 12",
										    1,
										    16,
										    23 ) ) ),
		      Levenshtein.substringMatch( "sx130 is",
						  "canon powershot sx130is 12" ) );

	assertEquals( new TreeSet( Arrays.asList( new Levenshtein.LevenshteinMatch( "canon powershot sd300 4mp digital elph camera with 3x optical zoom",
										    1,
										    18,
										    21 ) ) ),
		      Levenshtein.substringMatch( "600",
						  "canon powershot sd300 4mp digital elph camera with 3x optical zoom" ) );
    }

    // I'm aware that it looks a little redicuclus to be testing test code 
    // like this. But I put the test code in Levenshtein.java enough times
    // to warrant main(), and I've made enough typos to warrant testMain().
    // It's always the little things that mess you up :).
    @Test
    public void testMain()
	throws Exception
    {
	PrintStream systemOut = System.out;
	PrintStream systemErr = System.err;

	String expected = "0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 \n"
			+ "  c a n o n   p o w e r s h o t   s d 3 0 0   4 m p   d i g i t a l   e l p h   c a m e r a   w i t h   3 x   o p t i c a l   z o o m \n"
			+ "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 \n"
			+ "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 \n"
			+ "2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 1 1 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 2 \n"
			+ "3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 2 1 2 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 \n";

	String actual = null;
	try
	{
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    System.setOut( new PrintStream( baos ) );
	    System.setErr( new PrintStream( baos ) );
	    main( new String[]{ "600", "canon powershot sd300 4mp digital elph camera with 3x optical zoom" } );
	    actual = baos.toString();
	}
	finally
	{
	    System.setOut( systemOut );
	    System.setErr( systemErr );
	}

	assertEquals( expected, actual );
    }

    public static void main( String[] args )
	throws Exception
    {
	Method method = Levenshtein.class.getDeclaredMethod( "GetSubstringDistances", String.class, String.class );
	method.setAccessible( true );

	String needle = args[0];
	String haystack = args[1];
	int[][] distances = (int[][]) method.invoke( null, needle, haystack );

	for( int i = 0; i < distances[0].length; i += 1 )
	{
	    System.out.print( i % 10 + " " );
	}
	System.out.println();
	System.out.print( "  " );
	for( int i = 0; i < distances[0].length - 1; i += 1 )
	{
	    System.out.print( haystack.charAt( i ) + " " );
	}
	System.out.println();
	for( int i = 0; i < distances.length; i += 1 ) 
	{
	    for( int j = 0; j < distances[i].length; j += 1 )
	    {
		System.out.print( distances[i][j] + " " );
	    }
	    System.out.println();
	}
    }
}

