package test;

import java.io.File;
import java.io.IOException;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.export.xml.XMLExporter;
import com.jme3.math.Vector3f;

import spacegame.model.structure.ShipFrame;

public class LoadModelTest {
	public static void main(String[] args) throws IOException {
		ShipFrame frame = new ShipFrame();
		
		frame.setModelName("/Models/Complete/FirstShip/FirstShip_LowPoly.blend");
		frame.setName("FirstShip");
		
		frame.setMass(20f);
		frame.setStructuralIntegrity(4000f);
		
		frame.setOutline(new BoxCollisionShape(new Vector3f(1, 2, 1)));
		
		XMLExporter exporter=new XMLExporter();
		
		exporter.save(frame, new File("test.xml"));
	}
}
