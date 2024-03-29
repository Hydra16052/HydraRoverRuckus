package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

public class Wheels {

    /*Welcome, this is the team 16052(Hydra) code for the normals wheels locomotion system, first applied in the
     *Rover Ruckus season.
     *First of all, we declare 2 attributes of type Dc Motor, corresponding to the motors used on the robot locomotion.*/

    public DcMotor rightWheel, leftWheel;
    public EncoderConverter encoderConverter;

    Wheels(DcMotor leftWheel, DcMotor rightWheel, float wheelDiameter, float gearRatio, float distanceBetweenWheel) {
        this.rightWheel = rightWheel;
        this.rightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.leftWheel = leftWheel;
        this.leftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.encoderConverter = new EncoderConverter(wheelDiameter, gearRatio, distanceBetweenWheel);
    }

    /*Here we create three methods that we'll use repetitively in the methods for locomotion. The description of each
     *one is within the same.*/

    public void resetMotorAndEncoder() {
        // This method will stop all the locomotion motors and reset their encoders
        setMotorsPower(0, 0);
        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void setMotorsPower(double leftPower, double rightPower) {
        // This method is responsible to energize the motors
        leftPower = Range.clip(leftPower,-1,1);
        rightPower = Range.clip(rightPower, -1, 1);

        leftWheel.setPower(leftPower);
        rightWheel.setPower(rightPower);
    }
    public void waitEncoderCount(float encoderCount) {
        /* This method is responsible to wait the encoder be equals to value required.
         *The encoder parameter (Count encoder) can be positive or negative, so we use the absolute value of the
         *supplied parameter and the encoder of the motors to limit the amount of rotation we want. We use the empty while
         *structure to serve as a hold structure */
        while (Math.abs(leftWheel.getCurrentPosition()) < Math.abs(encoderCount) ||
                Math.abs(rightWheel.getCurrentPosition()) < Math.abs(encoderCount)) {
        }
    }

    /* We will use a pattern for the methods, where we will create a method to call in the configuration proposed by the
     * programmer (in the order: resetMotorAndEncoder (), setMotorsPower ()), and another similar method that will use the
     * encoder (in order: standardEntry (), waitEncoderCount , resetMotorAndEncoder ())
     * */

    /* walkOnBy() and walkCount() are responsible for the robot movements.
     *
     * COMMANDS:
     * standard = moves forward and backward
     * spin = rotates on its central axis
     * spinSideLeft & spinSideRight = rotates only one side of robot
     * */

    public void walkOnBy(double power, String walkType ) {
        resetMotorAndEncoder();
        switch(walkType) {
            case "standard":
                setMotorsPower(power, power);
                break;
            case "spin":
                setMotorsPower(power,-power);
                break;
            case "spinSideLeft":
                setMotorsPower(power, 0);
                break;
            case "spinSideRight":
                setMotorsPower(0, power);
                break;
        }
    }

    public void walkCount (double power, float encoderCount, String walkType ){
        walkOnBy(power, walkType);
        switch(walkType) {
            case "standard":
                waitEncoderCount(encoderConverter.centimeterLinear(encoderCount));
                break;
            case "spin":
                waitEncoderCount(encoderConverter.centimeterAngular(encoderCount));
                break;
            case "spinSideLeft":
            case "spinSideRight":
                waitEncoderCount(encoderConverter.centimeterAngularEx(encoderCount));
                break;
        }
        resetMotorAndEncoder();
    }

    public void braking(double power) {
        leftWheel.setPower(-power);
        rightWheel.setPower(-power);
        ElapsedTime recognitionTimer = new ElapsedTime();
        recognitionTimer.reset();
        while (recognitionTimer.time() < 0.01) {}
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }

}