package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;
import org.usfirst.frc.team2340.robot.RobotUtils.AutonomousState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BlueAllianceBoilerSide extends Command {
	long startTime = 0;
	boolean initialized;
	double desiredSpot = 0;
	boolean methodRotationComplete, methodDriving, methodcrDone, methodclDone;
	double methodDesiredSpot;
	int driveCount = 0;
	AutonomousState state;
	AutonomousState autonomousMode [] = {AutonomousState.DriveForward, AutonomousState.Rotate,
	                                     AutonomousState.DriveForward2, AutonomousState.CameraAssistRotate,
	                                     AutonomousState.CameraAssistDriveForward, AutonomousState.Done};

	public BlueAllianceBoilerSide()
	{
		requires(Robot.drive);
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		state = AutonomousState.DriveForward;
	}

	@Override
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		long elapsed = (System.currentTimeMillis() - startTime)/1000;

		SmartDashboard.putNumber("Auto Elapsed", elapsed);
		SmartDashboard.putNumber("Gyro angle", angle);
		SmartDashboard.putNumber("Left Position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("Right Position",Robot.oi.right.getPosition());

		if(state == AutonomousState.DriveForward) {
		  if(!initialized) {
        desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(105));
		    Robot.oi.left.set(desiredSpot);
		    Robot.oi.right.set(-desiredSpot);
		    initialized = true;
		  }
		  
		  if(driveForwardComplete()) {
		    initialized = false;
		    state = AutonomousState.Rotate;
		  }
		}
		
		if(state == AutonomousState.Rotate) {
		  if(RotateRight()) {
		    initialized = false;
		    state = AutonomousState.DriveForward2;
		  }
		}
		
		if(state == AutonomousState.DriveForward2) {
		  if(!initialized) {
		    System.out.println("DRIVE AGAIN");
		    desiredSpot = RobotUtils.getEncPositionFromIN(45);
		    Robot.oi.left.set(desiredSpot);
		    Robot.oi.right.set(-desiredSpot);
		    initialized = true;
		  }
      
      if(driveForwardComplete()) {
        initialized = false;
        state = AutonomousState.CameraAssistRotate;
      }
		}
		
		if(state == AutonomousState.CameraAssistRotate) {
		  if(driveCount < 10  && Robot.drive.finalDistance > 5){
		    boolean adjustAndDrive = false;
		    System.out.println("adjust count " + driveCount);
		    if(driveCount < 2) {
		      adjustAndDrive = adjustAndDriveHalfWay(-1, -1, 15);
		    }
		    else {
		      initialized = false;
		      state = AutonomousState.CameraAssistDriveForward;
		    }
		    if (adjustAndDrive) {
		      resetAdjustAndDriveHalfway();
		      driveCount++;
		    }
		  }
		}
		
		if (state == AutonomousState.CameraAssistDriveForward) {
		  if(!initialized)
		  {
		    Robot.drive.setForPosition();
		    double finalDistance = Robot.drive.finalDistance - 10;
		    desiredSpot = RobotUtils.getEncPositionFromIN(finalDistance);
		    System.out.println("FINAL drive: " + finalDistance);
		    Robot.oi.left.set(desiredSpot);
		    Robot.oi.right.set(-desiredSpot);
		    initialized = true;
		  }

		  if(driveForwardComplete()) {
        initialized = false;
        state = AutonomousState.Done;
      }
		}
		
		if (state == AutonomousState.Done) {
		  System.out.println("Auto is done!!");
		}

/*		
    if(state == AutonomousState.CameraAssistRotate){
			Robot.drive.setForSpeed();
			if(!adjustRotation())
			{
				System.out.println("Adjusting...");
			}
			else {
        initialized = false;
        state = AutonomousState.CameraAssistDriveForward;
      }
		}
		
		if(state == AutonomousState.CameraAssistDriveForward) {
		  if(!initialized) {
		    System.out.println("FINAL drive");
			  Robot.drive.setForPosition();
			  desiredSpot = RobotUtils.getEncPositionFromIN(Robot.drive.finalDistance - 8);
			  Robot.oi.frontLeft.set(desiredSpot);
			  Robot.oi.frontRight.set(-desiredSpot);
			  initialized = true;
			}
			
			if(driveForwardComplete()) {
        initialized = false;
        state = AutonomousState.Done;
      }
		}
*/
	}
	
	private boolean driveForwardComplete() {
	  boolean rDone = false;
      boolean lDone = false;
      
      if(Robot.oi.right.getPosition() <= -desiredSpot) {
        Robot.oi.right.setPosition(0);
        Robot.oi.right.set(0);
        System.out.println("R DONE");
        rDone = true;
      }
      
      if(Robot.oi.left.getPosition() >= desiredSpot) {
        Robot.oi.left.setPosition(0);
        Robot.oi.left.set(0);
        System.out.println("L DONE");
        lDone = true;
      }
      
      if(rDone && lDone) {
        return true;
      }
      else {
        return false;
      }
	}
	
	protected boolean RotateRight(){
	  double angle = Robot.oi.gyro.getAngle();
	  if(angle >=62){
	    Robot.drive.setForPosition();
	    System.out.print("ROTATE done");
	    return true;
	  }
	  else {
	    Robot.drive.setForSpeed();
	    Robot.oi.left.set(2.5* (64 - angle)+10);
	    Robot.oi.right.set(2.5* (64 - angle)+10);
	    return false;
	  }
	}
	
	private void resetAdjustAndDriveHalfway() {
		methodRotationComplete = false;
		methodDriving = false;
		methodclDone = false;
		methodcrDone = false;
	}
	
	private boolean adjustAndDriveHalfWay(int left, int right, int rpm) {
		if( !methodRotationComplete ) {
			Robot.drive.setForSpeed();
			methodRotationComplete = adjustRotation(left, right, rpm);
		}
		if (methodRotationComplete && !methodDriving) {
			Robot.drive.setForPosition();
			methodDriving = true;
			methodDesiredSpot = RobotUtils.getEncPositionFromIN( (Robot.drive.finalDistance - 8)/2);
			Robot.oi.left.set(methodDesiredSpot);
			Robot.oi.right.set(-methodDesiredSpot);
		}
		if ((!methodclDone || !methodcrDone) && (methodRotationComplete && methodDriving)) {
			if(Robot.oi.right.getPosition()<=-methodDesiredSpot){
				Robot.oi.right.setPosition(0);
				Robot.oi.right.set(0);
				methodcrDone = true;
			}
			if(Robot.oi.left.getPosition()>=methodDesiredSpot){
				Robot.oi.left.setPosition(0);
				Robot.oi.left.set(0);
				methodclDone = true;
			}
		}
		
		return methodRotationComplete && methodclDone && methodcrDone;
	}

	private boolean adjustRotation(int left, int right, int rpm)
	{
		int defaultLeft = 310;
		int defaultRight = 340;
		if (left != -1) {
			defaultLeft = left;
		}
		if (right != -1){
			defaultRight = right;
		}
		int defaultRpm = 40;
		if (rpm != -1){
			defaultRpm = rpm;
		}
		//CENTER IS 320
		if ( Robot.drive.centerX != -1 ) {
			if(Robot.drive.centerX > defaultRight) { //+rotate: go right
				System.out.println("Adjusting 330 " + Robot.drive.centerX);
				rotateRight(defaultRpm);
			}
			else if(Robot.drive.centerX < defaultLeft){ //-rotate: go left
				System.out.println("Adjusting 310 "  + Robot.drive.centerX);
				rotateLeft(defaultRpm);
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

