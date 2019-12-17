import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class GomokuGUI {
   private final String CONFIGFILE = "config.txt";
   private final Color BACKGROUNDCOLOUR = new Color(255, 166, 77);
   
   private JLabel[][] slots;
   private JFrame mainFrame;
   private ImageIcon[] pieceIcon;
   
   private String[] iconFile;
	
   public final int NUMPIECESTYLE = 3;  // how many png files there are
   public final int NUMROW = 15; 
   public final int NUMCOL = 15;
   
   private final int PIECESIZE = 50;
   private final int FRAMEWIDTH = (int)(NUMCOL * PIECESIZE * 1.07);
   private final int FRAMEHEIGHT = (int)(NUMROW * PIECESIZE * 1.1);
   
   public GomokuGUI () {
      initConfig();
      initImageIcon();
      initSlots();
      createMainFrame();
   }
   
   private void initConfig() {
      try {
         String input;
      	
         BufferedReader in = new BufferedReader(new FileReader(CONFIGFILE));
      
         iconFile = new String [NUMPIECESTYLE]; 
      
         for (int i = 0; i < NUMPIECESTYLE; i++) { 
            input = in.readLine(); 
            iconFile [i] = input; 
         }
         
         in.close();
      } 
      catch (IOException iox) {
         System.out.println("Error reading file.");
      }
   }
      
   private void initImageIcon() {
      pieceIcon = new ImageIcon[NUMPIECESTYLE];
      for (int i = 0; i < (NUMPIECESTYLE); i++) {
         pieceIcon[i] = new ImageIcon(iconFile[i]);
      }
   }

   private void initSlots() {
      slots = new JLabel[NUMROW][NUMCOL];
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j] = new JLabel ();
            slots[i][j].setPreferredSize(new Dimension(PIECESIZE, PIECESIZE));
            slots [i] [j].setHorizontalAlignment (SwingConstants.CENTER);      
         }
      }
   }

   private JPanel createPlayPanel() {
      JPanel panel = new JPanel(); 
      panel.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
      panel.setBackground(BACKGROUNDCOLOUR);
      panel.setLayout(new GridLayout(NUMROW, NUMCOL));
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            panel.add(slots[i][j]);
         }
      }
      return panel;    
   }
   
   private void createMainFrame() {
      mainFrame = new JFrame ("Gomoku");
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JPanel panel = (JPanel)mainFrame.getContentPane();
      panel.setLayout (new BoxLayout(panel,BoxLayout.Y_AXIS));
   
      JPanel bottomPane = new JPanel();
      bottomPane.setLayout(new BoxLayout(bottomPane,BoxLayout.X_AXIS));
      bottomPane.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
      bottomPane.add(createPlayPanel());
      
      panel.add(bottomPane);
   
      mainFrame.setContentPane(panel);
      mainFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
      mainFrame.setVisible(true);
      mainFrame.setResizable(false);
      mainFrame.setDefaultCloseOperation(2);
      
   }

   public int getRow(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = i;
            }
         }
      }
      return result;
   }

   public int getColumn(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = j;
            }
         }
      }
      return result;
   }

   public void addListener (GomokuListener listener) {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j].addMouseListener (listener);
         }
      }
   }

   public void setPiece(int row, int col, int piece) {
      slots[row][col].setIcon(pieceIcon[piece]);
   }
   
   public void gameoverWinWhite(){
      JOptionPane.showMessageDialog(null, "DAYum SON, not bad white. Play again tho.", "I guess you won or something..", JOptionPane.PLAIN_MESSAGE, null); 
   }
   
   public void gameoverWinBlack(){
      JOptionPane.showMessageDialog(null, "DAYum SON, not bad black. Play again tho.", "I guess you won or something..", JOptionPane.PLAIN_MESSAGE, null); 
   }
   
   public void gameoverDraw(){
      JOptionPane.showMessageDialog(null, "DAYum SON, not bad both. Play again tho.", "I guess no one won or something..", JOptionPane.PLAIN_MESSAGE, null); 
   }
   
   public static void main (String[] args) {
      GomokuGUI gui = new GomokuGUI ();
      Gomoku game = new Gomoku (gui);
      GomokuListener listener = new GomokuListener (game, gui);
   }
}