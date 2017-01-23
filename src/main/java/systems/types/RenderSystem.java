package systems.types;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import components.Component;
import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.Renderable;
import components.types.TopRenderable;
import managers.EntityManager;
import systems.SystemProcessor;
import util.RenderPriority;

public class RenderSystem extends Canvas implements SystemProcessor{

	private static final long serialVersionUID = 1L;
	private int width,height,tileSize;
	private long fps=0,fpsAvg=0;
	private JFrame frame;
	private final long SECOND_IN_NANOTIME = 1000000000;
	private long previousGameTick=0;
	private EntityManager em;
	private BufferStrategy buffer;
	private Graphics2D baseGraphics;
	private Graphics2D baseBufferedGraphics;
	private final int BITSHIFT;
	
	private Font fpsFont = new Font("Courier New",Font.BOLD,24);
	private FontMetrics metrics;
	private BufferedImage baseImage;

	public RenderSystem(int width,int height,int tileSize,EntityManager em){
		this.width=width;
		this.height=height;
		this.tileSize=tileSize;
		this.BITSHIFT = tileSize/4;
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
	
	public void setMouseListener(MouseMotionListener mML){
		this.addMouseMotionListener(mML);
	}
	
	public void setMouseEventListener(MouseListener mML){
		this.addMouseListener(mML);
	}
	
	@Override
	public void processOneTick(long lastFrameTick) {
		buffer = getBufferStrategy();
		if(buffer==null){
			createBufferStrategy(2);
			return;
		}
			
		// get buffered image to draw to
		baseGraphics = baseImage.createGraphics();
		
		// clears image
		baseGraphics.clearRect(0, 0, getWidth(), getHeight());
				
		for(Class<? extends Renderable> renderableClass: RenderPriority.INSTANCE.getRenderLayers()){
			Collection<? extends Renderable> renderLayer = em.getAllComponentsOfType(renderableClass);
			for(Renderable r : renderLayer){
				baseGraphics.setColor(r.tile.color);
				//TODO add logic to decide correct bit shifting
				baseGraphics.fillRect(r.position.x << BITSHIFT, r.position.y << BITSHIFT, tileSize, tileSize);
			}
		}	
		
		baseBufferedGraphics = (Graphics2D) buffer.getDrawGraphics();
		baseBufferedGraphics.drawImage(baseImage, 0, 0, null);	
		
		fps++;
		if(lastFrameTick-previousGameTick>SECOND_IN_NANOTIME){
			previousGameTick=lastFrameTick;
			fpsAvg=fps;
			fps=0;
			frame.setTitle("Testing | " + " FPS: " + fpsAvg + " | " + em.getPoolSizes());			
			
		}
		
		baseBufferedGraphics.dispose();
		baseGraphics.dispose();
		baseBufferedGraphics = null;
		baseGraphics = null;
		buffer.show();		
		Toolkit.getDefaultToolkit().sync();
				
	}

}
