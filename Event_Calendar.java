
package a;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;

@SuppressWarnings("serial")
class PlannerDisplay extends JPanel {

    public CalendarDisplay calendarDisplay;
    public ModificationPanel modificationPanel;

    public ArrayList<CalendarEvent> data;
    public boolean backToCalendar = true;

    private final String calendarCard = "calcard";
    private final String modificationCard = "modcard";
    private final String todoCard = "todocard";
    public String currentPanel;

    public PlannerDisplay() {

    	//gets current date in day/weekday/month/year format
        String current_date = (new SimpleDateFormat("dd/uu/MM/yyyy")).format(new Date());
        this.setBackground(Consts.bg);

        //loadData() requires a valid reference to the modificationPanel, instantiating that must come first
        modificationPanel = new ModificationPanel(current_date, this);
        this.loadData();
        calendarDisplay = new CalendarDisplay(current_date, this);

        this.setLayout(new CardLayout());
        this.add(calendarDisplay, calendarCard);
        this.add(modificationPanel, modificationCard);
        currentPanel = calendarCard;
        ((CardLayout)this.getLayout()).show(this, calendarCard);
    }

    public void loadData() {

        data = new ArrayList<>();
        Scanner fileScan = null;

        //if there are any errors that come about loading the data, the file is incorrectly formatted
        try {

            fileScan = new Scanner(new File("data.cld"));
            StringTokenizer st;
            int type;

            //load file into program
            while (fileScan.hasNextLine()) {

                st = new StringTokenizer(fileScan.nextLine(),"/");

                if (st.countTokens() != 5 && st.countTokens() != 6)
                    throw new Exception();

                type = Integer.parseInt(st.nextToken());
                data.add(new CalendarDate(modificationPanel, st, type));
            }

            if (data.size() == 0)
                throw new Exception();

            fileScan.close();

        } catch (Exception e) {
        	 //if file doesn't exist or is malformatted, offer to create a new default file
            if (fileScan != null)
                fileScan.close();

            data.clear();
            int selectedOption = JOptionPane.showConfirmDialog(null,
                    "Generezi un nou fisier data.cld?",
                    "Eroare incarcare \'data.cld\'",
                    JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                this.generateDefaultFile();
            }
            else {
            	System.exit(1);
            }
        }
    }

    public JMenuBar getMenuBar() {

        JMenuBar menuBar;
        JMenu file;
        JMenuItem exit, nosave;

        exit = new JMenuItem("Inchide");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        nosave = new JMenuItem("Inchide fara salvare");
        nosave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event_Calendar.noSave();
                System.exit(0);
            }
        });

        file = new JMenu("Fisier");
        file.add(exit);
        file.add(nosave);

      
        menuBar = new JMenuBar();
        menuBar.add(file);
 

        return menuBar;
    }


    public void toModCard(int[] spec) {

    	//clears and switches to modification card
        modificationPanel.clear();
        modificationPanel.setFields(spec);
        ((CardLayout)this.getLayout()).show(this, modificationCard);
    }

    public String getCalendarCard() {
        return calendarCard;
    }

    public String getModificationCard() {
        return modificationCard;
    }

    public String getTodoCard() {
        return todoCard;
    }

    private void generateDefaultFile() {

        PrintWriter out = null; 

        try {
            out = new PrintWriter("data.cld");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Eroare generare fisier", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        out.close();
    }
}

public class Event_Calendar {

    private static ArrayList<CalendarEvent> data;
    public static boolean save = true;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
            	if (data != null && save)
                	saveData();
            }
        }));
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("Agenda electronica");
        frame.setSize(1100, 650);

        PlannerDisplay display = new PlannerDisplay();
        data = display.data;

        frame.setJMenuBar(display.getMenuBar());
        frame.add(display);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void saveData() {
        PrintWriter out = null; 

        try {
            out = new PrintWriter("data.cld");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Eroare salvare fisier", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        Iterator<CalendarEvent> itr = data.iterator();
        CalendarEvent event;
        StringBuilder outStr;

      //save data in the proper format:
        // / type / year / month / day /time/ description /
        while (itr.hasNext()) {

            event = itr.next();
            outStr = new StringBuilder();

            outStr.append('/');
            outStr.append(event.getType());
            outStr.append('/');
            outStr.append(event.getYear());
            outStr.append('/');
            outStr.append(event.getMonth());
            outStr.append('/');
            outStr.append(event.getDay());
            outStr.append('/');
            outStr.append(event.getTitle());
            outStr.append('/');
            if (event.getDescription().length() != 0) {
                outStr.append(event.getDescription());
                outStr.append('/');
            }

            out.println(outStr.toString());
        }

        out.close();
    }

    public static void noSave() {
        save = false;
    }
    
    
    
}