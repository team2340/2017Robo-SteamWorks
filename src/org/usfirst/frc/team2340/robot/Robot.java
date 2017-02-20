
package org.usfirst.frc.team2340.robot;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.BlueAllianceBoilerSide;
import org.usfirst.frc.team2340.robot.commands.BlueAllianceRetrievalZone;
import org.usfirst.frc.team2340.robot.commands.CameraCommand;
import org.usfirst.frc.team2340.robot.commands.RedAllianceBoilerSide;
import org.usfirst.frc.team2340.robot.commands.RedAllianceRetrievalZone;
import org.usfirst.frc.team2340.robot.grip.GripPipeline;
import org.usfirst.frc.team2340.robot.subsystems.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
// Java docs (online):
// http://first.wpi.edu/FRC/roborio/release/docs/java/index.html
// http://www.ctr-electronics.com/downloads/api/java/html/index.html
public class Robot extends IterativeRobot {
	public static final OI oi = new OI();
	public static final DriveSubsystem drive = DriveSubsystem.getInstance();
	public static final AcquisitionSubsystem acquisition = AcquisitionSubsystem.getInstance();
	public static int lastTargets = 0;

	Command autonomousCommand = null;
	CameraCommand cameraCommand = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();

	private VisionThread visionThread;
	private final Object imgLock = new Object();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	public void robotInit() {
		Robot.drive.centerX = 0;
		RobotUtils.lengthOfRobot(38);
		RobotUtils.setWheelDiameter(4);

		oi.gyro = new ADXRS450_Gyro();

		autoMode.addDefault("DriveForward", AutoMode.AUTODRIVEFORWARD);
		autoMode.addObject("Disabled", AutoMode.DISABLED);
		autoMode.addObject("BlueAllianceBoilerSide", AutoMode.BLUEALLIANCEBOILERSIDE);
		autoMode.addObject("BlueAllianceRetrievalZone", AutoMode.BLUEALLIANCERETRIEVALZONE);
		autoMode.addObject("RedAllianceBoilerSide", AutoMode.REDALLIANCEBOILERSIDE);
		autoMode.addObject("RedAllianceRetrievalZone", AutoMode.REDALLIANCERETRIEVALZONE);
		SmartDashboard.putData("RedAllianceRetrievalZone", autoMode);

		// when we didn't have the camera open in this resolution 640x480, the grip pipeline was 
		// finding a different number of targets than the computer would find attached to the 
		// same camera in the grip application.

//		cameraCommand= new CameraCommand(); 
//		UsbCamera camera = cameraCommand.getcamera();
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		System.out.println("Camera's Name: " + camera.getName());
		
		camera.setResolution((int)Robot.oi.IMG_WIDTH, (int)Robot.oi.IMG_HEIGHT);

		visionThread = new VisionThread(camera,new GripPipeline(), grip -> {
			if(!grip.filterContoursOutput().isEmpty()){
				ArrayList<MatOfPoint> contours = grip.filterContoursOutput();
				ArrayList<MatOfPoint> targets = new ArrayList<MatOfPoint>();
				for(MatOfPoint point : contours){
					double expectedRation = 2.54;
					double tolerance = 2;
					Rect r = Imgproc.boundingRect(point);
					double ration = r.height/r.width;

					if(ration < expectedRation + tolerance && ration > expectedRation - tolerance){
						targets.add(point);
					}
				}
				if ( lastTargets != targets.size()) { 
					lastTargets = targets.size();
					System.out.println("num targets: " + lastTargets);
				}
				if(targets.size() == 2){
					Rect r = Imgproc.boundingRect(grip.filterContoursOutput().get(0));
					Rect q = Imgproc.boundingRect(grip.filterContoursOutput().get(1));
					synchronized(imgLock){
						Robot.drive.centerX = (r.x + (r.width/2) + q.x + (q.width/2))/2.0;
						//put code here
						double leftmost=0.0;
						double rightmost=0.0;
						double pxBetweenTargets=0.0;
						double angleBetweenTargets=0.0;
						double halfAngleBetweenTargets=0.0;
						double lengthOfOpposite=5.125;
						double distanceFromTarget =0.0;
						if ( r.x < q.x) {
							leftmost = (r.x - (r.width/2));
							rightmost = (q.x + (q.width/2));
						} else {
							leftmost = (q.x - (q.width/2));
							rightmost = (r.x + (r.width/2));
						}
						pxBetweenTargets = rightmost - leftmost;
						angleBetweenTargets = (61 * pxBetweenTargets)/ Robot.oi.IMG_WIDTH;
						halfAngleBetweenTargets = angleBetweenTargets/2;
						double radians = Math.toRadians(halfAngleBetweenTargets);
						distanceFromTarget = lengthOfOpposite/ (Math.tan(radians));
						Robot.drive.finalDistance = distanceFromTarget;
						System.out.println("r.x: "+r.x+", r.width: "+r.width
								+", q.x: "+q.x+", q.width: "+q.width
								+", centerX: "+Robot.drive.centerX + ", distance: "+distanceFromTarget);
						//System.out.println("leftmost: " + leftmost + " rightmost: " + rightmost);
					}
				} else {
					Robot.drive.centerX = -1;
				}
				SmartDashboard.putNumber("CenterX", Robot.drive.centerX);
			} else {
				Robot.drive.centerX = -1;
			}
		});
		visionThread.start();
	}

	public void disabledInit(){
		if(cameraCommand != null){
			cameraCommand.cancel();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		oi.gyro.reset();
		Robot.drive.setForPosition();
		AutoMode am = (AutoMode) autoMode.getSelected();

		if(am == AutoMode.BLUEALLIANCEBOILERSIDE){
			autonomousCommand = new BlueAllianceBoilerSide();
		}
		else if (am == AutoMode.AUTODRIVEFORWARD) {
			autonomousCommand = new AutoDriveForward();
		}
		else if(am == AutoMode.BLUEALLIANCERETRIEVALZONE){
			autonomousCommand = new BlueAllianceRetrievalZone();
		}
		else if(am == AutoMode.REDALLIANCERETRIEVALZONE){
			autonomousCommand = new RedAllianceRetrievalZone();
		}
		else if(am == AutoMode.REDALLIANCEBOILERSIDE){
			autonomousCommand = new RedAllianceBoilerSide();
		}	
		else if (am == AutoMode.DISABLED) {} //Do Nothing if disabled

		System.out.println(am);
		if (autonomousCommand != null) autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (autonomousCommand != null) autonomousCommand.cancel();
		Robot.drive.setForVBus();
//		cameraCommand.start();	
	}

	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		LiveWindow.run();
	}
}
