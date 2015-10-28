package com.qualcomm.ftcrobotcontroller.opmodes;

import hallib.HalServo;

public class ButtonPusher
{
    //
    // This component consists of a servo controlled swing arm.
    // It provides methods to set the swing arm position to either
    // neutral, left or right.
    //
    private static final double PUSHER_LEFT_POSITION = 0.0;
    private static final double PUSHER_NEUTRAL_POSITION = 90.0;
    private static final double PUSHER_RIGHT_POSITION = 180.0;

    private HalServo pusherServo;

    public ButtonPusher()
    {
        pusherServo = new HalServo("buttonPusher");
    }   //ButtonPusher

    public void pushLeftButton()
    {
        pusherServo.setPosition(PUSHER_LEFT_POSITION);
    }   //pushLeftButton

    public void pushRightButton()
    {
        pusherServo.setPosition(PUSHER_RIGHT_POSITION);
    }   //pushRightButton

    public void pushNoButton()
    {
        pusherServo.setPosition(PUSHER_NEUTRAL_POSITION);
    }   //pushNoButton

}   //class ButtonPusher