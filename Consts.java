//Brandon Mazur - CSCI230 Final Project

import java.awt.Color;

public class Consts {

    public final static int YEAR = 0;
    public final static int MONTH = 1;
    public final static int WEEK = 2;
    public final static int DAY = 3;
    public final static int NUM_MAIN_PANELS = 4;

    public final static int YEARLESS = -1;
    public final static int YEARLY_DATE = 2;
    public final static int ONETIME_DATE = 3;
    public final static int MONTHLY_DATE = 4;
    public final static int DAYLY_DATE = 5;

    public final static int NEXT_LISTENER = 0;
    public final static int PREV_LISTENER = 1;
    public final static int OUT_LISTENER = 2;
    public final static int TASK_LISTENER = 3;
    public final static int TODO_LISTENER = 4;
    public final static int MONTH_LISTENER = 11;
    public final static int YEAR_LISTENER = 12;
    public final static int CONFIRM_LISTENER = 20;
    public final static int RADIO_LISTENER = 21;
    public final static int DELETE_LISTENER = 22;

    public final static String[] cardNames = { "yearcard", "monthcard", "weekcard","daycards" };
    public final static String[] WEEK_ABBR = { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
    public final static int[] DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public final static int[] SUM_AT_MONTH = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
    public final static String[] MONTH_NAME = { "January", "February", "March", "April",
                                                 "May", "June", "July", "August",
                                                 "September", "October", "November", "December" };

    public final static Color bg = new Color(60,125,110);
    public final static Color modbg = new Color(80, 165, 180);
    public final static Color modrb = new Color(90, 180, 190);
    public final static Color modeditbg = new Color(115, 165, 85);
    public final static Color modeditrb = new Color(125, 180, 95);
    public final static Color todobg = new Color(230,200,160);
    public final static Color todoinbg = new Color(250, 230, 170);
    public final static Color manualbg = new Color(250, 235, 225);
    public final static Color buttonbg = new Color(235,150,150);
    public final static Color error = new Color(235,150,150);
}
