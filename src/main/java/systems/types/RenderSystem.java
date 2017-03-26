package systems.types;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.JFrame;

import components.types.MiddleRenderable;
import components.types.Renderable;
import managers.EntityManager;
import managers.FontManager;
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
	
	private Font fpsFont = new Font("Arial",Font.BOLD,12);
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
				if (r instanceof MiddleRenderable){
					baseGraphics.drawRect(r.position.x << BITSHIFT, r.position.y << BITSHIFT, tileSize, tileSize);
				}
				else{
					//TODO add logic to decide correct bit shifting
					baseGraphics.fillRect(r.position.x << BITSHIFT, r.position.y << BITSHIFT, tileSize, tileSize);
				}
			}
		}	
		
		setFontDetails(baseGraphics);
		baseBufferedGraphics = (Graphics2D) buffer.getDrawGraphics();
		baseBufferedGraphics.drawImage(baseImage, 0, 0, null);	
		
		fps++;
		if(lastFrameTick-previousGameTick>SECOND_IN_NANOTIME){
			previousGameTick=lastFrameTick;
			fpsAvg=fps;
			fps=0;
			//frame.setTitle("FPS: " +fpsAvg );			
		}
		
		baseBufferedGraphics.dispose();
		baseGraphics.dispose();
		baseBufferedGraphics = null;
		baseGraphics = null;
		buffer.show();		
		Toolkit.getDefaultToolkit().sync();
				
	}
	
	private void setFontDetails(Graphics2D baseGraphics){
		baseGraphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
        baseGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        baseGraphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        baseGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        baseGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        baseGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        baseGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        baseGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        baseGraphics.setFont(fpsFont);        
        baseGraphics.setColor(Color.RED);
        baseGraphics.drawString(" FPS: " + fpsAvg + " | " + em.getPoolSizes(), 0, height-1);
	}

}
