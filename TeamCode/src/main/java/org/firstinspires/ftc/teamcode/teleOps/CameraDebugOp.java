package org.firstinspires.ftc.teamcode.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.cameras.CameraDebug;
import org.firstinspires.ftc.teamcode.util.GamepadEx;

@TeleOp
public class CameraDebugOp extends LinearOpMode {

    CameraDebug camera;

    @Override
    public void runOpMode() throws InterruptedException {
        camera = new CameraDebug(hardwareMap, new GamepadEx(gamepad1), telemetry);

        waitForStart();

        while(!isStopRequested())
            ;
    }
}
