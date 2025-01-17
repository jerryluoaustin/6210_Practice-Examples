/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Concept: TensorFlow Object Detection", group = "Concept")
@Disabled
public class ConceptTensorFlowObjectDetection extends LinearOpMode {
    public static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    public static final String STONE = "Stone";
    public static final String SKYSTONE = "Skystone";

    //jerry test

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    public static final String VUFORIA_KEY = "AQt2xVL/////AAABmXIVKUnTcEJbqvVBjp/Sw/9SqarohYyKotzRjT/Xl1/S8KDwsFHv/zYw6rXqXTjKrnjk92GfBA4hbZaQP17d1N6BiBuXO2W/hFNoMGxiF+fWlnvtDmUM1H/MF9faMOjZcPNjnQ7X8DVwdDDha3A3aqaoegefkKxb4A5EjP8Xcb0EPJ1JA4RwhUOutLbCDJNKUq6nCi+cvPqShvlYTvXoROcOGWSIrPxMEiOHemCyuny7tJHUyEg2FTd2upiQygKAeD+LN3P3cT02aK6AJbQ0DlQccxAtoo1+b//H6/eGro2s0fjxA2dH3AaoHB7qkb2K0Vl7ReFEwX7wmqJleamNUG+OZu7K3Zm68mPudzNuhAWQ";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    public VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    public TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    detectSkystone();
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        //tfodParameters.minimumConfidence = 0.8;
        tfodParameters.minimumConfidence = 0.6;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, STONE, SKYSTONE);
        tfod.setClippingMargins(400,0,0,0);
    }

    public void detectSkystone(){
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();
        int pos = 0;
        while(opModeIsActive() && tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                int stoneX = -1;
                if(updatedRecognitions.size() == 1 && updatedRecognitions.get(0).getLabel() == STONE){
                    stoneX = (int) updatedRecognitions.get(0).getLeft();

                    if(stoneX < 440){
                        pos = 1;
                    }else if(stoneX > 440){
                        pos = 0;
                    }
                }else if (updatedRecognitions.size() == 2 && updatedRecognitions.get(0).getLabel() == STONE && updatedRecognitions.get(1).getLabel() == STONE){
                    pos = -1;
                }

                telemetry.addData("stone x:", stoneX);
                telemetry.addData("skystone pos ", pos);
                telemetry.update();


                /*if (updatedRecognitions.size() > 0) { //IF DETECT BOTH OBJECTS

                    int stone1X = -1, stone2X = -1;

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(STONE)) { //IF OBJECT DETECTED IS GOLD
                            stone1X = (int) recognition.getLeft();
                            skystoneConf = recognition.getConfidence();
                            //stoneWidth = recognition.getWidth();
                        } else if (recognition.getLabel().equals(LABEL_FIRST_ELEMENT) && stone1X != -1) {
                            stone1X = (int) recognition.getLeft();
                        } else {
                            stone2X = (int) recognition.getLeft();
                        }
                    }

                    //Adjust based on if it can see 3 stones or 2 stones
                    //Ask galligher how to get the y-value of the object, but could also use
                    //ratio of block height to frame height

                    //(*^*)\\ <-- A CHICK!

                    if (skystoneX != -1 && skystoneConf > 0.2) { //adjust confidence level

                        if (skystoneX < 600 || (skystoneX < stone1X && skystoneX < stone2X)) { //adjust threshold3
                            telemetry.addData("Skystone Position", "Left");
                            pos = 1;
                        } else{
                            telemetry.addData("Skystone Position", "Center");
                            pos = 2;
                        }
                    }else{
                        telemetry.addData("Skystone Position", "Right");
                        pos = 3;
                    }
                    telemetry.addData("skystone x pos ", skystoneX);
                    telemetry.addData("stone1 x pos ", stone1X);
                    telemetry.addData("stone2 x pos ", stone2X);
                    telemetry.addData("skystone conf ", skystoneConf);
                    telemetry.addData("Runtime", runtime.milliseconds());
                    telemetry.update();
                }*/
            }
        }
    }
}
