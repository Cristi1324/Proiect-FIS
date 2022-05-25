
package a;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;

public abstract class CalendarEvent implements MouseListener, Comparable<CalendarEvent> {

    protected int year, month, day;
    protected String title, description, time;
    protected ModificationPanel modref;
    private int mouseClicks;

    public CalendarEvent(ModificationPanel inref, StringTokenizer st) {
        modref = inref;
        year = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        day = Integer.parseInt(st.nextToken());
        title = st.nextToken();
        time=st.nextToken();
        description = st.hasMoreTokens() ? st.nextToken() : "";
    }

    public CalendarEvent(ModificationPanel inref, int year, int month, int day, String title, String description) {
        modref = inref;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
   

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseClicks = 0;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseClicks = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    	//simulate a double click
        mouseClicks++;
        if (mouseClicks == 2) {
            mouseClicks = 0;
            prepareModPanel();
            modref.showPanel();
        }
    }

    @Override
    public int compareTo(CalendarEvent o) {

    	  //comparing to sort events
        if (year != o.getYear())
            return year - o.getYear();

        else if (month != o.getMonth())
            return month - o.getMonth();

        else if (day != o.getDay())
            return day - o.getDay();

        else return o.getType() - this.getType();
    }
   


    public abstract String getCalendarText();
    public abstract int createCalendarPanel(JPanel contentPane);
    public abstract void prepareModPanel();
    public abstract int getType();

}