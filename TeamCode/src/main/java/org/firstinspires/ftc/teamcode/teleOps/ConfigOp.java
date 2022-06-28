package org.firstinspires.ftc.teamcode.teleOps;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Rate;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class ConfigOp extends OpMode {

    Intake intake;
    Cuva cuva;
    Glisiere glisiere;
    Rate rate;
    SampleMecanumDrive drive;

    GamepadEx g1;
    GamepadEx g2;

    Telemetry t;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        cuva = new Cuva(hardwareMap);
        glisiere = new Glisiere(hardwareMap);
        rate = new Rate(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);

        g1 = new GamepadEx(gamepad1);
        g2 = new GamepadEx(gamepad2);

        t = FtcDashboard.getInstance().getTelemetry();
    }

    @Override
    public void loop() {

        g1.update(); g2.update();

        if(gamepad1.dpad_right)
            drive.setMotorPowers(0.3, 0, 0, 0);
        else
            drive.setMotorPowers(0, 0, 0, 0);
        if(gamepad1.dpad_up)
            drive.setMotorPowers(0, 0.3, 0, 0);
        else
            drive.setMotorPowers(0, 0, 0, 0);
        if(gamepad1.dpad_left)
            drive.setMotorPowers(0, 0, 0.3, 0);
        else
            drive.setMotorPowers(0, 0, 0, 0);
        if(gamepad1.dpad_down)
            drive.setMotorPowers(0, 0, 0, 0.3);
        else
            drive.setMotorPowers(0, 0, 0, 0);


        if(g2.y_once) {
            switch (intake.servoState) {
                case UP: intake.intakeDown(); break;
                case DOWN: intake.intakeUp(); break;
            }
        }

        if(g2.x_once) {
            switch (cuva.cuvaState) {
                case INTAKE: cuva.setCuvaMid(); break;
                case MID: cuva.setCuvaOut(); break;
                case OUT: cuva.setCuvaIntake(); break;
            }
        }

        if(g2.a_once) {
            switch (cuva.impinsState) {
                case IN: cuva.setImpinsBlock(); break;
                case BLOCK: cuva.setImpinsOut(); break;
                case OUT: cuva.setImpinsIn(); break;
            }
        }

        intake.update();
        cuva.update();
        glisiere.update();

        telemetry.addLine("Chassis\n")
                .addData("RF", drive.rightFront.getCurrentPosition())
                .addData("RB", drive.rightRear.getCurrentPosition())
                .addData("LB", drive.leftRear.getCurrentPosition())
                .addData("LF", drive.leftFront.getCurrentPosition());
        telemetry.addLine("Intake\n")
                .addData("intakeState", intake.intakeState)
                .addData("servoState", intake.servoState);
        telemetry.addLine("Cuva\n")
                .addData("cuvaState", cuva.cuvaState)
                .addData("impinsState", cuva.impinsState);
        telemetry.addLine("Glisiere\n")
                .addData("glisiereState", glisiere.glisiereState)
                .addData("glisierePositions", glisiere.glisierePositions);
    }
}
