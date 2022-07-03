package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
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

    public static double T1_X = 7;
    public static double T1_Y = -54.0;
    public static double T1_ANGLE = -12.0;

    public static double T2_X = 7.0;
    public static double T2_Y = -63.3;
    public static double T2_TANGENT = 0.0;
    public static double T2_1_FWD = 40.0;

    public static double T3_BWD = 50.0;

    public static double T4_X = -4.0;
    public static double T4_Y = -50.0;
    public static double T4_ANGLE = -20.0;
    public static double T4_TANGENT = 90.0;

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

        Trajectory t1 = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(T1_X, T1_Y, rad(T1_ANGLE)))
                .build();
        Trajectory t2 = drive.trajectoryBuilder(t1.end())
                .lineToLinearHeading(new Pose2d(T2_X, T2_Y, 0.0))
                .build();
        Trajectory t2_1 = drive.trajectoryBuilder(t2.end())
                .forward(T2_1_FWD)
                .build();
        Trajectory t3 = drive.myTrajectoryBuilder(t2_1.end(), 20.0, 20.0)
                .back(T3_BWD)
                .build();
        Trajectory t4 = drive.myTrajectoryBuilder(t3.end(), 180.0, 20, 20)
                .splineToLinearHeading(new Pose2d(T4_X, T4_Y, rad(T4_ANGLE)), rad(90.0))
                .build();

        robit.capper.setBratPosition(0.4);
        drive.setPoseEstimate(startPose);

        drive.followTrajectory(t1); sleep(2000);
        //lasa primul element

        drive.followTrajectory(t2); sleep(2000);
        drive.followTrajectory(t2_1);
        //merge in warehouse
        //ia un element

        drive.followTrajectory(t3); sleep(2000);
        drive.followTrajectory(t4); sleep(2000);
        //lasa element

    }

    double rad(double deg) { return Math.toRadians(deg); }
}
