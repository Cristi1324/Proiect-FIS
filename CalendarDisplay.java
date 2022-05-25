
package a;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

@SuppressWarnings({ "serial", "unused" })
public class CalendarDisplay extends JPanel {

    private JLabel timeTitle;
    private JPanel centerPanel;
    private JPanel mainPanel[], weekdaysPanel;
    private JButton out;
    private PlannerDisplay outref;

    private int savedDate[] = new int[4];
    private int day, weekday, month, year;
    private int displayStatus;
    private boolean weeksShown;
    private ArrayList<String>[] loadedPanels;

    @SuppressWarnings("unchecked")
	public CalendarDisplay(String current_date, PlannerDisplay inref) {

        outref = inref;
        this.parseDate(current_date);
        this.setLayout(new BorderLayout());
        this.setBackground(Consts.bg);
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        loadedPanels = new ArrayList[Consts.NUM_MAIN_PANELS];
        for (int i = 0; i < Consts.NUM_MAIN_PANELS; i++)
            loadedPanels[i] = new ArrayList<>();

        timeTitle = new JLabel("<html><font size='6' color='#000000'>"
                + Consts.MONTH_NAME[month - 1] + ' ' + year + "</font></html>");
        JButton next = new JButton(new ImageIcon(CalendarDisplay.class.getResource("/a/right_arrow.png")));
        JButton back = new JButton(new ImageIcon(CalendarDisplay.class.getResource("/a/left_arrow.png")));
        JButton taskButton = new JButton(new ImageIcon(CalendarDisplay.class.getResource("/a/newtask.png")));
        out = new JButton(new ImageIcon(CalendarDisplay.class.getResource("/a/out_arrow.png")));
        next.setBackground(Consts.buttonbg);
        back.setBackground(Consts.buttonbg);
        taskButton.setBackground(Consts.buttonbg);
        out.setBackground(Consts.buttonbg);
        next.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        back.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        out.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        next.setPreferredSize(new Dimension(60,40));
        back.setPreferredSize(new Dimension(60,40));
        taskButton.setPreferredSize(new Dimension(50,100));
        out.setPreferredSize(new Dimension(50,100));
        next.addActionListener(this.getActionListener(Consts.NEXT_LISTENER));
        back.addActionListener(this.getActionListener(Consts.PREV_LISTENER));
        taskButton.addActionListener(this.getActionListener(Consts.TASK_LISTENER));
        out.addActionListener(this.getActionListener(Consts.OUT_LISTENER));
        taskButton.setActionCommand("default");
        
        //prepare top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(back);
        topPanel.add(Box.createRigidArea(new Dimension(50,0)));
        topPanel.add(timeTitle);
        topPanel.add(Box.createRigidArea(new Dimension(50,0)));
        topPanel.add(next);
        topPanel.setOpaque(false);
        
        //prepare right panel
        JPanel rightPanel = new JPanel(new GridLayout(3,1,0,95));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5,5,50,5));
        rightPanel.add(out);
      //  rightPanel.add(todoButton);
        rightPanel.add(taskButton);
        rightPanel.setOpaque(false);

        //prepare main panels
        mainPanel = new JPanel[Consts.NUM_MAIN_PANELS];
        centerPanel = new JPanel(new CardLayout());
        for (int i = 0; i < Consts.NUM_MAIN_PANELS; i++) {
            mainPanel[i] = new JPanel(new CardLayout());
            centerPanel.add(mainPanel[i], Consts.cardNames[i]);
        }

        
        //prepare the weekdays displayed at the bottom of the screen
        JLabel[] weekdays = new JLabel[7];
        weekdaysPanel = new JPanel(new GridLayout(0,7));
        weekdaysPanel.setOpaque(false);
        weekdaysPanel.setBorder(BorderFactory.createEmptyBorder(0,60,10,0));
        for (int i = 0; i < 7; i++) {
            weekdays[i] = new JLabel("<html><font size='5' color='#000000'>" + Consts.WEEK_ABBR[i] + "</font></html>");
            weekdays[i].setHorizontalAlignment(JLabel.CENTER);
            weekdaysPanel.add(weekdays[i]);
        }

        
        //finish panel
        this.add(topPanel, BorderLayout.NORTH);
        this.add(weekdaysPanel, BorderLayout.SOUTH);
        this.add(centerPanel, BorderLayout.CENTER);http:
        this.add(rightPanel, BorderLayout.WEST);

        
        //set up first-time display
        displayStatus = Consts.MONTH;
        weeksShown = true;
        this.updateDisplay();
    }

    public void updateDisplay() {

        String date;
        Iterator<String> itr;
        boolean loaded = false;

        this.saveDate();
        this.updateWeekdayDisplay();

        //disable out button if displaying year
        out.setEnabled(displayStatus != Consts.YEAR);

        
        //construct string for loaded panels
        if (displayStatus == Consts.YEAR)
            date = Integer.toString(year);
        else if (displayStatus == Consts.MONTH)
            date = month + "/" + year;
        else {

            while (weekday > 0) {

                day--;
                if (day == 0) {
                    month--;
                    if (month == 0) {
                        month = 12;
                        year--;
                    }
                    day = Consts.DAYS_IN_MONTH[month - 1];
                    if (month == 2 && year % 4 == 0)
                        day++;
                }
                weekday--;
            }

            date = day + "/" + month + "/" + year;
        }

        
        //load previously generated panels if possible
        itr = loadedPanels[displayStatus].iterator();
        while (itr.hasNext()) {
            if (date.equals(itr.next())) {
                loaded = true;
                break;
            }
        }

        //otherwise create a new panel
        if (!loaded) {

            JPanel newPanel = null;  //to satisfy compiler
            switch (displayStatus) {
                case Consts.YEAR: newPanel = prepareYear(); break;
                case Consts.MONTH: newPanel = prepareMonth(); break;
                case Consts.WEEK: newPanel = prepareWeek(); break;
                default: System.err.println("Error in updateDisplay() - displayStatus mismatch"); System.exit(1);
            }

            mainPanel[displayStatus].add(newPanel, date);
            loadedPanels[displayStatus].add(date);
        }

        //update title
        switch (displayStatus) {
            case Consts.YEAR: timeTitle.setText("<html><font size='6' color='#000000'>" + year + "</font></html>"); break;
            case Consts.MONTH: timeTitle.setText("<html><font size='6' color='#000000'>"
                    + Consts.MONTH_NAME[month - 1] + ' ' + year + "</font></html>"); break;
            case Consts.WEEK: timeTitle.setText("<html><font size='6' color='#000000'> Saptamana "
                    + Consts.MONTH_NAME[month - 1].substring(0,3) + ' ' + day + "</font></html>"); break;
            default: System.err.println("Error in updateDisplay() - displayStatus mismatch"); System.exit(1);
        }

        //update shown card
        ((CardLayout)centerPanel.getLayout()).show(centerPanel, Consts.cardNames[displayStatus]);
        ((CardLayout)mainPanel[displayStatus].getLayout()).show(mainPanel[displayStatus], date);

        this.loadDate();
    }

    private JPanel prepareWeek() {

        JPanel output = new JPanel(new GridLayout(1,7));
        output.setBackground(Consts.bg);
        JPanel dayPanel[] = new JPanel[7];
        Iterator<CalendarEvent> yearlyItr, monthlyItr, daylyItr, regularItr, itr;
        CalendarEvent yearlyEvent = null, regularEvent = null , monthlyEvent = null, daylyEvent = null;
        int height;

        //used to display the day of each day panel
        int tempDay = day;
        int tempMonth = month;
        int tempYear = year;

        
        //prepare each daily panel
        for (int i = 0; i < 7; i++) {

        	
        	 //prepare a panel for each day
            dayPanel[i] = new JPanel();
            dayPanel[i].setLayout(new BoxLayout(dayPanel[i], BoxLayout.Y_AXIS));
            dayPanel[i].setBackground(new Color(250 - 10*i,255,255));

            
            //label for each panel
            JLabel dayLabel = new JLabel("<html><font size='5' color='#000000'>" +
                    Consts.MONTH_NAME[tempMonth - 1].substring(0,3) + ' ' + tempDay + "</font></html>");
            

          //task button for each panel
            JButton dayButton = new JButton(new ImageIcon(CalendarDisplay.class.getResource("/a/newtask.png")));
            dayButton.setBackground(Consts.buttonbg);
            dayButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            dayButton.setPreferredSize(new Dimension(50,50));
            dayButton.addActionListener(this.getActionListener(Consts.TASK_LISTENER));
            dayButton.setActionCommand(tempDay + "/" + tempMonth + "/" + tempYear);

            
          //create panel for day and button
            JPanel topPanel = new JPanel();
            dayPanel[i].add(topPanel);
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            topPanel.add(dayLabel);
            topPanel.add(Box.createRigidArea(new Dimension(5,0)));
            topPanel.add(dayButton);
            topPanel.setOpaque(false);
            topPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            topPanel.setAlignmentY(JComponent.TOP_ALIGNMENT);
            dayPanel[i].add(Box.createRigidArea(new Dimension(0,5)));

            height = topPanel.getPreferredSize().height + 5;
            itr = outref.data.iterator();
            CalendarEvent tempEvent;
            while (itr.hasNext()) {
            	tempEvent = itr.next();
            	switch(tempEvent.getType())
            	{
            	case Consts.DAYLY_DATE:
                		height += tempEvent.createCalendarPanel(dayPanel[i]);
            		break;
            	case Consts.MONTHLY_DATE:
            		if(tempEvent.getDay() == tempDay)
            			height += tempEvent.createCalendarPanel(dayPanel[i]);
        		break;
            	case Consts.YEARLY_DATE:
            		if(tempEvent.getDay() == tempDay && tempEvent.getMonth() == tempMonth)
                		height += tempEvent.createCalendarPanel(dayPanel[i]);
            		break;
            	case Consts.ONETIME_DATE:
            		if(tempEvent.getDay() == tempDay && tempEvent.getMonth() == tempMonth && tempEvent.getYear() == tempYear)
                		height += tempEvent.createCalendarPanel(dayPanel[i]);
            		break;
            	}
            	
            }

           
            //set the final dynamically calculated size of the panel
            dayPanel[i].setPreferredSize(new Dimension(120, height));

            //convert panel into a scroll pane
            JScrollPane scroll = new JScrollPane(dayPanel[i]);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(BorderFactory.createMatteBorder(2,1,2,1, Color.BLACK));

          //add components to main panel
            output.add(scroll);

            //increment the day
            tempDay++;
            if (tempMonth == 2 && tempYear % 4 == 0 ? tempDay > 29 : tempDay > Consts.DAYS_IN_MONTH[tempMonth - 1]) {
                tempDay = 1;
                tempMonth++;
                if (tempMonth > 12) {
                    tempMonth = 1;
                    tempYear++;
                }
            }
        }

        return output;
    }

    private JPanel prepareMonth() {

        JPanel output = new JPanel(new GridLayout(0,7));
        JButton buttons[];
        String labelText[];
        int numEntries[];

        //calculates how many days in the month
        int numDays = Consts.DAYS_IN_MONTH[month - 1];
        if (year % 4 == 0 && month == 2)
            numDays++;

        //prepare arrays
        buttons = new JButton[numDays];
        labelText = new String[numDays];
        numEntries = new int[numDays];

      //prepare the day for each label
        for (int i = 0; i < numDays; i++)
            labelText[i] = "";

        //set up the frame border and background
        final Dimension daySize = new Dimension(100,80);
        output.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        output.setBackground(Consts.bg);

        //calculate the starting weekday of the month
        int startingWeekday = (weekday - day - 6) % 7;
        if (startingWeekday < 0)
            startingWeekday += 7;

        //fill days with blank spaces until the proper weekday for day1 is reached
        int count = 0;
        for (int i = 0; i < startingWeekday; i++, count++)
            output.add(Box.createRigidArea(daySize));

      //create each button and add it to panel
        for (int i = 0; i < numDays; i++, count++) {
            buttons[i] = new JButton();
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            buttons[i].setBackground(new Color(255 - 3*i,255,255));
            buttons[i].setActionCommand(Integer.toString(i + 1));
            buttons[i].addActionListener(this.getActionListener(Consts.MONTH_LISTENER));
            output.add(buttons[i]);
        }

      //pack the calendar to make all months look consistent
        while (count < 42) {
            output.add(Box.createRigidArea(daySize));
            count++;
        }

        
      //grab the contents of events from the current month
        Iterator<CalendarEvent> itr = outref.data.iterator();
        CalendarEvent tempEvent;
        while (itr.hasNext()) {

            tempEvent = itr.next();
            if ((tempEvent.getYear() == Consts.YEARLESS || tempEvent.getYear() == year) && (tempEvent.getMonth() == -1 || tempEvent.getMonth() == month)) {
            	
                int index = tempEvent.getDay() - 1;
                if(index<0)
                {
                	for(int i=1;i<numDays;i++)
                	{
                		numEntries[i]++;
                        labelText[i] += "<br>" + tempEvent.getCalendarText();
                        if (numEntries[i] == 5)
                            labelText[i] = '*' + labelText[index];
                	}
                }else
                {
                	numEntries[index]++;
                    labelText[index] += "<br>" + tempEvent.getCalendarText();
                    //adds to marker to show there are non-displayed events
                    if (numEntries[index] == 5)
                        labelText[index] = '*' + labelText[index];
                }
                

                
            }
        }

      //add proper text from data to each button
        for (int i = 0; i < numDays; i++) {
            JLabel tempLabel = new JLabel("<html><b><font size='5'>" + Integer.toString(i + 1)
                    + "</b></font><font size='2'>" + labelText[i] + "<br><br><br><br><br><br></font></html>");
            tempLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            tempLabel.setVerticalAlignment(JLabel.NORTH);
            buttons[i].add(tempLabel);
        }

        return output;
    }

    private JPanel prepareYear() {

        JPanel output = new JPanel(new GridLayout(3,4,30,30));
        output.setBackground(Consts.bg);
        output.setBorder(BorderFactory.createEmptyBorder(15,25,15,25));

        JPanel monthPanel;
        JButton monthButton;

        for (int i = 0; i < 12; i++) {
        	
        	 //create each month panel and set it up
            monthPanel = new JPanel();
            monthPanel.setLayout(new BorderLayout());
            monthPanel.setBackground(new Color(250 - i*8, 255, 255));
            monthPanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.BLACK));

          //set up and color the button
            monthButton = new JButton("<html><font size='6' color='#00000'>"
                    + Consts.MONTH_NAME[i] + "</font></html>");
            monthButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            monthButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            monthButton.setBorderPainted(false);
            monthButton.setBackground(new Color(250 - i*8, 255, 255));
            monthButton.setActionCommand(Integer.toString(i + 1));
            monthButton.addActionListener(this.getActionListener(Consts.YEAR_LISTENER));

            monthPanel.add(monthButton, BorderLayout.CENTER);
            output.add(monthPanel);
        }

        return output;
    }

    private ActionListener getActionListener(int code) {

        switch (code) {

        //listener for the forward button
            case Consts.NEXT_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switch (displayStatus) {

                        case Consts.YEAR: {

                        	 //calculate new weekday
                            weekday -= day - 1;
                            day = 1;
                            while (month <= 12) {
                                weekday += month == 2 && year % 4 == 0 ? 29 : Consts.DAYS_IN_MONTH[month - 1];
                                month++;
                            }
                            weekday %= 7;
                            if (weekday < 0)
                                weekday += 7;
                            month = 1;
                            year++;

                            updateDisplay();
                            break;
                        }

                        case Consts.MONTH: {

                            weekday += Consts.DAYS_IN_MONTH[month - 1] - day + 1;
                            if (month == 2 && year % 4 == 0)
                                weekday++;
                            weekday %= 7;
                            if (weekday < 0)
                                weekday += 7;
                            day = 1;

                            month++;
                            if (month == 13) {
                                month = 1;
                                year++;
                            }

                            updateDisplay();
                            break;
                        }

                        case Consts.WEEK: {

                            for (int i = 0; i < 7; i++) {
                                day++;
                                if (month == 2 && year % 4 == 0 ? day > 29 : day > Consts.DAYS_IN_MONTH[month - 1]) {
                                    day = 1;
                                    month++;
                                    if (month > 12) {
                                        month = 1;
                                        year++;
                                    }
                                }
                            }

                            updateDisplay();
                            break;
                        }
                    }
                }
            };

            //listener for the back button
            case Consts.PREV_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switch (displayStatus) {

                        case Consts.YEAR: {

                            weekday -= day - 1;
                            while (month > 1) {
                                month--;
                                weekday -= (month == 2 && year % 4 == 0) ? 29 : Consts.DAYS_IN_MONTH[month - 1];
                            }
                            year--;
                            month = 12;
                            weekday = (weekday - 1) % 7;
                            day = Consts.DAYS_IN_MONTH[month - 1];
                            if (weekday < 0)
                                weekday += 7;

                            updateDisplay();
                            break;
                        }

                        case Consts.MONTH: {
                            month--;
                            if (month == 0) {
                                month = 12;
                                year--;
                            }

                            weekday = weekday - day - Consts.DAYS_IN_MONTH[month - 1] + 1;
                            if (month == 2 && year % 4 == 0)
                                weekday--;
                            weekday %= 7;
                            if (weekday < 0)
                                weekday += 7;
                            day = 1;

                            updateDisplay();
                            break;
                        }

                        case Consts.WEEK: {

                            for (int i = 0; i < 7; i++) {
                                day--;
                                if (day == 0) {
                                    month--;
                                    if (month == 0) {
                                        month = 12;
                                        year--;
                                    }
                                    day = Consts.DAYS_IN_MONTH[month - 1];
                                    if (month == 2 && year % 4 == 0)
                                        day++;
                                }
                            }

                            updateDisplay();
                            break;
                        }
                    }
                }
            };

            //listener for the zoom-out button
            case Consts.OUT_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (displayStatus == Consts.MONTH)
                        displayStatus = Consts.YEAR;

                    else if (displayStatus == Consts.WEEK)
                        displayStatus = Consts.MONTH;

                    updateDisplay();
                }
            };

            //listener for the clipboard button
            case Consts.TODO_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    outref.backToCalendar = false;
                    outref.currentPanel = outref.getTodoCard();
                    ((CardLayout)outref.getLayout()).show(outref, outref.getTodoCard());
                }
            };

            //listener for the new-event add button
            case Consts.TASK_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                	 //spec[] is formatted: { year, month, day, type } (type being the event type; not useful here)
                    int[] spec = new int[] { -1, -1, -1, -1 };

                    //populate spec array with correct daya
                    if (e.getActionCommand().equals("default")) {
                        switch (displayStatus) {
                            case Consts.WEEK:
                            case Consts.MONTH: spec[1] = month;
                            case Consts.YEAR: spec[0] = year;
                        }
                    }
                    else {
                        StringTokenizer st = new StringTokenizer(e.getActionCommand(), "/");
                        for (int i = 2; i >= 0; i--)
                            spec[i] = Integer.parseInt(st.nextToken());
                    }

                    //send spec to modification card
                    outref.toModCard(spec);
                }
            };

            //listener for each button on month panels
            case Consts.MONTH_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int newDay = Integer.parseInt(e.getActionCommand());
                    weekday = (weekday + newDay - day) % 7;
                    if (weekday < 0)
                        weekday += 7;

                    day = newDay;
                    displayStatus = Consts.WEEK;
                    updateDisplay();
                }
            };

            //listener for each button on year panels
            case Consts.YEAR_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int newMonth = Integer.parseInt(e.getActionCommand());
                    weekday -= day - 1;
                    day = 1;
                    int increment = newMonth > month ? 1 : -1;
                    while (month != newMonth) {
                        month += increment;
                        weekday += (month == 2 && year % 4 == 0 ? 29 : Consts.DAYS_IN_MONTH[month - 1]) * increment;
                    }
                    weekday %= 7;
                    if (weekday < 0)
                        weekday += 7;

                    displayStatus = Consts.MONTH;
                    updateDisplay();
                }
            };

            default: System.err.print("Error in CalendarDisplay.getActionListener()"); System.exit(1);
        }

        return null; 
    }

    private void updateWeekdayDisplay() {

    	 //updates the weekdays shown at the bottom of the screen
        if (weeksShown && displayStatus == Consts.YEAR) {
            this.remove(weekdaysPanel);
            weeksShown = false;
        }

        else if (!weeksShown && displayStatus != Consts.YEAR) {
            this.add(weekdaysPanel, BorderLayout.SOUTH);
            weeksShown = true;
        }
    }

    public void deleteAffectedPanels(int afYear, int afMonth, int afDay, int type) {

    	 //if a yearly event is deleted, delete most panels
        if (type == Consts.YEARLY_DATE) {

            Iterator<String> itr = loadedPanels[Consts.MONTH].iterator();
            ArrayList<String> toRemove = new ArrayList<>();
            String temp;
            int monthValue;
            while (itr.hasNext()) {
                temp = itr.next();
                monthValue = temp.charAt(0) - '0';
                if (temp.charAt(1) != '/')
                    monthValue = (monthValue * 10) + (temp.charAt(1) - '0');
                if (monthValue == afMonth)
                    toRemove.add(temp);
            }
            itr = toRemove.iterator();
            while (itr.hasNext())
                loadedPanels[Consts.MONTH].remove(itr.next());

            loadedPanels[Consts.WEEK].clear();
        }
        else if (type == Consts.MONTHLY_DATE) {

            Iterator<String> itr = loadedPanels[Consts.WEEK].iterator();
            ArrayList<String> toRemove = new ArrayList<>();
            String temp;
            int weekValue;
            while (itr.hasNext()) {
                temp = itr.next();
                weekValue = temp.charAt(0) - '0';
                if (temp.charAt(1) != '/')
                    weekValue = (weekValue * 10) + (temp.charAt(1) - '0');
                if (weekValue == afMonth)
                    toRemove.add(temp);
            }
            itr = toRemove.iterator();
            while (itr.hasNext())
                loadedPanels[Consts.WEEK].remove(itr.next());

        } else if (type == Consts.DAYLY_DATE) {

            Iterator<String> itr = loadedPanels[Consts.DAY].iterator();
            ArrayList<String> toRemove = new ArrayList<>();
            String temp;
            int weekValue;
            while (itr.hasNext()) {
                temp = itr.next();
                weekValue = temp.charAt(0) - '0';
                if (temp.charAt(1) != '/')
                    weekValue = (weekValue * 10) + (temp.charAt(1) - '0');
                if (weekValue == afMonth)
                    toRemove.add(temp);
            }
            itr = toRemove.iterator();
            while (itr.hasNext())
                loadedPanels[Consts.DAY].remove(itr.next());

        }

        //otherwise just get rid of affected panels
        else {

            loadedPanels[Consts.MONTH].remove(afMonth + "/" + afYear);

            int tempYear = afYear, tempMonth = afMonth, tempDay = afDay;
            boolean yearLater, monthLater, dayLater;
            int calculatedWeekday = 0;

            //calculate the weekday of the date
            yearLater = afYear < year;
            monthLater = afMonth < month;
            dayLater = afDay < day;

            if (yearLater) while (afYear != year) {
                calculatedWeekday -= afYear % 4 == (afMonth > 3 ? 0 : 3) ? 366 : 365;
                afYear++;
            }
            else while (afYear != year) {
                calculatedWeekday += afYear % 4 == (afMonth > 3 ? 0 : 3) ? 366 : 365;
                afYear--;
            }

            if (monthLater) while (afMonth != month) {
                calculatedWeekday -= afYear % 4 == 0 && afMonth == 2 ? 29 : Consts.DAYS_IN_MONTH[afMonth - 1];
                afMonth++;
            }
            else while (afMonth != month) {
                afMonth--;
                calculatedWeekday += afYear % 4 == 0 && afMonth == 2 ? 29 : Consts.DAYS_IN_MONTH[afMonth - 1];
            }

            if (dayLater) while (afDay != day) {
                calculatedWeekday--;
                afDay++;
            }
            else while (afDay != day) {
                afDay--;
                calculatedWeekday++;
            }

            calculatedWeekday += weekday;
            calculatedWeekday %= 7;
            if (calculatedWeekday < 0)
                calculatedWeekday += 7;

            while (calculatedWeekday > 0) {
                tempDay--;
                if (tempDay == 0) {
                    tempMonth--;
                    if (tempMonth == 0) {
                        tempMonth = 12;
                        tempYear--;
                    }
                    tempDay = tempYear % 4 == 0 && tempMonth == 2 ? 29 : Consts.DAYS_IN_MONTH[tempMonth - 1];
                }
                calculatedWeekday--;
            }

            loadedPanels[Consts.WEEK].remove(tempDay + "/" + tempMonth + "/" + tempYear);
        }

    }

    private void saveDate() {
        savedDate[0] = day;
        savedDate[1] = weekday;
        savedDate[2] = month;
        savedDate[3] = year;
    }

    private void loadDate() {
        day = savedDate[0];
        weekday = savedDate[1];
        month = savedDate[2];
        year = savedDate[3];
    }

    private void parseDate(String date) {

    	 //loads date into calendar display
        StringTokenizer st = new StringTokenizer(date, "/");
        day = Integer.parseInt(st.nextToken());
        weekday = Integer.parseInt(st.nextToken());
      //sunday should translate to weekday 0
        if (weekday == 7) 
            weekday = 0;
        month = Integer.parseInt(st.nextToken());
        year = Integer.parseInt(st.nextToken());
    }
}