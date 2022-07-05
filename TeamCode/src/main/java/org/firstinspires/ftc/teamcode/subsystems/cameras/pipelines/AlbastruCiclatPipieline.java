package org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class AlbastruCiclatPipieline extends DetectionPipeline{

    public static int H_MIN = 55;
    public static int S_MIN = 120;
    public static int V_MIN = 155;
    public static int H_MAX = 105;
    public static int S_MAX = 255;
    public static int V_MAX = 255;

    public static int SEPARATION_LINE = 180;

    Scalar hsvMin = new Scalar(H_MIN, S_MIN, V_MIN);
    Scalar hsvMax = new Scalar(H_MAX, S_MAX, V_MAX);

    Rect leftScreenRect = new Rect(new Point(0, 0), new Point(SEPARATION_LINE, 240));
    Rect rightScreenRect = new Rect(new Point(SEPARATION_LINE, 0), new Point(320, 240));

    Mat hsvMat = new Mat();
    Mat filteredMat = new Mat();

    public static int nrPixelsLeft = 0;
    public static int nrPixelsRight = 0;

    @Override
    public Mat processFrame(Mat input) {

        hsvMin = new Scalar(H_MIN, S_MIN, V_MIN);
        hsvMax = new Scalar(H_MAX, S_MAX, V_MAX);
        leftScreenRect = new Rect(new Point(0, 0), new Point(SEPARATION_LINE, 240));
        rightScreenRect = new Rect(new Point(SEPARATION_LINE, 0), new Point(320, 240));

        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat, hsvMin, hsvMax, filteredMat);

        Mat leftScreen = filteredMat.submat(leftScreenRect);
        Mat rightScreen = filteredMat.submat(rightScreenRect);

        nrPixelsLeft = Core.countNonZero(leftScreen);
        nrPixelsRight = Core.countNonZero(rightScreen);

        if(nrPixelsLeft < 1000 && nrPixelsRight < 1000 )
            tsePosition = TsePosition.LEFT;
        else if(nrPixelsLeft > nrPixelsRight)
            tsePosition = TsePosition.MID;
        else
            tsePosition = TsePosition.RIGHT;

        Imgproc.line(input, new Point(SEPARATION_LINE, 0), new Point(SEPARATION_LINE, 240), new Scalar(0, 255, 0));
        return filteredMat;
    }
}
