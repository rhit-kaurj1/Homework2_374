package Data;

public class CardData {
    private final String id;
    private final int vp;
    private final String costString;

    public CardData(String id, int vp, String costString) {
        this.id = id;
        this.vp = vp;
        this.costString = costString == null ? "" : costString;
    }

    public String getId() { return id; }
    public int getVP() { return vp; }
    public String getCostString() { return costString; }
}
