import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    @org.junit.jupiter.api.Test
    void validMoves() {

        GameLogic g= new GameLogic();
        Player firstPlayer=new HumanPlayer(true);
        Player sectPlayer=new HumanPlayer(false);
        g.setPlayers(firstPlayer,sectPlayer);
        g.reset();

        BombDisc bom1=new BombDisc(firstPlayer);
        Position position=new Position(2,4);
        g.locate_disc(position,bom1);
         BombDisc bom2=new BombDisc(sectPlayer);
        Position position1=new Position(2,5);
        g.locate_disc(position1,bom2);
        BombDisc bom3=new BombDisc(firstPlayer);
        Position position2=new Position(2,6);
        g.locate_disc(position2,bom3);
        BombDisc bom4=new BombDisc(sectPlayer);
        Position position3=new Position(2,3);
        g.locate_disc(position3,bom4);

        int i=1;
        int j=2;
                Position pos=new Position(i,j);
                g.countFlips(pos);




       // g.ValidMoves();









    }

    @org.junit.jupiter.api.Test
    void countFlips() {

    }
}