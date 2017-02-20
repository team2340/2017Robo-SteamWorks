package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class AcquisitionCommand extends Command {
	private Joystick controller;
	private boolean buttonPressed, on;
	
	public AcquisitionCommand(){
		requires(Robot.acquisition);
	}
	
	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
		buttonPressed = false;
		Robot.oi.ballAq.set(1);
		on = true;
	}

	@Override
	protected void execute() {
		if (controller.getRawButton(RobotMap.BUTTON_2)){
			if(!buttonPressed)
			{
				toggleAq();
				buttonPressed = true;
			}
		}
		else
		{
			buttonPressed = false;
		}
		
		if (controller.getTrigger()){
			Robot.oi.ballShooter.set(1);
		}
		else{
			Robot.oi.ballShooter.set(0);
		}
		
		if(controller.getRawButton(RobotMap.BUTTON_5)){
			Robot.oi.climbing.set(1);
		}
		else{
			Robot.oi.climbing.set(0);
		}
	}
	
	private void toggleAq() {
		if(!on) {
			Robot.oi.ballAq.set(1);
			on = true;
		}
		else {
			Robot.oi.ballAq.set(0);
			on = false;
		}
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
