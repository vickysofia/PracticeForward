
package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.REVLibError;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DriveConstants;


 //for the drivetrain
 //for motor tan drivetrain
 //a leader on each side and a follower (mirrors te leader)
public class DriveSubsystem extends SubsystemBase {

    private SparkMax leftLeader;
    private SparkMax leftFollower;
    private SparkMax rightLeader;
    private SparkMax rightFollower;

    // false until all four controllers are configured
    private boolean motorsReady = false;

    private double leftOutput = 0.0;
    private double rightOutput = 0.0;

    public DriveSubsystem() {

      //stops robot from initialilze controls before the hardware control is enabled
      //manually change hardware_enabled in constants class
        if (!DriveConstants.HARDWARE_ENABLED) {
            DriverStation.reportWarning(
                "Drive hardware disabled: simulation only.",
                false
            );
            return;
        }

        // checking if CAN IDs are properly intialzed
        //refses the ids if they are not actual ids (ex: 1)
        int[] ids = {
            DriveConstants.LEFT_LEADER_ID,
            DriveConstants.LEFT_FOLLOWER_ID,
            DriveConstants.RIGHT_LEADER_ID,
            DriveConstants.RIGHT_FOLLOWER_ID
        };

        for (int i = 0; i < ids.length; i++) {
            if (ids[i] < 1 || ids[i] > 62) {
                DriverStation.reportError(
                    "Invalid drivetrain CAN ID: " + ids[i],
                    false
                );
                return;
            }

            for (int j = 0; j < i; j++) {
                if (ids[i] == ids[j]) {
                    DriverStation.reportError(
                        "Duplicate drivetrain CAN ID: " + ids[i],
                        false
                    );
                    return;
                }
            }
        }

        // intiialize the motors w/ correspondng CAN Id's
      
        leftLeader = new SparkMax(
            DriveConstants.LEFT_LEADER_ID,
            MotorType.kBrushless //NEO brushles motor type
        );

        leftFollower = new SparkMax(
            DriveConstants.LEFT_FOLLOWER_ID,
            MotorType.kBrushless
        );

        rightLeader = new SparkMax(
            DriveConstants.RIGHT_LEADER_ID,
            MotorType.kBrushless
        );

        rightFollower = new SparkMax(
            DriveConstants.RIGHT_FOLLOWER_ID,
            MotorType.kBrushless
        );

        // All configurations must succeed.
        motorsReady = configureMotors();

        if (!motorsReady) {
            stop();
            DriverStation.reportError(
                "Drive initialization failed. Driving disabled.",
                false
            );
        }
    }
  
    private boolean configureMotors() {

      //defining CONFIGURation of LEADERS:
      //configure new SparkMaxConfig object
        SparkMaxConfig leftLeaderConfig = new SparkMaxConfig();

      //method chaining
        leftLeaderConfig
            .inverted(DriveConstants.LEFT_INVERTED)
            .idleMode(IdleMode.kBrake) //resists motor rotation when output is zero
            .smartCurrentLimit(
                DriveConstants.CURRENT_LIMIT_AMPS //change in constants
            );

        SparkMaxConfig rightLeaderConfig =
            new SparkMaxConfig();

        rightLeaderConfig
            .inverted(DriveConstants.RIGHT_INVERTED)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(
                DriveConstants.CURRENT_LIMIT_AMPS
            );

            //CONFIGURE FOLLOWERS

        SparkMaxConfig leftFollowerConfig =
            new SparkMaxConfig();

        leftFollowerConfig
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(
                DriveConstants.CURRENT_LIMIT_AMPS
            )
            .follow( //folows the leader
                DriveConstants.LEFT_LEADER_ID,
                DriveConstants.LEFT_FOLLOWER_INVERTED
            );

        SparkMaxConfig rightFollowerConfig =
            new SparkMaxConfig();

        rightFollowerConfig
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(
                DriveConstants.CURRENT_LIMIT_AMPS
            )
            .follow(
                DriveConstants.RIGHT_LEADER_ID,
                DriveConstants.RIGHT_FOLLOWER_INVERTED
            );

        // Applying configuration
        boolean leftLeaderOk = checkConfiguration( //method on line 208
            "Left leader",
            leftLeader.configure(
                leftLeaderConfig,
                ResetMode.kResetSafeParameters, //resets controllers safe configurable parambers before applying new settings
                PersistMode.kNoPersistParameters
            )
        );

        boolean rightLeaderOk = checkConfiguration(
            "Right leader",
            rightLeader.configure(
                rightLeaderConfig,
                ResetMode.kResetSafeParameters,
                PersistMode.kNoPersistParameters //doesnt permannantly save them
            )
        );

        boolean leftFollowerOk = checkConfiguration(
            "Left follower",
            leftFollower.configure(
                leftFollowerConfig,
                ResetMode.kResetSafeParameters,
                PersistMode.kNoPersistParameters
            )
        );

        boolean rightFollowerOk = checkConfiguration(
            "Right follower",
            rightFollower.configure(
                rightFollowerConfig,
                ResetMode.kResetSafeParameters,
                PersistMode.kNoPersistParameters
            )
        );

        return leftLeaderOk
            && rightLeaderOk
            && leftFollowerOk
            && rightFollowerOk;
    }

   
    //reutrns true for when configuraton succeeds
    private boolean checkConfiguration(
        String motorName,
        REVLibError result
    ) {

        if (result == REVLibError.kOk) {
            return true;
        }

        DriverStation.reportError(
            motorName + " configuration failed: " + result,
            false
        );

        return false;
    }

    //controls each side of the drivetrain
    //w/ speed limits by MAX_DRIVE_OUTPUT
    //caleed in DriveCommand execute method
    public void tankDrive(
        double leftSpeed,
        double rightSpeed
    ) {

        leftOutput = MathUtil.clamp(
            leftSpeed, -1.0, 1.0
        ) * DriveConstants.MAX_DRIVE_OUTPUT;

        rightOutput = MathUtil.clamp(
            rightSpeed, -1.0, 1.0
        ) * DriveConstants.MAX_DRIVE_OUTPUT;

        if (DriveConstants.HARDWARE_ENABLED) {

            if (!motorsReady) {
                stop();
                return;
            }

            leftLeader.set(leftOutput);
            rightLeader.set(rightOutput);
        }
    }

    
    //stopping (zero output on both sides)
    public void stop() {

        leftOutput = 0.0;
        rightOutput = 0.0;

        if (leftLeader != null) {
            leftLeader.stopMotor();
        }

        if (rightLeader != null) {
            rightLeader.stopMotor();
        }
    }

    public double getLeftOutput() {
        return leftOutput;
    }

    public double getRightOutput() {
        return rightOutput;
    }

    public boolean areMotorsReady() {
        return motorsReady;
    }

    @Override
    public void periodic() {

        SmartDashboard.putNumber(
            "Drive/Left Output",
            leftOutput
        );

        SmartDashboard.putNumber(
            "Drive/Right Output",
            rightOutput
        );

        SmartDashboard.putBoolean(
            "Drive/Motors Ready",
            motorsReady
        );

        SmartDashboard.putBoolean(
            "Drive/Hardware Enabled",
            DriveConstants.HARDWARE_ENABLED
        );
    }
}
