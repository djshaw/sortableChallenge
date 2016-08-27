public class ConcreteProduct 
	implements Product
{
    protected int m_hash;

    protected String m_productName;
    protected String m_manufacturer;
    protected String m_model;
    protected String m_family;

    // An ISO-8601 formatted date string
    protected String m_announcedDate; // TODO: use a Date object

    public ConcreteProduct( String productName, String manufacturer, String model, String family, String announcedDate )
    {
	m_productName = productName;
	m_manufacturer = manufacturer;
	m_model = model;
	m_family = family;
	m_announcedDate = announcedDate;

	int result = 17;
	for( String s : java.util.Arrays.asList( Get_product_name(), Get_manufacturer(), Get_model(), Get_family(), Get_announced_date() ) )
	{
	    if( s != null ) 
	    {
		result = 37 * result + s.hashCode();
	    }
	}
	m_hash = result;

    }

    public int hashCode()
    {
	return m_hash;
    }

    public String Get_product_name()
    {
	return m_productName;
    }

    public String Get_manufacturer()
    {
	return m_manufacturer; 
    }

    public String Get_model()
    {
	return m_model;
    }
    
    public String Get_family()
    {
	return m_family;
    }

    public String Get_announced_date()
    {
	return m_announcedDate;
    }

    // TODO: move this to an AbstractProduct?
    /*
    public int compareTo( Object o )
    {
	Product that = (Product) o;
	int result = Get_product_name().compareTo( that.Get_product_name() );
	if( result != 0 )
	{
	    return result;
	}

	result = Get_manufacturer().compareTo( that.Get_manufacturer() );
	if( result != 0 )
	{
	    return result;
	}

	result = Get_model().compareTo( that.Get_model() );
	if( result != 0 )
	{
	    return result;
	}

	String family = Get_family();
	String that_family = that.Get_family();
	if( family != null && that_family != null ) {
	    result = family.compareTo( that_family  );
	    if( result != 0 ) 
	    {
		return result;
	    }
	}
	else if( family != null && that_family == null )
    	{
	    return 1;
	}
	else if( family == null && that_family != null )
	{
	    return -1;
	}
	else
	{
	    // family == null && that_family == null
	    // continue on
	}

	return Get_announced_date().compareTo( that.Get_announced_date() );
    }
    */
    
    public String toString()
    {
	return "productName: " + m_productName + "; "
	     + "manufacturer: " + m_manufacturer + "; "
	     + "model: " + m_model + "; "
	     + "family: " + m_family + "; "
	     + "announcedDate: " + m_announcedDate;
    }
}

