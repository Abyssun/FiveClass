package Utils;
 
 
import Domain.Position;
import Panel.ChessPadPanel;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * 该类实现像素坐标和矩阵的行列坐标之间的转换
 */
public class PositionUtils {
    /**
     * 该方法实现将行列坐标转换为像素坐标    行代表的是y轴，列代表的是x轴
     * @param p
     * @return 一个Positon对象，该对象的x和y属性的值时像素坐标
     */
    public static Position RowAndColChangeTOXAndY(Position p){
        ChessPadPanel chessPadPanel = ChessPadPanel.getInstance();
        int gap=chessPadPanel.gap;
        int size=chessPadPanel.size;
 
        //获取该行列坐标的横、纵的值
        int row = p.getX();   //此处获取的是行，所以该值用来计算y的值
        int col = p.getY();   //此处获取的是列，所以该值用来计算x的值
 
        Position position=new Position();
        position.setX(gap+col*size);
        position.setY(gap+row*size);
        return position;
    }
 
    /**
     * 该方法实现将像素坐标，转换为行列坐标
     * @param p
     * @return 返回一个Position，该对象的x和y值是行列坐标
     */
    public static Position XAndYChangeToRowAndCol(Position p){
        //获取该点的像素坐标
        int x = p.getX();
        int y = p.getY();
 
        //获取棋盘数据
        int radius = ChessPadPanel.radius;
        int row = ChessPadPanel.row;
        int col = ChessPadPanel.col;
 
 
        //计算出棋盘上所有交点的像素坐标
        List<Position> positionList = getAllPositionXAndY();
 
        //遍历该列表，计算每一个点与鼠标点击的点的距离，若距离小于棋子的半径，则说明该棋属于那一个点
        int count=-1;    //记录第几个点
        for (Position position : positionList) {
            double distance = Math.sqrt(Math.pow((position.getX() - x), 2) + Math.pow((position.getY() - y), 2));   //两像素点之间的距离
            count++;
 
            if(distance<=radius){
                break;
            }
        }
 
        //若像素点在两个焦点的中间段，即两个棋子之间的空白段，或无效位置，则上述遍历无法计算出满足距离小于棋子半径的点，所以会完成整个列表的遍历计算，因此需要对最后一各交点也就是第224个点进行单独的判断
        if (count==224){
            //判断传入的像素坐标距离最后一个点是否距离小于半径
            //获取最后一个位置
            Position position = positionList.get(positionList.size()-1);
            double distance = Math.sqrt(Math.pow((position.getX() - x), 2) + Math.pow((position.getY() - y), 2));   //两像素点之间的距离
 
            if (!(distance<=radius)){
                return null;
            }
        }
 
        //通过计数的到的点编号，计算出行列坐标
        Position XAndY=new Position(count/row,count%col);
 
        return XAndY;
    }
 
    /**
     * 该方法用于获取棋盘上所有交点的像素坐标
     * @return
     */
    public static List<Position> getAllPositionXAndY(){
        List<Position> positionList=new ArrayList<>();
        //获取棋盘数据
        int gap = ChessPadPanel.gap;
        int size = ChessPadPanel.size;
        int row = ChessPadPanel.row;
        int col = ChessPadPanel.col;
 
        for(int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                int x=gap+i*size;
                int y=gap+j*size;
                positionList.add(new Position(y,x));
            }
        }
 
        return positionList;
    }
 
    //判断该店是否为棋盘的边缘 p为行列坐标
    public static boolean isBound(Position p){
        return p.getX()>=0&&p.getX()<15;
    }
}