import javax.swing.*;
import java.awt.event.*;
import java.awt.Component;

public class GomokuListener implements MouseListener
{
   private GomokuGUI gui;
   private Gomoku game;
   public GomokuListener (Gomoku game, GomokuGUI gui) {
      this.game = game;
      this.gui = gui;
      gui.addListener (this);
   }

   public void mouseClicked (MouseEvent event) {
      Component objectClicked = event.getComponent();
   
      if (objectClicked instanceof JLabel) {
         JLabel label = (JLabel) objectClicked;		
         int row = gui.getRow(label);
         int column = gui.getColumn (label);
         try {
            game.play(row, column); 
         } catch (InterruptedException e) {       
         }
      }
   
   }

   public void mousePressed (MouseEvent event) {
   }

   public void mouseReleased (MouseEvent event) {
   }

   public void mouseEntered (MouseEvent event) {
   }

   public void mouseExited (MouseEvent event) {
   }
}