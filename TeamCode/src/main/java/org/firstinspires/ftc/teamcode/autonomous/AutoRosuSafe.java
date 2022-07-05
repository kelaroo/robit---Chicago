package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.subsystems.cameras.CameraRed;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.AlbastruCiclatPipieline;
import org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines.DetectionPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Config
@Autonomous
public class AutoRosuSafe extends LinearOpMode {

    Robit robit;
    SampleMecanumDrive drive;
    CameraRed camera;

    Pose2d startPose = new Pose2d(-43.0, -65.0, rad(-90.0));

    public static double T1_X_LEFT = -23;
    public static double T1_Y_LEFT = -48;
    public static double T1_ANGLE_LEFT = -120;

    public static double T1_X_MID = -23;
    public static double T1_Y_MID = -48;
    public static double T1_ANGLE_MID = -120;

    public static double T1_X_RIGHT = -23;
    public static double T1_Y_RIGHT = -48;
    public static double T1_ANGLE_RIGHT = -120;

    public static double T2_X = 0.0;
    public static double T2_Y = -50.0;
    public static double PARK_POWER = 0.8;

    @Override
    public void runOpMode() throws InterruptedException {
        robit = new Robit(hardwareMap, true);
        drive = robit.drive;
        robit.initAuto();

        camera = new CameraRed(hardwareMap, new AlbastruCiclatPipieline());

        DetectionPipeline.TsePosition tsePosition = DetectionPipeline.TsePosition.RIGHT;
        while(!isStarted()) {
            tsePosition = camera.getDetection();
            telemetry.addData("detection", tsePosition);
            telemetry.update();
        }
        if(isStopRequested()) return;

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(opModeIsActive())
                    robit.glisiere.update();
            }
        });
        updateThread.start();

        robit.capper.setBratPosition(0.4);

        Trajectory t1;
        switch (tsePosition) {
            case MID:
                t1 = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(T1_X_MID, T1_Y_MID, rad(T1_ANGLE_MID)))
                        .build();
                robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL1);
                robit.cuva.setCuvaSfera();
                drive.setPoseEstimate(startPose);
                break;
            case LEFT:
                t1 = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(T1_X_LEFT, T1_Y_LEFT, rad(T1_ANGLE_LEFT)))
                        .build();
                robit.cuva.setCuvaOut();
                drive.setPoseEstimate(startPose);
                break;
            case RIGHT:
            default:
                t1 = drive.trajectoryBuilder(startPose)
                        .lineToLinearHeading(new Pose2d(T1_X_RIGHT, T1_Y_RIGHT, rad(T1_ANGLE_RIGHT)))
                        .build();
                robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL2);
                robit.glisiere.update();
                robit.cuva.setCuvaShipping();
                drive.setPoseEstimate(startPose);
                break;
        }

        drive.followTrajectory(t1);
        robit.elementOut();
        sleep(250);

        robit.glisiereAuto(Glisiere.GlisierePositions.INTAKE);
        robit.glisiere.update();

        Trajectory t2 = drive.trajectoryBuilder(t1.end())
                .lineToLinearHeading(new Pose2d(T2_X, T2_Y, rad(180.0)))
                .build();

        drive.followTrajectory(t2);

        drive.setMotorPowers(PARK_POWER, PARK_POWER, PARK_POWER, PARK_POWER);

        while(!isStopRequested());
    }

    double rad(double deg) { return Math.toRadians(deg); }
}
