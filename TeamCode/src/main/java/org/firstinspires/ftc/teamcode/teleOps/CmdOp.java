package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.IntakeOffCmd;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Rate;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class CmdOp extends OpMode {
    GamepadEx g1;
    GamepadEx g2;

    SampleMecanumDrive drive;

    Intake intake;
    Cuva cuva;
    Glisiere glisiere;
    Rate rate;

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

        if(g2.y_once) {
            switch (intake.intakeState) {
                case ON: Robot.intakeOffCmd.runCmd(); break;
                case OFF: Robot.intakeOnCmd.runCmd(); break;
            }
        } else if(g2.right_trigger > 0.2)
            Robot.intakeReverseCmd.runCmd();
        else if(intake.intakeState == Intake.IntakeState.REVERSE)
            Robot.intakeOffCmd.runCmd();

        if(g2.x_once)
            switch (cuva.cuvaState) {
                case INTAKE: Robot.elementInsideCmd.runCmd(); break;
                case MID: Robot.scoreElementCmd.runCmd(); break;
                case OUT: Robot.elementInsideCmd.runCmd(); break;
            }

        switch (glisiere.glisiereState) {
            case AUTO:
                if(g2.left_stick_button_once)
                    glisiere.glisiereStop();
                else if(g2.dpad_up_once) {
                    Robot.glisiereAutoUpCmd.runCmd();
                } else if(g2.dpad_down_once) {
                    Robot.glisiereAutoDownCmd.runCmd();
                }
                break;
            default:
                if(g2.right_stick_button_once) {
                    Robot.glisiereToAutoCmd.runCmd();
                } else if(g2.dpad_up) {
                    Robot.glisiereUpCmd.runCmd();
                } else if(g2.dpad_down) {
                    Robot.glisiereDownCmd.runCmd();
                } else {
                    glisiere.glisiereStop();
                }
        }

        glisiere.update();

        telemetry.addLine("Glisiere\n")
                .addData("state", glisiere.glisiereState)
                .addData("pos", glisiere.getCurrentPosition());

    }
}
