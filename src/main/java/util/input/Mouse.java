package util.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

public class Mouse implements MouseMotionListener, MouseInputListener {

  private int currentX = 0, currentY = 0;
  private boolean mouseClickedFlag = false;
  public int tileSize;

  public int getCurrentX() {
    return currentX / tileSize;
  }
  

  public int getCurrentY() {
    return currentY/tileSize;
  }
  

  public boolean isMouseClickedFlag() {
    return mouseClickedFlag;
  }
  
  
  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      mouseClickedFlag = !mouseClickedFlag;
    }

  }

  @Override
  public void mouseMoved(MouseEvent e) {
    currentX = e.getX();
    currentY = e.getY();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseDragged(MouseEvent e) {
    // TODO Auto-generated method stub

  }

}
