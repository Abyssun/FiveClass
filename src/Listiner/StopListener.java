package Listiner;
 
import Domain.ChessPad;
import Domain.Sequence;
import Panel.ButtonPanel;
import Panel.ChessPadPanel;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * 结束按钮的监听器
 */
public class StopListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //点击结束按钮以后，清空棋盘上所有的棋子
        ChessPad.ClearColorPad();
        ChessPad.ClearValuePad();
 
        //清空上一局的下棋顺序
        Sequence.ClearSequence();
 
        //清空棋盘以后，重置按钮
        ButtonPanel.ResetButton();
        ChessPadPanel.getInstance().removeMouseListener(ChessPadPanel.listener);
        ChessPad.mode="";
        ChessPad.setBlackTurn(null);
 
        //然后重新绘制棋盘
        ChessPad.ClearColorPad();
        ChessPad.ClearValuePad();
        ChessPadPanel.getInstance().repaint();
    }
}