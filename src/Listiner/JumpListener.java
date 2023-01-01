package Listiner;
 
import Domain.AI;
import Domain.ChessPad;
import Panel.ButtonPanel;
import Panel.ChessPadPanel;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * 跳过按钮的监听器，跳过操作的含义是，落子权的交接，所以点击该按钮后，更改当前回合即可
 */
public class JumpListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(ChessPad.mode.equals("人人对战")){
            ChessPad.ChangeTurn();
 
            //更改提示信息
            ButtonPanel.changeMsg();
        }
        else {
            ChessPad.ChangeTurn();
            ButtonPanel.changeMsg();
            AI.AiPlay();
            ChessPadPanel.getInstance().repaint();
        }
    }
}