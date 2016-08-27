import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IdentityNormalizerTest
{
    @Test
    public void test()
    {
	Normalizer normalizer = new IdentityNormalizer();	
	assertEquals( null, normalizer.Normalize( null ) );
	assertEquals( "", normalizer.Normalize( "" ) );
	assertEquals( "a", normalizer.Normalize( "a" ) );
	assertEquals( "A", normalizer.Normalize( "A" ) );
    }
}	

