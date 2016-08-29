public class ConcreteListing 
    implements Listing
{
    protected String m_title;
    protected String m_manufacturer;
    
    // Currency and price could be parsed, but since we're not performing 
    // any calculations on either we'll keep them as strings and treat them
    // as opaque data. (Additionally, there's no specification on
    // currency beyond saying it's a currency code--it might not be ISO 4217, 
    // and price may not be in dollars).
    protected String m_currency;
    protected String m_price;

    public ConcreteListing( String title, 
			    String manufacturer, 
			    String currency, 
			    String price )
    {
	m_title = title;
	m_manufacturer = manufacturer;
	m_currency = currency;
	m_price = price;
    }
    
    public String GetTitle()
    {
	return m_title;
    }

    public String GetManufacturer()
    {
	return m_manufacturer; }

    public String GetCurrency()
    {
	return m_currency;
    }

    public String GetPrice()
    {
	return m_price;
    }

    public String toString()
    {
	return "title: " + m_title + "; "
	     + "manufacturer: " + m_manufacturer + "; "
	     + "currency: " +  m_currency + "; "
	     + "price: " + m_price;
    }
}

