/******************************************************************************
 ****************************** BidFrame.java *********************************
 * @author Scott DiTomaso******************************************************
 * Kettering University *******************************************************
 ******************************************************************************
 * Created on December 22, 2009 ***********************************************
 ******************************************************************************/

package openbridge;

/***********
 * Imports *
 ***********/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;

/******************
 * BidFrame class *
 ******************/
public class BidFrame extends javax.swing.JPanel {

    /*************************
     * Private class members *
     *************************/
	private Hand       Current;
	private Contract   tmpContract;
	private JPanel     PopupWindow;
	private JPanel     RightSide;
	private JPanel     LeftSide;
	private JButton    Bid_Button;
	private JButton    Pass_Button;
	private JButton    Dbl_Button;
	private JButton    Rdbl_Button;
	private JSlider    Value_Slider;
	private JSlider    Suit_Slider;
	private JLabel[][] Grid;
	private String     curr_conditions;
	private int        curr_value;
	private int        curr_suit;
	private int        numPass;
	private int        col;
	private int        row;
	private int        last_bidder;

	private static int FRAME_W    = 420;
	private static int FRAME_H    = 200;
	private static int RIGHT_ROW  = 6;
	private static int RIGHT_COL  = 4;
	private static int LOW_VALUE  = 1;
	private static int HIGH_VALUE = 7;
	private static int SUIT_NUM   = 4;
	private static int BTN_W      = 100;
	private static int BTN_H      = 20;

        /***************
         * Constructor *
         ***************/
	BidFrame(Hand Dealer, Contract c) {

	    setPreferredSize(new Dimension(FRAME_W, FRAME_H));
	    setLayout(new GridLayout(1,2));

	    Current = Dealer;
	    tmpContract = c;
	    curr_value = 0;
	    curr_suit = -1;
	    curr_conditions = "None";
	    numPass = 0;
	    col = 0;
	    row = 1;

	    Populate();

	    runBid();

	}

	/**********************************************************************
	 * Populate() *********************************************************
	 **********************************************************************
	 * Sets up the right and left sides of the bid frame ******************
	 **********************************************************************/
	private void Populate() {

	    SetupLeftSide();
	    SetupRightSide();

	    add(LeftSide);
	    add(RightSide);
	}

	/**********************************************************************
	 * SetupLeftSide() ****************************************************
	 **********************************************************************
	 * Sets up the part of the frame where the player makes their bid *****
	 **********************************************************************/
	private void SetupLeftSide() {

	    LeftSide = new JPanel();
	    LeftSide.setSize((FRAME_W / 2), FRAME_H);
	    LeftSide.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
	
	    //first slider
	    Value_Slider = new JSlider();
	    Value_Slider.setOrientation(JSlider.HORIZONTAL);
	    Value_Slider.setPaintLabels(true);
	    Value_Slider.setSnapToTicks(true);
	    Value_Slider.setMinimum(LOW_VALUE);
	    Value_Slider.setValue(1);
	    Value_Slider.setMaximum(HIGH_VALUE);
	    Value_Slider.setMajorTickSpacing(1);

	    //second slider
	    Suit_Slider = new JSlider();
	    Suit_Slider.setOrientation(JSlider.HORIZONTAL);
	    Suit_Slider.setSnapToTicks(true);
	    Suit_Slider.setMaximum(SUIT_NUM);
		Hashtable<Integer,JLabel>  labelTable = new Hashtable<Integer,JLabel>();
		labelTable.put(new Integer(0), new JLabel(new ImageIcon(getClass().getResource("/openbridge/cards/s0.gif"))));
		labelTable.put(new Integer(1), new JLabel(new ImageIcon(getClass().getResource("/openbridge/cards/s1.gif"))));
		labelTable.put(new Integer(2), new JLabel(new ImageIcon(getClass().getResource("/openbridge/cards/s2.gif"))));
		labelTable.put(new Integer(3), new JLabel(new ImageIcon(getClass().getResource("/openbridge/cards/s3.gif"))));
		labelTable.put(new Integer(4), new JLabel("NT"));
	    Suit_Slider.setLabelTable(labelTable);
	    Suit_Slider.setPaintLabels(true);
	    Suit_Slider.setValue(0);

	    //bid button
	    Bid_Button = new JButton("Bid");
	    Bid_Button.setSize(BTN_W, BTN_H);
	    Bid_Button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    BidActionPerformed(evt);
		}
	    });

	    //double button
	    Dbl_Button = new JButton("Double");
	    Dbl_Button.setSize(BTN_W, BTN_H);
	    Dbl_Button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    DoubleActionPerformed(evt);
		}
	    });


	    //redouble button
	    Rdbl_Button = new JButton("Redouble");
	    Rdbl_Button.setSize(BTN_W, BTN_H);
	    Rdbl_Button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    RedoubleActionPerformed(evt);
		}
	    });

	    //pass button
	    Pass_Button = new JButton("Pass");
	    Pass_Button.setSize(BTN_W, BTN_H);
	    Pass_Button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    PassActionPerformed(evt);
		}
	    });


	    LeftSide.add(Value_Slider);
	    LeftSide.add(Suit_Slider);
	    LeftSide.add(Dbl_Button);
	    LeftSide.add(Rdbl_Button);
	    LeftSide.add(Bid_Button);
	    LeftSide.add(Pass_Button);
	}


	/**********************************************************************
	 * SetupRightSide() ***************************************************
	 **********************************************************************
	 * Sets up the part of the frame that displays all of the bids as *****
	 * they are made, both by the player and computer players *************
	 **********************************************************************/
	private void SetupRightSide() {
	
	    RightSide = new JPanel();
	    RightSide.setSize((FRAME_W / 2), FRAME_H);
	    RightSide.setLayout(new GridLayout(RIGHT_ROW,RIGHT_COL));
	    RightSide.setBorder(BorderFactory.createLineBorder(Color.black, 2));

	    Grid = new JLabel[RIGHT_ROW][RIGHT_COL];

	    for(int i=0; i<RIGHT_ROW; ++i) {
		for(int j=0; j<RIGHT_COL; ++j) {
		    Grid[i][j] = new JLabel();
		    Grid[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 1));
		    RightSide.add(Grid[i][j]);
		}
	    }

	    Grid[0][0].setText(" N ");
	    Grid[0][1].setText(" E ");
	    Grid[0][2].setText(" S ");
	    Grid[0][3].setText(" W ");

	    FillBlanks(this.Current);
	}

	/**********************************************************************
	 * runBid() ***********************************************************
	 **********************************************************************
	 * Decides what the computer players will bid based on the ************
	 * information calculated from the Bid class **************************
	 **********************************************************************/
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
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
			    computerBid();
			} else if(tmpSuit > curr_suit) {
			    if(curr_value == 0)
				curr_value = 1;
			    curr_suit = tmpSuit;
			    curr_conditions = "None";
			    numPass = 0;
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
			    computerBid();
			} else {
			    curr_suit = tmpSuit;
			    curr_value += 1;
			    curr_conditions = "None";
			    numPass = 0;
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
			    computerBid();
			}
		    } else if (curr_value != 0 && tmpSuit == curr_suit && tmpValue == curr_value) {
			if(curr_conditions == "None" && clear2double()) {
			    curr_conditions = "DBL";
			    numPass = 0;
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
			    computerBid();
			} else if(curr_conditions == "DBL" && clear2double()) {
			    curr_conditions = "RDBL";
			    numPass = 0;
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
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
			    if(Current.getPosition() == "NORTH")
				last_bidder = 0;
			    else
				last_bidder = 1;
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

	/**********************************************************************
	 * clear2double() *****************************************************
	 **********************************************************************
	 * Checks to see if a computer or the player can double a current bid *
	 **********************************************************************/
	private boolean clear2double() {

	    return ((Current.getPosition() == "NORTH" && last_bidder == 1) ||
		   ((Current.getPosition() == "EAST" || Current.getPosition() == "WEST") && last_bidder == 0));

	}



	/**********************************************************************
	 * FillBlanks() *******************************************************
	 **********************************************************************
	 * This fills in the first row of the grid with --- until the first ***
	 * to bid is the reached **********************************************
	 **********************************************************************/
	private void FillBlanks(Hand Current) {

	    int numBlanks = 0;

	    if(this.Current.getPosition() == "EAST")
		numBlanks = 1;
	    if(this.Current.getPosition() == "SOUTH")
		numBlanks = 2;
	    if(this.Current.getPosition() == "WEST")
		numBlanks = 3;

	    for(int i=0; i<numBlanks; ++i) {
		Grid[1][i].setText(" ----");
		col++;
	    }
	}

	/**********************************************************************
	 * BidActionPerfromed() ***********************************************
	 **********************************************************************
	 * When the player clicks the Bid button, make sure it is their turn **
	 * and their bid is legal based on the current bid information ********
	 **********************************************************************/
	private void BidActionPerformed(ActionEvent evt) {
	
	    int tmp_value;
	    int tmp_suit;


	    if(this.Current.isplayer()) {
		tmp_value = Value_Slider.getValue();
		tmp_suit = Suit_Slider.getValue();

		if((tmp_suit > curr_suit && tmp_value >= curr_value) || (tmp_suit <= curr_suit && tmp_value > curr_value)) {
		    curr_suit = tmp_suit;
		    curr_value = tmp_value;
		    curr_conditions = "None";
		    if(curr_suit == 4) {
			Grid[row][col].setText(" " + Integer.toString(curr_value) + " NT") ;
		    } else {
			Grid[row][col].setText(" " + Integer.toString(curr_value));
			Grid[row][col].setIcon(new ImageIcon(getClass().getResource("/openbridge/cards/s"+curr_suit+".gif")));
		    }
		    Grid[row][col].setHorizontalTextPosition(JLabel.LEFT);

		    numPass = 0;

		    last_bidder = 0;

		    if(col == 3) {
			col = 0;
			row++;
		    } else
			col++;

		    if(row > 5)
			shiftUp();

		    this.Current = this.Current.getLeft();
		    runBid();
		}
	    }
	}

	/**********************************************************************
	 * DoubleActionPerformed() ********************************************
	 **********************************************************************
	 * Allow the user to double the bid if the current bid was done by ****
	 * one of the opponent computer players *******************************
	 **********************************************************************/
	private void DoubleActionPerformed(ActionEvent evt) {

	   if(this.Current.isplayer() && !isContract()) {
	
		if((curr_conditions == "None") && (curr_value > 0)) {
		    curr_conditions = "DBL";

		    Grid[row][col].setText(curr_conditions);

		    numPass = 0;

		    last_bidder = 0;

		    if(col == 3) {
			col = 0;
			row++;
		    } else
			col++;

		    if(row > 5)
			shiftUp();

		    this.Current = this.Current.getLeft();
		    runBid();
		}
	    }
	}

	/**********************************************************************
	 * RedoubleActionPerformed() ******************************************
	 **********************************************************************
	 * Allow the user to redouble the contract if a computer opponent *****
	 * has just doubled the contract **************************************
	 **********************************************************************/
	private void RedoubleActionPerformed(ActionEvent evt) {

	    if(this.Current.isplayer() && !isContract()) {
	
		if(curr_conditions == "DBL") {
		    curr_conditions = "RDBL";

		    Grid[row][col].setText(curr_conditions);

		    numPass = 0;

		    last_bidder = 0;

		    if(col == 3) {
			col = 0;
			row++;
		    } else
			col++;

		    if(row > 5)
			shiftUp();

		    this.Current = this.Current.getLeft();
		    runBid();
		}
	    }
	}

	/**********************************************************************
	 * PassActionPerformed() **********************************************
	 **********************************************************************
	 * Allow the user to pass *********************************************
	 **********************************************************************/
	private void PassActionPerformed(ActionEvent evt) {

	    if(this.Current.isplayer() && !isContract()) {

		++numPass;

		Grid[row][col].setText(" PASS");

		if(col == 3) {
		    col = 0;
		    row++;
		} else
		    col++;

		if(row > 5)
		    shiftUp();

		acceptCheck();

		this.Current = this.Current.getLeft();
		runBid();
	    }
	}

	// shift the bid view window up one line
	/**********************************************************************
	 * shiftUp() **********************************************************
	 **********************************************************************
	 * Shift the cells of the bid display panel up one row ****************
	 **********************************************************************/
	private void shiftUp() {

	    JLabel tmp;

	    for(int i=1; i<5; ++i) {
		for(int j=0; j<4; ++j) {

		    tmp = Grid[i+1][j];
		    String tmp_text = tmp.getText();
		    Icon tmp_icon = tmp.getIcon();
		    Grid[i][j].setText(tmp_text);
		    Grid[i][j].setIcon(tmp_icon);
		}
	    }

	    for(int k=0; k<4; ++k) {
		Grid[5][k].setText(null);
		Grid[5][k].setIcon(null);
	    }

	    row = 5;
	}

	//check to see if there are 3 passes and the bid can be accepted
	/**********************************************************************
	 * acceptCheck() ******************************************************
	 **********************************************************************
	 * Checks to see if there are 3 passes following a bid, if there are **
	 * four passes in a row without a bid, the deal gets passed ***********
	 **********************************************************************/
	private void acceptCheck() {

	    if(curr_value == 0 && numPass == 4)
		tmpContract.setContract("PASS", -1, -1, "None");
	    else if(numPass == 3 && curr_value != 0) {

		this.Current = this.Current.getLeft();
		if(curr_conditions == "DBL") {
		    this.Current = this.Current.getLeft();
		}
		tmpContract.setContract(Current.getPosition(), curr_value, curr_suit, curr_conditions);
	    }
	}

	// check to see if there is an accepted contract
	/**********************************************************************
	 * isContract() *******************************************************
	 **********************************************************************
	 * Checks to see if the bidding thus far has lead to a conrtact *******
	 **********************************************************************/
	public boolean isContract() {

	    if (tmpContract.getWinner() == "None")
		return false;
	    else
		return true;
	}

	/**********************************************************************
	 * computerBid() ******************************************************
	 **********************************************************************
	 * Displays the computer players bid in the right panel ***************
	 **********************************************************************/
	private void computerBid() {

	    if(curr_conditions != "None") {
		Grid[row][col].setText(" " + curr_conditions);
	    } else {
		if(curr_suit == 4) {
			Grid[row][col].setText(" " + Integer.toString(curr_value) + " NT") ;
		} else {
			Grid[row][col].setText(" " + Integer.toString(curr_value));
			Grid[row][col].setIcon(new ImageIcon(getClass().getResource("/openbridge/cards/s"+curr_suit+".gif")));
		}
		Grid[row][col].setHorizontalTextPosition(JLabel.LEFT);
	    }

	    if(col == 3) {
		col = 0;
		row++;
	    } else
		col++;

	    if(row > 5)
		shiftUp();
	}

	/**********************************************************************
	 * computerPass() *****************************************************
	 **********************************************************************
	 * Displays "PASS" in the right panel for a computer player ***********
	 **********************************************************************/
	private void computerPass() {

	    Grid[row][col].setText(" PASS");

	    if(col == 3) {
		col = 0;
		row++;
	    } else
		col++;

	    if(row > 5)
		shiftUp();
	}
}
