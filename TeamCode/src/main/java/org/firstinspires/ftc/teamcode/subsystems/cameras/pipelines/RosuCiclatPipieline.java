package org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class RosuCiclatPipieline extends DetectionPipeline{

    Scalar hsvMin = new Scalar(85, 50, 75);
    Scalar hsvMax = new Scalar(105, 255, 255);

    Rect leftScreenRect = new Rect(new Point(0, 0), new Point(182, 240));
    Rect rightScreenRect = new Rect(new Point(183, 0), new Point(320, 240));

    Mat hsvMat = new Mat();
    Mat filteredMat = new Mat();

    public static int nrPixelsLeft = 0;
    public static int nrPixelsRight = 0;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat, hsvMin, hsvMax, filteredMat);

        Mat leftScreen = filteredMat.submat(leftScreenRect);
        Mat rightScreen = filteredMat.submat(rightScreenRect);

        nrPixelsLeft = Core.countNonZero(leftScreen);
        nrPixelsRight = Core.countNonZero(rightScreen);

        if(nrPixelsLeft < 450 && nrPixelsRight < 450)
            tsePosition = TsePosition.RIGHT;
        else if(nrPixelsLeft > nrPixelsRight)
            tsePosition = TsePosition.LEFT;
        else
            tsePosition = TsePosition.MID;

        Imgproc.line(input, new Point(183, 0), new Point(183, 240), new Scalar(0, 255, 0));
        return input;
    }
}
