package com.calculator.vault.gallery.locker.hide.data.smartkit.utils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class StrobeRunner implements Runnable {
    public static StrobeRunner getInstance() {
        return instance == null ? instance = new StrobeRunner() : instance;
    }

    private static StrobeRunner instance;


    public volatile boolean requestStop = false;
    public volatile boolean isRunning = false;
    public volatile double delayOn = 40;
    public volatile double delayOff = 40;
    public volatile Context controller;
    public volatile String errorMessage = "";

    @Override
    public void run() {
        if (isRunning)
            return;

        requestStop = false;
        isRunning = true;

        Camera cam = null;
        Camera.Parameters pon = null, poff = null;


        CameraManager cameraManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager = (CameraManager) controller.getSystemService(Context.CAMERA_SERVICE);
        } else {
            cam = Camera.open();
            pon = cam.getParameters();
            poff = cam.getParameters();

            pon.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            poff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        while (!requestStop) {
            try {
                if (delayOn > 0) {

                    String cameraId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            cameraId = cameraManager.getCameraIdList()[0];
                                cameraManager.setTorchMode(cameraId, true);
                        } catch (CameraAccessException e) {
                        }
                    }
                    else
                    {
                        cam.startPreview();
//                        Log.e("StrobeRunner-1","if flash on");
                        cam.setParameters(pon);
                    }
                    Thread.sleep(Math.round(delayOn));
                }

                if (delayOff > 0) {

                    String cameraId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            cameraId = cameraManager.getCameraIdList()[0];
                            cameraManager.setTorchMode(cameraId, false);
                        } catch (CameraAccessException e) {
                        }
                    }
                    else
                    {
//                        Log.e("StrobeRunner-2","if flash off");
                        cam.stopPreview();
                        cam.setParameters(poff);
                    }
                    Thread.sleep(Math.round(delayOff));
                }
            } catch (InterruptedException ex) {
            } catch (RuntimeException ex) {
                requestStop = true;
                errorMessage = "Error setting camera flash status. Your device may be unsupported.";
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

        }
        else
        {
            cam.setParameters(poff);
            cam.release();
        }

        isRunning = false;
        requestStop = false;
    }
}
