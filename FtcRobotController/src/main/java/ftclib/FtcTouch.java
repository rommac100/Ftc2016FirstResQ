package ftclib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import trclib.TrcDigitalInput;
import trclib.TrcDbgTrace;

public class FtcTouch extends TrcDigitalInput
{
    private static final String moduleName = "FtcTouch";
    private static final boolean debugEnabled = false;
    private TrcDbgTrace dbgTrace = null;

    private TouchSensor touchSensor;

    public FtcTouch(HardwareMap hardwareMap, String instanceName)
    {
        super(instanceName);

        if (debugEnabled)
        {
            dbgTrace = new TrcDbgTrace(
                    moduleName + "." + instanceName,
                    false,
                    TrcDbgTrace.TraceLevel.API,
                    TrcDbgTrace.MsgLevel.INFO);
        }

        this.touchSensor = hardwareMap.touchSensor.get(instanceName);
    }   //FtcTouch

    public FtcTouch(String instanceName)
    {
        this(FtcOpMode.getInstance().hardwareMap, instanceName);
    }   //FtcTouch

    //
    // Implements TrcDigitalInput abstract methods.
    //

    @Override
    public boolean isActive()
    {
        final String funcName = "isActive";
        boolean active = touchSensor.isPressed();

        if (debugEnabled)
        {
            dbgTrace.traceEnter(funcName, TrcDbgTrace.TraceLevel.API);
            dbgTrace.traceExit(funcName, TrcDbgTrace.TraceLevel.API,
                               "=%s", Boolean.toString(active));
        }

        return active;
    }   //isActive

}   //class FtcTouch
