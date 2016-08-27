public abstract class WrappedListing implements Listing
{
    public abstract Listing Get_parent_listing();

    public int hashCode()
    {
	return Get_parent_listing().hashCode();
    }
}

