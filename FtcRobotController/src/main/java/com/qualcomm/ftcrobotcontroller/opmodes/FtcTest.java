package com.qualcomm.ftcrobotcontroller.opmodes;

import hallib.FtcMenu;
import hallib.FtcRobot;
import hallib.HalDashboard;

public class FtcTest extends FtcRobot implements FtcMenu.MenuButtons
{
    private static final int TEST_DRIVE_5SEC    = 0;
    private static final int TEST_DRIVE_8FT     = 1;
    private static final int TEST_TURN_360      = 2;
    private FtcMenu testMenu;

    @Override
    public void robotInit()
    {
        testMenu = new FtcMenu("Tests:", this);
        testMenu.addChoice("Drive 5 sec", TEST_DRIVE_5SEC);
        testMenu.addChoice("Drive forward 8 ft", TEST_DRIVE_8FT);
        testMenu.addChoice("Turn right 360 deg", TEST_TURN_360);
        int choice = testMenu.getChoice();
        HalDashboard.getInstance().displayPrintf(15, "Selected = %d", choice);
    }   //robotInit

    @Override
    public void startMode()
    {

    }   //startMode

    @Override
    public void stopMode()
    {

    }   //stopMode

    @Override
    public void runPeriodic()
    {

    }   //runPeriodic

    @Override
    public void runContinuous()
    {

    }   //runContinuous

    //
    // Implements MenuButtons
    //
    public boolean isMenuUp()
    {
        return gamepad1.dpad_up;
    }   //isMenuUp

    public boolean isMenuDown()
    {
        return gamepad1.dpad_down;
    }   //isMenuDown

    public boolean isMenuOk()
    {
        return gamepad1.a;
    }   //isMenuEnter

    public boolean isMenuCancel()
    {
        return gamepad1.b;
    }   //isMenuCancel

}   //class FtcTest
