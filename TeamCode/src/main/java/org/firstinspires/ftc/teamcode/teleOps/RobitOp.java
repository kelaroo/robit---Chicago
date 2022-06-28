package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class RobitOp extends OpMode {

    GamepadEx g1, g2;

    Robit robit;

    @Override
    public void init() {
        robit = new Robit(hardwareMap, true);

        g1 = new GamepadEx(gamepad1);
        g2 = new GamepadEx(gamepad2);
    }

    @Override
    public void loop() {

        g1.update(); g2.update();

        double d = -gamepad1.left_stick_y;
        double s = gamepad1.left_stick_x;
        double r = gamepad1.right_stick_x;
        robit.drive.powerFromAxis(d, s, r);

        if(g2.left_stick_button_once)
            switch (robit.intake.servoState) {
                case UP: robit.intakeDown(); break;
                case DOWN: robit.intakeUp(); break;
            }

        if(g2.left_trigger > 0.1)
            robit.intakeOn();
        else if(g2.right_trigger > 0.1)
            robit.intakeOut();
        else robit.intakeOff();

        double glisiereAxis = -g2.left_stick_y;
        if(glisiereAxis != 0)
            robit.glisiereManual(glisiereAxis);
        else if(g2.a_once)
            robit.glisiereAuto(Glisiere.GlisierePositions.INTAKE);
        else if(g2.x_once)
            robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL1);
        else if(g2.y_once)
            robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL2);
        else if(robit.glisiere.glisiereState != Glisiere.GlisiereState.AUTO)
            robit.glisiereManual(0);

        if(g2.dpad_down_once)
            robit.setCuva(Cuva.CuvaState.INTAKE);
        else if(g2.dpad_up_once)
            robit.setCuva(Cuva.CuvaState.SHIPPING);
        else if(g2.dpad_right_once)
            robit.setCuva(Cuva.CuvaState.SFERA);
        else if(g1.dpad_left_once)
            robit.setCuva(Cuva.CuvaState.OUT);

        if(g2.left_bumper)
            robit.spinDucks();
        else robit.stopDucks();

        if(g1.right_trigger > 0.1)
            robit.elementOut();

        telemetry.setItemSeparator("\n\t");
        telemetry.addLine("SensorCuva\n")
                .addData("isElementIn", robit.cuva.isElementIn())
                .addData("type", robit.cuva.isSphereOrCube());
/*
        NormalizedRGBA colors = robit.cuva.sensorCuva.getNormalizedColors();
        ColorRangeSensor sensor = robit.cuva.sensorCuva;
        telemetry.addLine("Sensor\n")
                .addData("distance", robit.cuva.sensorCuva.getDistance(DistanceUnit.MM))
                .addData("normColors", String.format("r=%.3f; g=%.3f; b=%.3f", colors.red, colors.green, colors.blue))
                .addData("colors", String.format("r=%d; g=%d; b=%d", sensor.red(), sensor.green(), sensor.blue()))
                .addData("light", robit.cuva.sensorCuva.getLightDetected())
                .addData("rawLight", robit.cuva.sensorCuva.getRawLightDetected());
*/

        robit.update();
    }
}
