
package a;
import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;

public class CalendarDate extends CalendarEvent {

    private boolean yearly, dayly, monthly;

    public CalendarDate(ModificationPanel inref, StringTokenizer st, int type) {
        super(inref, st);
        yearly = type == Consts.YEARLY_DATE;
        monthly = type == Consts.MONTHLY_DATE;
        dayly = type == Consts.DAYLY_DATE;
    }

    public CalendarDate(ModificationPanel inref, int year, int month, int day, String title, String description, boolean yearly, boolean monthly, boolean dayly)
 {
        super(inref, year, month, day, title, description);
        this.yearly = yearly;
        this.monthly = monthly;
        this.dayly = dayly;
    }

    public String getCalendarText() {
        if (yearly)
            return "<font color='#008000'><i>" + title + "</i></font>";
        else if (monthly)
            return "<font color='#000080'>" + title + "</font>";
        else if (dayly)
            return "<font color='#800000'><i>" + title + "</i></font>";
        else
            return "<font color='#505050'>" + title + "</font>";
    }

    public int createCalendarPanel(JPanel contentPane) {

    	 //prepare a jpanel for the weekly calendar display
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.Y_AXIS));
        output.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        output.setOpaque(false);

        JLabel label;
        if (yearly|monthly|dayly)
            label = new JLabel("<html><p><i><u>" + title + "</u>" + (description.length() == 0 ? "" : " - " + description) + "</i></p></html>");
        else
            label = new JLabel("<html><p><u>" + title + "</u>" + (description.length() == 0 ? "" : " - " + description) + "</p></html>");
        label.setForeground(new Color(0x008000));

      //calculate the necessary size dimension of the label
        Dimension d = label.getPreferredSize();
        int paneWidth = contentPane.getPreferredSize().width;
        int labelWidth = d.width;
        int labelHeight = (d.height + 3) * ((labelWidth / paneWidth) + 3);
        label.setPreferredSize(new Dimension(paneWidth, labelHeight));
        output.add(label);

        output.add(Box.createRigidArea(new Dimension(0,10)));
        output.addMouseListener(this);
        contentPane.add(output);

        //output height
        return labelHeight + 15;
    }

   

    public void prepareModPanel() {
    	 //prepares modification panel for a modification
    	int type = (yearly ? Consts.YEARLY_DATE : 0) + (monthly ? Consts.MONTHLY_DATE : 0)+ (dayly ? Consts.DAYLY_DATE : 0);
    	if(type == 0) type = Consts.ONETIME_DATE;
        int[] spec = new int[] { year, month, day, type};
        modref.clear();
        modref.setFields(spec);
        modref.setEventToModify(this);
        modref.setTitleDesc(title, description);
        modref.setBackground(Consts.modbg);
    }

    public int getType() {
        if(yearly) return Consts.YEARLY_DATE;
        else if(monthly) return Consts.MONTHLY_DATE;
        else if(dayly) return Consts.DAYLY_DATE;
        else return Consts.ONETIME_DATE;
    }
}
