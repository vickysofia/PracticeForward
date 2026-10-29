
package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.DriveSubsystem;



 //controls the tank drivetrain w/ teh ps5 controller
 // tank drivetrain: 
 //Left joystick  controls the left side
 //right joystick controlls the right side
public class DriveCommand extends Command {

    private final DriveSubsystem driveSubsystem;

    private final CommandPS5Controller controller;

   
     //new driving command
     //subsystem parameteer( the drivetrain to control)
     //ps5controlelr parameter (the drivers ps5 controller)
    public DriveCommand(
        DriveSubsystem subsystem,
        CommandPS5Controller ps5Controller
    ) {
        driveSubsystem = subsystem;
        controller = ps5Controller;

        //only one command can require this subsystem at a time
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() { // runs once each time the command starts
      //purpose: setup needed before the command begins excecuting
        driveSubsystem.stop(); //sts motor outputs to zero (makes motors start from a stopped stae before
        //reposnding to joystikc)
    }

    @Override
    public void execute() {

        // checks if controller is disconnecter
        //If the controller is disconnected request zero output
        if (!DriverStation.isJoystickConnected(
                OperatorConstants.kDriverControllerPort)) {
            driveSubsystem.stop();
            return;
        }

        // Joystick Y is negative when pushed forward
        // so its necessary to negate both readings
        double leftSpeed = -controller.getLeftY();
        double rightSpeed = -controller.getRightY();

        // Ignore small joystick movements (decreases sensitivity)
        leftSpeed = MathUtil.applyDeadband(
            leftSpeed,
            OperatorConstants.JOYSTICK_DEADBAND //the constant can be changed n constants
        );

        rightSpeed = MathUtil.applyDeadband(
            rightSpeed,
            OperatorConstants.JOYSTICK_DEADBAND
        );

        driveSubsystem.tankDrive(leftSpeed, rightSpeed); //calls the method from drivesubsystem
    }

    @Override
    // stops the command
    public void end(boolean interrupted) {
        driveSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        // Keep running until another command interrupts it
        return false;
    }
}
