package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveCommand extends Command {

	private Joystick controller;
//	private boolean gyroEnable;
//	private boolean prevState;
	
	public ArcadeDriveCommand(){
		requires(Robot.drive);
		controller = Robot.oi.driveController;
//		gyroEnable = false;
//		prevState = false;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		double x, y, z;
//		boolean currState = controller.getTrigger();
//		if( currState && !prevState){
//			gyroEnable = !gyroEnable;
//			Robot.oi.gyro.reset();
//			prevState = true;
//		} else if(!currState && prevState){
//			prevState = false;
//		}
//		SmartDashboard.putBoolean("Gyro Enabled Telop", gyroEnable);
		z = (3-controller.getZ())/2;
		y = -controller.getY()/z;
//		double angle = Robot.oi.gyro.getAngle();
//		if(!gyroEnable){
			 x = -controller.getX()/z; 
//		}else{
//			if(angle > 0.1) //veering to the right
//			{
//				System.out.println("Go left!");
//				x = .2; //go left
//			}
//			else if(angle < -0.1) //veering to the left
//			{
//				System.out.println("Go right!");
//				x = -0.2;//go right
//			}
//			else
//			{
//				System.out.println("Go straight!");
//				x = 0;
//			}
//		}
		
//		if (controller.getRawButton(RobotMap.BUTTON_3)) {
//			Robot.drive.setBrakeMode(true);
//		}
//		if (controller.getRawButton(RobotMap.BUTTON_4)) {
//			Robot.drive.setBrakeMode(false);
//		}
		
		Robot.drive.setArcadeSpeed(x, y);
//		SmartDashboard.putNumber("Gyro Angle Teleop", angle);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
