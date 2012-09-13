package openbridge;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class BidFrame extends javax.swing.JPanel {

	private String     curr_conditions;
	private int        curr_value;
	private int        curr_suit;
	private int        numPass;
	private int        col;
	private int        row;
	private int        last_bidder;

	private static int FRAME_W    = 420;
	private static int FRAME_H    = 240;
	private static int RIGHT_ROW  = 6;
	private static int RIGHT_COL  = 4;
	private static int LOW_VALUE  = 1;
	private static int HIGH_VALUE = 7;
	private static int SUIT_NUM   = 4;
	private static int BTN_W      = 100;
	private static int BTN_H      = 20;

  private ArrayList<JButton> bidButton = new ArrayList<JButton>();

  public BidFrame(Hand Dealer, Contract c) {
    setPreferredSize(new Dimension(FRAME_W, FRAME_H));
    initComponents();
  }

  private void initComponents() {

    for(int rank=0;rank<7;rank++) {
      for(int suit=0;suit<5;suit++) {
        JButton thisButton;

        if(suit<4) {
          thisButton = new JButton( (""+(rank+1)) , new javax.swing.ImageIcon(getClass().getResource("/openbridge/cards/s" + suit + ".gif")));
        }
        else {
          thisButton = new JButton( (rank+1) + " NT");
        }

        thisButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            bidAction(evt);
          }
        });
        thisButton.setActionCommand("" + rank + suit);
        thisButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bidButton.add(thisButton);
      }
    }

    GridLayout theLayout = new GridLayout(8,5);
    this.setLayout(theLayout);

    for(JButton b : bidButton) this.add(b);

  }

  private void bidAction(ActionEvent e) {
    int unParsed = Integer.parseInt(e.getActionCommand());
    int suit = unParsed % 10;
    int rank = (unParsed - suit) / 10;
    disableThrough((rank*5)+suit);
  }

  private void disableThrough(int btnNum) {
    for(int curBtn = 0;curBtn<=btnNum;curBtn++) {
      bidButton.get(curBtn).setEnabled(false);
    }
  }


}
