package util.input;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public class KeyBindings {

  public Map<KeyStroke, String> defaultKeyStrokes = new HashMap<KeyStroke, String>();
  public Map<String, Action> defaultActionMap = new HashMap<String, Action>();

  public KeyBindings() {
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("W"), "moveForward");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("S"), "moveBack");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("A"), "moveLeft");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("D"), "moveRight");
    
    defaultActionMap.put("moveForward", moveForward);
    defaultActionMap.put("moveBack", moveBack);
    defaultActionMap.put("moveLeft", moveLeft);
    defaultActionMap.put("moveRight", moveRight);
  }

  Action moveForward = new AbstractAction() {
    
    public void actionPerformed(ActionEvent e) {
     System.out.println("Move forward");
    }
    
  };
  Action moveBack = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      System.out.println("Move back");
    }
  };
  Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      System.out.println("Move left");
    }
  };
  Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      System.out.println("Move right");
    }
  };

  
}
