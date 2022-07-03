package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.subsystems.cameras.CameraRed;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.DetectionPipeline;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.RosuCiclatPipieline;

@Config
@Autonomous
public class AutoRosuCiclat extends LinearOpMode {

    Robit robit;
    SampleMecanumDrive drive;
    CameraRed camera;

    Pose2d startPose = new Pose2d(7.0, -65.0, rad(-90.0));

    public static double SPEED = 40.0;

    public static double T1_X = -10.0;
    public static double T1_Y = -55;

    public static double T2_X = 15.0;
    public static double T2_Y = -64.0;

    public static double T3_FWD = 27.0;

    @Override
    public void runOpMode() throws InterruptedException {

        robit = new Robit(hardwareMap, true);
        drive = robit.drive;
        robit.initAuto();

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(opModeIsActive() || !isStarted())
                    robit.update();
            }
        });
        updateThread.start();

        camera = new CameraRed(hardwareMap, new RosuCiclatPipieline());
        DetectionPipeline.TsePosition tsePosition = DetectionPipeline.TsePosition.RIGHT;
        while(!isStarted()) {
            tsePosition = camera.getDetection();
            telemetry.addData("tsePosition", tsePosition);
            telemetry.update();
        }
        if(isStopRequested()) return;


        Trajectory t1 = drive.myTrajectoryBuilder(startPose, SPEED, SPEED)
                .lineToConstantHeading(new Vector2d(T1_X, T1_Y))
                .build();
        Trajectory t2 = drive.myTrajectoryBuilder(t1.end(), SPEED, SPEED)
                .splineToLinearHeading(new Pose2d(T2_X, T2_Y, 0.0), rad(0.0))
                .build();
        Trajectory t3 = drive.myTrajectoryBuilder(t2.end(), SPEED, SPEED)
                .forward(T3_FWD)
                .build();

        robit.capper.setBratPosition(0.4);
        drive.setPoseEstimate(startPose);

        drive.followTrajectory(t1); sleep(2000);
        drive.followTrajectory(t2); sleep(2000);
        drive.followTrajectory(t3); sleep(2000);

        while(!isStopRequested())
            ;

    }

    double rad(double deg) { return Math.toRadians(deg); }
}
