package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class IntakeOnCmd extends Command{

    @Override
    public void runCmd() {
        if(Robot.glisiere.isAtIntake()) {
            Robot.intake.intakeDown();
            Robot.cuva.setCuvaIntake();
            Robot.cuva.setImpinsIn();
            Robot.intake.intakeOn();
        }
    }
}
