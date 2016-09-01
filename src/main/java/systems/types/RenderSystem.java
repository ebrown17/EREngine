package systems.types;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;

import components.types.Renderable;
import managers.EntityManager;
import systems.SystemProcessor;

public class RenderSystem extends Canvas implements SystemProcessor{

	private static final long serialVersionUID = 1L;
	private int width,height,tileSize;
	private long fps=0,fpsAvg=0;
	private JFrame frame;
	final double ns = 1000000000.0 / 60;
	double delta = 0.0;
	private long previousGameTick, frameTime=0;
	private EntityManager em;
	private BufferStrategy buffer;
	private Graphics2D baseGraphics;
	private Graphics2D baseBufferedGraphics;
	private Collection<Renderable> renderables;
	private Font fpsFont = new Font("Courier New",Font.PLAIN,12);
	private BufferedImage baseImage;

	public RenderSystem(int width,int height,int tileSize,EntityManager em){
		this.width=width;
		this.height=height;
		this.tileSize=tileSize;		
		this.em = em;
		this.baseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(width,height));
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Testing");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	@Override
	public void processOneTick(long lastFrameTick) {
		buffer = getBufferStrategy();
		if(buffer==null){
			createBufferStrategy(2);
			return;
		}
		renderables = em.getAllComponentsOfType(Renderable.class);
		
		// get buffered image to draw to
		baseGraphics = (Graphics2D) baseImage.getGraphics();
		
		baseBufferedGraphics = (Graphics2D) buffer.getDrawGraphics();
	
		for(Iterator<Renderable> rendable = renderables.iterator();rendable.hasNext();){
			Renderable rend = rendable.next();
			baseGraphics.setColor(rend.tile.color);
			baseGraphics.fillRect(rend.position.x*5, rend.position.y*5, 5, 5);
		}
		
		baseBufferedGraphics.drawImage(baseImage, 0, 0, null);	
		
		fps++;
		if(lastFrameTick-previousGameTick>1000000000){
			previousGameTick=lastFrameTick;
			fpsAvg=fps;
			fps=0;
			frame.setTitle("Testing | " + " FPS: " + fpsAvg );
		}
		
		
		
		baseBufferedGraphics.dispose();
		baseGraphics.dispose();
		baseBufferedGraphics = null;
		baseGraphics = null;
		buffer.show();		
		Toolkit.getDefaultToolkit().sync();
		
		
	}
	

}
