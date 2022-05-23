//Brandon Mazur - CSCI230 Final Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserManual extends JPanel implements ActionListener {

    PlannerDisplay outref;

    public UserManual(PlannerDisplay inref) {

        outref = inref;

        //set up back button and panel
        JButton outButton = new JButton(new ImageIcon("out_arrow.png"));
        outButton.setPreferredSize(new Dimension(50,100));
        outButton.setBackground(Consts.buttonbg);
        outButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        outButton.addActionListener(this);

        JPanel backPanel = new JPanel(new GridLayout(2,1,0,80));
        backPanel.setBorder(BorderFactory.createEmptyBorder(0,0,270,10));
        backPanel.setOpaque(false);
        backPanel.add(outButton);
        backPanel.add(Box.createRigidArea(new Dimension(0,100)));

        //put together manual panel
        JPanel manualPanel = createManual();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setBackground(Consts.manualbg);
        this.add(new JLabel("<html><font size='7' color='#000000'> User Manual </font></html>", JLabel.CENTER),
                BorderLayout.NORTH);
        this.add(backPanel, BorderLayout.WEST);
        this.add(manualPanel, BorderLayout.CENTER);

    }

    private JPanel createManual() {

        final int OUT = 0;
        final int MOD = 1;
        final int ADD = 2;
        final int CONFIRM = 3;
        final int DELETE = 4;
        final int AMNT = 5;

        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));

        //general instructions
        JLabel instructions = new JLabel();
        instructions.setHorizontalAlignment(JLabel.LEFT);
        instructions.setText("<html><font size='5' color='#000000'><p> - Click on months in the year display, or days" +
                " in the month display to zoom the date into more specific days.<br> - In the week display, you can" +
                " check off events via the checkbox. You can also double click on calendar entries to modify or delete" +
                " it.<br> - Calendar <font color='#800000'> tasks <font color='#000000'> are displayed in red and notable" +
                " <font color='#008000'>dates<font color='#000000'> are displayed in green. Additionally," +
                " <i>yearly</i> events are shown in italics.</p></font></html>");
        manual.add(instructions);

        //set up specific button explanations
        JButton test = null;
        JLabel explain = null;
        JPanel display;

        for (int i = 0; i < AMNT; i++) {

            switch (i) {
                case OUT:
                    test = new JButton(new ImageIcon("out_arrow.png"));
                    explain = new JLabel("<html><font size='5' color='#000000'><p>" +
                            "Zooms out in the calendar display. In other displays," +
                            " it will take you back to the previous display. </p></font></html>");
                    break;

                case MOD:
                    test = new JButton(new ImageIcon("checklist.png"));
                    explain = new JLabel("<html><font size='5' color='#000000'><p>" +
                            "Takes you to the calendar event addition screen. Here you can add" +
                            " new tasks and important dates. </p></font></html>");
                    break;

                case ADD:
                    test = new JButton(new ImageIcon("newtask.png"));
                    explain = new JLabel("<html><font size='5' color='#000000'><p>" +
                            "Takes you to the todo list. The list will display upcoming due-dates" +
                            " and important dates. </p></font></html>");
                    break;

                case CONFIRM:
                    test = new JButton(new ImageIcon("check.png"));
                    explain = new JLabel("<html><font size='5' color='#000000'><p>" +
                            "Confirms a task or important date addition to the calendar. </p></font></html>");
                    break;

                case DELETE:
                    test = new JButton(new ImageIcon("delete.png"));
                    explain = new JLabel("<html><font size='5' color='#000000'><p>" +
                            "Confirm deletion of a task or important date. </p></font></html>");
                    break;
            }

            //add each button label combo to the panel
            test.setBackground(Consts.buttonbg);
            test.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            display = new JPanel();
            display.setLayout(new BoxLayout(display, BoxLayout.X_AXIS));
            display.add(test);
            display.add(Box.createRigidArea(new Dimension(15,0)));
            display.add(explain);
            display.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            display.setOpaque(false);
            manual.add(Box.createRigidArea(new Dimension(0,25)));
            manual.add(display);
        }

        manual.setOpaque(false);
        manual.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return manual;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((CardLayout)outref.getLayout()).show(outref, outref.currentPanel);
    }
}
