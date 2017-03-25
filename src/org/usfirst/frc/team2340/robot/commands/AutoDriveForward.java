package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	boolean rDone, lDone, rotationComplete, inMotion, finalBackupDone, nothingDetected;
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
		finalBackupDone = false;
		nothingDetected = false;
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(88));
		Robot.oi.left.set(desiredSpot);
		Robot.oi.right.set(-desiredSpot);
	}
	@Override
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		long elapsed = (System.currentTimeMillis() - startTime)/1000;
		long beginFinalApproachTime = 0;

		SmartDashboard.putNumber("left position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getPosition());

		System.out.println("left position: "+Robot.oi.left.getPosition());
		System.out.println("right position: "+Robot.oi.right.getPosition());

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
		if(rotationComplete && !inMotion && nothingDetected == false){
			Robot.drive.setForPosition();
			Robot.drive.setPeakOutputVoltage(3);
			desiredSpot = RobotUtils.getEncPositionFromIN(Robot.drive.finalDistance- 0);
			Robot.oi.left.set(desiredSpot);
			Robot.oi.right.set(-desiredSpot);
			inMotion = true;
			beginFinalApproachTime = System.currentTimeMillis();
			RobotMap.TAKE_PIC = true;
		} else if ( rotationComplete && !inMotion && nothingDetected == true ) {
			Robot.drive.setForPosition();
			Robot.drive.setPeakOutputVoltage(3);
			Robot.oi.left.set(5);
			Robot.oi.right.set(-5);
			inMotion = true;
		}

		if(rDone && lDone && !rotationComplete){
			Robot.drive.setForSpeed();
			rotationComplete= adjustRotation();
			if(!rotationComplete)
			{
				System.out.println("Adjusting...");
			}
		}
		
//		if (inMotion) {
//			//final backup logic, to unwedge gear
//			if ( (System.currentTimeMillis() - beginFinalApproachTime) > 1000 ) {
//				//it has been 1 second since we started the final approach, what should be do
//				System.out.println("Might be time to back up speeds: " + Robot.oi.right.getSpeed()
//				                   + " speed2 " + Robot.oi.left.getSpeed());
//				if (Robot.oi.right.getSpeed() == 0 && Robot.oi.left.getSpeed() == 0){
//					//speed is 0, maybe we are wedged?
//					if ( !finalBackupDone ) {
//						//only backup 1 inch once
//						Robot.drive.setForPosition();
//						Robot.drive.setPeakOutputVoltage(3);
//						desiredSpot = -1;
//						Robot.oi.left.set(desiredSpot);
//						Robot.oi.right.set(-desiredSpot);
//						finalBackupDone = true;
//					}
//				}
//			}
//		}
	} 

	private boolean adjustRotation()
	{
		//CENTER IS 320
		if ( Robot.drive.centerX != -1 ) {
			if(Robot.drive.centerX > 340) { //+rotate: go right
				System.out.println("Adjusting 330 " + Robot.drive.centerX);
				rotateRight(20);
			}
			else if(Robot.drive.centerX < 310){ //-rotate: go left
				System.out.println("Adjusting 310 "  + Robot.drive.centerX);
				rotateLeft(20);
			}
			else{
				System.out.println("Good Enough!");
				setSpeed(0);
				RobotMap.TAKE_PIC = true;
				return true;
			}
		} else {
			setSpeed(0);
			nothingDetected = true;
			System.out.println("Nothing detected");
			return true;	
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
