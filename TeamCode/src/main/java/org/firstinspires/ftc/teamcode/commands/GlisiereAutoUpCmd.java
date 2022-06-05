package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class GlisiereAutoUpCmd extends Command {

    @Override
    public void runCmd() {
        Robot.cuva.setCuvaMid();
        Robot.intake.intakeOff();
        Robot.glisiere.glisierePositions = Robot.glisiere.glisierePositions.next();
    }
}
