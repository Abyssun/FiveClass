package Domain;
 
/*
棋盘值
 */
public class ChessPad {
    public static int size=15; //棋盘的规格
    public static ChessColor[][] colorPad=new ChessColor[size][size];  //棋盘的颜色矩阵，用来记录哪些地方下了棋
    public static int[][] valuePad=new int[size][size];    //棋盘的权值矩阵，用来记录每一个位置的权值
 
    private static Boolean BlackTurn=null;   //该值用来判断是否为黑棋的回合，黑棋永远先手，所以初值为true
 
    public static String mode=""; //该值用来表示当前整个游戏的对战模式,初值为空串
 
    //构造函数，完成程序启动时，第一次初始化棋盘的操作
    public ChessPad(){
 
    }
 
 
 
    //回合转换
    public static void ChangeTurn(){
        BlackTurn=!BlackTurn;
    }
 
    /**
     * 该函数用于判断当前是否为黑棋的回合
     * @return
     */
    public static Boolean isBlackTurn(){
        return BlackTurn;
    }
 
    public static void setBlackTurn(boolean flag){
        BlackTurn=flag;
    }
 
    public static void setBlackTurn(Boolean blackTurn) {
        BlackTurn = blackTurn;
    }
 
    /**
     * 请除棋盘上所有的颜色，将棋盘全部置为Blank
     */
    public static void ClearColorPad(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                colorPad[i][j]=ChessColor.Blank;
            }
        }
    }
 
    /**
     * 清除棋盘上所有的权值，权值设为-1
     */
    public static void ClearValuePad(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                valuePad[i][j]=-1;
            }
        }
    }
}