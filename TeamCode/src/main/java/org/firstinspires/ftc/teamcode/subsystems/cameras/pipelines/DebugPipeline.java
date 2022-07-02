package org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.GamepadEx;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Config
public class DebugPipeline extends OpenCvPipeline {

    GamepadEx g;
    Telemetry telemetry;

    public DebugPipeline(GamepadEx gamePad, Telemetry t) {
        g = gamePad;
        telemetry = t;
    }

    boolean retInput = true;
    String detection = "LEFT";

    public static int H_MIN = 0;
    public static int S_MIN = 0;
    public static int V_MIN = 0;
    public static int H_MAX = 255;
    public static int S_MAX = 255;
    public static int V_MAX = 255;

    public static int SEPARATION_LINE = 180;

    public static int HSV_SPEED = 5;

    int editingNow = 0;
    String edited = "H_MIN";

    /////// Detection
    Mat hsvMat = new Mat();
    Mat filteredMat = new Mat();

    public static int nrPixelsLeft = 0;
    public static int nrPixelsRight = 0;

    @Override
    public Mat processFrame(Mat input) {

        g.update();

        if(g.y_once)
            editingNow = (editingNow+1) % 6;
        else if(g.a_once)
            editingNow = (editingNow-1) % 6;

        if(g.dpad_right)
            SEPARATION_LINE = Range.clip(SEPARATION_LINE+HSV_SPEED, 0, 320);
        else if(g.dpad_left)
            SEPARATION_LINE = Range.clip(SEPARATION_LINE-HSV_SPEED, 0, 320);

        switch (editingNow) {
            case 0: // H_MIN
                edited = "H_MIN";
                if(g.b) H_MIN = Range.clip(H_MIN + HSV_SPEED, 0, 255);
                else if(g.x) H_MIN = Range.clip(H_MIN - HSV_SPEED, 0, 255); break;
            case 1: // H_MAX
                edited = "H_MAX";
                if(g.b) H_MAX = Range.clip(H_MAX + HSV_SPEED, 0, 255);
                else if(g.x) H_MAX = Range.clip(H_MAX - HSV_SPEED, 0, 255); break;
            case 2: // S_MIN
                edited = "S_MIN";
                if(g.b) S_MIN = Range.clip(S_MIN + HSV_SPEED, 0, 255);
                else if(g.x) S_MIN = Range.clip(S_MIN - HSV_SPEED, 0, 255); break;
            case 3: // S_MAX
                edited = "S_MAX";
                if(g.b) S_MAX = Range.clip(S_MAX + HSV_SPEED, 0, 255);
                else if(g.x) S_MAX = Range.clip(S_MAX - HSV_SPEED, 0, 255); break;
            case 4: // V_MIN
                edited = "V_MIN";
                if(g.b) V_MIN = Range.clip(V_MIN + HSV_SPEED, 0, 255);
                else if(g.x) V_MIN = Range.clip(V_MIN - HSV_SPEED, 0, 255); break;
            case 5: // V_MAX
                edited = "V_MAX";
                if(g.b) V_MAX = Range.clip(V_MAX + HSV_SPEED, 0, 255);
                else if(g.x) V_MAX = Range.clip(V_MAX - HSV_SPEED, 0, 255); break;
        }

        if(g.left_stick_button_once)
            retInput = !retInput;

        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat,
                new Scalar(H_MIN, S_MIN, V_MIN),
                new Scalar(H_MAX, S_MAX, V_MAX),
                filteredMat);

        Mat leftScreen = filteredMat.submat(new Rect(new Point(0, 0), new Point(SEPARATION_LINE, 240)));
        Mat rightScreen = filteredMat.submat(new Rect(new Point(SEPARATION_LINE, 0), new Point(320, 240)));

        nrPixelsLeft = Core.countNonZero(leftScreen);
        nrPixelsRight = Core.countNonZero(rightScreen);

        if(nrPixelsLeft < 450 && nrPixelsRight < 450)
            detection = "RIGHT";
        else if(nrPixelsLeft > nrPixelsRight)
            detection = "LEFT";
        else
            detection = "MID";

        Imgproc.line(input, new Point(SEPARATION_LINE, 0), new Point(SEPARATION_LINE, 240), new Scalar(0, 255, 0));

        telemetry.addLine("HSV_MIN\n")
                .addData("h", H_MIN)
                .addData("s", S_MIN)
                .addData("v", V_MIN);
        telemetry.addLine("HSV_MAX\n")
                .addData("h", H_MAX)
                .addData("s", S_MAX)
                .addData("v", V_MAX);

        telemetry.addData("Editing", edited);
        telemetry.addData("SeparationLine", SEPARATION_LINE);
        telemetry.addData("Detection", detection);

        telemetry.update();

        if(retInput)
            return input;
        return filteredMat;
    }
}
