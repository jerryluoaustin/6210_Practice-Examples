package org.firstinspires.ftc.teamcode.Autonomous.AML1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.CameraDevice;


import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@Autonomous(name="bread and one apple", group = "auto")

@Disabled
public class Backup1a extends SkystoneLinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        //sets up imu and vuforia
        init(hardwareMap, true);

        boolean red = true;

        //should we keep this button pressing system? is it legal?
        while (!isStarted() && opModeIsActive()) {

            if (gamepad1.b) red = false;

            telemetry.addData("red? ", red);
            telemetry.update();
        }

        //resetArm();

        waitForStart();

        // scan for skystone
        // variables based on:
        //      - distance needed to move to be in front of skystone
        //      - distance to get to other side of field
        // no need to set a variable for distance traveled to skystone 2 because skystones are paired up
        // and thus a set distance apart << measured
        // will be different for red and blue side so set inside if else loop
        int stonePos = 0;
        //int whereStone = detectSkystone(getBitmap(1000));
        int seeyouontheotherside = 0;
        int heading = 0; // << we only turn one way ever but the angle depends on alliance side

        // +-+-+-+-+-+-+-+-+-+- NOTE - find diff in inches between each pair of skystones


        if (red){
            heading = -90;

            // if stone is on left
            stonePos = 0;
            seeyouontheotherside = 0;
        }
        else {
            heading = 90;

            // if stone is on left
            stonePos = 0;
            seeyouontheotherside = 0;
        }

        // +-+-+- first skystone

        setArmPosition(-600);

        //open = -610
        //lifted -550

        driveDistance(0.5, 38);
        //setClawPosition(false);

        sleep(250);

        driveDistance(-0.3, 14);
        setArmPosition(-603);

        sleep(250);

        //turnPID(-90, 0.75, 0, 2, 3500);
        turnPID(heading, 0.6, 0, 2, 3500);

        sleep(250);

        driveDistance(0.5, 40);
        //setClawPosition(true);

        sleep(250);

        turnPID(heading, 0.6, 0, 0, 3500);

        sleep(250);

        driveDistance(-0.5, 33);
        setArmPosition(-600);

        sleep(250);

        turnPID(0, 0.6, 0, 0, 3500);

        sleep(250);

        driveDistance(0.3, 9);
        //setClawPosition(false);

        sleep(250);

        setArmPosition(-550);
        driveDistance(-0.3, 9);

        sleep(250);

        turnPID(heading, 0.6, 0, 2, 3500);

        sleep(250);

        driveDistance(0.5, 33);
        //setClawPosition(true);
    }

}
