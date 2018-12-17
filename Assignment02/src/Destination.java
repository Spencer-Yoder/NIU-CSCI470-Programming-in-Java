public class Destination {
    private String city;
    private int milesReq;
    private int upgrade;
    private int superSaver;
    private int saverStart;
    private int saverEnd;

    public Destination(String city, int milesReq, int superSaver, int upgrade, int saverStart, int saverEnd){
        this.city = city;
        this.milesReq= milesReq;
        this.upgrade = upgrade;
        this.superSaver = superSaver;
        this.saverStart = saverStart;
        this.saverEnd = saverEnd;
    }

    //Returns city name
    public String getCity() {
        return city;
    }

    //Returns Total miles required
    public int getMilesReq() {
        return milesReq;
    }

    //Returns the Miles to upgrade
    public int getUpgrade() {
        return upgrade;
    }

    //Returns the super saver price
    public int getSuperSaver() {
        return superSaver;
    }

    //Returns the super saver start date
    public int getSaverStart() {
        return saverStart;
    }

    //Returns the super saver end data
    public int getSaverEnd() {
        return saverEnd;
    }
}

