package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@TeleOp(name="VuforiaTFtest", group = "auto")
@Disabled
public class VuforiaTFtest extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //init(hardwareMap, true);
        initVuforia();
        //initTensorFlow();

        // ^ SET UP DETECTOR

        telemetry.addData("All Initialization", "Complete");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            if(updateRobotPosition()){
                telemetry.addData("Robot X-Pos:" , getRobotX());
                telemetry.addData("Robot Y-Pos:" , getRobotY());
                telemetry.addData("Robot Angle:" , getRobotHeading());
            }

          //  telemetry.addData("Skystone Position (1 to 3)", detectSkystone(1000));
            telemetry.update();
        }
    }
}
