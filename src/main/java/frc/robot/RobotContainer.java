
package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.DriveSubsystem;


 //Creates the robot's subsystems, controllers, and commands
public class RobotContainer {

    // create the drivetrain subsystem.
    private final DriveSubsystem driveSubsystem =
        new DriveSubsystem();

    // Create the PS5 controller on USB port 0
    private final CommandPS5Controller driverController =
        new CommandPS5Controller(
            OperatorConstants.kDriverControllerPort
        );

    // creates the robot container
    public RobotContainer() {
        configureBindings();
    }

    //connects driver controls to robot commands
    private void configureBindings() {

        // Run the driving command whenever the drivetrain subssytem is not being used by another command
        driveSubsystem.setDefaultCommand(
            new DriveCommand(
                driveSubsystem,
                driverController
            )
        );
    }
//ony need the teleop command so no need for autonomous command rn
   public Command getAutonomousCommand() {
       return null;
    }
}
