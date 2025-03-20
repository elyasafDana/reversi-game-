import java.util.List;

public class GreedyAI extends AIPlayer{

    public GreedyAI(boolean isPlayerOne){
        super( isPlayerOne);
    }
    public Move makeMove(PlayableLogic gameStatus){
        List<Position> validMove= gameStatus.ValidMoves();
        int size=validMove.size();
        int bestFlipsNum=0,place=0;
        int currentNum=0;
        for(int i=0;i<size;i++){
            currentNum=gameStatus.countFlips(validMove.get(i));
            if (currentNum>bestFlipsNum){
                bestFlipsNum=currentNum;
                place=i;
            }
            if (currentNum==bestFlipsNum) {

                if(validMove.get(i).col()>validMove.get(place).col()){
                    place=i;
                }
                if(validMove.get(i).col()==validMove.get(place).col()){
                    if (validMove.get(i).row()>validMove.get(place).row()) place=i;
                }
            }
        }
        SimpleDisc disc;
        if(gameStatus.isFirstPlayerTurn()){
            disc=new SimpleDisc(gameStatus.getFirstPlayer());
        }else {
            disc=new SimpleDisc(gameStatus.getSecondPlayer());
        }


        Move myMove=new Move(validMove.get(place),disc);
        return myMove;

    }
}
