package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShooter extends Command {
	long startTime = 0;
	double currentTime;
	public AutoShooter() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		currentTime = 0;
	}
	@Override
	protected void execute() {
		long elapsed = (System.currentTimeMillis() - startTime)/1000;
		if(currentTime == 0) {
			currentTime = System.currentTimeMillis();
		}
		
		Robot.oi.ballShooter.set(0.74/*controller.getZ()*/);
		
		if(currentTime <= System.currentTimeMillis() - 3000) {
			Robot.oi.ballFeeder.set(-1);
		}

	} 

	
	protected boolean isFinished() {
		return System.currentTimeMillis() -startTime >= 15000; 
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
	}
}



