
package a;
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
    public final static String[] WEEK_ABBR = { "Dum", "Lu", "Ma", "Mi", "Jo", "Vi", "Sam" };
    public final static int[] DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    public final static int[] SUM_AT_MONTH = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
    public final static String[] MONTH_NAME = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August",
            									"Septembrie", "Octombrie", "Noiembrie", "Decembrie" };

    public final static Color bg = new Color(255,182,193);
    
    public final static Color modbg = new Color(216,191,216);
    
    public final static Color modrb = new Color(221,160,221);

    public final static Color modeditbg = new Color(106,90,205);

    public final static Color modeditrb = new Color(106,90,205);


    public final static Color buttonbg = new Color(255,192,203);


    public final static Color error = new Color(235,150,150);
}