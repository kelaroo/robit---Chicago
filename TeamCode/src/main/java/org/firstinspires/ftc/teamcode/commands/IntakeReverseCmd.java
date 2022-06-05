package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class IntakeReverseCmd extends Command{

    @Override
    public void runCmd() {
        Robot.cuva.setImpinsIn();
        Robot.intake.intakeReverse();
    }
}
