package org.firstinspires.ftc.teamcode.Autonomous.X_Other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.CameraDevice;


import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@Autonomous(name="bread and one banana", group = "auto")

@Disabled
public class Backup1b extends SkystoneLinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        boolean red = true;

        if (gamepad1.b) red = false;

        telemetry.addData("red? ", red);

        resetArm();
        resetEncoders();

        waitForStart();

        // scan for skystone
        // variables based on:
        //      - distance needed to move to be in front of skystone
        //      - distance to get to other side of field
        // no need to set a variable for distance traveled to skystone 2 because skystones are paired up
        // and thus a set distance apart
        // will be different for red and blue side so set inside if else loop
        int stonePos = 0;
        int seeyouontheotherside = 0;
        int heading = 0; // << we only turn one way ever but the angle depends on alliance side
        boolean parkRight;

        // +-+-+-+-+-+-+-+-+-+- NOTE - find diff in inches between each pair of skystones

        if (red){
            heading = 90;

            // if stone is on left
            stonePos = 0;
            seeyouontheotherside = 0;
            parkRight = false;
        }
        else {
            heading = 270;

            // if stone is on left
            stonePos = 0;
            seeyouontheotherside = 0;
            parkRight = true;
        }

        // +-+-+- first skystone
        strafeDistance(0.5, 6, true);       // strafe to in front of skystone
        driveDistance(0.8, 15);                 // drive to skystone
        setArmPosition(350);                   //Deploy arm
        sleep(1000);
        setClawPosition(false);                 //Close claw
        sleep(500);
        setArmPosition(325);                  //Lift arm
        driveDistance(-0.5, 5);                 // back up
        turnPIDV(heading, 0, 0, 0, false);
        driveDistance(0.5, seeyouontheotherside);       // drive to other side
        setClawPosition(true);

        // +-+-+- other skystone

        seeyouontheotherside += 10;

        driveDistance(-0.5, seeyouontheotherside);      // back up to other skystone
        turnPIDV(0, 0, 0, 0, false);    // turn to skystone
        // intake
        driveDistance(-0.5, 5);                 // back up
        turnPIDV(heading, 0, 0, 0, false);
        driveDistance(0.5, seeyouontheotherside);       // drive to other side
        // drop stone

        // park
        driveDistance(-0.5, 10);
        strafeDistance(0.8,25, parkRight);
    }
}
