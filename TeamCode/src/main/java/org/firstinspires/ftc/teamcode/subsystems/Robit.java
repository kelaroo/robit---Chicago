package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class Robit {

    public SampleMecanumDrive drive;

    public Intake intake;
    public Cuva cuva;
    public Glisiere glisiere;
    public Rate rate;

    private boolean isRedAlliance;

    public Robit(HardwareMap hw, boolean isRed) {
        drive = new SampleMecanumDrive(hw);

        intake = new Intake(hw);
        cuva = new Cuva(hw);
        glisiere = new Glisiere(hw);
        rate = new Rate(hw);

        isRedAlliance = isRed;
    }

    public void update() {
        drive.update();
        intake.update();
        cuva.update();
        glisiere.update();
    }

    public void intakeDown() { intake.intakeDown(); }
    public void intakeUp() { intake.intakeUp(); }

    public void intakeOn() {
        if(glisiere.isAtIntake()) {
            intake.intakeOn();
            cuva.setCuvaIntake();
            cuva.setImpinsIn();
        }
    }

    public void intakeOff() {
        intake.intakeOff();
    }

    public void intakeOut() {
        intake.intakeReverse();
    }

    public void glisiereManual(double axis) {
        if(axis != 0) {
            cuva.setCuvaMid();
            intake.intakeOff();
        }
        glisiere.glisiereManualPower(axis);
    }

    public void glisiereAuto(Glisiere.GlisierePositions positions) {
        intake.intakeOff();
        cuva.setCuvaMid();
        glisiere.glisiereState = Glisiere.GlisiereState.AUTO;
        glisiere.glisierePositions = positions;
    }

    public void elementIn() {
        intake.intakeOff();
        cuva.setImpinsBlock();
    }

    public void setCuva(Cuva.CuvaState pos) {
        switch (pos) {
            case INTAKE: cuva.setCuvaIntake(); break;
            case MID: cuva.setCuvaMid(); break;
            case SHIPPING: cuva.setCuvaShipping(); break;
            case SFERA: cuva.setCuvaSfera(); break;
            case OUT: cuva.setCuvaOut(); break;
        }
    }

    public void spinDucks() {
        if(isRedAlliance)
            rate.rateRed();
        else rate.rateBlue();
    }

    public void stopDucks() { rate.rateStop(); }

    Thread elementOutThread;
    public void elementOut() {
        if(elementOutThread != null && elementOutThread.isAlive())
            return;

        if(cuva.cuvaState != Cuva.CuvaState.INTAKE) {
            elementOutThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    cuva.setImpinsOut();
                    ElapsedTime timer = new ElapsedTime();
                    while(timer.milliseconds() < 1000);
                    cuva.setImpinsBlock();
                }
            });
            elementOutThread.start();
        }
    }

}
