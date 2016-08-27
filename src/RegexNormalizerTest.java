import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

public class RegexNormalizerTest 
{
    @Test
    public void test()
	throws Exception
    {
	RegexNormalizer identity = new RegexNormalizer( new HashMap< String, String >(), new IdentityNormalizer() );
	assertEquals( null, identity.Normalize( null ) );
	assertEquals( "", identity.Normalize( "" ) );
	assertEquals( "a", identity.Normalize( "a" ) );

	HashMap< String, String > whitespaceMap = new HashMap< String, String >(); 
	whitespaceMap.put( "\\s+", " " );
	RegexNormalizer removeWhitespaceNormalizer = new RegexNormalizer( whitespaceMap, new IdentityNormalizer() );
	assertEquals( " ", removeWhitespaceNormalizer.Normalize( "  " ) );

	Normalizer delegate = mock( Normalizer.class );
	(new RegexNormalizer( delegate )).Normalize( "" );
	verify( delegate, atLeastOnce() ).Normalize( "" );
    }
}

