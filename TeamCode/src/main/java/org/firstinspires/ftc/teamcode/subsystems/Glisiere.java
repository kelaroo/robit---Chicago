package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Glisiere {

    // Constants
    public static double POWER = 1;
    public static double MANUAL_POWER = 1;
    public static double IDLE_POWER = 0.0;
    public static double DOWN_POWER = -0.7;

    public static int INTAKE_POS = 0;
    public static int LEVEL1_POS = 600;
    public static int LEVEL2_POS = 1300;

    // Hardware
    DcMotorEx rightMotor;
    DcMotorEx leftMotor;

    public enum GlisierePositions {

        INTAKE(0) {
            @Override
            public GlisierePositions prev() { return this; }
        },
        LEVEL1(0),
        LEVEL2(0) {
            @Override
            public GlisierePositions next() { return this; }
        };

        int ticks;
        GlisierePositions(int t) {ticks = t;}
        public int getTicks() { return ticks; }
        public void setTicks(int t) { ticks = t; }
        public GlisierePositions prev() { return values()[ordinal()-1]; }
        public GlisierePositions next() { return values()[ordinal()+1]; }
    }
    public GlisierePositions glisierePositions = GlisierePositions.INTAKE;

    public enum GlisiereState {
        IDLE, MOVING, AUTO
    }
    public GlisiereState glisiereState = GlisiereState.IDLE;

    public Glisiere(HardwareMap hw) {
        rightMotor = hw.get(DcMotorEx.class, "outtake_right");
        leftMotor = hw.get(DcMotorEx.class, "outtake_left");

        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void glisiereUp() {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor.setPower(POWER);
        leftMotor.setPower(POWER);

        glisiereState = GlisiereState.MOVING;
    }

    public void glisiereStop() {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftMotor.setPower(IDLE_POWER);
        rightMotor.setPower(IDLE_POWER);

        glisiereState = GlisiereState.IDLE;
    }

    public void glisiereDown() {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor.setPower(DOWN_POWER);
        leftMotor.setPower(DOWN_POWER);

        glisiereState = GlisiereState.MOVING;
    }

    public void glisiereManualPower(double axis) {
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if(axis != 0) {
            leftMotor.setPower(axis * MANUAL_POWER);
            rightMotor.setPower(axis * MANUAL_POWER);
        } else {
            leftMotor.setPower(IDLE_POWER);
            rightMotor.setPower(IDLE_POWER);
        }


        glisiereState = GlisiereState.MOVING;
    }

    public int getCurrentPosition() { return leftMotor.getCurrentPosition(); }

    public boolean isAtIntake() { return Math.abs(getCurrentPosition()) < 50; }

    public boolean autoIsAtTarget() {
        return glisiereState == GlisiereState.AUTO
                && !leftMotor.isBusy();
    }

    public void update() {
        GlisierePositions.INTAKE.setTicks(INTAKE_POS);
        GlisierePositions.LEVEL1.setTicks(LEVEL1_POS);
        GlisierePositions.LEVEL2.setTicks(LEVEL2_POS);

        switch (glisiereState) {
            case AUTO:
                leftMotor.setTargetPosition(glisierePositions.getTicks());
                rightMotor.setTargetPosition(glisierePositions.getTicks());

                leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftMotor.setPower(POWER);
                rightMotor.setPower(POWER);

                if(autoIsAtTarget()) {
                    glisiereStop();
                }
                break;
        }
    }

}
