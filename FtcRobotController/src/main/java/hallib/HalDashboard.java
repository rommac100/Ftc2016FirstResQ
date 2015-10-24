package hallib;

import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.NoSuchElementException;

import trclib.TrcDbgTrace;

public class HalDashboard
{
    public static final int MAX_NUM_TEXTLINES = 16;

    private static final String moduleName = "HalDashboard";
    private static final boolean debugEnabled = false;
    private TrcDbgTrace dbgTrace = null;

    private static final String displayKeyFormat = "[%02d]";
    private static Telemetry telemetry = null;
    private static HalDashboard instance = null;
    private static String[] display = new String[MAX_NUM_TEXTLINES];

    public HalDashboard(Telemetry telemetry)
    {
        instance = this;
        this.telemetry = telemetry;
        telemetry.clearData();
        clearDisplay();
    }   //HalDashboard

    public static HalDashboard getInstance()
    {
        return instance;
    }   //getInstance

    public void displayPrintf(int lineNum, String format, Object... args)
    {
        if (lineNum >= 0 && lineNum < display.length)
        {
            display[lineNum] = String.format(format, args);
            telemetry.addData(String.format(displayKeyFormat, lineNum), display[lineNum]);
        }
    }   //displayPrintf

    public void clearDisplay()
    {
        for (int i = 0; i < display.length; i++)
        {
            display[i] = "";
        }
        refreshDisplay();
    }   //clearDisplay

    public void refreshDisplay()
    {
        for (int i = 0; i < display.length; i++)
        {
            telemetry.addData(String.format(displayKeyFormat, i), display[i]);
        }
    }   //refreshDisplay

    public boolean getBoolean(String key)
    {
        boolean value;
        String strValue = getValue(key);

        if (strValue.equals("true"))
        {
            value = true;
        }
        else if (strValue.equals("false"))
        {
            value = false;
        }
        else
        {
            throw new IllegalArgumentException("object is not boolean");
        }

        return value;
    }   //getBoolean

    public boolean getBoolean(String key, boolean defaultValue)
    {
        boolean value;

        try
        {
            value = getBoolean(key);
        }
        catch (NoSuchElementException e)
        {
            putBoolean(key, defaultValue);
            value = defaultValue;
        }

        return value;
    }   //getBoolean

    public void putBoolean(String key, boolean value)
    {
        telemetry.addData(key, Boolean.toString(value));
    }   //putBoolean

    public double getNumber(String key)
    {
        double value;

        try
        {
            value = Double.parseDouble(getValue(key));
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("object is not a number");
        }

        return value;
    }   //getNumber

    public double getNumber(String key, double defaultValue)
    {
        double value;

        try
        {
            value = getNumber(key);
        }
        catch (NoSuchElementException e)
        {
            putNumber(key, defaultValue);
            value = defaultValue;
        }

        return value;
    }   //getNumber

    public void putNumber(String key, double value)
    {
        telemetry.addData(key, Double.toString(value));
    }   //putNumber

    public String getString(String key)
    {
        return getValue(key);
    }   //getString

    public String getString(String key, String defaultValue)
    {
        String value;

        try
        {
            value = getString(key);
        }
        catch (NoSuchElementException e)
        {
            putString(key, defaultValue);
            value = defaultValue;
        }

        return value;
    }   //getString

    public void putString(String key, String value)
    {
        telemetry.addData(key, value);
    }   //putString

    private String getValue(String key)
    {
        String value = telemetry.getDataStrings().get(key);

        if (value == null)
        {
            throw new NoSuchElementException("No such key");
        }

        return value;
    }   //getValue

}   //class HalDashboard
