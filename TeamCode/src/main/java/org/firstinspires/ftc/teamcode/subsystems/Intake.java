package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Intake {

    // Constants
    public static double INTAKE_ON = 1.0;
    public static double SERVO_UP = 0.5;
    public static double SERVO_DOWN = 0.5;

    // Hardware
    DcMotorEx intake;
    Servo servoIntake;

    public enum IntakeState {
        ON, OFF, REVERSE
    }
    public IntakeState intakeState = IntakeState.OFF;

    public Intake(HardwareMap hw) {
        intake = hw.get(DcMotorEx.class, "intake");
        servoIntake = hw.get(Servo.class, "servoIntake");
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

    public void intakeDown() { servoIntake.setPosition(SERVO_DOWN); }
    public void intakeUp() { servoIntake.setPosition(SERVO_UP); }
}
