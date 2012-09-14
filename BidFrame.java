package openbridge;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class BidFrame extends javax.swing.JPanel {

	private Hand       Current;
	private Contract   tmpContract;

	private String     curr_conditions;
	private int        curr_value;
	private int        curr_suit;
	private int        numPass;
	private int        last_bidder;

	private static int FRAME_W    = 420;
	private static int FRAME_H    = 240;

  private JButton    XButton;
  private JButton    XXButton;
  private JButton    passButton;

  private ArrayList<JButton> bidButton = new ArrayList<JButton>();

  ImageIcon[] suitIcon = new ImageIcon[4];

  public BidFrame(Hand Dealer, Contract c) {
    Current         = Dealer;
    tmpContract     = c;
    curr_value      = 0;
    curr_suit       = -1;
    curr_conditions = "None";
    numPass         = 0;

    setPreferredSize(new Dimension(FRAME_W, FRAME_H));
    initComponents();
    runBid();
  }

  private void initComponents() {

    suitIcon[0] = getIcon("s0.gif");
    suitIcon[1] = getIcon("s1.gif");
    suitIcon[2] = getIcon("s2.gif");
    suitIcon[3] = getIcon("s3.gif");

    for(int rank=0;rank<7;rank++) {
      for(int suit=0;suit<5;suit++) {
        JButton thisButton;

        if(suit<4) { thisButton = new JButton( (""+(rank+1)) , suitIcon[suit]); }
        else { thisButton = new JButton( (rank+1) + " NT"); }

        thisButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) { bidAction(e); }
        });
        thisButton.setActionCommand("" + (rank+1) + suit);
        thisButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bidButton.add(thisButton);
      }
    }

    XButton    = new JButton("X");
    XXButton   = new JButton("XX");
    passButton = new JButton("Pass");

    XButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { doDouble(e); }
    });

    XXButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { doRedouble(e); }
    });

    passButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { doPass(e); }
    });

    this.setLayout(new GridLayout(8,5));

    for(JButton b : bidButton) this.add(b);

    this.add( Box.createHorizontalGlue() );
    this.add(XButton);
    this.add(XXButton);
    this.add(passButton);
    this.add( Box.createHorizontalGlue() );


  }

  private ImageIcon getIcon(String fName) {
    return (new ImageIcon(getClass().getResource("/openbridge/cards/"+fName)));
  }

  private void bidAction(ActionEvent e) {
    if(this.Current.isplayer()) {
      int unParsed = Integer.parseInt(e.getActionCommand());
      int suit = unParsed % 10;
      int rank = (unParsed - suit) / 10;

      if(bidValue(rank,suit) > bidValue(curr_value,curr_suit)) {
        disableThrough(rank,suit);
        curr_suit = suit;
        curr_value = rank;
        curr_conditions = "None";
        if(curr_suit == 4) {
          //Grid[row][col].setText(" " + Integer.toString(curr_value) + " NT") ;
        } else {
          //Grid[row][col].setText(" " + Integer.toString(curr_value));
          //Grid[row][col].setIcon(new ImageIcon(getClass().getResource("/openbridge/cards/s"+curr_suit+".gif")));
        }
        //Grid[row][col].setHorizontalTextPosition(JLabel.LEFT);

        numPass = 0;
        last_bidder = 0;
        this.Current = this.Current.getLeft();
        runBid();
      }
    }


  }

  int bidValue(int rank, int suit) {
    return((rank*5)+suit);
  }

  private void disableThrough(int btnNum) {
    for(int curBtn = 0;curBtn<=btnNum;curBtn++) {
      bidButton.get(curBtn).setEnabled(false);
    }
  }

  private void disableThrough(int rank, int suit) {
    disableThrough((int)(rank/5)+suit);
  }

  /*********************************************************************
  * runBid() ***********************************************************
  **********************************************************************
  * Decides what the computer players will bid based on the ************
  * information calculated from the Bid class **************************
  *********************************************************************/
  public void runBid() {
    int tmpSuit = -1;
    int tmpValue = -1;

    while(!this.Current.isplayer()) {
      tmpSuit = this.Current.getMaxSuit();
      tmpValue = this.Current.getMaxValue();

      if(!isContract()) {
        if(tmpValue > curr_value) {
          if(curr_conditions == "DBL" && tmpSuit == curr_suit && clear2double()) {
            curr_conditions = "RDBL";
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          } else if(tmpSuit > curr_suit) {
            if(curr_value == 0) curr_value = 1;
            curr_suit = tmpSuit;
            curr_conditions = "None";
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          } else {
            curr_suit = tmpSuit;
            curr_value += 1;
            curr_conditions = "None";
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          }
        } else if (curr_value != 0 && tmpSuit == curr_suit && tmpValue == curr_value) {
          if(curr_conditions == "None" && clear2double()) {
            curr_conditions = "DBL";
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          } else if(curr_conditions == "DBL" && clear2double()) {
            curr_conditions = "RDBL";
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          } else {
            ++numPass;
            computerPass();
          }
        } else if(tmpValue == curr_value && curr_value != 0) {
          if(tmpSuit > curr_suit) {
            curr_conditions = "None";
            curr_suit = tmpSuit;
            numPass = 0;
            if(Current.getPosition() == "NORTH") last_bidder = 0;
            else last_bidder = 1;
            computerBid();
          } else {
            ++numPass;
            computerPass();
          }
        } else {
          ++numPass;
          computerPass();
        }

        acceptCheck();

        this.Current = this.Current.getLeft();

      } else {
        break;
      }
    }
  }

  private void doDouble(ActionEvent e) {
    if(this.Current.isplayer() && !isContract()) {
      if((curr_conditions == "None") && (curr_value > 0)) {
        curr_conditions = "DBL";
        numPass = 0;
        last_bidder = 0;
        this.Current = this.Current.getLeft();
        runBid();
      }
    }
  }

  private void doRedouble(ActionEvent e) {
    if(this.Current.isplayer() && !isContract()) {
      if(curr_conditions.equals("DBL")) {
        curr_conditions = "RDBL";
        numPass = 0;
        last_bidder = 0;
        this.Current = this.Current.getLeft();
        runBid();
      }
    }
  }

  private void doPass(ActionEvent e) {
    if(this.Current.isplayer() && !isContract()) {
      numPass++;
      acceptCheck();
      this.Current = this.Current.getLeft();
      runBid();
    }
  }

  public boolean isContract() {
    if (tmpContract.getWinner().equals("None")) return false;
    else return true;
  }

  private boolean clear2double() {
    return ( (Current.getPosition() == "NORTH" && last_bidder == 1) ||
             ((Current.getPosition() == "EAST" || Current.getPosition() == "WEST") && last_bidder == 0));
  }

	private void computerBid() {
    if(!curr_conditions.equals("None")) {
      //Grid[row][col].setText(" " + curr_conditions);
    } else {
      if(curr_suit == 4) {
        //Grid[row][col].setText(" " + Integer.toString(curr_value) + " NT") ;
      } else {
        //Grid[row][col].setText(" " + Integer.toString(curr_value));
        //Grid[row][col].setIcon(new ImageIcon(getClass().getResource("/openbridge/cards/s"+curr_suit+".gif")));
      }
      //Grid[row][col].setHorizontalTextPosition(JLabel.LEFT);
    }
    disableThrough(curr_value,curr_suit);
  }

  private void computerPass() {
  }

  private void acceptCheck() {
    if(curr_value == 0 && numPass == 4) tmpContract.setContract("PASS", -1, -1, "None");
    else if(numPass == 3 && curr_value != 0) {
      this.Current = this.Current.getLeft();
      if(curr_conditions == "DBL") this.Current = this.Current.getLeft();
      tmpContract.setContract(Current.getPosition(), curr_value, curr_suit, curr_conditions);
    }
  }


}
