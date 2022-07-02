package org.firstinspires.ftc.teamcode.subsystems.cameras;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.DebugPipeline;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.DetectionPipeline;
import org.firstinspires.ftc.teamcode.util.GamepadEx;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CameraDebug {
    OpenCvCamera webcam;

    public CameraDebug(HardwareMap hw, GamepadEx g, Telemetry t) {
        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hw.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setPipeline(new DebugPipeline(g, t));

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        FtcDashboard.getInstance().startCameraStream(webcam, 0);
    }
}
