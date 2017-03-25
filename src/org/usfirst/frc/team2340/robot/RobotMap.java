package org.usfirst.frc.team2340.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
//magic numbers~~
//	public static final int BUTTON_1 = 1;
	public static final int BUTTON_2 = 2; 
//	public static final int BUTTON_3 = 3;
//	public static final int BUTTON_4 = 4;
	public static final int BUTTON_5 = 5;
	public static final int BUTTON_6 = 6;
	public static final int BUTTON_7 = 7;
/*Controller Ports*/
	public static final int DRIVE_PORT = 1;
	public static final int ACQUISITION_PORT = 2;
	
/*TALON IDs*/
	//Wheel Ids
	public static final int RIGHT_TAL_ID = 3; //Right Side 3
	public static final int LEFT_TAL_ID = 4;  //Left Side 4
	//Acquisition Ids
	public static final int BALL_AQ_TAL_ID = 5; // Ball Acquisition is 5
	public static final int CLIMBING_TAL_ID = 6; //Climbing is 6
	public static final int BALL_FEEDER_TAL_ID = 7; //Ball Feeder is 7
	public static final int BALL_SHOOTER_TAL_ID = 8; //Ball Shooter is 8
	public static final int GEAR_AQ_TAL_ID = 9; //expand or retract gear holder window motor is 9
	public static boolean TAKE_PIC = false;
}
	