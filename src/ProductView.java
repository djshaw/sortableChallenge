public class ProductView
    extends HTMLView
{
    protected Product m_product;

    public ProductView( Product product )
    {
	m_product = product;
    }

    @Override
    public String Render()
    {
	return "<table><tr><td><b>Model</b>:</td><td>" + m_product.GetModel() + "</td></tr>"
		    + "<tr><td><b>Family</b>:</td><td>" + m_product.GetFamily() + "</td></tr>"
		    + "<tr><td><b>Manufacturer</b>:</td><td>" + m_product.GetManufacturer() + "</td></tr>"
	     + "</table>";
    }
}

