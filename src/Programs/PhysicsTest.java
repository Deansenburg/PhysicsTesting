package Programs;

import GxEngine3D.Camera.Camera;
import GxEngine3D.Controller.GXController;
import GxEngine3D.Controller.ITickListener;
import GxEngine3D.Controller.Scene;
import GxEngine3D.Lighting.Light;
import GxEngine3D.View.Screen;
import GxEngine3D.View.ViewHandler;
import MenuController.LookMenuController;
import Physics.Controller.ForceController;
import Shapes.*;
import Shapes.Shape2D.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PhysicsTest {

	public static void main(String[] args) {
		final ViewHandler vH = new ViewHandler();

		final Camera c = new Camera(50, -50, 35, vH);
		Line ln = new Line(0, 0, 0, 1, 1, 1, vH);
		Light ls = new Light(0, 0, -20, 100, ln);

		ForceController fCon = new ForceController();

		final Scene scene = new Scene(c, ls, 0);

		Cube cube = new Cube(1, 1, 0, 5, 5, 5, Color.decode("#FF00FF"), vH);
		scene.addObject(cube);
//		fCon.add(cube);

		Pyramid pyr = new Pyramid(2, 2, 6, 1, 1, 1, Color.decode("#00FF00"), vH);
		scene.addObject(pyr);
//		fCon.add(pyr);

//		cube = new Cube(1.5, 1.5, 6, 4, 4, 4, Color.decode("#0000FF"), vH);
//		scene.addObject(cube);
//		fCon.add(cube);
//
		cube = new Cube(2, 2, 11, 3, 3, 3, Color.decode("#00FFFF"), vH);
		scene.addObject(cube);
//		fCon.add(cube);
//
//		cube = new Cube(2.5, 2.5, 16, 2, 2, 2, Color.decode("#00FF00"), vH);
//		scene.addObject(cube);
//		fCon.add(cube);
//
//		cube = new Cube(3, 3, 20, 1, 1, 1, Color.decode("#FFFF00"), vH);
//		scene.addObject(cube);
//		fCon.add(cube);

		Cube floor = new Cube(-25, -25, 0, 50, 50, 0.1, Color.red,  vH);
		scene.addObject(floor);

//		Pyramid pyr = new Pyramid(10, 10, 100, 5, 5, 5, Color.white, vH);
//		pyr.roll(Math.toRadians(60));
//		scene.addObject(pyr);
//		fCon.add(pyr);

		Screen panel = new Screen(scene);
		panel.setPreferredSize(new Dimension(500, 500));

		vH.setPanel(panel);

		final JMenu lookMenu = new JMenu("Look At");
		final LookMenuController lookCon = new LookMenuController();

		final GXController gCon = new GXController(scene, c, panel, vH);
		ActionListener actions = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String act = e.getActionCommand();
				if (act.startsWith("look")) {
					c.lookAt((BaseShape) scene.getShapes().get(
							Integer.parseInt(act.split(":")[1])));
					gCon.CenterMouse();
				} else
					return;;
			}
		};
		ITickListener tickGraph = new ITickListener() {
			@Override
			public void onTick() {
				fCon.update();
				scene.scheduleRedraw();
			}
		};
		gCon.add(tickGraph);

		panel.addKeyListener(gCon);
		panel.addMouseListener(gCon);
		panel.addMouseMotionListener(gCon);
		panel.addMouseWheelListener(gCon);

		// main window with menu bar
		JMenuBar menuBar = new JMenuBar();
		// start fill menu
		JMenu menu;

		menu = new JMenu("View");
		menuBar.add(menu);
		
		menu.add(lookMenu);
		
		JFrame frame = new JFrame();
		frame.setJMenuBar(menuBar);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		lookCon.updateMenu(lookMenu, scene, actions);
		while (true) {
			gCon.update();
		}
	}

}
