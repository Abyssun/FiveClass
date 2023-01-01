package Test;
 
import Panel.MainPanel;
 
import javax.swing.*;
 
 
public class PanelTest {
    public static void main(String[] args) {
        JFrame btn=new JFrame();
 
        btn.add(MainPanel.getInstance());
        btn.setTitle("第六组 小组作业 五子棋");
        btn.setSize(800,800);
        btn.setResizable(false);
        btn.setVisible(true);
    }
 
}