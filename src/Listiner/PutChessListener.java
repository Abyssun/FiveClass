package Listiner;
 
import Domain.*;
import Panel.ChessPadPanel;
import Utils.PositionUtils;
 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
 
//鼠标监听器，该监听器作用对象是棋盘（ChessPadPanel），当棋盘对象上有鼠标点击且释放的事件时，执行该监听器，完成下棋动作
//由于有两个对战模式，所以需要
public class PutChessListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
 
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
 
    }
 
 
    //鼠标释放事件
    @Override
    public void mouseReleased(MouseEvent e) {
        //获取鼠标释放的位置,像素坐标
        int x = e.getX();
        int y = e.getY();
 
        Position position=new Position(x,y);
        Position change = PositionUtils.XAndYChangeToRowAndCol(position);   //将像素坐标转换为行列坐标
 
        //判断点击的区域是否为有效区域
        if(change!=null){
            ChessColor[][] colorPad = ChessPad.colorPad;
            //由于有人人对战模式，所以需要判断当前下棋是哪一方下
 
            //判断下棋位置是否有棋
            if (colorPad[change.getX()][change.getY()]!=ChessColor.Blank){
                return;
            }
 
            if(ChessPad.mode.equals("人人对战")){
                if(ChessPad.isBlackTurn()){
                    colorPad[change.getX()][change.getY()]=ChessColor.Black;
                    Sequence.push(change,ChessColor.Black);  //当前这一步棋入栈
                }
                else {
                    colorPad[change.getX()][change.getY()] = ChessColor.White;
                    Sequence.push(change,ChessColor.White);  //当前这一步棋入栈
                }
 
                ChessPadPanel.getInstance().repaint();
                //变更下棋回合
                ChessPad.ChangeTurn();
                AI.Judge(); //Ai判断本局游戏是否结束
 
            }
            else {
                //当前模式为人机对战，则人下棋完成以后，从新绘制了棋盘，然后在让AI完成下棋
 
                if(!ChessPad.isBlackTurn()){
                    //鼠标点击了棋盘，且当前不是黑棋回合，则完成下棋
                    colorPad[change.getX()][change.getY()]=ChessColor.White;
 
                    Sequence.push(change,ChessColor.White);  //当前这一步棋入栈
 
                    ChessPadPanel.getInstance().repaint();
                    //变更下棋回合
                    ChessPad.ChangeTurn();
                    AI.Judge(); //Ai判断本局游戏是否结束
                    //启用Ai，完成下棋
                    AI.AiPlay();
                    ChessPadPanel.getInstance().repaint();
                    AI.Judge(); //Ai判断本局游戏是否结束
                }
            }
 
 
        }
 
        //需要判断棋盘的结果，看看是否有获胜
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
 
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
 
    }
}