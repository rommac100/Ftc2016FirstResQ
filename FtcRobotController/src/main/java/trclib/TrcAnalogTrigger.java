package trclib;

public class TrcAnalogTrigger implements TrcTaskMgr.Task
{
    private static final String moduleName = "TrcAnalogTrigger";
    private static final boolean debugEnabled = false;
    private TrcDbgTrace dbgTrace = null;

    public enum Zone
    {
        UNKNOWN_ZONE,
        LOW_ZONE,
        MID_ZONE,
        HIGH_ZONE
    }   //enum Zone

    public interface TriggerHandler
    {
        public void AnalogTriggerEvent(
                TrcAnalogTrigger analogTrigger,
                Zone zone,
                double value);
    }   //interface TriggerHandler

    private String instanceName;
    private TrcAnalogInput analogInput;
    private double lowThreshold;
    private double highThreshold;
    private TriggerHandler eventHandler;
    private Zone prevZone;

    public TrcAnalogTrigger(
            final String instanceName,
            TrcAnalogInput analogInput,
            double lowThreshold,
            double highThreshold,
            TriggerHandler eventHandler)
    {
        if (debugEnabled)
        {
            dbgTrace = new TrcDbgTrace(
                    moduleName + "." + instanceName,
                    false,
                    TrcDbgTrace.TraceLevel.API,
                    TrcDbgTrace.MsgLevel.INFO);
        }

        if (analogInput == null || eventHandler == null)
        {
            throw new NullPointerException("AnalogInput/EventHandler must be provided");
        }

        this.instanceName = instanceName;
        this.analogInput = analogInput;
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        this.eventHandler = eventHandler;
        prevZone = Zone.UNKNOWN_ZONE;
    }   //TrcAnalogTrigger

    public TrcAnalogTrigger(
            final String instanceName,
            TrcAnalogInput analogInput,
            double threshold,
            TriggerHandler eventHandler)
    {
        this(instanceName, analogInput, threshold, threshold, eventHandler);
    }   //TrcAnalogTrigger

    public void setThresholds(double lowThreshold, double highThreshold)
    {
        final String funcName = "setThreshold";

        if (debugEnabled)
        {
            dbgTrace.traceEnter(funcName, TrcDbgTrace.TraceLevel.API,
                    "lowThreshold=%f,highThreshold=%f",
                    lowThreshold, highThreshold);
            dbgTrace.traceExit(funcName, TrcDbgTrace.TraceLevel.API);
        }

        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
    }   //setThresholds

    public void setEnabled(boolean enabled)
    {
        final String funcName = "setEnabled";

        if (debugEnabled)
        {
            dbgTrace.traceEnter(
                    funcName, TrcDbgTrace.TraceLevel.FUNC,
                    "enabled=%s", Boolean.toString(enabled));
            dbgTrace.traceExit(funcName, TrcDbgTrace.TraceLevel.FUNC);
        }

        if (enabled)
        {
            TrcTaskMgr.getInstance().registerTask(
                    instanceName, this, TrcTaskMgr.TaskType.PRECONTINUOUS_TASK);
        }
        else
        {
            TrcTaskMgr.getInstance().unregisterTask(this, TrcTaskMgr.TaskType.PRECONTINUOUS_TASK);
        }
    }   //setEnabled

    //
    // Implements TrcTaskMgr.Task
    //

    @Override
    public void startTask(TrcRobot.RunMode runMode)
    {
    }   //startTask

    @Override
    public void stopTask(TrcRobot.RunMode runMode)
    {
    }   //stopTask

    @Override
    public void prePeriodicTask(TrcRobot.RunMode runMode)
    {
    }   //prePeriodicTask

    @Override
    public void postPeriodicTask(TrcRobot.RunMode runMode)
    {
    }   //postPeriodicTask

    @Override
    public void preContinuousTask(TrcRobot.RunMode runMode)
    {
        final String funcName = "preContinuousTask";
        double value = analogInput.getData().value;

        Zone zone;
        if (value <= lowThreshold)
        {
            zone = Zone.LOW_ZONE;
        }
        else if (value <= highThreshold)
        {
            zone = Zone.MID_ZONE;
        }
        else
        {
            zone = Zone.HIGH_ZONE;
        }

        if (zone != prevZone)
        {
            //
            // We have crossed to another zone, let's notify somebody.
            //
            prevZone = zone;
            if (eventHandler != null)
            {
                eventHandler.AnalogTriggerEvent(this, zone, value);
            }

            if (debugEnabled)
            {
                dbgTrace.traceInfo(
                        funcName, "%s entering %s (data=%f)",
                        instanceName, zone.toString(), value);
            }
        }
    }   //preContinuousTask

    @Override
    public void postContinuousTask(TrcRobot.RunMode runMode)
    {
    }   //postContinuousTask

}   //class TrcAnalogTrigger
