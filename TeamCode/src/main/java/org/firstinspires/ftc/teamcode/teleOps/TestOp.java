package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Rate;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class TestOp extends OpMode {

    GamepadEx g1;
    GamepadEx g2;

    SampleMecanumDrive drive;

    Intake intake;
    Cuva cuva;
    Glisiere glisiere;
    Rate rate;

    boolean intakeOn = false;
    int cuvaIntake = 0;
    boolean impinsBlock = true;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);

        intake = new Intake(hardwareMap);
        cuva = new Cuva(hardwareMap);
        glisiere = new Glisiere(hardwareMap);
        rate = new Rate(hardwareMap);

        g1 = new GamepadEx(gamepad1);
        g2 = new GamepadEx(gamepad2);

        g1.update();
        g2.update();

        glisiere.glisiereState = Glisiere.GlisiereState.IDLE;

        Robot.initHardware(drive, intake, cuva, glisiere, rate);
    }

    @Override
    public void loop() {
        g1.update();
        g2.update();

        double d = -gamepad1.left_stick_y;
        double s = gamepad1.left_stick_x;
        double r = gamepad1.right_stick_x;

        drive.powerFromAxis(d, s, r);

        //region Intake
        if(g2.y_once) intakeOn = !intakeOn;
        if(intakeOn) intake.intakeOn();
        else intake.intakeOff();

        if(g2.right_trigger > 0.2) {
            intakeOn = false;
            intake.intakeReverse();
        }
        //endregion

        //region Cuva
        if(g2.x_once)
            switch (cuvaIntake) {
                case 0:
                case 1:
                    cuvaIntake = 2;
                    break;
                case 2:
                    cuvaIntake = 0;
            }

        if(g2.left_bumper_once) impinsBlock = !impinsBlock;
        if(impinsBlock)
            cuva.setImpinsIn();
        else cuva.setImpinsOut();
        //endregion

        //region Glisiere
        switch (glisiere.glisiereState) {
            case AUTO:
                if(g2.left_stick_button_once) {
                    glisiere.glisiereStop();
                } else {
                    if(g2.dpad_up_once)
                        glisiere.glisierePositions = glisiere.glisierePositions.next();
                    else if(g2.dpad_down_once)
                        glisiere.glisierePositions = glisiere.glisierePositions.prev();
                }
                break;
            default:
                if(g2.right_stick_button_once) {
                    glisiere.glisiereState = Glisiere.GlisiereState.AUTO;
                    cuvaIntake = 1;
                }
                else if(g2.dpad_up)
                    glisiere.glisiereUp();
                else if(g2.dpad_down)
                    glisiere.glisiereDown();
                else glisiere.glisiereStop();
        }
        //endregion

        //region Rate
        if(g1.left_trigger > 0.2)
            rate.rateRed();
        else rate.rateStop();
        //endregion

        if(glisiere.glisiereState == Glisiere.GlisiereState.MOVING)
            cuvaIntake = 1;

        switch (cuvaIntake) {
            case 0: cuva.setCuvaIntake(); break;
            case 1: cuva.setCuvaMid(); break;
            case 2: cuva.setCuvaOut(); break;
        }

        glisiere.update();

        telemetry.addLine("Glisiere\n")
                .addData("state", glisiere.glisiereState)
                .addData("pos", glisiere.getCurrentPosition());
    }
}
