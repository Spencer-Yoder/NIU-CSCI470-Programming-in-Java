import java.util.Comparator;

//Function that sorts the MilageRedeemer Array list from High to Low
class MilageComparator implements Comparator<Destination> {
    public int compare(Destination p1, Destination p2)
    {
        return (p2.getMilesReq() - p1.getMilesReq());
    }
}