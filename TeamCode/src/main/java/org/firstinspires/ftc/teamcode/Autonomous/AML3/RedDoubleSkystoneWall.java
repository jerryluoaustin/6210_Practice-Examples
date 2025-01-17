package org.firstinspires.ftc.teamcode.Autonomous.AML3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@Autonomous(name="Red DoubleSkystone Wall", group = "auto") // RED SIDE DOUBLE

@Disabled
public class RedDoubleSkystoneWall extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //sets up imu and vuforia
        init(hardwareMap, true);
        initBitmapVuforia();

        int pos = 0;
        double adjust = 0.0;
        double longAdjust = 0.0;

        waitForStart();

        pos = detectSkystoneOnePix(getBitmap(),false); //DETECT SKYSTONE

        adjustForSkystone(pos, false); //MOVE ROBOT FORWARD OR BACKWARD ALONG WALL TO LINE UP WITH SKYSTONE

        longAdjust = forLongAdjust(pos,false) + 95;

        strafeAdjust(0.4,2,0,true);

        turnPID(-90, 0.6/90,0.0001,2,2000);

        driveAdjust(270,0.4,62, 7); //GO TO STONES

        grabStone(pos,false); //GRAB SKYSTONE

        driveAdjust(270,-0.8,62, 7); //MOVE BACKWARD

        turnPID(180, 0.6/90,0.0001,2,2000);

        strafeAdjust(.6,3,180,true);

        if (pos == 1){
            driveAdjust(180, 0.8, longAdjust, 4); // MOVE OTHER SIDE
        } else {
            driveAdjust(180, 0.8, longAdjust, 4); // MOVE OTHER SIDE
        }

        foundationD(true); // drop stone

        turnPID(180, 0.6/90,0.0001,2,1500); // autocorrect angle to account for stone friction

        // +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+ second stone

        // MOVE BACK TO STONE SIDE
        switch (pos) {
            case -1:
                driveAdjust(180, -0.8, longAdjust + 35, 3);
                break;
            case 0:
                driveAdjust(180, -0.8, longAdjust + 45, 3);
                break;
            case 1:
                driveAdjust(180, -0.8, longAdjust + 43, 3);
                break;
        }
        //driveAdjust(180, -0.8, longAdjust + 31, 3);

        //sleep(250);
        //turnPID(-90, 0.5/360,0.001,2,4000);
        turnPID(-90, 0.6/90,0.0001,2,1500);

        if (pos == -1) strafeAdjust(0.4,5,270,false); // strafe

        driveAdjust(270, 0.4, 62, 5); //GO TO STONES

        grabStone(pos, false);

        //MOVE BACKWARD
        if (pos == 1)
            driveAdjust(270,-0.6,62, 7);
        else
            driveAdjust(270,-0.6,62, 7);

        turnPID(180, 0.6/90,0.0001,2,2000);

        strafeAdjust(.6,3,180,true);

        // MOVE TO OTHER SIDE
        switch (pos) {
            case -1:
                driveAdjust(180, 1, longAdjust + 53, 3000); // MOVE OTHER SIDE
                break;
            case 0:
                driveAdjust(180, 1, longAdjust + 52, 3000); // MOVE OTHER SIDE
                break;
            case 1:
                driveAdjust(180, 1, longAdjust + 60, 3000); // MOVE OTHER SIDE
                break;
        }

        hook(false, false);

        driveAdjust(180, -0.8, 30, 2); //park

        strafeAdjust(.6,10,180,true);

        telemetry.addData("auto:", "complete");
        telemetry.update();
    }
}
