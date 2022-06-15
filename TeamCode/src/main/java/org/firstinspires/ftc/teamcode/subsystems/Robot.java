package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.commands.CuvaIntakeCmd;
import org.firstinspires.ftc.teamcode.commands.ElementInsideCmd;
import org.firstinspires.ftc.teamcode.commands.GlisiereAutoDownCmd;
import org.firstinspires.ftc.teamcode.commands.GlisiereAutoUpCmd;
import org.firstinspires.ftc.teamcode.commands.GlisiereDownCmd;
import org.firstinspires.ftc.teamcode.commands.GlisiereToAutoCmd;
import org.firstinspires.ftc.teamcode.commands.GlisiereUpCmd;
import org.firstinspires.ftc.teamcode.commands.IntakeOffCmd;
import org.firstinspires.ftc.teamcode.commands.IntakeOnCmd;
import org.firstinspires.ftc.teamcode.commands.IntakeReverseCmd;
import org.firstinspires.ftc.teamcode.commands.ScoreElementCmd;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class Robot {

    public static SampleMecanumDrive drive;
    public static Intake intake;
    public static Cuva cuva;
    public static Glisiere glisiere;
    public static Rate rate;

    public static CuvaIntakeCmd cuvaIntakeCmd;
    public static ElementInsideCmd elementInsideCmd;
    public static GlisiereAutoDownCmd glisiereAutoDownCmd;
    public static GlisiereAutoUpCmd glisiereAutoUpCmd;
    public static GlisiereDownCmd glisiereDownCmd;
    public static GlisiereToAutoCmd glisiereToAutoCmd;
    public static GlisiereUpCmd glisiereUpCmd;
    public static IntakeOffCmd intakeOffCmd;
    public static IntakeOnCmd intakeOnCmd;
    public static IntakeReverseCmd intakeReverseCmd;
    public static ScoreElementCmd scoreElementCmd;

    public static void initHardware(SampleMecanumDrive d, Intake i, Cuva c, Glisiere g, Rate r) {
        drive = d;
        intake = i;
        cuva = c;
        glisiere = g;
        rate = r;

        cuvaIntakeCmd = new CuvaIntakeCmd();
        elementInsideCmd = new ElementInsideCmd();
        glisiereAutoDownCmd = new GlisiereAutoDownCmd();
        glisiereAutoUpCmd = new GlisiereAutoUpCmd();
        glisiereDownCmd = new GlisiereDownCmd();
        glisiereToAutoCmd = new GlisiereToAutoCmd();
        glisiereUpCmd = new GlisiereUpCmd();
        intakeOffCmd = new IntakeOffCmd();
        intakeOnCmd = new IntakeOnCmd();
        intakeReverseCmd = new IntakeReverseCmd();
        scoreElementCmd = new ScoreElementCmd();
    }
}
