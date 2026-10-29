

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


//methods in Robot are called correspondingly to each mode
public class Robot extends TimedRobot {
  private Command m_autonomousCommand; //create command instand w/ name
  // like in the template given when made the code (m_ = member)



  private final RobotContainer m_robotContainer;

  public Robot() { //for when robot is started up &sed for any intialization

    // Instantiate our RobotContainer
    //perform button bindings (but we use the joysticks)
    m_robotContainer = new RobotContainer();
  }

 

   //runs every 20 ms always
   
  @Override
  public void robotPeriodic() {
    //runs Scheduler (system that manages when commands start, run, stop and ge interrupted)
    //runs commands and removing finished/interrupted commands and subsystem periodic() emthods
    //must be called from robotcs periodic block (robotPeriodic) in order for anything in the command framework to work
    CommandScheduler.getInstance().run();
  }

  //called once when the robot entered disabled mode
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  //IGNORE LINES 46-60 bc thres no autonomous command rn
  //runs autnomous command from ROboTConainter class
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // scheduls autonomous command but doesnt matter rn bc there is no autonomous command
    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  //called periodically during autonomous 
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
  
    //stops  autonomous runnng and strts teleop
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  //caled periodically during operator control
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode
    CommandScheduler.getInstance().cancelAll();
  }

  //called periodicaly during test mode
  @Override
  public void testPeriodic() {}

  //called when robot first started up
  @Override
  public void simulationInit() {}


  //called during simulation
  //onl rlly usefule for the WPILib: Simulate Robot Code
  @Override
  public void simulationPeriodic() {}
}
