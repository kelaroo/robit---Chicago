package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class GlisiereToAutoCmd extends Command{
    @Override
    public void runCmd() {
        Robot.cuva.setCuvaMid();
        Robot.intake.intakeOff();
        Robot.glisiere.glisiereState = Glisiere.GlisiereState.AUTO;
    }
}
