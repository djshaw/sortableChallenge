import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

public class LowercaseNormalizerTest
{
    @Test
    public void testAccentNormalizer()
	throws Exception
    {
	Normalizer normalizer = new AccentNormalizer();

	assertEquals( null, normalizer.Normalize( null ) );
	assertEquals( "", normalizer.Normalize( "" ) );
	assertEquals( "a", normalizer.Normalize( "a" ) );
	assertEquals( "A", normalizer.Normalize( "A" ) );

	Normalizer delegate = mock( Normalizer.class );
	(new LowercaseNormalizer( delegate )).Normalize( "" );
	verify( delegate, atLeastOnce() ).Normalize( "" );
    }
}

