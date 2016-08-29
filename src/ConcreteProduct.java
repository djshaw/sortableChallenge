public class ConcreteProduct 
    implements Product
{
    protected int m_hash;

    protected String m_productName;
    protected String m_manufacturer;
    protected String m_model;
    protected String m_family;

    // An ISO-8601 formatted date string
    protected String m_announcedDate; // Because this data isn't used, keep it 
				      // as a string and treat it as an opaque string

    public ConcreteProduct( String productName, String manufacturer, String model, String family, String announcedDate )
    {
	m_productName = productName;
	m_manufacturer = manufacturer;
	m_model = model;
	m_family = family;
	m_announcedDate = announcedDate;
    }

    public String GetProductName()
    {
	return m_productName;
    }

    public String GetManufacturer()
    {
	return m_manufacturer; 
    }

    public String GetModel()
    {
	return m_model;
    }
    
    public String GetFamily()
    {
	return m_family;
    }

    public String GetAnnouncedDate()
    {
	return m_announcedDate;
    }
    
    public String toString()
    {
	return "productName: " + m_productName + "; "
	     + "manufacturer: " + m_manufacturer + "; "
	     + "model: " + m_model + "; "
	     + "family: " + m_family + "; "
	     + "announcedDate: " + m_announcedDate;
    }
}

