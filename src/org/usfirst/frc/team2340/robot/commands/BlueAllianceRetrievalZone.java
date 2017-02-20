package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BlueAllianceRetrievalZone extends Command {
	long startTime = 0;
	boolean lDone = false, rDone = false;
	
	public BlueAllianceRetrievalZone()
	{
		requires(Robot.drive);
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		Robot.oi.left.set(2.95);
		Robot.oi.right.set(-2.95);
		lDone = rDone = false;
	}

boolean RotateRight(){
	double angle = Robot.oi.gyro.getAngle();
	if(angle <=-56){
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.Position);
	    Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.Position);
		Robot.oi.left.setPosition(0);
		Robot.oi.left.set(0);
		Robot.oi.right.setPosition(0);
		Robot.oi.right.set(0);
		return true;
	}
	else{
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.Speed);
	    Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.Speed);
		Robot.oi.left.set(-3* (60 + angle)-80);
		Robot.oi.right.set (-3* (60 + angle)-80);
//		Robot.oi.frontLeft.set(0.5);
//		Robot.oi.frontRight.set(0.5);
		return false;
	}
	
//	double angle = Robot.oi.gyro.getAngle();
//	if(angle >=60){
//	    Robot.oi.frontLeft.changeControlMode(CANTalon.TalonControlMode.Position);
//        Robot.oi.frontRight.changeControlMode(CANTalon.TalonControlMode.Position);
//		Robot.oi.frontLeft.setPosition(0);
//		Robot.oi.frontLeft.set(0);
//		Robot.oi.frontRight.setPosition(0);
//		Robot.oi.frontRight.set(0);
//		return true;
//	}
//	else{
//		Robot.oi.frontLeft.changeControlMode(CANTalon.TalonControlMode.Speed);
//	    Robot.oi.frontRight.changeControlMode(CANTalon.TalonControlMode.Speed);
//		Robot.oi.frontLeft.set(5);
//		Robot.oi.frontRight.set(5);
//		return false;
//	}
}
	@Override
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		long elapsed = (System.currentTimeMillis() - startTime)/1000;
		SmartDashboard.putNumber("Auto Elapsed", elapsed);
		SmartDashboard.putNumber("Gyro angle", angle);
		SmartDashboard.putNumber("left position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getPosition());
		if(Robot.oi.right.getPosition()<=-2.95){
			Robot.oi.right.setPosition(0);
			Robot.oi.right.set(0);
			rDone = true;
		}
		if(Robot.oi.left.getPosition()>=2.95){
			Robot.oi.left.setPosition(0);
			Robot.oi.left.set(0);
			lDone = true;
		}
		if(rDone && lDone){
			RotateRight(); 
		}
	} 
	
	@Override
	protected boolean isFinished() {
		//return System.currentTimeMillis() -startTime >= 15000;
		return false;
	}

	@Override
	protected void end() {
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}

	@Override
	protected void interrupted() {
	}
}
