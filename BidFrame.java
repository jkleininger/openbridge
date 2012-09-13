package openbridge;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;

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

    private javax.swing.JLabel bidButton0;
    private javax.swing.JLabel bidButton1;

  private JLabel[][] bidButton = new JLabel[5][7];

    public BidFrame(Hand Dealer, Contract c) {
      setPreferredSize(new Dimension(FRAME_W, FRAME_H));
      initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

      for(int suit=0;suit<5;suit++) {
        for(int rank=0;rank<7;rank++) {
          bidButton[suit][rank] = new JLabel();
        }
      }

      for(int suit=0;suit<5;suit++) {
        for(int rank=0;rank<7;rank++) {
          bidButton[suit][rank].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
          if(suit<4) {
            bidButton[suit][rank].setText(""+(rank+1));
            bidButton[suit][rank].setIcon(new javax.swing.ImageIcon(getClass().getResource("/openbridge/cards/s" + suit + ".gif")));
          }
          else {
            bidButton[suit][rank].setText( (rank+1) + " NT");
          }
        }
      }

      GridLayout theLayout = new GridLayout(8,5);
      this.setLayout(theLayout);

      for(int rank=0;rank<7;rank++) {
        for(int suit=0;suit<5;suit++) {
          this.add(bidButton[suit][rank]);
        }
      }

    }


}
