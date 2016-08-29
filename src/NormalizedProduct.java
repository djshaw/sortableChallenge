public class NormalizedProduct 
    extends WrappedProduct
{
    protected Product m_product;
    protected String m_normalizedProductName;
    protected String m_normalizedManufacturer;
    protected String m_normalizedModel;
    protected String m_normalizedFamily;

    public NormalizedProduct( Normalizer normalizer, Product product )
    {
	m_product = product;
	m_normalizedProductName = normalizer.Normalize( m_product.GetProductName() );
	m_normalizedManufacturer = normalizer.Normalize( m_product.GetManufacturer() );
	m_normalizedModel = normalizer.Normalize( m_product.GetModel() );
	m_normalizedFamily = normalizer.Normalize( m_product.GetFamily() );
    }

    public Product GetParentProduct()
    {
	return m_product;
    }

    public String GetProductName()
    {
	return m_normalizedProductName;
    }

    public String GetManufacturer()
    {
	return m_normalizedManufacturer;
    }

    public String GetModel()
    {
	return m_normalizedModel;
    }

    public String GetFamily()
    {
	return m_normalizedFamily;
    }

    public String GetAnnouncedDate()
    {
	return m_product.GetAnnouncedDate();
    }

    public String toString()
    {
	return m_product.toString();
    }
}

