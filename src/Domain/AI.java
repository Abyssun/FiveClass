package Domain;
 
import Listiner.StopListener;
import Panel.ChessPadPanel;
import Panel.MainPanel;
import Utils.PositionUtils;
 
import javax.swing.*;
import java.util.*;
 
/**
 * AI对象，人机对战模式时被激活
 *
 *
 */
public class AI {
 
    private static Map<String,Integer> AttackMap=new HashMap<>();
    private static Map<String,Integer> DefendMap=new HashMap<>();
    /**
     * 静态代码块，往map里面加入权值计算的规则
     *
     *      -:表示空棋
     *      *:表示白棋
     *      o:表示白棋
     */
    static {
        //还要重新设计赋分策略，才能保证五子棋机器人的准确判断
        //因为已经固定了 *为黑子，且默认将机器人设为黑方，所以机器人只能是黑方
 
        //还要设置防守方的数值，防止被gank掉
        //右边put的map值，是防守分数，这样Ai就不会一味的猛冲
 
        //左边：进攻棋链，权值越大说明该棋链越接近胜利   右边：防御棋链，权值越大说明该棋链约值得防御
        AttackMap.put("*****",   100000);//连五
        /*                                 */DefendMap.put("ooooo",   30000);
        AttackMap.put("-****-",   5000);//活四
        /*                                 */DefendMap.put("-oooo-",   3000);
        AttackMap.put("*-***",   700);//冲四  1
        /*                                 */DefendMap.put("o-ooo",   150);
        AttackMap.put("***-*",   700);//冲四  1  反向
        /*                                 */DefendMap.put("ooo-o",   150);
        AttackMap.put("-****o",   1000);//冲四  2
        /*                                 */DefendMap.put("-oooo*",   200);
        AttackMap.put("o****-",   1000);//冲四  2  反向
        /*                                 */DefendMap.put("*oooo-",   200);
        AttackMap.put("**-**",   700);//冲四   3
        /*                                 */DefendMap.put("oo-oo",   200);
        AttackMap.put("-***-",   500);//活三   1
        /*                                 */DefendMap.put("-ooo-",   100);
        AttackMap.put("*-**",   150);//活三  2
        /*                                 */DefendMap.put("o-oo",   50);
        AttackMap.put("**-*",   150);//活三  2   反向
        /*                                 */DefendMap.put("oo-o",   50);
        AttackMap.put("--***o",   100);//眠三  1
        /*                                 */DefendMap.put("--ooo*",   20);
        AttackMap.put("o***--",   100);//眠三  1  反向
        /*                                 */DefendMap.put("*ooo--",   20);
        AttackMap.put("-*-**o",   80);//眠三   2
        /*                                 */DefendMap.put("-o-oo*",   15);
        AttackMap.put("o**-*-",   80);//眠三   2  反向
        /*                                 */DefendMap.put("*oo-o-",   15);
        AttackMap.put("-**-*o",   60);//眠三   3
        /*                                 */DefendMap.put("-oo-o*",   10);
        AttackMap.put("o*-**-",   60);//眠三   3   反向
        /*                                 */DefendMap.put("*o-oo-",   10);
        AttackMap.put("*--**",   60);//眠三   4
        /*                                 */DefendMap.put("o--oo",   10);
        AttackMap.put("**--*",   60);//眠三   4   反向
        /*                                 */DefendMap.put("oo--o",   10);
        AttackMap.put("*-*-*",   60);//眠三   5
        /*                                 */DefendMap.put("o-o-o",   10);
        AttackMap.put("o-***-o",   60);//眠三   6
        /*                                 */DefendMap.put("*-ooo-*",   2);
        AttackMap.put("--**--",   50);//活二  1
        /*                                 */DefendMap.put("--oo--",   2);
        AttackMap.put("-*-*-",   20);//活二   2
        /*                                 */DefendMap.put("-o-o-",   2);
        AttackMap.put("*--*",   20);//活二   3
        /*                                 */DefendMap.put("o--o",   2);
        AttackMap.put("---**o",   10);//眠二  1
        /*                                 */DefendMap.put("---oo*",   1);
        AttackMap.put("o**---",   10);//眠二  1   反向
        /*                                 */DefendMap.put("*oo---",   1);
        AttackMap.put("--*-*o",   10);//眠二  2
        /*                                 */DefendMap.put("--o-o*",   1 );
        AttackMap.put("o*-*--",   10);//眠二  2   反向
        /*                                 */DefendMap.put("*o-o--",   1);
        AttackMap.put("-*--*o",   10);//眠二  3
        /*                                 */DefendMap.put("-o--o*",   1);
        AttackMap.put("o*--*-",   10);//眠二  3   反向
        /*                                 */DefendMap.put("*o--o-",   1);
        AttackMap.put("*---*",   10);//眠二  4
        /*                                 */DefendMap.put("o---o",   1);
        //上面之所以int类型不能自动向上转换为long类型
        //是因为hashMap中的value使用的是包装类Long
        //包装类虽然能自动装箱，但是不能将基础类型转换，再装箱
        //所以需要手动转换为long类型
    }
 
    //Ai下棋主流程
    public static void AiPlay(){
        //清空权值矩阵，避免前一回合影响本回合的判断
        ChessPad.ClearValuePad();
 
        //判断是否为黑棋回合，AI永远下黑棋
        if (!ChessPad.isBlackTurn()){
            return;
        }
 
        Position bestPosition=null;
        if(isEmptyPad()){
            //如果是空棋盘，则永远下中间点的位置，
            Random random=new Random();
           //bestPosition = new Position(random.nextInt(15),random.nextInt(15));
            bestPosition = new Position(7,7);
        }
        else {
            //先计算防御棋链，看看当前棋局是否有需要防御的地方
            boolean defend=false;
            /*x:
            for (int i=0;i<15;i++){
                for (int j=0;j<15;j++){
                    List<String> defends = getPositionChessLink(new Position(i, j), "defend");
                    for (String s : defends) {
                        if (s.equals("ooooo")){
                            System.out.println("防御");
                            bestPosition=new Position(i,j);
                            defend=true;
                            break x;
                        }
                    }
                }
            }*/
 
            if (!defend){
                //计算整个棋盘的权值
                getAllValue();
 
                //选取最佳下棋位置
                bestPosition = getBestPosition();
            }
        }
 
        //将棋下入颜色棋盘
        putChess(bestPosition);
 
        Sequence.push(bestPosition,ChessColor.Black);  //当前这一步棋入栈
 
        //更换回合
        ChessPad.ChangeTurn();
    }
 
    /**
     * 该方法实现Ai的下棋操作，传入的Position对象 坐标为行列坐标
     * @param p
     */
    public static void putChess(Position p){
        System.out.println(p);
        //更改颜色矩阵，完成下棋操作
        ChessPad.colorPad[p.getX()][p.getY()]=ChessColor.Black;
    }
 
    /**
     * 该方法啊实现选取权值最大的点,AI下的棋就是下在这个点上
     * @return  返回的是一个行列坐标的position对象
     */
    public static Position getBestPosition(){
        //遍历权值棋盘，找出权值最大的点
        int[][] valuePad = ChessPad.valuePad;
        ChessColor[][] colorPad = ChessPad.colorPad;
        int size = ChessPad.size;
 
        int max=-1;
        Position position=new Position();
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
 
                //判断该点的权值是否大于最大值，同时该点必须为空棋
                if(colorPad[i][j]==ChessColor.Blank&&valuePad[i][j]>max){
                    max=valuePad[i][j];
                    position.setX(i);
                    position.setY(j);
                }
            }
        }
 
       /* return position;
      */  //遍历找出所有的最大权值点
        List<Position> allMaxPosition=new ArrayList<>();
 
        for(int i=0;i<size;i++){
            for (int j=0;j<size;j++){
 
                //判断该点的权值是否大于最大值，同时该点必须为空棋
                if(colorPad[i][j]==ChessColor.Blank&&valuePad[i][j]==max){
                    allMaxPosition.add(new Position(i,j));
                }
            }
        }
 
        //在最大权值点列表种，随机取出一个点（随机取的原因是：遍历数组都是按固定顺序遍历的，若不采取随机取的方法，则玩家能够通过固定的套路取胜）
        int s=allMaxPosition.size()-1;
        return allMaxPosition.get((int)(Math.random()*s));
    }
 
    /**
     * 为整个权值棋盘，赋值
     */
    public static void getAllValue(){
        //获取权值棋盘
        int[][] valuePad = ChessPad.valuePad;
        int size = ChessPad.size;
 
        for (int i=0;i<size;i++){
            for (int j = 0; j < size; j++) {
                //将每一个点传入计算权值
                Position position=new Position(i,j);
                valuePad[i][j]=CalculateValue(position);
            }
        }
    }
 
    public static int getPositionDefendValue(Position p){
        List<String> defendList = getPositionChessLink(p,"defend");
 
        //计算权值
        int defendValue=0;
 
        for (String s : defendList) {
            if(DefendMap.containsKey(s)){
                defendValue+=DefendMap.get(s);
            }
        }
 
        return defendValue;
    }
 
    /**
     * 该方法实现计算传入点的权值
     *      p的坐标应该是行列坐标
     * @return
     */
   public static int CalculateValue(Position p){
        //调用方法，返回该店的棋链
       List<String> attackList = getPositionChessLink(p,"attack");
       List<String> defendList = getPositionChessLink(p,"defend");
 
       //计算权值
       int attackValue=0;
       int defendValue=0;
       for (String s : attackList) {
           if(AttackMap.containsKey(s)){
               attackValue+=AttackMap.get(s);
           }
       }
 
       for (String s : defendList) {
           if(DefendMap.containsKey(s)){
               defendValue+=DefendMap.get(s);
           }
       }
 
       return Math.abs(attackValue-defendValue);  //值越大说明该点即适合防御又适合进攻
   }
 
    /**
     * 该方法实现获取一个点的所有棋链
     *  参数p必须为行列坐标
     *
     *  棋链的获取分为四个方向：横、竖、斜、反斜
     * @return
     */
   public static List<String> getPositionChessLink(Position p,String type){
 
       //获取棋盘
       ChessColor[][] colorPad = ChessPad.colorPad;
       int size = ChessPad.size;    //棋盘的大小
 
       boolean flag=false;
       if(colorPad[p.getX()][p.getY()]==ChessColor.Blank&&type.equals("attack")){
           //假设该空点下的棋为黑棋
           colorPad[p.getX()][p.getY()]=ChessColor.Black;
           flag=true;
       }
       else if(colorPad[p.getX()][p.getY()]==ChessColor.Blank&&type.equals("defend")){
           colorPad[p.getX()][p.getY()]=ChessColor.White;
           flag=true;
       }
 
       List<String> resultList=new ArrayList<>();
 
//       System.out.println("竖方向:");
       //先从竖方向获取棋链
       for (int i=4;i<=7;i++){  //棋链长度最短4 最长不超过7
 
           for (int j = 0; j <i; j++) {    //判断传入的棋子在棋链上的位置（即第几个棋子，从左向右数）
               String s="";
 
               int startRow=p.getX()-j;   //计算竖方向的开始坐标
               int endRow=startRow+i-1;   //计算竖方向的结束坐标
 
               //此处需要判断开始的点和结束的点是否超出了棋盘
               if(startRow<0||endRow>=size){
                   //该点超过了棋盘的范围
                   continue;
               }
 
               for (;startRow<=endRow;startRow++){
                   if(colorPad[startRow][p.getY()]==ChessColor.Blank){
                       s=s+"-";
                   }
                   else if (colorPad[startRow][p.getY()]==ChessColor.White){
                       s=s+"o";
                   }
                   else {
                       s=s+"*";
                   }
               }
 
//               System.out.println(s);
               resultList.add(s);       //将该棋链加入结果列表
           }
       }
 
//       System.out.println("横方向:");
       //计算横方向的棋链权值
       for (int i = 4; i <= 7 ; i++) {  //棋链长度，最短4，最长7
 
           for (int j = 0; j <i; j++) {    //棋在棋链上的位置，（从上往下）
               
               String s="";
               
               //由于是横方向，故只需要计算开始的y和结束的y坐标
               int startCol= p.getY()-j;
               int endCol=startCol+i-1;
 
               //判断开始位置和结束位置是否在棋盘范围内
                if (startCol<0||endCol>=size){
                    continue;
                }
                
                for (;startCol<=endCol;startCol++){
                    if(colorPad[p.getX()][startCol]==ChessColor.Blank){
                        s=s+"-";
                    }
                    else if (colorPad[p.getX()][startCol]==ChessColor.White){
                        s=s+"o";
                    }
                    else {
                        s=s+"*";
                    }
                }
 
//               System.out.println(s);
                resultList.add(s);
           }
       }
 
//       System.out.println("斜方向:");
       //从斜方向获取棋链
       for (int i=4;i<=7;i++){
           
           for (int j=0;j<i;j++){
               
               //此处为斜方向，改变棋在棋链上的位置，涉及 x 和 y 两个方向的改变，从左下往右上的方向来计算 两个坐标的变化为
               int startRow= p.getX()+j;
               int startCol= p.getY()-j;
 
               int endRow=startRow-i+1;
               int endCol=startCol+i-1;
               
               //判断开始点和结束点是否在棋盘内
               if (!((startRow>=0&&startRow<size&&startCol>=0&&startCol<size)&&(endRow>=0&&endRow<size&&endCol>=0&&endCol<size))){
                   continue;
               }
               
               String s="";
               
               for (int row=startRow,col=startCol;
                    row>=endRow && col<=endCol;
                    row--,col++){
 
                   if(colorPad[row][col]==ChessColor.Blank){
                       s=s+"-";
                   }
                   else if (colorPad[row][col]==ChessColor.White){
                       s=s+"o";
                   }
                   else {
                       s=s+"*";
                   }
               }
 
//               System.out.println(s);
               resultList.add(s);
           }
       }
 
//       System.out.println("反斜方向:");
       //反斜方向
       for(int i=4;i<=7;i++){
           for(int j=0;j<i;j++){
 
               //计算开始的点
               int startRow=p.getX()-j;
               int startCol=p.getY()-j;
 
               int endRow=startRow+i-1;
               int endCol=startCol+i-1;
 
               String s="";
 
               if (!((startRow>=0&&startRow<size&&startCol>=0&&startCol<size)&&(endRow>=0&&endRow<size&&endCol>=0&&endCol<size))){
                   continue;
               }
 
               for (int row=startRow,col=startCol;row<=endRow && col<=endCol ; row++,col++){
                   if(colorPad[row][col]==ChessColor.Blank){
                       s=s+"-";
                   }
                   else if (colorPad[row][col]==ChessColor.White){
                       s=s+"o";
                   }
                   else {
                       s=s+"*";
                   }
               }
 
//               System.out.println(s);
               resultList.add(s);
           }
       }
 
       //返回之前将临时下的棋恢复
       if (flag){
           colorPad[p.getX()][p.getY()]=ChessColor.Blank;
       }
 
       return resultList;
   }
 
    /**
     * 该方法用于判断棋盘是不是都为空
     * @return
     */
   public static boolean isEmptyPad(){
       for (ChessColor[] chessColors : ChessPad.colorPad) {
           for (ChessColor chessColor : chessColors) {
               if(chessColor!=ChessColor.Blank){
                   return false;
               }
           }
       }
 
       return true;
   }
 
    /**
     * 判断游戏进行状态
     */
   public static void Judge(){
       //获取所有的棋链
       List<String> allChessLink = getAllChessLink();
 
       for (String s : allChessLink) {
           if(s.equals("ooooo")){
               JOptionPane.showMessageDialog(MainPanel.getInstance(),"白方获胜","游戏结束",JOptionPane.INFORMATION_MESSAGE);
 
               new StopListener().actionPerformed(null);    //完成重置工作
 
               return;
           }
           else if(s.equals("*****")){
               JOptionPane.showMessageDialog(MainPanel.getInstance(),"黑方获胜","游戏结束",JOptionPane.INFORMATION_MESSAGE);
 
               new StopListener().actionPerformed(null);    //完成重置工作
 
               return;
           }
       }
 
       //判断是否为平局
       boolean flag=true;
       for (int i=0;i<15;i++){
           for (int j=0;j<15;j++){
               flag=ChessPad.colorPad[i][j]==ChessColor.Blank;
           }
       }
 
       if(!flag){
           JOptionPane.showMessageDialog(MainPanel.getInstance(),"平局","游戏结束",JOptionPane.INFORMATION_MESSAGE);
 
           new StopListener().actionPerformed(null);    //完成重置工作
 
           return;
       }
   }
 
    public static List<String> getAllChessLink(){
 
        //获取棋盘
        ChessColor[][] colorPad = ChessPad.colorPad;
        int size = ChessPad.size;    //棋盘的大小
 
 
        List<String> resultList=new ArrayList<>();
 
//       System.out.println("竖方向:");
        for (int k=0;k<15;k++){
            for (int z=0;z<15;z++){
                Position p=new Position(k,z);
                //先从竖方向获取棋链
                for (int i=4;i<=7;i++){  //棋链长度最短4 最长不超过7
 
                    for (int j = 0; j <i; j++) {    //判断传入的棋子在棋链上的位置（即第几个棋子，从左向右数）
                        String s="";
 
                        int startRow=p.getX()-j;   //计算竖方向的开始坐标
                        int endRow=startRow+i-1;   //计算竖方向的结束坐标
 
                        //此处需要判断开始的点和结束的点是否超出了棋盘
                        if(startRow<0||endRow>=size){
                            //该点超过了棋盘的范围
                            continue;
                        }
 
                        for (;startRow<=endRow;startRow++){
                            if(colorPad[startRow][p.getY()]==ChessColor.Blank){
                                s=s+"-";
                            }
                            else if (colorPad[startRow][p.getY()]==ChessColor.White){
                                s=s+"o";
                            }
                            else {
                                s=s+"*";
                            }
                        }
 
//               System.out.println(s);
                        resultList.add(s);       //将该棋链加入结果列表
                    }
                }
 
//       System.out.println("横方向:");
                //计算横方向的棋链权值
                for (int i = 4; i <= 7 ; i++) {  //棋链长度，最短4，最长7
 
                    for (int j = 0; j <i; j++) {    //棋在棋链上的位置，（从上往下）
 
                        String s="";
 
                        //由于是横方向，故只需要计算开始的y和结束的y坐标
                        int startCol= p.getY()-j;
                        int endCol=startCol+i-1;
 
                        //判断开始位置和结束位置是否在棋盘范围内
                        if (startCol<0||endCol>=size){
                            continue;
                        }
 
                        for (;startCol<=endCol;startCol++){
                            if(colorPad[p.getX()][startCol]==ChessColor.Blank){
                                s=s+"-";
                            }
                            else if (colorPad[p.getX()][startCol]==ChessColor.White){
                                s=s+"o";
                            }
                            else {
                                s=s+"*";
                            }
                        }
 
//               System.out.println(s);
                        resultList.add(s);
                    }
                }
 
//       System.out.println("斜方向:");
                //从斜方向获取棋链
                for (int i=4;i<=7;i++){
 
                    for (int j=0;j<i;j++){
 
                        //此处为斜方向，改变棋在棋链上的位置，涉及 x 和 y 两个方向的改变，从左下往右上的方向来计算 两个坐标的变化为 x减小 y减小
                        int startRow= p.getX()+j;
                        int startCol= p.getY()-j;
 
                        int endRow=startRow-i+1;
                        int endCol=startCol+i-1;
 
                        //判断开始点和结束点是否在棋盘内
                        if (!((startRow>=0&&startRow<size&&startCol>=0&&startCol<size)&&(endRow>=0&&endRow<size&&endCol>=0&&endCol<size))){
                            continue;
                        }
 
                        String s="";
 
                        for (int row=startRow,col=startCol;
                             row>=endRow && col<=endCol;
                             row--,col++){
 
                            if(colorPad[row][col]==ChessColor.Blank){
                                s=s+"-";
                            }
                            else if (colorPad[row][col]==ChessColor.White){
                                s=s+"o";
                            }
                            else {
                                s=s+"*";
                            }
                        }
 
//               System.out.println(s);
                        resultList.add(s);
                    }
                }
 
//       System.out.println("反斜方向:");
                //反斜方向
                for(int i=4;i<=7;i++){
                    for(int j=0;j<i;j++){
 
                        //计算开始的点
                        int startRow=p.getX()-j;
                        int startCol=p.getY()-j;
 
                        int endRow=startRow+i-1;
                        int endCol=startCol+i-1;
 
                        String s="";
 
                        if (!((startRow>=0&&startRow<size&&startCol>=0&&startCol<size)&&(endRow>=0&&endRow<size&&endCol>=0&&endCol<size))){
                            continue;
                        }
 
                        for (int row=startRow,col=startCol;row<=endRow && col<=endCol ; row++,col++){
                            if(colorPad[row][col]==ChessColor.Blank){
                                s=s+"-";
                            }
                            else if (colorPad[row][col]==ChessColor.White){
                                s=s+"o";
                            }
                            else {
                                s=s+"*";
                            }
                        }
 
//               System.out.println(s);
                        resultList.add(s);
                    }
                }
            }
        }
 
        return resultList;
    }
}
 