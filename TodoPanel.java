//Brandon Mazur - CSCI230 Final Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class TodoPanel extends JPanel implements ActionListener {

    private final int MAX_DAYS_AHEAD = 15;

    private int day, month, year;
    private PlannerDisplay outref;
    private JPanel todoList, upcomingEvents;
    private JScrollPane taskScroll, dateScroll;

    public TodoPanel(PlannerDisplay inref, String date) {

        //prepare panel basics
        outref = inref;
        this.parseDate(date);

        JPanel todoHolder, eventsHolder;

        //prepare buttons
        JButton outButton = new JButton(new ImageIcon("out_arrow.png"));
        outButton.setBackground(Consts.buttonbg);
        outButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        outButton.setPreferredSize(new Dimension(50,100));
        outButton.addActionListener(this);

        JPanel controlPanel = new JPanel(new GridLayout(2,1,0,80));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(40,0,237,10));
        controlPanel.setOpaque(false);
        controlPanel.add(outButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0,100)));

        //set up important subpanels
        todoList = new JPanel();
        todoList.setLayout(new BoxLayout(todoList, BoxLayout.Y_AXIS));
        todoList.setBackground(Consts.todoinbg);

        taskScroll = new JScrollPane(todoList);
        taskScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScroll.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.BLACK));

        todoHolder = new JPanel();
        todoHolder.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        todoHolder.setLayout(new BoxLayout(todoHolder, BoxLayout.Y_AXIS));
        todoHolder.setOpaque(false);
        todoHolder.add(new JLabel("<html><font size='6' color='#000000'> Todo List </font></html>"));
        todoHolder.add(taskScroll);

        upcomingEvents = new JPanel();
        upcomingEvents.setLayout(new BoxLayout(upcomingEvents, BoxLayout.Y_AXIS));
        upcomingEvents.setBackground(Consts.todoinbg);

        dateScroll = new JScrollPane(upcomingEvents);
        dateScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dateScroll.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.BLACK));

        eventsHolder = new JPanel();
        eventsHolder.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        eventsHolder.setLayout(new BoxLayout(eventsHolder, BoxLayout.Y_AXIS));
        eventsHolder.setOpaque(false);
        eventsHolder.add(new JLabel("<html><font size='6' color='#000000'> Upcoming Events </font></html>"));
        eventsHolder.add(dateScroll);

        //put everything together
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20,10,50,70));
        this.setBackground(Consts.todobg);
        this.add(controlPanel, BorderLayout.WEST);
        this.add(todoHolder, BorderLayout.CENTER);
        this.add(eventsHolder, BorderLayout.EAST);
        this.clearPanel();
    }

    public void loadPanel() {

        //set up to-do and event panels
        Iterator<CalendarEvent> itr = outref.data.iterator();
        CalendarEvent tempEvent = null;
        JPanel tempPanel;

        int taskHeight = 0, dateHeight = 0;
        int dayDiff;
        boolean laterFlag = false;
        boolean moreEvents = true;

        int nextMonth = month == 12 ? 1 : month + 1;

        //load yearly events
        while (itr.hasNext()) {

            tempEvent = itr.next();
            if (tempEvent.getYear() != Consts.YEARLESS)
                break;

            else if ((tempEvent.getMonth() == month && tempEvent.getDay() >= day)
                    || (tempEvent.getMonth() == nextMonth && tempEvent.getDay() < day)) {
                tempPanel = tempEvent.getTodoPanel(upcomingEvents);
                upcomingEvents.add(tempPanel);
                upcomingEvents.add(Box.createRigidArea(new Dimension(0,10)));
                dateHeight += tempPanel.getPreferredSize().height;
                laterFlag = true;
            }

            else if (laterFlag)
                while (itr.hasNext()) {
                    tempEvent = itr.next();
                    if (tempEvent.getYear() != Consts.YEARLESS)
                        break;
                }
        }

        //loading relevant events into lists
        if (tempEvent != null && itr.hasNext()) do {

            dayDiff = ((tempEvent.getYear() * 365) + ((tempEvent.getYear() - (tempEvent.getMonth() > 2 ? 0 : 1)) / 4) +
                    Consts.SUM_AT_MONTH[tempEvent.getMonth() - 1] + tempEvent.getDay())
                    - ((year * 365) + ((year - (month > 2 ? 0 : 1)) / 4) + Consts.SUM_AT_MONTH[month - 1] + day);

            if (dayDiff >= 0 && dayDiff <= MAX_DAYS_AHEAD && tempEvent.getType() == Consts.ONETIME_DATE) {
                tempPanel = tempEvent.getTodoPanel(upcomingEvents);
                upcomingEvents.add(tempPanel);
                upcomingEvents.add(Box.createRigidArea(new Dimension(0,10)));
                dateHeight += tempPanel.getPreferredSize().height + 20;
            }

            if (dayDiff > MAX_DAYS_AHEAD)
                break;

            if (itr.hasNext())
                tempEvent = itr.next();
            else
                moreEvents = false;

        } while (moreEvents);

        //if there are no tasks or dates, display it in the panels
        if (taskHeight == 0) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.add(new JLabel("<html><font size='4' color='#000000'> No upcoming tasks </font></html>"));
            todoList.add(panel);
        }
        if (dateHeight == 0) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.add(new JLabel("<html><font size='4' color='#000000'> No upcoming events </font></html>"));
            upcomingEvents.add(panel);
        }

        todoList.setPreferredSize(new Dimension(todoList.getPreferredSize().width, taskHeight));
        upcomingEvents.setPreferredSize(new Dimension(upcomingEvents.getPreferredSize().width, dateHeight));
    }

    public void clearPanel() {

        //clears panel
        todoList.removeAll();
        todoList.setPreferredSize(new Dimension(400,10));
        upcomingEvents.removeAll();
        upcomingEvents.setPreferredSize(new Dimension(300,10));
    }

    private void parseDate(String date) {

        //loads date into panel
        StringTokenizer st = new StringTokenizer(date, "/");
        day = Integer.parseInt(st.nextToken());
        st.nextToken();
        month = Integer.parseInt(st.nextToken());
        year = Integer.parseInt(st.nextToken());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        outref.backToCalendar = true;
        outref.currentPanel = outref.getCalendarCard();
        ((CardLayout)outref.getLayout()).show(outref, outref.getCalendarCard());
    }
}
