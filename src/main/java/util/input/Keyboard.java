package util.input;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class Keyboard implements KeyListener {

  private HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();

  private Action doNothing = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      // do nothing
    }
  };

  public Keyboard() {
    keyMap.put(KeyEvent.VK_W, false);
    keyMap.put(KeyEvent.VK_A, false);
    keyMap.put(KeyEvent.VK_S, false);
    keyMap.put(KeyEvent.VK_D, false);
  }

  public HashMap<Integer, Boolean> getKeyMap() {
    return keyMap;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    keyMap.put(e.getKeyCode(), true);

  }

  @Override
  public void keyReleased(KeyEvent e) {
    keyMap.put(e.getKeyCode(), false);

  }

}
