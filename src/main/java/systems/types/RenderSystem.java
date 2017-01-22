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
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import components.types.BaseRenderable;
import components.types.MiddleRenderable;
import components.types.Renderable;
import components.types.TopRenderable;
import managers.EntityManager;
import systems.SystemProcessor;

public class RenderSystem extends Canvas implements SystemProcessor{

	private static final long serialVersionUID = 1L;
	private int width,height,tileSize;
	private long fps=0,fpsAvg=0;
	private JFrame frame;
	final double ns = 1000000000.0 / 60;
	double delta = 0.0;
	private long previousGameTick=0, frameTime=0;
	private EntityManager em;
	private BufferStrategy buffer;
	private Graphics2D baseGraphics;
	private Graphics2D baseBufferedGraphics;
	private Collection<BaseRenderable> base;
	private Collection<MiddleRenderable> middle;
	private Collection<TopRenderable> top;
	private Set<Renderable> testq ;
	private Font fpsFont = new Font("Courier New",Font.BOLD,24);
	private FontMetrics metrics;
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
		
		baseGraphics.clearRect(0, 0, getWidth(), getHeight());
		
		testq = em.getAllRenderComponents();
		for(Renderable rend : testq){
			baseGraphics.setColor(rend.tile.color);
			baseGraphics.fillRect(rend.position.x*tileSize, rend.position.y*tileSize, tileSize, tileSize);
		}
		/*for(Iterator<Renderable> rendable = testq.iterator();rendable.hasNext();){
			Renderable rend = rendable.next();
			baseGraphics.setColor(rend.tile.color);
			baseGraphics.fillRect(rend.position.x*tileSize, rend.position.y*tileSize, tileSize, tileSize);
		}*/
	/*	for(Iterator<MiddleRenderable> rendable = middle.iterator();rendable.hasNext();){
			MiddleRenderable rend = rendable.next();
			baseGraphics.setColor(rend.tile.color);			
			baseGraphics.fillRect(rend.position.x*tileSize, rend.position.y*tileSize, tileSize, tileSize);
		}
		for(Iterator<TopRenderable> rendable = top.iterator();rendable.hasNext();){
			Renderable rend = rendable.next();
			baseGraphics.setColor(rend.tile.color);
			baseGraphics.fillRect(rend.position.x*tileSize, rend.position.y*tileSize, tileSize, tileSize);
		}	*/	
		
		baseBufferedGraphics = (Graphics2D) buffer.getDrawGraphics();
		//baseBufferedGraphics.clearRect(0, 0, getWidth(), getHeight());
		baseBufferedGraphics.drawImage(baseImage, 0, 0, null);	
		
		fps++;
		if(lastFrameTick-previousGameTick>1000000000){
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
	
	public JFrame getRenderFrame(){
		return frame;
	}

}
