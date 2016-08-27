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
	return "<table><tr><td><b>Model</b>:</td><td>" + m_product.Get_model() + "</td></tr>"
		    + "<tr><td><b>Family</b>:</td><td>" + m_product.Get_family() + "</td></tr>"
		    + "<tr><td><b>Manufacturer</b>:</td><td>" + m_product.Get_manufacturer() + "</td></tr>"
	     + "</table>";
    }
}

