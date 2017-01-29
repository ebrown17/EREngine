package managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FontManager {

	static BufferedImage font;
	
	public FontManager(){
		loadFont();
		
	}
	
	private void loadFont(){
		try {
			font = ImageIO.read(new File("/home/ebbz/git/EREngine/resources/fonts/sam_coupe_8x8.png"));
		} catch (IOException e) {
			System.out.println("font failed to load");
		}
		System.out.println("font loaded");
	}
	
	public BufferedImage getFont(){
		
		return font.getSubimage(8,0, 8, 8);
	}
	
}
