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
	private boolean button2Pressed,on;
	
	public ArcadeDriveCommand(){
		requires(Robot.drive);
		controller = Robot.oi.driveController;
//		gyroEnable = false;
//		prevState = false;
	}
	
	@Override
	protected void initialize() {
		button2Pressed=on =false;
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
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		SmartDashboard.putNumber("Gyro angle", angle);
		SmartDashboard.putNumber("left position", Robot.oi.left.getPosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getPosition());

		double x, y, z;

		z = (3-controller.getZ())/2;
		y = -controller.getY()/z;
		x = -controller.getX()/z; 
		if(controller.getRawButton(RobotMap.BUTTON_2)){
			if(!button2Pressed){
				System.out.println("button two pressed");
				if(on){
					System.out.println("button two on");
					setSpeed(0);
					Robot.drive.setForVBus();
					on =false;
				}
				else{
					System.out.println("button two off");
					Robot.drive.setForSpeed();
					x=0;
					if(y >= 0){
						y = -controller.getY();
						y *= .2;
					}
					else{
						y = 0;
					}
					adjustRotation();
					on= true;
				}
				button2Pressed = true;
			}
		}
		else
		{
			button2Pressed = false;
			if(on){
				adjustRotation();
				x=0;
				if (y >= 0){
					y = -controller.getY();
					y *= .2;
				}
				else{
					y = 0;
				}
				System.out.println("button two adjustRotation");
			}
			else{
				//System.out.println("button two not pressed");	
			}
		}
		
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
