package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.AcquisitionCommand;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class AcquisitionSubsystem extends Subsystem {
	static private AcquisitionSubsystem subsystem;
	
	private AcquisitionSubsystem() {
		try {
			Robot.oi.ballAq = new CANTalon(RobotMap.BALL_AQ_TAL_ID);
		} catch (Exception ex) {
			System.out.println(" createTalon FAILED");
		}
		
		try {
			Robot.oi.ballShooter = new CANTalon(RobotMap.BALL_SHOOTER_TAL_ID);
		} catch (Exception ex) {
			System.out.println(" createTalon FAILED");
		}
		
		try {
			Robot.oi.ballFeeder = new CANTalon(RobotMap.BALL_FEEDER_TAL_ID);
			Robot.oi.ballFeeder.changeControlMode(CANTalon.TalonControlMode.Follower);
		} catch (Exception ex) {
			System.out.println(" createTalon FAILED");
		}
		
		try {
			Robot.oi.climbing = new CANTalon(RobotMap.CLIMBING_TAL_ID);
			Robot.oi.climbing.enableBrakeMode(true);
		} catch (Exception ex) {
			System.out.println(" createTalon FAILED");
		}
	}

	public static AcquisitionSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new AcquisitionSubsystem();
		}
		return subsystem;
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new AcquisitionCommand(this));
	}
}
