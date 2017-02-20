package org.usfirst.frc.team2340.robot;

public class RobotUtils {
	public enum AutoMode {
		AUTODRIVEFORWARD,
		DISABLED,
		BLUEALLIANCEBOILERSIDE,
		BLUEALLIANCERETRIEVALZONE,
		REDALLIANCEBOILERSIDE,
		REDALLIANCERETRIEVALZONE
	}
	
	public enum AutonomousState {
		DriveForward,
		Rotate,
		CameraAssistRotate,
		CameraAssistDriveForward
	}
	
	private static double wheelDiameter = 1;
	private static double lengthOfRobot = 0;
	public static void lengthOfRobot(double _lengthOfRobot) {
		lengthOfRobot = _lengthOfRobot;
	}
	public static double getEncPositionFromIN(double distanceInInches) {
		return distanceInInches/(wheelDiameter * Math.PI);
	}
	public static double distanceMinusRobot(double distance){
		return distance-lengthOfRobot ;
	}

	public static void setWheelDiameter(double _wheelDiameter) {
		wheelDiameter = _wheelDiameter;
	}
}
