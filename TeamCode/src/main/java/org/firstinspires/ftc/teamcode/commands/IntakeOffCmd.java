package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class IntakeOffCmd extends Command {
    @Override
    public void runCmd() {
        Robot.cuva.setImpinsBlock();
        Robot.intake.intakeOff();
    }
}
