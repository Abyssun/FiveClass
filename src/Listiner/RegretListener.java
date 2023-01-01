package Listiner;
 
import Domain.ChessPad;
import Domain.Position;
import Domain.Sequence;
import Panel.ButtonPanel;
import Panel.ChessPadPanel;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * 悔棋按钮的操作
 */
public class RegretListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!(Sequence.positions.size()>=3)){
            return;
        }
 
        //判断当前的模式，如果是人机对战的话，就将栈顶前两个棋子出栈，如果是人人对战模式，就出前一个
        if(ChessPad.mode.equals("人机对战")){
            Sequence.pop();
            Sequence.pop();
        }
        else {
            Sequence.pop();
            ChessPad.ChangeTurn();
        }
 
        //安照栈重置颜色棋盘
        ChessPad.ClearColorPad();
        for (int i=0;i<Sequence.positions.size();i++){
            ChessPad.colorPad[Sequence.positions.get(i).getX()][Sequence.positions.get(i).getY()]=Sequence.colorsSequence.get(i);
        }
 
        ButtonPanel.changeMsg();
        ChessPadPanel.getInstance().repaint();
    }
}