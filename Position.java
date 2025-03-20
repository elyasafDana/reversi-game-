public class Position {
    private int col;
    private int row;


    public  Position(int row,int col){
        if(col>=0 && col<8 && row>=0 && row<8 ){
            this.col=col;
            this.row=row;
        }

    }
    public  int col(){
        return col;
    }
    public  int row(){
        return row;
    }


}
