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

	private static int FRAME_W    = 480;
	private static int FRAME_H    = 240;

  private JPanel     bidButtonPanel;
  private JPanel     bidListPanel;

  private JList      bidList;
  private DefaultListModel bidListModel;

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

    bidButtonPanel = new JPanel();
    bidListPanel = new JPanel();

    bidButtonPanel.setLayout(new GridLayout(8,5));
    bidListPanel.setLayout(new FlowLayout());

    bidButtonPanel.setPreferredSize(new Dimension(FRAME_W-100, FRAME_H));
    bidListPanel.setPreferredSize(new Dimension(100, FRAME_H));

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

    bidListModel = new DefaultListModel();
    bidList    = new JList(bidListModel);

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

    this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

    for(JButton b : bidButton) bidButtonPanel.add(b);
    bidButtonPanel.add( Box.createHorizontalGlue() );
    bidButtonPanel.add(XButton);
    bidButtonPanel.add(XXButton);
    bidButtonPanel.add(passButton);
    bidButtonPanel.add( Box.createHorizontalGlue() );

    bidListPanel.add(bidList);

    this.add(bidButtonPanel);
    this.add(bidListPanel);

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
          displayBid(this.Current,curr_value,curr_suit,curr_conditions);
        } else {
          displayBid(this.Current,curr_value,curr_suit,curr_conditions);
        }

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
    int curBtn = 0;
    for(;curBtn<=btnNum;curBtn++) {
      bidButton.get(curBtn).setEnabled(false);
    }
    String theText = bidButton.get(btnNum).getText();
    theText = Current.getPosition().substring(0,1) + " " + theText;
    bidButton.get(btnNum).setText(theText);
  }

  private void disableThrough(int rank, int suit) {
    disableThrough(((rank-1)*5)+suit);
  }

  /*********************************************************************
  * runBid()                                                           *
  **********************************************************************
  * Decides what the computer players will bid based on the            *
  * information calculated from the Bid class                          *
  *********************************************************************/
  public void runBid() {
    int tmpSuit = -1;
    int tmpValue = -1;

    while(!this.Current.isplayer()) {
      tmpSuit = this.Current.getMaxSuit();
      tmpValue = this.Current.getMaxValue();

      if(!isContract()) {
        if(tmpValue > curr_value) {

          if(curr_conditions.equals("DBL") && tmpSuit == curr_suit && clear2double()) {
            curr_conditions = "RDBL";
          } else if(tmpSuit > curr_suit) {
            if(curr_value == 0) curr_value = 1;
            curr_conditions = "None";
          } else {
            curr_value += 1;
            curr_conditions = "None";
          }
          curr_suit = tmpSuit;
          numPass = 0;
          last_bidder=Current.getPosition().equals("NORTH")?0:1;
          computerBid();

        } else if (curr_value != 0 && tmpSuit == curr_suit && tmpValue == curr_value) {

          if(clear2double()) {
            curr_conditions=curr_conditions.equals("None")?"DBL":(curr_conditions.equals("DBL")?"RDBL":"None");
            numPass=0;
            last_bidder=Current.getPosition().equals("NORTH")?0:1;
            computerBid();
          } else {
            numPass++;
            computerPass();
          }

        } else if(tmpValue == curr_value && curr_value != 0) {

          if(tmpSuit > curr_suit) {
            curr_conditions = "None";
            curr_suit = tmpSuit;
            numPass = 0;
            last_bidder=Current.getPosition().equals("NORTH")?0:1;
            computerBid();
          } else {
            numPass++;
            computerPass();
          }

        } else {
          numPass++;
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
      displayBid(this.Current,-1,-1,curr_conditions);
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
      displayBid(this.Current,-1,-1,curr_conditions);
      //double, redouble
    } else {
      if(curr_suit == 4) {
        //computer bid NT
      } else {
        //computer bid trump
      }
      displayBid(this.Current,curr_value,curr_suit,curr_conditions);
      disableThrough(curr_value,curr_suit);
    }
  }

  private void computerPass() {
    displayBid(this.Current,-1,-1,curr_conditions);
  }

  private void acceptCheck() {
    if(curr_value == 0 && numPass == 4) tmpContract.setContract("PASS", -1, -1, "None");
    else if(numPass == 3 && curr_value != 0) {
      this.Current = this.Current.getLeft();
      if(curr_conditions == "DBL") this.Current = this.Current.getLeft();
      tmpContract.setContract(Current.getPosition(), curr_value, curr_suit, curr_conditions);
    }
  }

  private void displayBid(Hand player, int rank, int suit, String condition) {
    String theString = new String();
    if(rank>-1 && suit>-1) {
      theString = (player.getPosition() + ": " + curr_value + " " + curr_suit);
    } else {
      theString = (player.getPosition() + ": " + condition);
    }
    //System.out.println(theString);
    bidListModel.addElement(theString);
  }


}
