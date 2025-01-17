package org.firstinspires.ftc.teamcode.Autonomous.X_Other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@Autonomous(name="Old Red Skystone", group = "auto") // RED SIDE DOUBLE
@Disabled
public class OldSkystoneRed extends SkystoneLinearOpMode{

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

        turnPID(-90, 0.6/180,0.0001,0.5,2000);

        driveAdjust(270,0.4,62, 7); //GO TO STONES

        grabStone(pos,false); //GRAB SKYSTONE

        driveAdjust(270,-0.8,9, 7); //MOVE BACKWARD

        turnPID(180, 0.6/180,0.0001,0.5,2000);

        if (pos == 1){
            driveAdjust(180, 0.8, longAdjust, 4); // MOVE OTHER SIDE
        } else {
            driveAdjust(180, 0.8, longAdjust, 4); // MOVE OTHER SIDE
        }

        hook(false, false); // drop stone

        turnPID(180, 0.6/180,0.0001,0.5,2000); // autocorrect angle to account for stone friction

        // +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+ second stone

        // MOVE BACK TO STONE SIDE
        /*switch (pos) {
            case -1:
                driveAdjust(180, -0.8, longAdjust + 35, 3);
                break;
            case 0:
                driveAdjust(180, -0.8, longAdjust + 45, 3);
                break;
            case 1:
                driveAdjust(180, -0.8, longAdjust + 43, 3);
                break;
        }*/

        driveAdjust(180, -0.6, 175, 3); //all the way back

        switch (pos) {
            case -1:
                driveAdjust(180, 0.6, 5, 2);
                break;
            case 0:
                driveAdjust(180, 0.6, 20.5, 2);
                break;
            case 1:
                driveAdjust(180,0.6,12, 1);
                break;
        }


        //turnPID(-90, 0.6/180,0.0001,0.5,2000);

        if (pos == -1) strafeAdjust(0.4,5,270,false); // strafe


         driveAdjust(270, 0.4, 23, 5); //GO TO STONES

        grabStone(pos, false);

        //MOVE BACKWARD
        if (pos == 1)
            driveAdjust(270,-0.6,13, 7);
        else
            driveAdjust(270,-0.6,15, 7);

        // MOVE TO OTHER SIDE

        turnPID(180, 0.6/180,0.0001,0.5,2000);


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

        strafeAdjust(.6,10,180,false);



        telemetry.addData("auto:", "complete");
        telemetry.update();
    }
}
