package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.subsystems.cameras.CameraRed;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.DetectionPipeline;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.RosuCiclatPipieline;

@Autonomous
public class AutoRosuCiclat extends LinearOpMode {

    Robit robit;
    SampleMecanumDrive drive;
    CameraRed camera;

    Pose2d startPose = new Pose2d(7.0, -65.0, rad(-90.0));

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

        Trajectory t1 = drive.trajectoryBuilder(startPose, true)
                .splineToConstantHeading(new Vector2d(-12.0, -43.0), rad(180.0))
                .build();
        Trajectory t2 = drive.trajectoryBuilder(t1.end(), false)
                .splineTo(new Vector2d(10.0, -65.0), rad(0.0))
                .forward(30.0)
                .build();
        Trajectory t3 = drive.trajectoryBuilder(t2.end(), false)
                .back(30.0)
                .build();
        Trajectory t4 = drive.trajectoryBuilder(t3.end(), true)
                .splineToLinearHeading(new Pose2d(-12.0, -43.0, rad(-90.0)), rad(90.0))
                .build();

        robit.capper.setBratPosition(0.4);
        drive.setPoseEstimate(startPose);

        drive.followTrajectory(t1);
        //lasa primul element

        drive.followTrajectory(t2);
        //merge in warehouse
        //ia un element

        drive.followTrajectory(t3);
        drive.followTrajectory(t4);
        //lasa element

    }

    double rad(double deg) { return Math.toRadians(deg); }
}
