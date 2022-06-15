package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public class CuvaIntakeCmd extends Command{
    @Override
    public void runCmd() {
        if(Robot.glisiere.isAtIntake())
            Robot.cuva.setCuvaIntake();
    }
}
