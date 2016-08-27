import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

public class SubstringReplacerNormalizerTest 
{
    @Test
    public void test()
	throws Exception
    {
	SubstringReplacerNormalizer identity = new SubstringReplacerNormalizer( new HashMap< String, String >(), new IdentityNormalizer() );
	assertEquals( null, identity.Normalize( null ) );
	assertEquals( "", identity.Normalize( "" ) );
	assertEquals( "a", identity.Normalize( "a" ) );

	HashMap< String, String > parenthesisMap = new HashMap< String, String >(); 
	parenthesisMap.put( "(", " " );
	parenthesisMap.put( ")", " " );
	SubstringReplacerNormalizer removeParenthesis = new SubstringReplacerNormalizer( parenthesisMap, new IdentityNormalizer() );
	assertEquals( " foo ", removeParenthesis.Normalize( "(foo)" ) );
	assertEquals( "  foo ", removeParenthesis.Normalize( "((foo)" ) );
	
	Normalizer delegate = mock( Normalizer.class );
	(new SubstringReplacerNormalizer( delegate )).Normalize( "" );
	verify( delegate, atLeastOnce() ).Normalize( "" );
    }
}

