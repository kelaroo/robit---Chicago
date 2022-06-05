package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class ElementInsideCmd extends Command {

    @Override
    public void runCmd() {
        Robot.cuva.setImpinsBlock();
        Robot.cuva.setCuvaMid();
        Robot.intake.intakeOff();
    }
}
