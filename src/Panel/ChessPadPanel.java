package Panel;
 
import Domain.ChessColor;
import Domain.ChessPad;
import Domain.Position;
import Utils.PositionUtils;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
 
/*
棋盘容器
 */
public class ChessPadPanel extends JPanel{
    public static int gap=80;  //棋盘与窗口的间隔
    public static int size=40; //棋盘每一个的长宽
    public static int radius=10;   //旗子的半径
    public static int row=15;  //棋盘行数
    public static int col=15;  //棋盘列数
    private static ChessPadPanel instance=new ChessPadPanel();
 
    public static MouseListener listener;
 
 
    //构造函数，初始化棋盘
    public ChessPadPanel(){
        this.setSize(800,600);  //设置容器大小
        this.setBackground(new Color(173, 216, 230));    //设置容器背景颜色
    }
 
    public static ChessPadPanel getInstance() {
        return instance;
    }
 
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawPad(g);
        drawChess(g);
    }
 
    //绘制棋盘,传入画笔对象
    public void drawPad(Graphics g){
        //将模式显示在此处
        g.setFont(new Font("宋体",Font.BOLD,20));
        if (ChessPad.mode==""){
            g.drawString("请先选择模式",30,30);
        }
        else{
            g.drawString(ChessPad.mode,30,30);
        }
 
        ButtonPanel.changeMsg();
 
        //绘制棋盘行
        for(int i=0;i<row;i++){
            g.drawLine(gap,gap+i*size,(col+1)*size,gap+i*size);
        }
 
        for(int i=0;i<col;i++){
            g.drawLine(gap+i*size,gap,gap+i*size,(row+1)*size);
        }
    }
 
    /**
     * 该方法根据棋盘的颜色矩阵，在棋盘上画棋
     */
    public void drawChess(Graphics g){
        ChessColor[][] colorPad = ChessPad.colorPad;
        //先画棋盘上的黑棋，为画笔设置颜色
        g.setColor(Color.black);
 
        //遍历颜色数组，若颜色值等于黑色，则将该点画在棋盘上
        for (int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                if(colorPad[i][j]==ChessColor.Black){
                    //获取该行列的 像素坐标
                    Position position=new Position(i,j);
                    Position y = PositionUtils.RowAndColChangeTOXAndY(position);
                    g.fillOval( y.getX()-radius, y.getY()-radius,radius*2,radius*2);
                }
            }
        }
 
        //遍历颜色数组，若颜色值等于白色，则将该点画在棋盘上,白起需要加边框
 
        for (int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                if(colorPad[i][j]==ChessColor.White){
                    //获取该行列的 像素坐标
                    Position position=new Position(i,j);
                    Position y = PositionUtils.RowAndColChangeTOXAndY(position);
                    g.setColor(Color.WHITE);
                    g.fillOval((int) y.getX()-radius,(int) y.getY()-radius,radius*2,radius*2);  //将棋画在对应的坐标上
                    g.setColor(Color.BLACK);
                    g.drawOval((int) y.getX()-radius,(int) y.getY()-radius,radius*2,radius*2);
                }
            }
        }
    }
}