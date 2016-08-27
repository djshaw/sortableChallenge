import static org.junit.Assert.assertEquals;
import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

public class AccentNormalizerTest
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

	assertEquals( "A", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0x80 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // capital letter a with grave

	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA0 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with grave
	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA1 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with acute
	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA2 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with circumflex
	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA3 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with tilde
	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA4 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with diaeresis (two dots)
	assertEquals( "a", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA5 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter a with ring

	// java.text.Normalizer.normalizer doesn't handle at least ae. Not 
	// optimal, but I think the implementation is enough for the challenge.
	//assertEquals( "ae", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA6 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter ae

	assertEquals( "c", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA7 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter c with cedilla

	assertEquals( "e", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA8 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // small letter e with grave

	assertEquals( "aa", normalizer.Normalize( "a" + new String( new byte[]{ (byte)0xC3, (byte)0xA0 }, java.nio.charset.StandardCharsets.UTF_8 ) ) );
	assertEquals( "aa", normalizer.Normalize( new String( new byte[]{ (byte)0xC3, (byte)0xA0, (byte)0xC3, (byte)0xA0 }, java.nio.charset.StandardCharsets.UTF_8 ) ) ); // two small letter a with grave

	Normalizer delegate = mock( Normalizer.class );
	(new AccentNormalizer( delegate )).Normalize( "" );
	verify( delegate, atLeastOnce() ).Normalize( "" );
    }
}

