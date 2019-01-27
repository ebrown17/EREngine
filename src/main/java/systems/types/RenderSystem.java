package systems.types;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;
import java.util.Collection;

import javax.swing.JFrame;

import components.types.Renderable;
import managers.EntityManager;
import systems.SystemProcessor;
import util.RenderPriority;
import util.input.Mouse;

public class RenderSystem extends JFrame implements SystemProcessor {

  private static final long serialVersionUID = 1L;
  private int width, height, tileSize;
  private long fps = 0, fpsAvg = 0;
  private Canvas canvas;
  private Dimension dimension;
  private final long SECOND_IN_NANOTIME = 1000000000;
  private long previousGameTick = 0;
  private EntityManager em;
  private BufferStrategy buffer;
  private Graphics2D baseGraphics;
  private int bitShift = 0;
  private boolean bitShiftable = false;
  private Mouse mouse;
  int scaleX, scaleY;

  private Font fpsFont = new Font("Arial", Font.BOLD, 18);

  public RenderSystem(int width, int height, int tileSize, EntityManager em) {
    this.width = width;
    this.height = height;
    this.tileSize = tileSize;
    this.em = em;

    scaleX = width / tileSize;
    scaleY = height / tileSize;

    bitShiftable = setTileRenderOffset(tileSize);
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(width, height));
    canvas.setIgnoreRepaint(true);

    add(canvas);
    setResizable(true);
    setTitle("Testing");
    setIgnoreRepaint(true);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
    getContentPane().addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        onComponentResized(e);
      }

    });

    canvas.createBufferStrategy(2);
    buffer = canvas.getBufferStrategy();
    setFontDetails((Graphics2D)buffer.getDrawGraphics());

  }

  public void setMouseListener(Mouse mouse) {
    this.mouse = mouse;
    canvas.addMouseListener(mouse);
    canvas.addMouseMotionListener(mouse);
  }

  public void processOneTick(long lastFrameTick) {

    calcFps(lastFrameTick);

    // get buffered image to draw to
    baseGraphics =(Graphics2D) buffer.getDrawGraphics();
    // set bg color
    baseGraphics.setBackground(Color.BLACK);

    // clears image
    baseGraphics.clearRect(0, 0, getWidth(), getHeight());


    // use multiplication or use bitshifting to determine render postion
    if (bitShiftable) {
      for (Class<? extends Renderable> renderableClass :
          RenderPriority.INSTANCE.getRenderLayers()) {
        Collection<? extends Renderable> renderLayer = em.getAllComponentsOfType(renderableClass);
        for (Renderable r : renderLayer) {
            baseGraphics.setColor(r.tile.color);
            baseGraphics.fillRect(
                r.position.x << bitShift, r.position.y << bitShift, tileSize, tileSize);
        }
      }
    } else {
      for (Class<? extends Renderable> renderableClass :
          RenderPriority.INSTANCE.getRenderLayers()) {
        Collection<? extends Renderable> renderLayer = em.getAllComponentsOfType(renderableClass);
        for (Renderable r : renderLayer) {
          baseGraphics.setColor(r.tile.color);
          baseGraphics.fillRect(
              r.position.x * tileSize, r.position.y * tileSize, tileSize, tileSize);
        }
      }
    }
    drawFPS(baseGraphics);

    baseGraphics.dispose();
    if (!buffer.contentsLost()) {
      buffer.show();
    }
    Toolkit.getDefaultToolkit().sync();
  }

  protected void onComponentResized(ComponentEvent e){
    canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));

    tileSize = getWidth() /scaleX;
    bitShiftable = setTileRenderOffset(tileSize);
    mouse.tileSize = tileSize;
/*    scaleX = width / tileSize;
    scaleY = height / tileSize;*/

  }
  private void calcFps(long lastFrameTick){
    fps++;
    if (lastFrameTick - previousGameTick > SECOND_IN_NANOTIME) {
      previousGameTick = lastFrameTick;
      fpsAvg = fps;
      fps = 0;
      setTitle("FPS: " + fpsAvg);
    }
  }

  private void drawFPS(Graphics2D graphics){
    graphics.setFont(fpsFont);
    graphics.setColor(Color.RED);
    graphics.drawString(" FPS: " + fpsAvg + " | " + em.getPoolSizes(), 0, tileSize/2);
  }

  private void setFontDetails(Graphics2D graphics) {
    graphics.setRenderingHint(
        RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
    graphics.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setRenderingHint(
        RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    graphics.setRenderingHint(
        RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    graphics.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.setRenderingHint(
        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(
        RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
  }

  private boolean setTileRenderOffset(int tileSize) {
    int square = 1;
    int power = 0;
    while (tileSize >= square) {
      if (tileSize == square) {
        bitShift = power;
        return true;
      }
      square = square * 2;
      power++;
    }
    return false;
  }
}
