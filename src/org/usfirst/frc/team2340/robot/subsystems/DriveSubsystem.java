package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ArcadeDriveCommand;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem {

	static private DriveSubsystem subsystem;
	RobotDrive robotDrive;
	public double centerX;
	public double finalDistance;
	public double speedP = 7.0;
	public double speedI = 0.005;
	public double speedD = 0.0;
	public double speedF = 0.0;
	public double speedMaxOutput = 350;
	public double speedPeakOutputVoltage = 12.0f;
	
	public double positionP = 2.0;
	public double positionI = .0001;
	public double positionD = 0.0;
	public double positionF = 0.0;
	public float positionPeakOutputVoltage = 5.0f;
	
	public double vBusMaxOutput = 1.0;

	static public DriveSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new DriveSubsystem();
		}
		return subsystem;
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveCommand());
	}

	private DriveSubsystem() {
		centerX = -1;
		finalDistance = 0.0;
		createLeftSide();
		createRightSide();
		setForPosition();
		robotDrive = new RobotDrive(Robot.oi.left, Robot.oi.right);
		robotDrive.setSafetyEnabled(false);
	}

	private void createLeftSide() {
		try {
			Robot.oi.left = new CANTalon(RobotMap.LEFT_TAL_ID);
			Robot.oi.left.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//			Robot.oi.left.reverseSensor(true);
			Robot.oi.left.configEncoderCodesPerRev(250);
			Robot.oi.left.configNominalOutputVoltage(+0.0f, -0.0f);
		    Robot.oi.left.setProfile(0);
		} catch (Exception ex) {
			System.out.println("createLeftSide FAILED");
		}
	}

	private void createRightSide() {
		try {
			Robot.oi.right = new CANTalon(RobotMap.RIGHT_TAL_ID);
			
			Robot.oi.right.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//			Robot.oi.right.reverseSensor(true);
			Robot.oi.right.configEncoderCodesPerRev(250);
			Robot.oi.right.configNominalOutputVoltage(+0.0f, -0.0f);
		    Robot.oi.right.setProfile(0);
		} catch (Exception ex) {
			System.out.println("createRightSide FAILED");
		}
	}
	
	public void setForSpeed() {
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.Speed);
		Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.Speed);
		Robot.oi.right.setF(speedF);
	    Robot.oi.right.setP(speedP);
	    Robot.oi.right.setI(speedI); 
	    Robot.oi.right.setD(speedD);
	    Robot.oi.left.setF(speedF);
	    Robot.oi.left.setP(speedP);  
	    Robot.oi.left.setI(speedI);  
	    Robot.oi.left.setD(speedD);
	    robotDrive.setMaxOutput (speedMaxOutput);
	    Robot.oi.left.configPeakOutputVoltage(speedPeakOutputVoltage, -speedPeakOutputVoltage);
	    Robot.oi.right.configPeakOutputVoltage(speedPeakOutputVoltage, -speedPeakOutputVoltage);	
	}
	
	public void setForPosition() {
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.Position);
		Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.Position);
		Robot.oi.right.setF(positionF);
	    Robot.oi.right.setP(positionP);
	    Robot.oi.right.setI(positionI); 
	    Robot.oi.right.setD(positionD);
	    Robot.oi.left.setF(positionF);
	    Robot.oi.left.setP(positionP);  
	    Robot.oi.left.setI(positionI);  
	    Robot.oi.left.setD(positionD);   
	    Robot.oi.left.configPeakOutputVoltage(positionPeakOutputVoltage, -positionPeakOutputVoltage);
	    Robot.oi.right.configPeakOutputVoltage(positionPeakOutputVoltage, -positionPeakOutputVoltage);
	    Robot.oi.right.setPosition(0);
	    Robot.oi.left.setPosition(0);
	    
	}
	
	public void setForVBus() {
		Robot.oi.left.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        Robot.oi.right.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        robotDrive.setMaxOutput (vBusMaxOutput);
		setArcadeSpeed(0,0);
	}
	
	public void setBrakeMode(boolean brake) {
		Robot.oi.right.enableBrakeMode(brake);
		Robot.oi.left.enableBrakeMode(brake);
	}
	
	public void setArcadeSpeed(double x, double y){
		robotDrive.arcadeDrive(y, x);
	}
}
