public abstract class WrappedProduct implements Product
{
    public abstract Product Get_parent_product();

    public int hashCode()
    {
	return Get_parent_product().hashCode();
    }
}

