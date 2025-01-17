package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="teleop")
//@Disabled
public class RevisedTeleOp extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);
        int lowestArm = 0;
        double xAxis = 0, yAxis = 0, zAxis = 0, lStrafePower = 0, rStrafePower = 0, currHeading = 0, tHeading = 0, correction = 0, zeroAng = 0;
        double armPower = 0, liftPower = 0;
        boolean foundation = false, rHook = false, lHook = false, robotOriented = true, correctionBool = true, rFang = false, lFang = false;
        double deployTime = 0, min = 0, max = 0, pow = 0;
        double[] motorP = new double[4];


        //For more controlled movement when moving the foundation
        boolean halfSpeed = false, goArm = false, actuallyGo = false, changeMode = true;
        resetEncoders();
        resetArm();

        //Set up a timer for half speed
        ElapsedTime time = new ElapsedTime();
        resetTime();

        telemetry.addData("Mode: ", "Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

            /**
             * GAMEPAD 1
             *
             * Controls:
             * Right Joystick - Robot Movement (Arcade Drive)
             * Left Joystick - Robot Orientation
             * Right Trigger - Right Strafe , Left Trigger - Left Strafe
             * Right Dpad - Right Hook, Left Dpad - Left Hook
             * Right Bumper - Right Grabber , Left Bumper - Left Grabber
             * X - Half Speed , B - Foundation Hooks
             * Y - Switch Drive Modes (Robot Oriented or Field Oriented)
             * A - Reset Gyro
             *
             *
             */
            currHeading = get180Yaw();

            if (correctionBool)
                tHeading = get180Yaw();

            //RESET GYRO
            if (gamepad1.a) {
                zeroAng = getYaw();
                zeroAng += 180;
                if (zeroAng > 360) zeroAng -= 360;
            }

            //HALFSPEED TOGGLE
            if (ifPressed(gamepad1.x)) halfSpeed = !halfSpeed;

            //DRIVE MODE TOGGLE
            if (ifPressed(gamepad1.y)) robotOriented = !robotOriented;

            //CONTROLLER INPUTS
            yAxis = gamepad1.left_stick_y;
            xAxis = -gamepad1.left_stick_x;
            zAxis = -gamepad1.right_stick_x;
            if(halfSpeed) zAxis *= .75;
            lStrafePower = gamepad1.left_trigger * 0.75;
            rStrafePower = gamepad1.right_trigger * 0.75;

            if (Math.abs(gamepad1.left_stick_x) > .05 ||
                    Math.abs(gamepad1.left_stick_y) > .05 ||
                    Math.abs(gamepad1.right_stick_x) > .05) {

                //ROBOT ORIENTED HOLONOMIC
                if (robotOriented)
                    motorP = holonomicDrive(xAxis, yAxis, zAxis, 0);
                //FIELD ORIENTED HOLONOMIC
                else
                    motorP = fieldOriented(xAxis, yAxis, zAxis, 0, zeroAng);

                //RIGHT AUTO STRAFE
            } else if (gamepad1.right_trigger > 0.05) {
                correctionBool = false;
                correction = getCorrection(currHeading, tHeading);
                motorP = strafeCorrection(rStrafePower, correction, true);

                //LEFT AUTO STRAFE
            } else if (gamepad1.left_trigger > 0.05) {
                correctionBool = false;
                correction = getCorrection(currHeading, tHeading);
                motorP = strafeCorrection(lStrafePower, correction, false);

                //AUTO TURNS
            } else if (gamepad1.dpad_down) {
                motorP = autoTurn(currHeading, true);

            } else if(gamepad1.dpad_up) {
                motorP = autoTurn(currHeading, false);
            }

            else {
                correctionBool = true;
                for(int i = 0; i < motorP.length; i++){ motorP[i] = 0.0;}
            }

            //SET ALL POWERS
            setEachPower(motorP[0], motorP[1], motorP[2], motorP[3], halfSpeed);

            //FOUNDATION GRABBER CONTROLS
            if (ifPressed(gamepad1.b)) {
                foundation = !foundation;
                foundationD(foundation);
            }

            //HOOK CONTROLS
            if (ifPressed(gamepad1.dpad_right)) {
                rHook = !rHook;
                hook(lHook, rHook);
            }
            if (ifPressed(gamepad1.dpad_left)) {
                lHook = !lHook;
                hook(lHook, rHook);
            }

            //SKYSTONE GRABBER CONTROLS
            if (ifPressed(gamepad1.right_bumper)) {
                rFang = !rFang;
                fang(lFang, rFang);
            }
            if (ifPressed(gamepad1.left_bumper)) {
                lFang = !lFang;
                fang(lFang, rFang);
            }

            /**
             * GAMEPAD 2
             *
             * Controls:
             * Left Trigger - Lift Up ,  Right Trigger - Lift Down
             * Right Bumper - Expel , Left Bumper - Intake
             * Y - Close Claw , X - Open Claw , A - Unstick Block
             * B - Arm Deploy , Right Joystick Y - Rotate Arm
             *
             */

            //Intake controls
            //intake
            if (gamepad2.right_bumper) {
                intakeL.setPower(-1);
                intakeR.setPower(-1);
            }
            //expel
            else if (gamepad2.left_bumper) {
                intakeL.setPower(1);
                intakeR.setPower(1);
            }
            //unstick block
            else if (gamepad2.a) {
                intakeL.setPower(1);
                intakeR.setPower(-1);
            }
            //idle
            else if(changeMode) {
                intakeL.setPower(0);
                intakeR.setPower(0);
            }

            //Arm Module Controls
            //Claw Controls
            if (ifPressed(gamepad2.x)) {
                if (claw.getPosition() == 0)
                    claw.setPosition(1);
                else
                    claw.setPosition(0);
            }

            //Arm Controls
            //Manual
            if (Math.abs(gamepad2.right_stick_y) > 0.05) {
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                min = -1;
                max = 1;
                pow = -gamepad2.right_stick_y;
                if (arm.getCurrentPosition() > lowestArm) //technically higher because its weird
                    lowestArm = arm.getCurrentPosition();
                if (gamepad2.right_stick_y > 0.05 && arm.getCurrentPosition() > 100)
                    max = 0.2;
                if (gamepad2.right_stick_y < -0.05 && arm.getCurrentPosition() < 120)
                    min = -0.2;
                armPower = Range.clip(pow, min, max);
                arm.setPower(armPower);
            } else if (changeMode) {
                arm.setPower(0);
            }
            //Automatic
            //Arm (automatic to manuel) Toggle
            if (gamepad2.b){ //go to up position or back inside the robot
                changeMode = false;
                deployTime = time.milliseconds(); //for compliant unstick later
                if (arm.getCurrentPosition() > -100)//if arm inside the robot
                    goArm = true;
                else //else it's outside
                    goArm = false;
            }

            //DEPLOY ARM
            if (goArm && !changeMode){ //if inside (goArm is true) and manuel movement is not being used (changeMode)
                if (deployTime > time.milliseconds() - 5000){ //expel block for 5 second
                    actuallyGo = true;
                    intakeL.setPower(1);
                    intakeR.setPower(-1);
                }else{
                    intakeL.setPower(0);
                    intakeR.setPower(0);
                }

                arm.setTargetPosition(lowestArm - 540); //-540 for up, -830 for horizontal
                arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                arm.setPower(1);

            }

            if (!goArm && !changeMode){ //if outside of robot (goArm is false)
                //move lift down
                lift.setTargetPosition(0);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                lift.setPower(1);

                if (lift.getCurrentPosition() > -50) {
                    arm.setTargetPosition(lowestArm);
                    arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    arm.setPower(1);
                }
            }

            //Lift Controls
            //Manual
            if (changeMode) {
                if (gamepad2.right_trigger > 0.05){
                    lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    liftPower = Range.clip(gamepad2.right_trigger, 0, 1);  //LIFT DOWN
                } else if (gamepad2.left_trigger > 0.05) {
                    liftPower = -Range.clip(gamepad2.left_trigger, 0, 1);  //LIFT UP
                } else {
                    liftPower = 0;
                }

                lift.setPower(liftPower);
            }
            //Automatic...
            //double y = blockHieght * x + first hieght (if not equal to blockHieght)
            /**if (gamepad2.dpad_up) {
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) + 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setPower(0.5);
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }
            if (gamepad2.dpad_down) {
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                double currPos = lift.getCurrentPosition();//get current position
                int tarPos = ((int)currPos/10) - 1; //find tens place and add 1 to it -----------------------------Fix values so it adjusts to the right increments (levels of skyscraper) in inches (encoders to inches)
                lift.setPower(0.5);
                lift.setTargetPosition(tarPos * 10); //Make lift go to position
            }

            //SAVE LIFT HEIGHT
            if (gamepad2.dpad_left)
            {
                liftHeight = lift.getCurrentPosition(); //saves the current height of lift
            }

            if (gamepad2.dpad_right) //Lift to certain position
            {
                goLift = true; //start lift
            }

            if(goLift)
            {
                lift.setTargetPosition(liftHeight - 1000); //Last recorded block height + one block up
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION); //set mode for target position
                lift.setPower(0.8);
                if(!lift.isBusy() || Math.abs(liftHeight) - Math.abs(lift.getCurrentPosition()) < 5 ){ //if not busy or lift height is close to target
                    liftHeight = lift.getCurrentPosition(); //save position for next time
                    lift.setPower(0);
                    goLift = false;
                }
            }**/

            if (gamepad2.right_trigger > 0.05
                    || gamepad2.left_trigger > 0.05
                    || Math.abs(gamepad2.right_stick_y) > 0.05
                    || gamepad2.right_bumper
                    || gamepad2.left_bumper
                    || gamepad2.a) //STOP MACRO STUFF
            {
                changeMode = true;
            }

            booleanIncrementer = 0;
            telemetry.addData("Drive Mode (Y): ", robotOriented ? "Robot Oriented" : "Field Oriented")
                     .addData("Halfspeed (X): ", halfSpeed)
                     .addData("claw pos", claw.getPosition());
            telemetry.update();
            }
        }
    }

