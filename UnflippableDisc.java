public class UnflippableDisc implements Disc {
    private Player owner;



    public Player getOwner() {
        return this.owner;
    }

    public UnflippableDisc(Player player){
        owner=player;
    }


    public void setOwner(Player player) {
        this.owner=player;

    }


    public String getType() {
        return "⭕";

    }
}
