import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{
@Override
    public Move makeMove(PlayableLogic gameStatus){
        List<Position> validList= gameStatus.ValidMoves();
        Random rnd=new Random();
        int size=validList.size();
        int randomNum= (int)rnd.nextInt(size);
        Position position=validList.get(randomNum);
        Player playerturn;
        if(gameStatus.isFirstPlayerTurn()){
           playerturn=gameStatus.getFirstPlayer();
        }else {
            playerturn=gameStatus.getSecondPlayer();
        }
        boolean alreadyChose=false;
        Disc disc=null;
        if (chooseIt()&& playerturn.getNumber_of_bombs()>0){
            disc=new BombDisc(playerturn);
            alreadyChose=true;
        }
        if (!alreadyChose && chooseIt() && playerturn.getNumber_of_unflippedable()>0){
            disc=new UnflippableDisc(playerturn);
            alreadyChose=true;
        }
        if (!alreadyChose) disc=new SimpleDisc(playerturn);



        Move move=new Move(position,disc);
        return move;

    }

    private boolean chooseIt(){
    Random rnd =new Random();
    int randomNum= rnd.nextInt(2);
    if (randomNum==0)return true;
    else return false;
    }

    public RandomAI(boolean isPlayerOne){
        super(isPlayerOne);
    }
}
