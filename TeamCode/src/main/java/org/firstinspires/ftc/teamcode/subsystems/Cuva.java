package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Cuva {

    // Constants
    public static double CUVA_INTAKE = 0.96;
    public static double CUVA_MID = 0.47;
    public static double CUVA_OUT = 0.4;
    public static double IMPINS_IN = 0.65;
    public static double IMPINS_BLOCK = 0.5;
    public static double IMPINS_OUT = 0.3;

    // Hardware
    Servo impins;
    Servo vLeft;
    Servo vRight;

    public enum CuvaState {
        INTAKE, MID, OUT
    }
    public CuvaState cuvaState = CuvaState.INTAKE;

    public enum ImpinsState {
        IN, BLOCK, OUT
    }
    public ImpinsState impinsState = ImpinsState.IN;

    public Cuva(HardwareMap hw) {
        impins = hw.get(Servo.class, "claw");
        vLeft = hw.get(Servo.class, "virtual_stanga");
        vRight = hw.get(Servo.class, "virtual_dreapta");

        vLeft.setDirection(Servo.Direction.REVERSE);
    }

    public void setCuvaIntake() {
        vLeft.setPosition(CUVA_INTAKE);
        vRight.setPosition(CUVA_INTAKE);

        cuvaState = CuvaState.INTAKE;
    }
    public void setCuvaMid() {
        vLeft.setPosition(CUVA_MID);
        vRight.setPosition(CUVA_MID);

        cuvaState = CuvaState.MID;
    }
    public void setCuvaOut() {
        vLeft.setPosition(CUVA_OUT);
        vRight.setPosition(CUVA_OUT);

        cuvaState = CuvaState.OUT;
    }
    public void setImpinsIn() {
        impins.setPosition(IMPINS_IN);

        impinsState = ImpinsState.IN;
    }
    public void setImpinsBlock() {
        impins.setPosition(IMPINS_BLOCK);

        impinsState = ImpinsState.BLOCK;
    }
    public void setImpinsOut() {
        impins.setPosition(IMPINS_OUT);

        impinsState = ImpinsState.OUT;
    }
}
