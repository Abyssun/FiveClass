package Listiner;
 
import Domain.AI;
import Domain.ChessPad;
import Panel.ButtonPanel;
import Panel.ChessPadPanel;
 
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * 模式监听器，作用在模式按钮上
 */
public class ModeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
 
        ChessPadPanel.getInstance().addMouseListener(ChessPadPanel.listener);
        if(actionCommand.equals("人机对战")){
            //选择的模式为人机对战，则将当前对战模式设为人机对战，同时禁用人人对战按钮
            ChessPad.mode="人机对战";
            //人机对战永远时机器人先手，所以在此处设置回合为机器人回合
            ChessPad.setBlackTurn(true);
            JButton pvp = ButtonPanel.getPVP();
            pvp.setEnabled(false);
            ButtonPanel.getPVE().setEnabled(false);
 
            //清空棋盘
            ChessPad.ClearColorPad();
            ChessPad.ClearValuePad();
            //一选择模式，就要完成人机下棋操作，并重绘棋盘
            AI.AiPlay();
            ChessPadPanel.getInstance().repaint();
        }
        else {
            ChessPad.setBlackTurn(true);
            ChessPad.mode="人人对战";
            JButton pve = ButtonPanel.getPVE();
            pve.setEnabled(false);
            ButtonPanel.getPVP().setEnabled(false);
 
            //清空棋盘
            ChessPad.ClearColorPad();
            ChessPad.ClearValuePad();
            ChessPadPanel.getInstance().repaint();
        }
 
        ButtonPanel.ActiveButton();
    }
}