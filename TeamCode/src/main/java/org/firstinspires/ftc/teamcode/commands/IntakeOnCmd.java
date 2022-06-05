package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class IntakeOnCmd extends Command{

    @Override
    public void runCmd() {
        if(Robot.cuva.cuvaState != Cuva.CuvaState.INTAKE &&
        Robot.glisiere.isAtIntake()) {
            Robot.cuva.setImpinsIn();
            Robot.intake.intakeOn();
        }
    }
}
