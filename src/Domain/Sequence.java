package Domain;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * 该对象保存的是下棋的顺序，用于悔棋操作
 *  使用List数据结构保存，按栈的先进先出方式对数据进行存取
 */
public class Sequence {
    public static List<Position> positions=new ArrayList<>();   //下棋位置的顺序
    public static List<ChessColor> colorsSequence=new ArrayList<>();    //下棋颜色的顺序
    /**
     * 出栈操作
     * @return
     */
    public static Position pop(){
        colorsSequence.remove(colorsSequence.size()-1);
        return positions.remove(positions.size()-1);
    }
 
    public static void push(Position p,ChessColor color){
        colorsSequence.add(color);
        positions.add(p);
    }
 
    /**
     * 清空下棋顺序
     */
    public static void ClearSequence(){
        positions=new ArrayList<>();
        colorsSequence=new ArrayList<>();
    }
}