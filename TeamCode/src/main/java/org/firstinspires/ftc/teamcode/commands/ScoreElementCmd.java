package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class ScoreElementCmd extends Command{
    @Override
    public void runCmd() {
        Robot.intake.intakeOff();
        Robot.cuva.setCuvaOut();
        Robot.cuva.setImpinsOut();
    }
}
