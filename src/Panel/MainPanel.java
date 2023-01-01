package Panel;
 
import Domain.ChessPad;
import Listiner.PutChessListener;
 
import javax.swing.*;
import java.awt.Toolkit;
 
//主界面，父类是JSplitPane
public class MainPanel extends JFrame {
	public MainPanel() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\eclipse\\eclipse-workspace\\五子棋\\image\\微信图片_20221221110644.png"));
	}
    private  static JSplitPane instance;
 
    //主界面的构造函数，用于初始化界面
    static {
        ButtonPanel buttonPanel=new ButtonPanel();
        ChessPadPanel chessPadPanel=ChessPadPanel.getInstance();
        chessPadPanel.listener=new PutChessListener();
 
 
        ChessPad.ClearColorPad();   //初始化棋盘
        instance=new JSplitPane(JSplitPane.VERTICAL_SPLIT,chessPadPanel,buttonPanel);
        instance.setDividerLocation(700);
        instance.setEnabled(false);
    }
 
    public static JSplitPane getInstance(){
        return instance;
    }
}