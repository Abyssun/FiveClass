package Panel;
 
import Domain.ChessPad;
import Listiner.JumpListener;
import Listiner.ModeListener;
import Listiner.RegretListener;
import Listiner.StopListener;
 
import javax.swing.*;
import java.awt.*;
 
//按钮容器
/*
本五子棋项目，实现的功能有，人机对战、人人对战、悔棋、结束游戏功能
 */
public class ButtonPanel extends JPanel{
 
    private static Font font = new Font("黑体", Font.BOLD, 20);  //按钮文本字体
    private static JButton PVE=new JButton("人机对战");    //人机模式
    private static JButton PVP=new JButton("人人对战");    //人人对战
    private static JButton stop = new JButton("结束");    //结束按钮
    private static JButton regret = new JButton("悔棋");    //悔棋按钮
    private static JButton jump=new JButton("跳过");  //跳过按钮，该按钮的功能交换回合
    private static JLabel msg=new JLabel("请选择对局模式");
 
 
    //构造函数，初始化各个按钮和容器
    public ButtonPanel(){
 
        stop.setFont(font);
        regret.setFont(font);
        PVE.setFont(font);
        PVP.setFont(font);
        jump.setFont(font);
        msg.setFont(font);
 
        //将各个组件加入到容器内
        this.add(msg);
        this.add(PVP);
        this.add(PVE);
        this.add(stop);
        this.add(jump);
        this.add(regret);
 
        ResetButton();
 
        PVE.addActionListener(new ModeListener());
        PVP.addActionListener(new ModeListener());
        jump.addActionListener(new JumpListener());
        stop.addActionListener(new StopListener());
        regret.addActionListener(new RegretListener());
    }
 
    /**
     * 该方法实现重置各个按钮的状态（即按钮是否能够被点击），在点击结束按钮，以及棋局结束时被调用
     *
     */
    public static void ResetButton(){
        PVE.setEnabled(true);
        PVP.setEnabled(true);
        jump.setEnabled(false);
        regret.setEnabled(false);
        stop.setEnabled(false);
    }
 
    /**
     * 该方法实现按钮的激活，在选择了对局模式后被调用
     */
    public static void ActiveButton(){
        jump.setEnabled(true);
        regret.setEnabled(true);
        stop.setEnabled(true);
    }
 
 
    public static JButton getStop() {
        return stop;
    }
 
    public static void setStop(JButton stop) {
        ButtonPanel.stop = stop;
    }
 
    public static JButton getRegret() {
        return regret;
    }
 
    public static void setRegret(JButton regret) {
        ButtonPanel.regret = regret;
    }
 
    public static JButton getPVE() {
        return PVE;
    }
 
    public static void setPVE(JButton PVE) {
        ButtonPanel.PVE = PVE;
    }
 
    public static JButton getPVP() {
        return PVP;
    }
 
    public static void setPVP(JButton PVP) {
        ButtonPanel.PVP = PVP;
    }
 
    public static JButton getJump() {
        return jump;
    }
 
    public static void setJump(JButton jump) {
        ButtonPanel.jump = jump;
    }
 
    public static JLabel getMsg() {
        return msg;
    }
 
    public static void setMsg(JLabel msg) {
        ButtonPanel.msg = msg;
    }
 
    public static void changeMsg(){
        if(ChessPad.isBlackTurn()==null){
            msg.setText("请选择对局模式");
        }
        else if(!ChessPad.isBlackTurn()){
            msg.setText("白棋回合");
        }
        else {
            msg.setText("黑棋回合");
        }
    }
}