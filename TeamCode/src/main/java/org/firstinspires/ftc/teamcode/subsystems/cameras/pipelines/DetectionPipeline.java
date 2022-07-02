package org.firstinspires.ftc.teamcode.subsystems.cameras.pipelines;

import org.openftc.easyopencv.OpenCvPipeline;

public abstract class DetectionPipeline extends OpenCvPipeline {
    public enum TsePosition {
        LEFT, MID, RIGHT
    }
    protected TsePosition tsePosition = TsePosition.LEFT;

    public TsePosition getDetection() { return tsePosition; }
}
