package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Capper {

    // Constants
    public static double BRAT_SPEED = 0.02;
    public static double BRAT_MIN = 0.1;
    public static double BRAT_MAX = 0.88;

    public static double CLAW_GRAB = 0.5;
    public static double CLAW_DROP = 0.85;

    // Hardware
    Servo capperBrat;
    Servo capperClaw;

    public Capper(HardwareMap hw) {
        capperBrat = hw.get(Servo.class,"capperBrat");
        capperClaw = hw.get(Servo.class, "capperClaw");
    }

    public void setBratPosition(double pos) {
        if(pos < BRAT_MIN) pos = BRAT_MIN;
        if(pos > BRAT_MAX) pos = BRAT_MAX;
        capperBrat.setPosition(pos);
    }
    public void bratUp() { setBratPosition(capperBrat.getPosition() - BRAT_SPEED); }
    public void bratDown() { setBratPosition(capperBrat.getPosition() + BRAT_SPEED); }

    public void clawGrab() { capperClaw.setPosition(CLAW_GRAB); }
    public void clawDrop() { capperClaw.setPosition(CLAW_DROP); }

}
