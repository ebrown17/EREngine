package util.input;

import systems.types.InputSystem;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class KeyBindings {

  private Map<KeyStroke, String> defaultKeyStrokes = new HashMap<KeyStroke, String>();
  private Map<String, Action> defaultActionMap = new HashMap<String, Action>();

  private InputSystem inputSystem;

  public KeyBindings() {
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("W"), "moveForward");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("S"), "moveBack");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("A"), "moveLeft");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("D"), "moveRight");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("UP"), "moveForward");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("DOWN"), "moveBack");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
    defaultKeyStrokes.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

    defaultActionMap.put("moveForward", moveForward);
    defaultActionMap.put("moveBack", moveBack);
    defaultActionMap.put("moveLeft", moveLeft);
    defaultActionMap.put("moveRight", moveRight);
  }

  public void registerBindingListener(JRootPane root){
    int in_focus = JComponent.WHEN_IN_FOCUSED_WINDOW;
    InputMap inMap = root.getInputMap(in_focus);
    ActionMap aMap = root.getActionMap();

    for (Map.Entry<KeyStroke, String> entry : defaultKeyStrokes.entrySet()) {
      inMap.put(entry.getKey(), entry.getValue());
    }

    for (Map.Entry<String, Action> entry : defaultActionMap.entrySet()) {
      aMap.put(entry.getKey(), entry.getValue());
    }
  }

  private Action moveForward = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      inputSystem.move(0,-1,"Up");
    }

  };
  private Action moveBack = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      inputSystem.move(0,1,"Down");
    }
  };
  private Action moveLeft = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      inputSystem.move(-1,0,"left");
    }
  };
  private Action moveRight = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      inputSystem.move(1,0,"Right");
    }
  };

  public void setInputSystem(InputSystem inputSystem) {
    this.inputSystem = inputSystem;
  }
  public enum actions {

  }
}
