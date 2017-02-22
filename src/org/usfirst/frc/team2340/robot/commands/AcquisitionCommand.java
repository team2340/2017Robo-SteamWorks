package org.usfirst.frc.team2340.robot.commands;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AcquisitionCommand extends Command {
	private Joystick controller;
	private boolean button2Pressed, button6Pressed, on;
	double currentTime;
	public AcquisitionCommand(Subsystem _subsystem){
		requires(_subsystem);
	}
	
	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
		button6Pressed = button2Pressed = false;
		currentTime = 0;
//		Robot.oi.ballAq.set(-1);
//		on = true;
	}

	@Override
	protected void execute() {
		//SmartDashboard.putNumber("shooter position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("climber position ",Robot.oi.climbing.getPosition());
		
		if (controller.getRawButton(RobotMap.BUTTON_2)){
			if(!button2Pressed)
			{
				toggleAq();
				button2Pressed = true;
			}
		}
		else
		{
			button2Pressed = false;
		}
		
		if (controller.getTrigger()){
			if(currentTime == 0) {
				currentTime = System.currentTimeMillis();
			}
			
			Robot.oi.ballShooter.set(controller.getZ());
			
			if(currentTime <= System.currentTimeMillis() - 3000) {
				Robot.oi.ballFeeder.set(-1);
			}
		}
		else{
			currentTime = 0;
			Robot.oi.ballFeeder.set(0);
			Robot.oi.ballShooter.set(0);
		}
		
		if(controller.getRawButton(RobotMap.BUTTON_5)){
			Robot.oi.climbing.set(1);
		}
		else{
			Robot.oi.climbing.set(0);
		}
		if(controller.getRawButton(RobotMap.BUTTON_6)){
			if(!button6Pressed)
			{
				if(on){
					Robot.oi.ballAq.set(0);
					on =false;
				}
				else{
					Robot.oi.ballAq.set(1);
					on= true;
				}
				button6Pressed = true;
			}
		}
		else
		{
			button6Pressed = false;
		}
		SmartDashboard.putNumber("z",controller.getZ());
		SmartDashboard.putNumber("ball shooter ", Robot.oi.ballShooter.getSpeed());
	}
	
	private void toggleAq() {
		if(!on) {
			Robot.oi.ballAq.set(-1);
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
