package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.Cuva;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class RobitOp extends OpMode {

    GamepadEx g1, g2;

    Robit robit;

    boolean elementInLastLoop = false;

    @Override
    public void init() {
        robit = new Robit(hardwareMap, true);

        robit.intakeDown();
        robit.update();

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

        if(elementInLastLoop == false
        && robit.cuva.isElementIn()) {
            robit.autoElementIn();
        }
        elementInLastLoop = robit.cuva.isElementIn();

        if(robit.elementInThread == null || !robit.elementInThread.isAlive())
            if(g2.left_trigger > 0.1 && !elementInLastLoop)
                robit.intakeOn();
            else if(g2.right_trigger > 0.1)
                robit.intakeOut();
            else robit.intakeOff();


        /*if(robit.cuva.isElementIn() && robit.glisiere.isAtIntake()) {
            if(g2.right_trigger > 0.1) {
                robit.intakeOut();
            } else {
                robit.elementIn();
            }
        } else {
            if(g2.left_trigger > 0.1)
                robit.intakeOn();
            else if(g2.right_trigger > 0.1)
                robit.intakeOut();
            else robit.intakeOff();
        }*/


        double glisiereAxis = -g2.left_stick_y;
        if(glisiereAxis != 0)
            robit.glisiereManual(glisiereAxis);
        else if(g2.a_once)
            robit.glisiereAuto(Glisiere.GlisierePositions.INTAKE);
        else if(g2.x_once) {
            robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL1);
            robit.cuva.setCuvaSfera();
        }
        else if(g2.y_once) {
            robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL2);
            robit.cuva.setCuvaShipping();
        }
        else if(robit.glisiere.glisiereState != Glisiere.GlisiereState.AUTO)
            robit.glisiereManual(0);

        if(g2.dpad_down_once)
            robit.setCuva(Cuva.CuvaState.INTAKE);
        else if(g2.dpad_up_once)
            switch (robit.cuva.cuvaState) {
                case SHIPPING: robit.setCuva(Cuva.CuvaState.SFERA); break;
                case SFERA: robit.setCuva(Cuva.CuvaState.SHIPPING); break;
            }
        else if(g2.dpad_right_once)
            robit.setCuva(Cuva.CuvaState.SFERA);
        else if(g2.dpad_left_once)
            robit.setCuva(Cuva.CuvaState.OUT);

        if(g2.left_bumper)
            robit.spinDucks();
        else robit.stopDucks();

        if(g1.right_trigger > 0.1)
            robit.elementOut();

        if(robit.cuva.cuvaState != Cuva.CuvaState.INTAKE)
            robit.intakeUp();
        else robit.intakeDown();

        if(g1.dpad_up)
            robit.capper.bratUp();
        else if(g1.dpad_down)
            robit.capper.bratDown();
        else if(g1.dpad_right_once)
            robit.capper.clawDrop();
        else if(g1.dpad_left_once)
            robit.capper.clawGrab();

        telemetry.setItemSeparator("\n\t");
        telemetry.addLine("SensorCuva\n")
                .addData("isElementIn", robit.cuva.isElementIn())
                .addData("rawLight", robit.cuva.sensorCuva.getRawLightDetected())
                .addData("distance", robit.cuva.sensorCuva.getDistance(DistanceUnit.MM))
                .addData("type", robit.cuva.isSphereOrCube());
        telemetry.addLine("Glisiere\n")
                .addData("position", robit.glisiere.getCurrentPosition());
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
