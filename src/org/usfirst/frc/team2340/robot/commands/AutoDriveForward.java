package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	boolean rDone, lDone, rotationComplete, inMotion;
	double desiredSpot = 0;

	public AutoDriveForward() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		rotationComplete= false;
		lDone = rDone = false;
		inMotion = false;
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(88));
		Robot.oi.left.set(desiredSpot);
		Robot.oi.right.set(-desiredSpot);
	}
	@Override
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		long elapsed = (System.currentTimeMillis() - startTime)/1000;

		SmartDashboard.putNumber("left position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getPosition()); 

		SmartDashboard.putNumber("Auto Elapsed", elapsed);
		SmartDashboard.putNumber("Gyro angle", angle);
		if (!lDone || !rDone) {
			if(Robot.oi.right.getPosition()<=-desiredSpot){
				Robot.oi.right.setPosition(0);
				Robot.oi.right.set(0);
				rDone = true;
			}
			if(Robot.oi.left.getPosition()>=desiredSpot){
				Robot.oi.left.setPosition(0);
				Robot.oi.left.set(0);
				lDone = true;
			}
		}
//		if(rotationComplete && !inMotion){
//			Robot.drive.setForPosition();
//			desiredSpot = RobotUtils.getEncPositionFromIN(Robot.drive.finalDistance- 0);
//			Robot.oi.left.set(desiredSpot);
//			Robot.oi.right.set(-desiredSpot);
//			inMotion = true;
//		}

		if(rDone && lDone && !rotationComplete){
			Robot.drive.setForSpeed();
			rotationComplete= adjustRotation();
			if(!rotationComplete)
			{
				System.out.println("Adjusting...");
			}
		}
	} 

	private boolean adjustRotation()
	{
		//CENTER IS 320
		if ( Robot.drive.centerX != -1 ) {
			if(Robot.drive.centerX > 340) { //+rotate: go right
				System.out.println("Adjusting 330 " + Robot.drive.centerX);
				rotateRight(40);
			}
			else if(Robot.drive.centerX < 310){ //-rotate: go left
				System.out.println("Adjusting 310 "  + Robot.drive.centerX);
				rotateLeft(40);
			}
			else{
				System.out.println("Good Enough!");
				setSpeed(0);
				return true;
			}
		} else {
			setSpeed(0);
			System.out.println("Nothing detected");
		}

		return false;
	}
	void setSpeed(double rpm) {
		Robot.oi.left.set(+rpm);
		Robot.oi.right.set(-rpm);
	}
	void rotateRight(double rpm) {
		Robot.oi.left.set(rpm);
		Robot.oi.right.set(rpm);
	}
	void rotateLeft(double rpm) {
		Robot.oi.left.set(-rpm);
		Robot.oi.right.set(-rpm);
	}

	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() -startTime >= 15000; 
	}

	@Override
	protected void end() {
		Robot.drive.setForVBus();
	}

	@Override
	protected void interrupted() {
	}
}
