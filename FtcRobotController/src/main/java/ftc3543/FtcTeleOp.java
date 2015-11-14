package ftc3543;

import ftclib.FtcGamepad;
import ftclib.FtcOpMode;
import hallib.HalDashboard;
import trclib.TrcBooleanState;
import trclib.TrcRobot;

public class FtcTeleOp extends FtcOpMode implements FtcGamepad.ButtonHandler
{
    private HalDashboard dashboard;
    private FtcRobot robot;
    private FtcGamepad driverGamepad;
    private FtcGamepad operatorGamepad;
    private TrcBooleanState climbMode;
    private TrcBooleanState hookDeployed;
    private TrcBooleanState cattleGuardDeployed;

    //
    // Implements FtcOpMode abstract methods.
    //

    @Override
    public void robotInit()
    {
        //
        // Initializing global objects.
        //
        dashboard = HalDashboard.getInstance();
        robot = new FtcRobot(TrcRobot.RunMode.TELEOP_MODE);
        //
        // Initializing Gamepads.
        //
        driverGamepad = new FtcGamepad("DriverGamepad", gamepad1, this);
        operatorGamepad = new FtcGamepad("OperatorGamepad", gamepad2, this);
        driverGamepad.setYInverted(true);
        operatorGamepad.setYInverted(true);
        //
        // TreadDrive subsystem.
        //
        climbMode = new TrcBooleanState("climbMode", false);
        //
        // HangingHook subsystem.
        //
        hookDeployed = new TrcBooleanState("hangingHook", false);
        //
        // CattleGuard subsystem.
        //
        cattleGuardDeployed = new TrcBooleanState("cattleGuardDeployed", false);

    }   //robotInit

    @Override
    public void startMode()
    {
        //
        // There is an issue with the gamepad objects that may not be valid
        // before waitForStart() is called. So we call the setGamepad method
        // here to update their references in case they have changed.
        //
        driverGamepad.setGamepad(gamepad1);
        operatorGamepad.setGamepad(gamepad2);
    }   //startMode

    @Override
    public void stopMode()
    {
    }   //stopMode

    @Override
    public void runPeriodic()
    {
        //
        // DriveBase subsystem.
        //
        double leftPower  = driverGamepad.getLeftStickY(true);
        double rightPower = driverGamepad.getRightStickY(true);
        robot.driveBase.tankDrive(leftPower, rightPower);
        dashboard.displayPrintf(1, "leftPower = %.3f", leftPower);
        dashboard.displayPrintf(2, "rightPower = %.3f", rightPower);
        //
        // Elevator subsystem.
        //
        double elevatorPower = operatorGamepad.getRightStickY(true);
        robot.elevator.setPower(elevatorPower);
        dashboard.displayPrintf(3, "elevatorPower = %.3f", elevatorPower);
        dashboard.displayPrintf(4, "lowerLimit = %s, upperLimit = %s",
                                robot.elevator.isLowerLimitSwitchPressed()? "pressed": "released",
                                robot.elevator.isUpperLimitSwitchPressed()? "pressed": "released");
        robot.elevator.displayDebugInfo(5);
        //
        // TreadDrive subsystem.
        //
        double treadPower = 0.0;
        if (climbMode.getState())
        {
            treadPower = (leftPower + rightPower)/2.0;
        }
        robot.treadDrive.setPower(treadPower);
        dashboard.displayPrintf(7, "treadPower = %.3f", treadPower);
    }   //runPeriodic

    @Override
    public void runContinuous()
    {
    }   //runContinuous

    //
    // Implements FtcGamepad.ButtonHandler interface.
    //

    @Override
    public void gamepadButtonEvent(FtcGamepad gamepad, final int btnMask, final boolean pressed)
    {
        dashboard.displayPrintf(8, "%s: %04x->%s",
                gamepad.toString(), btnMask, pressed? "Pressed": "Released");
        if (gamepad == driverGamepad)
        {
            switch (btnMask)
            {
                case FtcGamepad.GAMEPAD_A:
                    break;

                case FtcGamepad.GAMEPAD_B:
                    break;

                case FtcGamepad.GAMEPAD_X:
                    if (pressed)
                    {
                        climbMode.toggleState();
                    }
                    break;

                case FtcGamepad.GAMEPAD_Y:
                    break;

                case FtcGamepad.GAMEPAD_LBUMPER:
                    if (pressed)
                    {
                        robot.leftWing.extend();
                    }
                    else
                    {
                        robot.leftWing.retract();
                    }
                    break;

                case FtcGamepad.GAMEPAD_RBUMPER:
                    if (pressed)
                    {
                        robot.rightWing.extend();
                    }
                    else
                    {
                        robot.rightWing.retract();
                    }
                    break;
            }
        }
        else if (gamepad == operatorGamepad)
        {
            switch (btnMask)
            {
                case FtcGamepad.GAMEPAD_A:
                    if (pressed)
                    {
                        cattleGuardDeployed.toggleState();
                        if (cattleGuardDeployed.getState())
                        {
                            robot.cattleGuard.extend();
                        }
                        else
                        {
                            robot.cattleGuard.retract();
                        }
                    }
                    break;

                case FtcGamepad.GAMEPAD_B:
                    if (pressed)
                    {
                        hookDeployed.toggleState();
                        if (hookDeployed.getState())
                        {
                            robot.hangingHook.extend();
                        }
                        else
                        {
                            robot.hangingHook.retract();
                        }
                    }
                    break;

                case FtcGamepad.GAMEPAD_X:
                    break;

                case FtcGamepad.GAMEPAD_Y:
                    break;

                case FtcGamepad.GAMEPAD_RBUMPER:
                    robot.elevator.setElevatorOverride(pressed);
                    break;

                case FtcGamepad.GAMEPAD_BACK:
                    if (pressed)
                    {
                        robot.elevator.zeroCalibrate(RobotInfo.ELEVATOR_CAL_POWER);
                    }
                    break;
            }
        }
    }   //gamepadButtonEvent

}   //class FtcTeleOp