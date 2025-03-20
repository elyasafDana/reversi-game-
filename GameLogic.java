import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic{
    private Disc[][] Board;
    private boolean firstPlayerTurn;
    private Stack<Move> moveStack;
    private Stack<List<Position>> flippedDiscsStack;

    private Player firstPlayer;
    private Player secondPlayer;


    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (playerTurn().getNumber_of_bombs()==0 && disc instanceof BombDisc )return false;
        if (playerTurn().getNumber_of_unflippedable()==0 && disc instanceof UnflippableDisc)return false;
        if(getDiscAtPosition(a)!=null)return false;
        if (countFlips(a)==0)return false;
        if(!disc.getOwner().equals(playerTurn()))return false;

        int playerNum=2;
        if (isFirstPlayerTurn())playerNum=1;
        System.out.println("Player "+ playerNum+" placed a "+disc.getType()+" in("+a.row()+","+a.col()+")");

        List<Position> fliipedList= getListOfCounted(a);
        flippedDiscsStack.push(fliipedList);
        Move move= new Move(a,disc);
        moveStack.push(move);
        flip(a);
        Board[a.row()][a.col()]=disc;

        if(disc instanceof BombDisc) playerTurn().reduce_bomb();
        if (disc instanceof UnflippableDisc) playerTurn().reduce_unflippedable();
        firstPlayerTurn=!firstPlayerTurn;

        System.out.println();

        return true;
    }



    @Override
    public void undoLastMove() {
        System.out.println("Undoing last move ");

        if (!moveStack.empty() ) {
            if (!(playerTurn() instanceof AIPlayer)) {
                
            List<Position> ListOfPositon = flippedDiscsStack.pop();
            Move move = moveStack.pop();
            Position position = move.position();
            Board[position.row()][position.col()] = null;
            System.out.println("\tUndo: removing " + move.disc().getType() + " from (" + position.row() + "," + position.col() + ")");

            Disc disc;
            for (int i = 0; i < ListOfPositon.size(); i++) {
                disc = getDiscAtPosition(ListOfPositon.get(i));
                if (disc.getOwner().isPlayerOne()) disc.setOwner(secondPlayer);
                else disc.setOwner(firstPlayer);
                System.out.println("\tUndo: flipping back " + disc.getType() + " in (" + ListOfPositon.get(i).row() + "," + ListOfPositon.get(i).col() + ")");
            }

            Player player;
            if (firstPlayerTurn) player = secondPlayer;
            else player = firstPlayer;

            int currentBombCount = player.getNumber_of_bombs();
            int currentUnflippeableCount = player.getNumber_of_unflippedable();
            player.reset_bombs_and_unflippedable();
            if (move.disc() instanceof BombDisc)
                updateBombOrUnfliipable(player, true, currentBombCount, currentUnflippeableCount);
            if (move.disc() instanceof UnflippableDisc)
                updateBombOrUnfliipable(player, false, currentBombCount, currentUnflippeableCount);
            firstPlayerTurn = !firstPlayerTurn;
        }
        }

        else{
            System.out.println("\tNo previous move available to undo ");
        }

        System.out.println();
    }


    private void updateBombOrUnfliipable(Player player,boolean isBomb,int bombNum,int unflipableNum){
        int i=0,j=0;
        if (isBomb){
            i=1;
            j=0;
        }
        else {
            i=0;j=1;
        }

        while(player.getNumber_of_bombs()!=bombNum+i)player.reduce_bomb();
        while (player.getNumber_of_unflippedable()!=unflipableNum+j) player.reduce_unflippedable();
    }



    private boolean isPositionInArr(Position position,List<Position> arr){
        for (int i=0;i<arr.size();i++){
            if(arr.get(i)==null)return false;
            if(position.col()==arr.get(i).col()&&position.row()==arr.get(i).row())return true;
        }
        return  false;
    }
    public GameLogic(){
        Board=new Disc[8][8];
        firstPlayerTurn=true;
        moveStack=new Stack<>();
        flippedDiscsStack=new Stack<>();
    }



    @Override
    public Disc getDiscAtPosition(Position position) {
        int x,y;
        x=position.row();
        y=position.col();
        if (Board[x][y]==null)return null;
        return Board[x][y];
    }

    @Override
    public int getBoardSize() {
        return Board.length;
    }


    @Override
    public List<Position> ValidMoves() {
        Disc disc;
        disc= new SimpleDisc(playerTurn());

        List<Position> valid= new LinkedList<Position>();;
        Position position;

        for (int i=0;i<getBoardSize();i++){
            for (int j=0;j<getBoardSize();j++){
                position=new Position(i,j);
                if (getDiscAtPosition(position)==null){

                    if( countFlips(position)!=0){
                        valid.add(position);
                    }

                }
            }
        }
        int num=valid.size();
        return valid;
    }


    public void flip(Position a){
        List<Position> listOfBombCount=new ArrayList<>();
        listOfBombCount=getListOfCounted(a);
        int playerNum=2;
        if (isFirstPlayerTurn())playerNum=1;

        Position position;
        Disc disc;
        for(int i=0;i<listOfBombCount.size();i++) {
            position=listOfBombCount.get(i);
            disc=getDiscAtPosition(position);
            disc.setOwner(playerTurn());
            System.out.println("Player "+playerNum+" flipped the "+disc.getType()+" in("+position.col()+","+position.row()+")");
        }
    }



    @Override
    public int countFlips(Position a) {
        List<Position> listOfBombCount=new ArrayList<>();

        listOfBombCount=getListOfCounted(a);
        return listOfBombCount.size() ;

    }

    private List<Position> getListOfCounted(Position position){ // give me the list of counted
        List<Position> listOfBombCount=new ArrayList<>();
        for (int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i!=0 || j!=0)
                    if (thereIsSomeThingToCount(position,i,j)) {
                        listOfBombCount= count(position,i,j,listOfBombCount);
                    }

            }
        }
        return listOfBombCount;
    }


    private  List<Position> count( Position position, int x,int y,List<Position> listOfBombCount){
        Position newPosition=position;
        int counter=0;
            while (isleagal(newPosition)) {
                if ((newPosition.row() + x) > 7 || (newPosition.row() + x) < 0 || (newPosition.col() + y) > 7 || (newPosition.col() + y) < 0) {
                    removeFromList(listOfBombCount, counter);
                    return listOfBombCount;
                }
                newPosition = new Position(newPosition.row() + x, newPosition.col() + y);
                Disc disc = getDiscAtPosition(newPosition);
                if (disc == null) {
                    removeFromList(listOfBombCount, counter);
                    return listOfBombCount;
                }
                if (disc.getOwner().equals(playerTurn())) return listOfBombCount;
                if (!(disc instanceof UnflippableDisc) && !isPositionInArr(newPosition, listOfBombCount)) {
                    counter++;
                    listOfBombCount.add(newPosition);
                }
                if (disc instanceof BombDisc) {
                    if (thereIsSomeThingToCount(newPosition, x, y)) {
                        listOfBombCount = countBomb(newPosition, listOfBombCount);
                    }
                }
            }
            removeFromList(listOfBombCount, counter);
            return listOfBombCount;
    }


    private boolean thereIsSomeThingToCount(Position position,int i , int j){
        Position newPosition=position;
        Disc disc;
        while(true){
            newPosition=new Position(newPosition.row()+i,newPosition.col()+j);
            disc=getDiscAtPosition(newPosition);
            if((newPosition.row()+i)>7||(newPosition.row()+i)<0 || (newPosition.col()+j)>7 || (newPosition.col()+j)<0)return false;
            if (disc==null)return false;
            if(disc.getOwner().equals(playerTurn()))return true;
        }
    }
    private List<Position> countBomb(Position position,List<Position> listOfBombCount){
        for (int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                if(i!=0 || j!=0){
                    Position newPositon=new Position(position.row()+i,position.col()+j);
                    Disc disc=getDiscAtPosition(newPositon);
                    if (disc != null && !isPositionInArr(newPositon, listOfBombCount) && !(disc instanceof UnflippableDisc)) {
                        if (!disc.getOwner().equals(playerTurn()) ) listOfBombCount.add(newPositon);
                        if(disc instanceof BombDisc && !disc.getOwner().equals(playerTurn())){
                            listOfBombCount=countBomb(newPositon,listOfBombCount);
                        }
                    }
                }
            }
        }
        return  listOfBombCount;
    }

    private void removeFromList(List<Position> list,int num){
        for (int i=0;i<num;i++){
            list.remove(list.size()-1);
        }
    }
    private boolean isleagal(Position position){
        if(position.col()<0 || position.col()>8 || position.row()>8 || position.col()<0 )return false;
        return true;
    }
    private Player playerTurn(){
        if(isFirstPlayerTurn())return firstPlayer;
        return secondPlayer;
    }

    @Override
    public  Player getFirstPlayer() {
        return firstPlayer;
    }

    @Override
    public  Player getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        firstPlayer=player1;
        secondPlayer=player2;

    }

    @Override
    public boolean isFirstPlayerTurn() {
        return firstPlayerTurn;
    }

    @Override
    public boolean isGameFinished() {
        if(ValidMoves().size()==0) {
            int playerOneCunt=countDiscOfPlayer(getFirstPlayer());
            int playerTowCount=countDiscOfPlayer(getSecondPlayer());
            if (playerOneCunt>playerTowCount){
                getFirstPlayer().addWin();
                System.out.println("Player 1 wins with "+playerOneCunt+" discs! player 2 had "+playerTowCount+" discs.");
            }
            if (playerTowCount>playerOneCunt){
                getSecondPlayer().addWin();
                System.out.println("Player 2 wins with "+playerTowCount+" discs! player 1 had "+playerOneCunt+" discs.");

            }

            return true;
        }
        return false;
    }
    private int countDiscOfPlayer(Player player){
        int playerCount=0;
        Disc disc;
        Position position;
        for (int i=0;i<getBoardSize();i++){
            for (int j=0;j<getBoardSize();j++){
                position=new Position(i,j);
                disc=getDiscAtPosition(position);
                if (disc!=null){
                    if (disc.getOwner().equals(player))playerCount++;
                }
            }
        }
        return playerCount;
    }


    @Override
    public void reset() {
        clearBoard();
        Disc disc1= new SimpleDisc(getFirstPlayer());
        Board[3][3]=disc1;
        Disc disc2= new SimpleDisc(getFirstPlayer());
        Board[4][4]=disc2;
        Disc disc3= new SimpleDisc(getSecondPlayer());
        Board[4][3]=disc3;
        Disc disc4= new SimpleDisc(getSecondPlayer());
        Board[3][4]=disc4;
        firstPlayerTurn=true;
        moveStack=new Stack<>();
        flippedDiscsStack=new Stack<>();
        getFirstPlayer().reset_bombs_and_unflippedable();
        getSecondPlayer().reset_bombs_and_unflippedable();

    }

    private void clearBoard(){
        for (int i=0;i<getBoardSize();i++){
            for (int j=0;j<getBoardSize();j++){
                Board[i][j]=null;
            }
        }
    }

}
