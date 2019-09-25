package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@Autonomous(name="Position 1a", group = "auto")  // PARKS AWAY FORM WALL

// -------------------NOTE: DIFF VALUES AND DIRECTIONS FOR BLUE AND RED SIDE SO MAKE AN IF ELSE STATEMENT
// THAT WILL RUN DIFFERENT CODE DEPENDING ON WHICH VUFORIA MARK IS IDENTIFIED ON THE WALL RIGHT NEXT TO ROBOT
// ------------ ASK MINDY FOR CLARIFICATION

//@Disabled
public class Position1a extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        // SET UP DETECTOR

        initVuforia();

        telemetry.addData("Mode", "setting up detector...");
        telemetry.update();

        telemetry.addData("detector", "enabled");
        telemetry.update();

        waitForStart();

        // scan for skystones
        StrafetoPosition(1.0, 50, 50, getRobotHeading());     // strafe to align with skystone
        driveToPoint(0.8, 50, 50);      // drive toward skystone
        setClawPosition(false);// pick up skystone --------------------(Don't know how Pranav has robot designed so I will adjust once I know how robot will work)
        driveToPoint(0.8, 50, 50);      // turns and drives toward other side of field
        setClawPosition(true);// drop skystone --------------------(Don't know how Pranav has robot designed so I will adjust once I know how robot will work)
        driveToPoint(0.8, 50, 50);      // turns? back around and drives to other skystone
                                                                // or drive backwards to other skystone
        turnPIDV(0, 0, 0, 0, false);   // turns to face other skystone
        setClawPosition(false);// pick up skystone --------------------(Don't know how Pranav has robot designed so I will adjust once I know how robot will work)
        driveToPoint(0.8, 50, 50);      // turns and drives toward other side of field
        setClawPosition(true);// drop skystone --------------------(Don't know how Pranav has robot designed so I will adjust once I know how robot will work)
        driveToPoint(0.5, 0, 0);        // parks on tape AWAY FROM WALL

        telemetry.addData("bread", "kneaded");
        telemetry.addData("mindy likes", "bread");
        telemetry.addData(":D", "!");
        telemetry.update();
    }
}
