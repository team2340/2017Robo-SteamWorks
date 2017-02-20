package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArcadeDriveCommand extends Command {
	private Joystick controller;
	
	public ArcadeDriveCommand(){
		requires(Robot.drive);
		controller = Robot.oi.driveController;
	}
	
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		double x, y, z;
		z = (3-controller.getZ())/2;
		y = -controller.getY()/z;
		x = -controller.getX()/z; 
		
		Robot.drive.setArcadeSpeed(x, y);
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
