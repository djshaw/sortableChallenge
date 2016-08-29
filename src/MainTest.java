import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MainTest
{
    @Test
    public void testNormalizer()
	throws Exception
    {
	Method method = Main.class.getDeclaredMethod( "GetNormalizer" );
	Normalizer normalizer = (Normalizer) method.invoke( null );

	assertEquals( null, normalizer.Normalize( null ) );
	assertEquals( "", normalizer.Normalize( "" ) );
	assertEquals( "a", normalizer.Normalize( "a" ) );
	assertEquals( "a", normalizer.Normalize( "A" ) );
	assertEquals( "", normalizer.Normalize( "(" ) );
    }
}

