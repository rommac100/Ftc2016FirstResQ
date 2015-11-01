package com.qualcomm.ftcrobotcontroller.opmodes;

import hallib.FtcDcMotor;
import hallib.FtcTouch;
import hallib.HalSpeedController;
import trclib.TrcEvent;
import trclib.TrcMotorPosition;
import trclib.TrcPidController;
import trclib.TrcPidMotor;

public class Elevator implements TrcMotorPosition, TrcPidController.PidInput
{
    //
    // This component consists of an elevator motor, a lower
    // limit switch, an upper limit switch and an encoder to
    // keep track of the position of the elevator.
    //
    private FtcDcMotor elevatorMotor;
    private TrcPidController pidController;
    private TrcPidMotor pidMotor;
    private FtcTouch lowerLimitSwitch;
    private FtcTouch upperLimitSwitch;
    private boolean elevatorOverride = false;

    public Elevator()
    {
        elevatorMotor = new FtcDcMotor("elevator");
        pidController = new TrcPidController(
                "elevator",
                RobotInfo.ELEVATOR_KP,
                RobotInfo.ELEVATOR_KI,
                RobotInfo.ELEVATOR_KD,
                RobotInfo.ELEVATOR_KF,
                RobotInfo.ELEVATOR_TOLERANCE,
                RobotInfo.ELEVATOR_SETTLING,
                this,
                TrcPidController.PIDCTRLO_ABS_SETPT);
        pidMotor = new TrcPidMotor("elevator", elevatorMotor, pidController, this);
        pidMotor.setTargetScale(RobotInfo.ELEVATOR_INCHES_PER_CLICK);
        lowerLimitSwitch = new FtcTouch("lowerLimitSwitch");
        upperLimitSwitch = new FtcTouch("upperLimitSwitch");
    }

    public void zeroCalibrate(double calPower)
    {
//        pidMotor.zeroCalibrate(calPower);
    }

    public void setElevatorOverride(boolean enabled)
    {
        elevatorOverride = enabled;
    }

    public void setPower(double power)
    {
        if (elevatorOverride)
        {
            if (power > 0.0 && !upperLimitSwitch.isPressed() ||
                power < 0.0 && !lowerLimitSwitch.isPressed())
            {
                pidMotor.setPower(power);
            }
            else
            {
                pidMotor.setPower(0.0);
            }
        }
        else
        {
            pidMotor.setPidPower(power,
                                 RobotInfo.ELEVATOR_MIN_HEIGHT,
                                 RobotInfo.ELEVATOR_MAX_HEIGHT,
                                 true);
        }
    }

    public void setHeight(double height)
    {
        pidMotor.setTarget(height, true);
    }

    public void setHeight(double height, TrcEvent event, double timeout)
    {
        pidMotor.setTarget(height, event, timeout);
    }

    public double getHeight()
    {
        return getMotorPosition(elevatorMotor)*RobotInfo.ELEVATOR_INCHES_PER_CLICK;
    }

    public boolean isLowerLimitSwitchPressed()
    {
        return lowerLimitSwitch.isPressed();
    }

    public boolean isUpperLimitSwitchPressed()
    {
        return upperLimitSwitch.isPressed();
    }

    public void displayDebugInfo(int lineNum)
    {
        pidController.displayPidInfo(lineNum);
    }

    //
    // Implements TrcDriveBase.MotorPosition.
    //
    public double getMotorPosition(HalSpeedController speedController)
    {
        return -1.0*speedController.getCurrentPosition();
    }   //getMotorPosition

    public double getMotorSpeed(HalSpeedController speedController)
    {
        return 0.0;
    }   //getMotorSpeed

    public void resetMotorPosition(HalSpeedController speedController)
    {
        speedController.resetCurrentPosition();
    }   //resetMotorPosition

    public void reversePositionSensor(HalSpeedController speedController, boolean flip)
    {
        return;
    }   //reversePositionSensor

    public boolean isForwardLimitSwitchActive(HalSpeedController speedController)
    {
        return upperLimitSwitch.isPressed();
    }   //isForwardLimitSwitchActive

    public boolean isReverseLimitSwitchActive(HalSpeedController speedController)
    {
        return lowerLimitSwitch.isPressed();
    }   //isReverseLimitSwitchActive

    //
    // Implements TrcPidController.PidInput.
    //
    public double getInput(TrcPidController pidCtrl)
    {
        double value = 0.0;

        if (pidCtrl == pidController)
        {
            value = getHeight();
        }

        return value;
    }   //getInput

}   //class Elevator
