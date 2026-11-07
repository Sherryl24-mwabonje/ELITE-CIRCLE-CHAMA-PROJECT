package app;

import javax.swing.JFrame;
import ui.MemberPanel;

public class MemberPanelTest {
    public static void main(String[] args) {
        JFrame f = new JFrame("Elite Circle Chama - Members");
        f.add(new MemberPanel());
        f.setSize(980, 620);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}