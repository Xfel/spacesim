package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTiler {
	
	public static void main(String[] args) throws IOException {
		
		File source=new File("assets/Textures/Background/spheremapgalaxyasteroid.png");
		
		int tilesW = 4;
		int tilesH = 4;
		
		BufferedImage img=ImageIO.read(source);
		
		int pptW = img.getWidth()/tilesW;
				int pptH = img.getHeight()/tilesH;
				
		for (int x = 0; x < tilesW; x++) {
			for (int y = 0; y < tilesW; y++) {
				File result=new File(source.getParent(), "result_"+x+"_"+y+".png");
				
				BufferedImage ri=img.getSubimage(x*pptW, y*pptH, pptW, pptH);
				
				ImageIO.write(ri, "png", result);
			}
		}
		
	}
	
}
