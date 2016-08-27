import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

public class TrimNormalizerTest 
{
    @Test
    public void test()
	throws Exception
    {
	TrimNormalizer normalizer = new TrimNormalizer( new IdentityNormalizer() );
	assertEquals( null, normalizer.Normalize( null ) );
	assertEquals( "", normalizer.Normalize( "" ) );
	assertEquals( "a", normalizer.Normalize( "a" ) );

	assertEquals( "", normalizer.Normalize( " " ) );
	assertEquals( "", normalizer.Normalize( "  " ) );
	assertEquals( "", normalizer.Normalize( "   " ) );

	assertEquals( "a", normalizer.Normalize( " a" ) );
	assertEquals( "a", normalizer.Normalize( "  a " ) );
	assertEquals( "a", normalizer.Normalize( "   a  " ) );
	assertEquals( "a", normalizer.Normalize( "   a  " ) );

	Normalizer delegate = mock( Normalizer.class );
	(new TrimNormalizer( delegate )).Normalize( "" );
	verify( delegate, atLeastOnce() ).Normalize( "" );
    }
}

