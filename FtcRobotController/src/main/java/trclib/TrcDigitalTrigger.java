package trclib;

public class TrcDigitalTrigger implements TrcTaskMgr.Task
{
    private static final String moduleName = "TrcDigitalTrigger";
    private static final boolean debugEnabled = false;
    private TrcDbgTrace dbgTrace = null;

    public interface DigitalTriggerHandler
    {
        public void DigitalTriggerEvent(
                TrcDigitalTrigger digitalTrigger,
                boolean active);
    }   //interface DigitalTriggerHandler

    private String instanceName;
    private TrcDigitalInput digitalInput;
    private DigitalTriggerHandler eventHandler;
    private boolean prevState = false;

    public TrcDigitalTrigger(
            final String instanceName,
            TrcDigitalInput digitalInput,
            DigitalTriggerHandler eventHandler)
    {
        if (debugEnabled)
        {
            dbgTrace = new TrcDbgTrace(
                    moduleName + "." + instanceName,
                    false,
                    TrcDbgTrace.TraceLevel.API,
                    TrcDbgTrace.MsgLevel.INFO);
        }

        if (digitalInput == null || eventHandler == null)
        {
            throw new NullPointerException("DigitalInput/EventHandler must be provided");
        }

        this.instanceName = instanceName;
        this.digitalInput = digitalInput;
        this.eventHandler = eventHandler;
    }   //TrcDigitalTrigger

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

        TrcTaskMgr taskMgr = TrcTaskMgr.getInstance();
        if (enabled)
        {
            taskMgr.registerTask(instanceName, this, TrcTaskMgr.TaskType.PREPERIODIC_TASK);
        }
        else
        {
            taskMgr.unregisterTask(this, TrcTaskMgr.TaskType.PREPERIODIC_TASK);
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
        final String funcName = "prePeriodic";
        boolean currState = digitalInput.isActive();

        if (currState != prevState)
        {
            if (eventHandler != null)
            {
                eventHandler.DigitalTriggerEvent(this, currState);
            }
            prevState = currState;

            if (debugEnabled)
            {
                dbgTrace.traceInfo(
                        funcName, "%s triggered (state=%s)",
                        instanceName, Boolean.toString(currState));
            }
        }
    }   //prePeriodicTask

    @Override
    public void postPeriodicTask(TrcRobot.RunMode runMode)
    {
    }   //postPeriodicTask

    @Override
    public void preContinuousTask(TrcRobot.RunMode runMode)
    {
    }   //preContinuousTask

    @Override
    public void postContinuousTask(TrcRobot.RunMode runMode)
    {
    }   //postContinuousTask

}   //class TrcDigitalTrigger
