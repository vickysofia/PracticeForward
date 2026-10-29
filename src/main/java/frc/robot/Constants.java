
package frc.robot;

public final class Constants {
//so that settings can be easily interchangable
    private Constants() {} 

    public static final class DriveConstants { //nested clas specifically for drive constants (morot ids, 
      //motor inversion, current li its, max drive output)

        private DriveConstants() {}

        // false until hardware congfigeration is figured out
        //gotta figure out the CAN ids and which motors are inverted
        //flase until all the details are sorted out and can connect to robot
        public static final boolean HARDWARE_ENABLED = false;

        // PLACEHOLDER CAN IDs (replace w the actual ones)
        public static final int LEFT_LEADER_ID = 1;
        public static final int LEFT_FOLLOWER_ID = 2;

        public static final int RIGHT_LEADER_ID = 3;
        public static final int RIGHT_FOLLOWER_ID = 4;

        // Leader direction  (which are inverted)
        public static final boolean LEFT_INVERTED = false;
        public static final boolean RIGHT_INVERTED = false;

        // Follower direction relative to leader
        public static final boolean LEFT_FOLLOWER_INVERTED = false;
        public static final boolean RIGHT_FOLLOWER_INVERTED = false;

        // initail power limit
        //SparkMax.set() only accepts values from -1 (max reverse) to 1 (max forward)

        public static final double MAX_DRIVE_OUTPUT = 0.50;


        // curent limit in amps (ampere)
        // (how much electrical current the sparkmax controllers are allowed 
        //suppy the NEO motors)
        //current limit reduces the risk of  overheating

        public static final int CURRENT_LIMIT_AMPS = 30;
    }

    public static final class OperatorConstants {
      //constants speciicaly for controller (controller port and joystick deadband)

        private OperatorConstants() {}

        // PS5 controller USB port.
        public static final int kDriverControllerPort = 0;

        // Ignore small joystick movements in teh center
        public static final double JOYSTICK_DEADBAND = 0.05;
    }
}
