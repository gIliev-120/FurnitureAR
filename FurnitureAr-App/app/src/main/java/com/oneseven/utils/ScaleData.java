package com.oneseven.utils;

import com.google.ar.sceneform.math.Vector3;

public class ScaleData {
    private Float maxScale;
    private Float minScale;
    private Vector3 currentScale;


    public ScaleData(Float minScale, Float maxScale, Vector3 currentScale) {
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.currentScale = currentScale;
    }

    public Float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(Float maxScale) {
        this.maxScale = maxScale;
    }

    public Float getMinScale() {
        return minScale;
    }

    public void setMinScale(Float minScale) {
        this.minScale = minScale;
    }

    public Vector3 getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(Vector3 currentScale) {
        this.currentScale = currentScale;
    }
}
