public class BombDisc implements Disc{

      private   Player owner;



        public Player getOwner() {
        return this.owner;
    }


        public void setOwner(Player player) {
        this.owner=player;

    }



        public String getType() {
        return "\uD83D\uDCA3";

    }
    public BombDisc(Player player){
        owner=player;
    }

}
