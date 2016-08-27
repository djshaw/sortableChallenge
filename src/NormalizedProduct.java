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
	m_normalizedProductName = normalizer.Normalize( m_product.Get_product_name() );
	m_normalizedManufacturer = normalizer.Normalize( m_product.Get_manufacturer() );
	m_normalizedModel = normalizer.Normalize( m_product.Get_model() );
	m_normalizedFamily = normalizer.Normalize( m_product.Get_family() );
    }

    public Product Get_parent_product()
    {
	return m_product;
    }

    public String Get_product_name()
    {
	return m_normalizedProductName;
    }

    public String Get_manufacturer()
    {
	return m_normalizedManufacturer;
    }

    public String Get_model()
    {
	return m_normalizedModel;
    }

    public String Get_family()
    {
	return m_normalizedFamily;
    }

    public String Get_announced_date()
    {
	return m_product.Get_announced_date();
    }

    public String toString()
    {
	return m_product.toString();
    }
}

