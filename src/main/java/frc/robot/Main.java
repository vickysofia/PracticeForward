
package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
starts robot, creates robotContainer, creates controls and drive train
 */
public final class Main {
  private Main() {}

  public static void main(String... args) {
    RobotBase.startRobot(Robot::new); //starts WPILib robot machinery
  }
}
