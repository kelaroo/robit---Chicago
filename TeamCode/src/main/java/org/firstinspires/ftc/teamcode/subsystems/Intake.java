package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Intake {

    // Constants
    public static double INTAKE_ON = 1.0;

    // Hardware
    DcMotorEx intake;

    public enum IntakeState {
        ON, OFF, REVERSE
    }
    public IntakeState intakeState = IntakeState.OFF;

    public Intake(HardwareMap hw) {
        intake = hw.get(DcMotorEx.class, "intake");
    }

    public void intakeOn() {
        intake.setPower(INTAKE_ON);

        intakeState = IntakeState.ON;
    }

    public void intakeOff() {
        intake.setPower(0.0);

        intakeState = IntakeState.OFF;
    }

    public void intakeReverse() {
        intake.setPower(-INTAKE_ON);

        intakeState = IntakeState.REVERSE;
    }
}
