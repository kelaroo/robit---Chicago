package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Cuva {

    // Constants
    public static double CUVA_INTAKE = 0.868;
    public static double CUVA_MID = 0.47;
    public static double CUVA_SHIPPING = 0.28;
    public static double CUVA_SFERA = 0.35;
    public static double CUVA_OUT = 0.2 ;

    public static double IMPINS_IN = 0.45;
    public static double IMPINS_BLOCK = 0.375;
    public static double IMPINS_OUT = 0.17;

    public static int RAW_THRESH = 750;

    // Hardware
    Servo impins;
    Servo vLeft;
    Servo vRight;

    public ColorRangeSensor sensorCuva;

    public enum CuvaState {
        INTAKE, SHIPPING, SFERA, MID, OUT
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
        sensorCuva = hw.get(ColorRangeSensor.class, "sensorCuva");

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
    public void setCuvaShipping() {
        vLeft.setPosition(CUVA_SHIPPING);
        vRight.setPosition(CUVA_SHIPPING);

        cuvaState = CuvaState.SHIPPING;
    }
    public void setCuvaSfera() {
        vLeft.setPosition(CUVA_SFERA);
        vRight.setPosition(CUVA_SFERA);

        cuvaState = CuvaState.SFERA;
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

    public boolean isElementIn() {
        return sensorCuva.getRawLightDetected() >= RAW_THRESH;
    }

    public String isSphereOrCube() {
        if(sensorCuva.red() > sensorCuva.blue())
            return "cube";
        else return "sphere";
    }

    public void update() {
        switch (cuvaState) {
            case INTAKE: setCuvaIntake(); break;
            case MID: setCuvaMid(); break;
            case SHIPPING: setCuvaShipping(); break;
            case SFERA: setCuvaSfera(); break;
            case OUT: setCuvaOut(); break;
        }

        switch (impinsState) {
            case IN: setImpinsIn(); break;
            case OUT: setImpinsOut(); break;
            case BLOCK: setImpinsBlock(); break;
        }
    }
}
