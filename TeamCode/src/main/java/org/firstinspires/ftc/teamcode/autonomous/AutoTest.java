package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Glisiere;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robit;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Config
@Autonomous
public class AutoTest extends LinearOpMode {

    Robit robit;
    SampleMecanumDrive drive;

    Pose2d startPose = new Pose2d(7.0, -65.0, rad(-90.0));

    public static int TIMEOUT = 1000;
    public static int SWITCH_TIME = 150;
    public static double DRIVE = 0.3, STRAFE = 0, ROTATE = 0.3;

    public static double T1_X = -15;
    public static double T1_Y = -51.5;
    public static double T2_X = 15;
    public static double T2_Y = -65;
    public static double T2_FWD = 10;
    public static double T2_STRAFE = -4;
    public static double T3_FWD = 10;

    public static double T4_X = 15;
    public static double T4_Y = -65;
    public static double T5_X = 5.5;
    public static double T5_Y = -54;
    public static double T5_ANGLE = -55;

    @Override
    public void runOpMode() throws InterruptedException {
        robit = new Robit(hardwareMap, true);
        drive = robit.drive;
        robit.initAuto();

        while(!isStarted());
        if(isStopRequested()) return;

        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(opModeIsActive())
                    robit.glisiere.update();
            }
        });
        updateThread.start();

        Trajectory t1 = drive.trajectoryBuilder(startPose)
                .lineToConstantHeading(new Vector2d(T1_X, T1_Y))
                .build();
        TrajectorySequence t2 = drive.trajectorySequenceBuilder(t1.end())
                .splineToLinearHeading(new Pose2d(T2_X, T2_Y, 0.0), 0.0)
                .addDisplacementMarker(()->{
                    robit.cuva.setCuvaIntake();
                    robit.intakeOn();
                    robit.intakeDown();
                    robit.cuva.setImpinsIn();
                })
                .lineToConstantHeading(new Vector2d(T2_X+T2_FWD, T2_Y+T2_STRAFE))
                .build();
        Trajectory t3 = drive.trajectoryBuilder(t2.end())
                .forward(T3_FWD)
                .build();

        robit.capper.setBratPosition(0.4);
        robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL1);
        robit.cuva.setCuvaSfera();
        drive.setPoseEstimate(startPose);

        drive.followTrajectory(t1);
        robit.elementOut();
        sleep(250);

        robit.glisiereAuto(Glisiere.GlisierePositions.INTAKE);

        drive.followTrajectorySequence(t2);

        drive.followTrajectory(t3);

        //nebuneala
        ElapsedTime timer = new ElapsedTime();
        while(!robit.cuva.isElementIn()) {
            telemetry.addData("IN_WHILE", "YES");
            telemetry.update();
            if((int) timer.milliseconds()/SWITCH_TIME % 2 == 0)
                drive.powerFromAxis(DRIVE, STRAFE, ROTATE);
            else drive.powerFromAxis(DRIVE, -STRAFE, -ROTATE);
            drive.update();
        }
        drive.setMotorPowers(0, 0, 0, 0);
        if(robit.cuva.isElementIn())
            robit.autoElementIn();

        Pose2d pose = drive.getPoseEstimate();
        Trajectory t4 = drive.trajectoryBuilder(pose)
                .lineToConstantHeading(new Vector2d(T4_X, T4_Y))
                .build();
        Trajectory t5 = drive.trajectoryBuilder(t4.end(), rad(180.0))
                .splineToLinearHeading(new Pose2d(T5_X, T5_Y, rad(T5_ANGLE)), rad(90.0))
                .build();

        drive.followTrajectory(t4);

        robit.glisiereAuto(Glisiere.GlisierePositions.LEVEL2);
        robit.cuva.setCuvaSfera();

        drive.followTrajectory(t5);

        robit.cuva.setImpinsOut();
        sleep(300);
        robit.glisiereAuto(Glisiere.GlisierePositions.INTAKE);

        while(!isStopRequested());
    }

    double rad(double deg) { return Math.toRadians(deg); }
}
