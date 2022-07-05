package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Rate {

    // Constants
    public static double POWER = 0.22;

    // Hardware
    public DcMotorEx rate;

    public Rate(HardwareMap hw) {
        rate = hw.get(DcMotorEx.class, "carousel");
        rate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rate.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void rateRed() {
        rate.setPower(POWER);
    }

    public void rateBlue() {
        rate.setPower(-POWER);
    }

    public void rateStop() {
        rate.setPower(0.0);
    }

}
