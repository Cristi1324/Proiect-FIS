
package a;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.StringTokenizer;


@SuppressWarnings("serial")
public class ModificationPanel extends JPanel {

    private final int DESCRIPTION_MAX = 250;
    private final int TITLE_MAX = 40;
    private final String topLabelDefault = "<html><font size='6' color='#000000'> Adauga un eveniment </font></html>";
    private final String topLabelModify = "<html><font size='6' color='#600000'> Modifica evenimentul </font></html>";

    private int currentDay, currentMonth, currentYear;
    private PlannerDisplay outref;
    private JTextField dayText, monthText, yearText;
    private JTextArea description, title;
    private JRadioButton  onetimeRadioButton, yearlyRadioButton, monthlyRadioButton, daylyRadioButton;
    private JLabel descLimit, titleLimit, topLabel;
    private JButton deleteButton;
    private JPanel radioPanel;
    private CalendarEvent eventToModify;

    public ModificationPanel(String date, PlannerDisplay out_reference) {
    	
    	//prepare panel basics
        this.parseDate(date);
        outref = out_reference;
        eventToModify = null;

        JPanel controlPanel, datePanel, descriptionPanel;
        JButton outButton, confirmButton;

        //setting up buttons
        outButton = new JButton(new ImageIcon(ModificationPanel.class.getResource("/a/out_arrow.png")));
        confirmButton = new JButton(new ImageIcon(ModificationPanel.class.getResource("/a/check.png")));
        deleteButton = new JButton(new ImageIcon(ModificationPanel.class.getResource("/a/delete.png")));
        outButton.setBackground(Consts.buttonbg);
        confirmButton.setBackground(Consts.buttonbg);
        deleteButton.setBackground(Consts.buttonbg);
        deleteButton.setBackground(Consts.buttonbg);
        outButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        deleteButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        outButton.setPreferredSize(new Dimension(50,100));
        confirmButton.setPreferredSize(new Dimension(50,100));
        deleteButton.setPreferredSize(new Dimension(50,100));
        outButton.addActionListener(this.getActionListener(Consts.OUT_LISTENER));
        confirmButton.addActionListener(this.getActionListener(Consts.CONFIRM_LISTENER));
        deleteButton.addActionListener(this.getActionListener(Consts.DELETE_LISTENER));
        deleteButton.setEnabled(false);

        //prepare control panel
        controlPanel = new JPanel(new GridLayout(3,1,0,95));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(48,0,80,0));
        controlPanel.setOpaque(false);
        controlPanel.add(outButton);
        controlPanel.add(deleteButton);
        controlPanel.add(confirmButton);

        //set up description panel
        titleLimit = new JLabel("<html><font size='4'> Caractere: 0/" + TITLE_MAX + "</font></html>");
        title = new JTextArea();
        title.setLineWrap(true);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 16));
        title.setPreferredSize(new Dimension(200,20));
        title.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
        title.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { titleLimit.setText("<html><font size='4'> Caractere: "
                    + title.getText().length() + '/' + TITLE_MAX + " </font></html>"); }

            @Override
            public void removeUpdate(DocumentEvent e) { titleLimit.setText("<html><font size='4'> Caractere: "
                    + title.getText().length() + '/' + TITLE_MAX + " </font></html>"); }

            @Override
            public void changedUpdate(DocumentEvent e) { titleLimit.setText("<html><font size='4'> Caractere: " +
                    title.getText().length() + '/' + TITLE_MAX + " </font></html>"); }
        });
        descLimit = new JLabel("<html><font size='4'> Caractere: 0/" + DESCRIPTION_MAX + "</font></html>");
        description = new JTextArea();
        description.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { descLimit.setText("<html><font size='4'> Caractere: "
                    + description.getText().length() + '/' + DESCRIPTION_MAX + " </font></html>"); }

            @Override
            public void removeUpdate(DocumentEvent e) { descLimit.setText("<html><font size='4'> Caractere: "
                    + description.getText().length() + '/' + DESCRIPTION_MAX + " </font></html>"); }

            @Override
            public void changedUpdate(DocumentEvent e) { descLimit.setText("<html><font size='4'> Caractere: " +
                        description.getText().length() + '/' + DESCRIPTION_MAX + " </font></html>"); }
        });
        description.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
        description.setLineWrap(true);
        description.setFont(new Font(title.getFont().getName(), Font.PLAIN, 12));
        descriptionPanel = new JPanel();
        descriptionPanel.setPreferredSize(new Dimension(600,800));
        descriptionPanel.setLayout(new GridLayout(0,1));
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(10,50,20,50));
        descriptionPanel.add(new JLabel("<html><font size='5' color='#000000'> Titlu </font></html>"));
        descriptionPanel.add(title);
        descriptionPanel.add(titleLimit);
        descriptionPanel.add(new JLabel("<html><font size='5' color='#000000'> Descriere </font></html>"));
        descriptionPanel.add(new JScrollPane(description));
        descriptionPanel.setOpaque(false);
        descriptionPanel.add(descLimit);
        descriptionPanel.add(Box.createRigidArea(new Dimension(0,240)));

        //setting up date panel (controls the date / condition of new event)
        dayText = new JTextField();
        dayText.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
        dayText.setBackground(Color.WHITE);
        monthText = new JTextField();
        monthText.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
        monthText.setBackground(Color.WHITE);
        yearText = new JTextField();
        yearText.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK));
        yearText.setBackground(Color.WHITE);

      //radio buttons for date panel
        onetimeRadioButton = new JRadioButton("Doar o data");
        onetimeRadioButton.setActionCommand("onetime");
        onetimeRadioButton.addActionListener(this.getActionListener(Consts.RADIO_LISTENER));
        onetimeRadioButton.setSelected(true);
        onetimeRadioButton.setEnabled(false);
        onetimeRadioButton.setOpaque(false);
        yearlyRadioButton = new JRadioButton("Anual");
        yearlyRadioButton.setActionCommand("yearly");
        yearlyRadioButton.addActionListener(this.getActionListener(Consts.RADIO_LISTENER));
        yearlyRadioButton.setEnabled(false);
        yearlyRadioButton.setOpaque(false);
        
        monthlyRadioButton = new JRadioButton("Lunar");
        monthlyRadioButton.setActionCommand("monthly");
        monthlyRadioButton.addActionListener(this.getActionListener(Consts.RADIO_LISTENER));
        monthlyRadioButton.setEnabled(false);
        monthlyRadioButton.setOpaque(false);
        
        daylyRadioButton = new JRadioButton("Zilnic");
        daylyRadioButton.setActionCommand("dayly");
        daylyRadioButton.addActionListener(this.getActionListener(Consts.RADIO_LISTENER));
        daylyRadioButton.setEnabled(false);
        daylyRadioButton.setOpaque(false);

        ButtonGroup eventGroup = new ButtonGroup();
        eventGroup.add(onetimeRadioButton);
        eventGroup.add(yearlyRadioButton);
        eventGroup.add(monthlyRadioButton);
        eventGroup.add(daylyRadioButton);

      //put together radio panel
        radioPanel = new JPanel(new GridLayout(4,1,20,6));
        radioPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        radioPanel.setBackground(Consts.modrb);
        radioPanel.add(onetimeRadioButton);
        radioPanel.add(yearlyRadioButton);
        radioPanel.add(monthlyRadioButton);
        radioPanel.add(daylyRadioButton);

      //finish date panel
        datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        datePanel.setOpaque(false);
        datePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        topLabel = new JLabel(topLabelDefault);
        topLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        topLabel.setHorizontalAlignment(JLabel.LEFT);
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(topLabel, BorderLayout.CENTER);
        tempPanel.setOpaque(false);
        datePanel.add(tempPanel);
        datePanel.add(Box.createRigidArea(new Dimension(0,50)));
        datePanel.add(new JLabel("<html><font size='5' color='#000000'> Ziua </font></html>", JLabel.LEFT));
        datePanel.add(dayText);
        datePanel.add(Box.createRigidArea(new Dimension(0,50)));
        datePanel.add(new JLabel("<html><font size='5' color='#000000'> Luna </font></html>"));
        datePanel.add(monthText);
        datePanel.add(Box.createRigidArea(new Dimension(0,50)));
        datePanel.add(new JLabel("<html><font size='5' color='#000000'> Anul </font></html>"));
        datePanel.add(yearText);
        datePanel.add(Box.createRigidArea(new Dimension(0,50)));
        datePanel.add(radioPanel);

        //putting everything together
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(controlPanel);
        this.add(datePanel);
        this.add(descriptionPanel);
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setBackground(Consts.modbg);
    }

    private ActionListener getActionListener(int code) {

        switch (code) {
        
        //listener for the back button
            case Consts.OUT_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	 //go back to previously shown panel
                    outref.currentPanel = outref.backToCalendar
                            ? outref.getCalendarCard() : outref.getTodoCard();
                    ((CardLayout)outref.getLayout()).show(outref, outref.backToCalendar
                            ? outref.getCalendarCard() : outref.getTodoCard());
                }
            };

            //listener for confirmation
            case Consts.CONFIRM_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    StringBuilder all_errors = new StringBuilder();
                    boolean valid = true;

                    //checks for errors
                    if (!validDate(all_errors))
                        valid = false;
                    if (!validDescription(all_errors))
                        valid = false;
                    if (!validTitle(all_errors))
                        valid = false;
              
                    //if errors display them
                    if (!valid)
                        JOptionPane.showMessageDialog(null, all_errors.toString(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    else {

                        String outMsg;
                        boolean isYearly = yearlyRadioButton.isSelected();
                        boolean isMonthly = monthlyRadioButton.isSelected();
                        boolean isDayly = daylyRadioButton.isSelected();
                        boolean isOnetime = onetimeRadioButton.isSelected();
                        int day = -1;
                        int month = -1;
                        int year = -1;
                        if(isMonthly)
                        {
                        	day = Integer.parseInt(dayText.getText());
                        }
                        if(isYearly)
                        {
                        	month = Integer.parseInt(monthText.getText());
                        	day = Integer.parseInt(dayText.getText());
                        };
                        if(isOnetime) 
                        {
                        	year = Integer.parseInt(yearText.getText());
                        	month = Integer.parseInt(monthText.getText());
                        	day = Integer.parseInt(dayText.getText());
                        }

                        //new events
                        if (eventToModify == null) {

                        	 //add new task to data
                   
                            outref.data.add(new CalendarDate(outref.modificationPanel, year, month, day,
                                        title.getText(), description.getText(), isYearly, isMonthly, isDayly));

                            outref.calendarDisplay.deleteAffectedPanels(year, month, day,
                                    (isYearly ? Consts.YEARLY_DATE : 0) + (isMonthly ? Consts.MONTHLY_DATE : 0)+ (isDayly ? Consts.DAYLY_DATE : 0));

                            
                            outMsg = "Eveniment creat";
                        }

                        //modifying events
                        else {

                            if (JOptionPane.showConfirmDialog(null,
                                    "Doriti sa modificati acest evenimet?", "Confirm:",
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                                CalendarEvent newEvent;

                                //prepare tasks
                                newEvent = new CalendarDate(outref.modificationPanel, year, month, day,
                                            title.getText(), description.getText(), isYearly, isMonthly, isDayly);

                                //replace old task with modified one
                                outref.data.set(outref.data.indexOf(eventToModify), newEvent);

                                //fix potentially changed panels
                                outref.calendarDisplay.deleteAffectedPanels(year, month, day,
                                        (isYearly ? Consts.YEARLY_DATE : 0) + (isMonthly ? Consts.MONTHLY_DATE : 0)+ (isDayly ? Consts.DAYLY_DATE : 0));
                                outref.calendarDisplay.deleteAffectedPanels(eventToModify.getYear(),
                                        eventToModify.getMonth(), eventToModify.getDay(), eventToModify.getType());

                                outMsg = "Eveniment modificat";
                            }

                            else
                                outMsg = "Nu s-a putut modifica evenimentul.";
                        }

                        //if no change is made then don't do anything
                        if (!outMsg.equals("")) {
                            JOptionPane.showMessageDialog(null, outMsg);
                            Collections.sort(outref.data);
                            outref.calendarDisplay.updateDisplay();
                         
                        }

                        //go back to previous panel
                        outref.currentPanel = outref.backToCalendar
                                ? outref.getCalendarCard() : outref.getTodoCard();
                        ((CardLayout) outref.getLayout()).show(outref, outref.backToCalendar
                                ? outref.getCalendarCard() : outref.getTodoCard());
                    }
                }
            };

          //listener for radio buttons
            case Consts.RADIO_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switch (e.getActionCommand()) {

                        case "yearly":
                            yearText.setEnabled(false);
                            yearText.setBackground(Color.GRAY);
                            monthText.setEnabled(true);
                            monthText.setBackground(Color.WHITE);
                            dayText.setEnabled(true);
                            dayText.setBackground(Color.WHITE);
                            break;
                            
                        case "monthly":
                        	yearText.setEnabled(false);
                            yearText.setBackground(Color.GRAY);
                            monthText.setEnabled(false);
                            monthText.setBackground(Color.GRAY);
                            dayText.setEnabled(true);
                            dayText.setBackground(Color.WHITE);
                            break;
                            
                        case "dayly":
                        	yearText.setEnabled(false);
                            yearText.setBackground(Color.GRAY);
                            monthText.setEnabled(false);
                            monthText.setBackground(Color.GRAY);
                            dayText.setEnabled(false);
                            dayText.setBackground(Color.GRAY);
                            break;

                        case "onetime":
                            yearText.setEnabled(true);
                            yearText.setBackground(Color.WHITE);
                            monthText.setEnabled(true);
                            monthText.setBackground(Color.WHITE);
                            dayText.setEnabled(true);
                            dayText.setBackground(Color.WHITE);
                            break;
                    }
                }
            };

            //listener for deleting events
            case Consts.DELETE_LISTENER : return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                	//makes sure there's an event to delete
                    if (eventToModify != null) {

                    	  //confirm deletion
                        int selectedOption = JOptionPane.showConfirmDialog(null,
                                "Doriti sa stergeti acest eveniment?", "Confirm:", JOptionPane.YES_NO_OPTION);
                        if (selectedOption == JOptionPane.YES_OPTION) {

                        	//remove the event
                            outref.data.remove(eventToModify);
                            outref.calendarDisplay.deleteAffectedPanels(eventToModify.getYear(),
                                    eventToModify.getMonth(), eventToModify.getDay(), eventToModify.getType());

                          //fix up panels and data as a result of deletion
                            Collections.sort(outref.data);
                            outref.calendarDisplay.updateDisplay();
   
                         
                        }

                        //go back to previous panel
                        outref.currentPanel = outref.backToCalendar
                                ? outref.getCalendarCard() : outref.getTodoCard();
                        ((CardLayout) outref.getLayout()).show(outref, outref.backToCalendar
                                ? outref.getCalendarCard() : outref.getTodoCard());
                    }
                }
            };

            default: System.err.print("Error in CalendarDisplay.getActionListener()"); System.exit(1);
        }

        return null; 
    }


    private boolean validDate(StringBuilder errors) {

        int day, month, year;

        //check year if its a yearly event
        if (!yearlyRadioButton.isSelected()&&!monthlyRadioButton.isSelected()&&!daylyRadioButton.isSelected()) {
            try {
                year = Integer.parseInt(yearText.getText());
                if (year < 1)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                yearText.setBackground(Consts.error);
                year = 0;
            }
        }

        //if a yearly, make sure feb29 is okay
        else year = currentYear - (currentYear % 4);

        //check month
        if (!monthlyRadioButton.isSelected()&&!daylyRadioButton.isSelected()) try {
            month = Integer.parseInt(monthText.getText());
            if (month > 12 || month < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            monthText.setBackground(Consts.error);
            month = 0;
        }
        else month = currentMonth;

        
        //check day
        if (!daylyRadioButton.isSelected())try {
            day = Integer.parseInt(dayText.getText());
            if ((month != 0 && day > Consts.DAYS_IN_MONTH[month - 1] + (year % 4 == 0 && month == 2 ? 1 : 0)) || day < 1)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            dayText.setBackground(Consts.error);
            day = 0;
        }
        else day = currentDay;

        //final check
        if (!(day > 0 && month > 0 && year > 0)) {
            errors.append("Data incorecta\n"+ day +"."+ month+"." + year);
            return false;
        }
        return true;
    }

    private boolean validTitle(StringBuilder errors) {

    	//double check for title validity
        boolean valid = true;
        String text = title.getText();

      //no @ to ruin storage
        if (text.contains("@")) {
            title.setBackground(Consts.error);
            errors.append("Nu se accepta '@' in titlu\n");
            valid = false;
            
        }

        if (text.length() > TITLE_MAX) {
            title.setBackground(Consts.error);
            errors.append("\nTitlul nu poate depasi " + TITLE_MAX + " caractere\n");
            valid = false;
        }

        else if (text.length() == 0) {
            title.setBackground(Consts.error);
            errors.append("\nTitlul nu poate fi gol\n");
            valid = false;
        }

        return valid;
    }
      
  
   
    private boolean validDescription(StringBuilder errors) {

    	//double check for description validity
        boolean valid = true;
        String text = description.getText();

    
        if (text.contains("/")) {
            description.setBackground(Consts.error);
            errors.append("Nu se accepta @ in descriere\n");
            valid = false;
        }

        //max length cannot be exceeded
        if (text.length() > DESCRIPTION_MAX) {
            description.setBackground(Consts.error);
            errors.append("Descrierea nu poate depasi " + DESCRIPTION_MAX + " caractere\n");
            valid = false;
        }

        return valid;
    }

    private void parseDate(String date) {

    	//load date into panel
        StringTokenizer st = new StringTokenizer(date, "/");
        currentDay = Integer.parseInt(st.nextToken());
        currentMonth = Integer.parseInt(st.nextToken());
        st.nextToken();
        currentYear = Integer.parseInt(st.nextToken());
    }

    public void setFields(int[] spec) {

    	 //spec[] is a int array of size 4, of format { year, month, int, type }
        //set up texts to match the event
        yearText.setText(spec[0] > 0 ? Integer.toString(spec[0]) : "");
        monthText.setText(spec[1] > 0 ? Integer.toString(spec[1]) : "");
        dayText.setText(spec[2] > 0 ? Integer.toString(spec[2]) : "");

        //set up radio buttons to represent the event
        if (spec[3] != Consts.YEARLESS) {

            switch (spec[3]) {

                case Consts.YEARLY_DATE:
                    onetimeRadioButton.setEnabled(true);
                    yearlyRadioButton.setEnabled(true);
                    monthlyRadioButton.setEnabled(true);
                    daylyRadioButton.setEnabled(true);

                    onetimeRadioButton.setSelected(false);
                    yearlyRadioButton.setSelected(true);
                    monthlyRadioButton.setSelected(false);
                    daylyRadioButton.setSelected(false);
                    yearText.setBackground(Color.GRAY);
                    yearText.setEnabled(false);
                    break;
                    
                case Consts.MONTHLY_DATE:
                    onetimeRadioButton.setEnabled(true);
                    yearlyRadioButton.setEnabled(true);
                    monthlyRadioButton.setEnabled(true);
                    daylyRadioButton.setEnabled(true);

                    onetimeRadioButton.setSelected(false);
                    yearlyRadioButton.setSelected(false);
                    monthlyRadioButton.setSelected(true);
                    daylyRadioButton.setSelected(false);
                    yearText.setBackground(Color.GRAY);
                    yearText.setEnabled(false);
                    monthText.setBackground(Color.GRAY);
                    monthText.setEnabled(false);
                    break;
                    
                case Consts.DAYLY_DATE:
                    onetimeRadioButton.setEnabled(true);
                    yearlyRadioButton.setEnabled(true);
                    monthlyRadioButton.setEnabled(true);
                    daylyRadioButton.setEnabled(true);

                    onetimeRadioButton.setSelected(false);
                    yearlyRadioButton.setSelected(false);
                    monthlyRadioButton.setSelected(false);
                    daylyRadioButton.setSelected(true);
                    yearText.setBackground(Color.GRAY);
                    yearText.setEnabled(false);
                    monthText.setBackground(Color.GRAY);
                    monthText.setEnabled(false);
                    dayText.setBackground(Color.GRAY);
                    dayText.setEnabled(false);
                    break;

                case Consts.ONETIME_DATE:
                    onetimeRadioButton.setEnabled(true);
                    yearlyRadioButton.setEnabled(true);
                    monthlyRadioButton.setEnabled(true);
                    daylyRadioButton.setEnabled(true);

                    break;

            }

            deleteButton.setEnabled(true);
            this.setBackground(Consts.modeditbg);
            radioPanel.setBackground(Consts.modeditrb);
            topLabel.setText(topLabelModify);
        }
    }

    public void setEventToModify(CalendarEvent toMod) {
        eventToModify = toMod;
    }

    public void setTitleDesc(String ti, String desc) {
        title.setText(ti);
        description.setText(desc);
        titleLimit.setText("<html><font size='4'> Caractere: " + ti.length() + '/' + TITLE_MAX + "</font></html>");
        descLimit.setText("<html><font size='4'> Caractere: " + desc.length() + '/' + DESCRIPTION_MAX + "</font></html>");
    }

    public void clear() {

    	 //clear everything to be default
        onetimeRadioButton.setSelected(true);
        yearlyRadioButton.setSelected(false);
        monthlyRadioButton.setSelected(false);
        daylyRadioButton.setSelected(false);

        onetimeRadioButton.setEnabled(true);
        yearlyRadioButton.setEnabled(true);
        monthlyRadioButton.setEnabled(true);
        daylyRadioButton.setEnabled(true);

        dayText.setText("");
        monthText.setText("");
        yearText.setText("");
        description.setText("");
        title.setText("");

        dayText.setBackground(Color.WHITE);
        monthText.setBackground(Color.WHITE);
        yearText.setBackground(Color.WHITE);
        yearText.setEnabled(true);
        monthText.setEnabled(true);
        dayText.setEnabled(true);

        description.setBackground(Color.WHITE);
        title.setBackground(Color.WHITE);

        topLabel.setText(topLabelDefault);
        deleteButton.setEnabled(false);
        eventToModify = null;

        this.setBackground(Consts.modbg);
        radioPanel.setBackground(Consts.modrb);
    }

    public void showPanel() {
        outref.currentPanel = outref.getModificationCard();
        ((CardLayout)outref.getLayout()).show(outref, outref.getModificationCard());
    }
}