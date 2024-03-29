package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp  (name = "HydraTeleOpOficial")

public class HydraTeleOpOficial extends HydraTeleOp {

    int MAX_RANGE_COLLECT = 19000;
    int MAX_RANGE_DEPOSIT = 17000;

    @Override
    public void runOpMode() throws InterruptedException {
        /*Here we declare the devices of our robot (servos, motors and sensors)
         *Or the locomotion system with two traction engines (Wheels)
         */
        float wheelDiameter = 10.0f;
        float gearRatio = 1.0f;
        float distanceBetweenWheels = 35.0f;

        tesseract = new Robot(hardwareMap.get(DcMotor.class, "leftMotor"),
                hardwareMap.get(DcMotor.class, "rightMotor"),
                hardwareMap.get(Servo.class, "rightColectServo"),
                hardwareMap.get(Servo.class, "leftColectServo"),
                hardwareMap.get(DcMotor.class, "colectMotor"),
                hardwareMap.get(DcMotor.class, "landerMotor"),
                wheelDiameter, gearRatio, distanceBetweenWheels);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //runtime.reset();
        initMotors();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){

            //Locomotion movement system
            refreshEncoderPosition();
            moveRobot();

            //Arms movement system
            collectArmControls();
            depositArmsControls();

            defineLimitServos();

            setWheelsSpeed();
        }
        stop();
    }
    private void collectArmControls() {

        // This method controls the expansion and retraction system of the collection arm

        if (gamepad2.left_bumper == true || gamepad2.dpad_up == true){
            collectExpansion = 1.0f;

        }
        else if (gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
            collectExpansion = -1.0f;
        }
        else {
            collectExpansion = 0;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");
        telemetry.addData("EncoderCollect:", encoderCollectSlide);
        telemetry.update();
    }

    private void depositArmsControls() {

        // This method controls the expansion and retraction system of the deposit arm

        if (gamepad2.right_bumper == true || gamepad2.y == true){
            depositExpansion = 1.0f;
        }
        else if (gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
            depositExpansion = -1.0f;
        }
        else {
            depositExpansion = 0f;
        }

        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
        telemetry.addData("EncoderDeposit:", encoderDepositSlide);
        telemetry.addLine();
        telemetry.addData("EncoderDeposit:",encoderDepositSlide);
        telemetry.update();
    }
    /*private void collectArmControls() {

        // This method controls the expansion and retraction system of the collection arm

        if (encoderCollectSlide <= 0 || gamepad2.left_bumper == true || gamepad2.dpad_up == true){
            if (encoderCollectSlide > MAX_RANGE_COLLECT){
                collectExpansion = -1.0f;
            } else {
                collectExpansion = 1.0f;
            }
        }
        else if (encoderCollectSlide >= MAX_RANGE_COLLECT || gamepad2.left_trigger >= 0.3f || gamepad2.dpad_down == true){
            collectExpansion = -1.0f;
        }
        else {
            collectExpansion = 0;
        }
        tesseract.arms.moveOnBy(collectExpansion, "collect_slide");
        telemetry.addData("EncoderCollect:", encoderCollectSlide);
        telemetry.update();
    }

    private void depositArmsControls() {

        // This method controls the expansion and retraction system of the deposit arm

        if (encoderDepositSlide < 0 || gamepad2.right_bumper == true || gamepad2.y == true){
            if (encoderDepositSlide > MAX_RANGE_DEPOSIT){
                depositExpansion = -1.0f;
            } else {
                depositExpansion = 1.0f;
            }
        }
        else if (encoderDepositSlide > MAX_RANGE_DEPOSIT || gamepad2.right_trigger >= 0.3f || gamepad2.a == true){
            depositExpansion = -1.0f;
        }
        else {
            depositExpansion = 0;
        }
        tesseract.arms.moveOnBy(depositExpansion, "deposit_slide");
        telemetry.addData("EncoderDeposit:", encoderDepositSlide);
        telemetry.addLine();
        telemetry.addData("EncoderDeposit:",encoderDepositSlide);
        telemetry.update();
    }*/
}
