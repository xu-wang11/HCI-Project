package com.example.zyx.toolkit;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;

public class Orientation implements SensorEventListener {

    public interface Listener {
        void onOrientationChanged(float pitch, float roll, float yaw);
    }

    private static final int SENSOR_DELAY_MICROS = 50 * 1000; // 50ms

    private final WindowManager mWindowManager;

    private final SensorManager mSensorManager;

    private double _yaw;

    private double _pitch;

    private double _roll;

    @Nullable
    private final Sensor mRotationSensor;

    private int mLastAccuracy;
    private Listener mListener;

    public Orientation(Activity activity) {
        mWindowManager = activity.getWindow().getWindowManager();
        mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);

        // Can be null if the sensor hardware is not available
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public double GetPitch()
    {
        return _pitch;
    }

    public double GetYaw()
    {
        return _yaw;
    }

    public double GetRoll()
    {
        return _roll;
    }

    public void startListening(Listener listener) {
        if (mListener == listener) {
            return;
        }
        mListener = listener;
        if (mRotationSensor == null) {
            //LogUtil.w("Rotation vector sensor not available; will not provide orientation data.");
            return;
        }
        mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY_MICROS);
    }

    public void stopListening() {
        mSensorManager.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener == null) {
            return;
        }
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == mRotationSensor) {
            updateOrientation(event.values);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);
        /*
        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        // Remap the axes as if the device screen was the instrument panel,
        // and adjust the rotation matrix for the device orientation.
        switch (mWindowManager.getDefaultDisplay().getRotation()) {



            case Surface.ROTATION_90:
                worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_180:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
                break;
            case Surface.ROTATION_270:
                worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
                worldAxisForDeviceAxisY = SensorManager.AXIS_X;
                break;
            case Surface.ROTATION_0:
            default:
                worldAxisForDeviceAxisX = SensorManager.AXIS_X;
                worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
                break;

        }
        System.out.println(mWindowManager.getDefaultDisplay().getRotation());

        float[] adjustedRotationMatrix = new float[9];

        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transform rotation matrix into azimuth/pitch/roll
        */
        float[] orientation = new float[3];
        //SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        SensorManager.getOrientation(rotationMatrix, orientation);
        // Convert radians to degrees
        float pitch = (float)Math.toDegrees(orientation[1]);
        float roll = (float) Math.toDegrees(orientation[2]);
        float yaw = (float)Math.toDegrees(orientation[0]);
        _pitch = pitch;
        _roll = roll;
        _yaw = yaw;

        mListener.onOrientationChanged(pitch, roll, yaw);
    }
}
