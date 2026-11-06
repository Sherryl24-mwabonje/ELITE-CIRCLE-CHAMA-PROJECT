
package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {

        // Set window properties
        setTitle("ELITE CIRCLE CHAMA MANAGEMENT SYSTEM");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create the different panels
        MemberPanel memberPanel = new MemberPanel();
        ContributionPanel contributionPanel = new ContributionPanel();

        // Add panels as tabs
        tabbedPane.addTab("Members", memberPanel);
        tabbedPane.addTab("Contributions", contributionPanel);

        // Add tabs to the main window
        add(tabbedPane, BorderLayout.CENTER);

        // Make window visible
        setVisible(true);
    }
}
