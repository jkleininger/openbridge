/******************************************************************************
 ******************************** Hand.java ***********************************
 * @author Scott DiTomaso******************************************************
 * Kettering University *******************************************************
 ******************************************************************************
 * Created on December 14, 2009 ***********************************************
 ******************************************************************************/

package openbridge;


/***********
 * Imports *
 ***********/
import java.lang.Object;

/**************
 * Hand class *
 **************/
public class Hand {

    /*************************
     * Private class members *
     *************************/
	private boolean player;
	private Hand left;
	private Card[] cards;
	public int numCrds;
	private String position;
	private Bid bid;
    
    
	/****************
	 * Constructors *
	 ****************/
	Hand () {
           this.player = false;
           this.left = null;
           this.cards = new Card[13];
           this.numCrds = 0;
	}

	Hand(boolean p, String pos) {
           this.player = p;
           this.left = null;
           this.cards = new Card[13];
           this.numCrds = 0;
	   this.position = pos;
	}

	Hand(boolean p, Hand l, String pos) {
           this.player = p;
           this.left = l;
           this.cards = new Card[13];
           this.numCrds = 0;
           this.position = pos;
	}


    /************************
     * Public class members *
     ************************/

	/**********************************************************************
	 * isPlayer() *********************************************************
	 **********************************************************************
	 * Returns true if the current hand is the players hand ***************
	 **********************************************************************/
	public boolean isplayer() {
           return this.player;
	}

	/**********************************************************************
	 * addLeft() **********************************************************
	 **********************************************************************
	 * Adds a given hand to the left of the current hand ******************
	 **********************************************************************/
	public void addLeft(Hand l) {
           this.left = l;
	}

	/**********************************************************************
	 * getLeft() **********************************************************
	 **********************************************************************
	 * Returns Hand that is to the left of the current hand ***************
	 **********************************************************************/
	public Hand getLeft() {
           return this.left;
	}

	/**********************************************************************
	 * getPosition() ******************************************************
	 **********************************************************************
	 * Returns the position of the current hand as a String ***************
	 **********************************************************************/
	public String getPosition() {
           return this.position;
	}

	/**********************************************************************
	 * getNumCrds() *******************************************************
	 **********************************************************************
	 * Returns the number of cards currently in the hand ******************
	 **********************************************************************/
	public int getNumCrds() {
           return this.numCrds;
	}

	/**********************************************************************
	 * getCard() **********************************************************
	 **********************************************************************
	 * Returns the card from a specific spot in the hand ******************
	 **********************************************************************/
	public Card getCard(int spot) {
           return this.cards[spot];
	}

	/**********************************************************************
	 * removeCard() *******************************************************
	 **********************************************************************
	 * Removes a specific card from the hand and shifts the rest of the ***
	 * cards down one spot, then calls moveComputerCards to update the ****
	 * visual representation of the hand **********************************
	 **********************************************************************/
	public void removeCard(OpenBridgeGUI g, int spot) {

	    for(int i=spot; i<(numCrds-1); ++i) {
		cards[i] = cards[i + 1];
	    }
	    numCrds--;

	    g.moveComputerCards(spot, numCrds, this.position);
	}	

	/**********************************************************************
	 * blankCard() ********************************************************
	 **********************************************************************
	 * Instead of removing the card from the hand, make its suit and ******
	 * value both -1, junk values *****************************************
	 **********************************************************************/
	public void blankCard(int spot) {

	    this.cards[spot] = new Card();
	}

	/**********************************************************************
	 * addCard() **********************************************************
	 **********************************************************************
	 * Adds a card to the current hand, putting it in the correct place ***
	 * using the cards getSortValue method ********************************
	 **********************************************************************/
	public void addCard(Card c) {

	   Card temp;
	   Card temp2;
	   boolean flag = true;


	   for(int i=0; i<this.numCrds; ++i) {
		if((flag) && (c.getSortValue() < this.cards[i].getSortValue())) {
		   temp2 = c;
		   flag = false;

		   for(int j=i; j<(this.numCrds+1); ++j) {
			temp = this.cards[j];
			this.cards[j] = temp2;
			temp2 = temp;
		   }
		}
	   }

	   if(flag)
		this.cards[this.numCrds] = c;

	   this.numCrds += 1;
	}

	/**********************************************************************
	 * drawHand() *********************************************************
	 **********************************************************************
	 * Calls display for each card in the hand giving the position of the *
	 * card in the hand as well *******************************************
	 **********************************************************************/
	public void drawHand(OpenBridgeGUI g) {

	   for(int i=0; i<13; ++i)
		(this.cards[i]).display(this.isplayer(), g, this.position, i);
	}

	/**********************************************************************
	 * flipHand() *********************************************************
	 **********************************************************************
	 * Flips all of the cards in the hand over, this is to display the ****
	 * dummy's hand to the table ******************************************
	 **********************************************************************/
	public void flipHand(OpenBridgeGUI g) {

	   for(int i=0; i<this.numCrds; ++i)
		(this.cards[i]).flipCard(g, i);
	}

	/**********************************************************************
	 * resetHand() ********************************************************
	 **********************************************************************
	 * Zeros out the array of cards and sets the number of cards to 0 *****
	 **********************************************************************/
	public void resetHand() {

	    this.cards = new Card[13];
	    this.numCrds = 0;	

	}

	/**********************************************************************
	 * unlock() ***********************************************************
	 **********************************************************************
	 * Unlocks a certain suit so the player can play it, if there are *****
	 * no cards of that suit, unlock all of the cards *********************
	 **********************************************************************/
	public void unlock(OpenBridgeGUI g, int suit) {

	    int spot = -1;
	    int end = 0;

	    if(suit == 4)
		g.Unlock(this.position, 0, (this.numCrds-1));
	    else {
		for(int i=0; i<this.numCrds; ++i) {
		    if(spot == -1 && (this.cards[i]).getNumSuit() == suit) {
			spot = i;
			end = i;
		    } else if((this.cards[i]).getNumSuit() == suit)
			end = i;
		}
		if(spot == -1)
		    g.Unlock(this.position, 0, (this.numCrds-1));
		else
		    g.Unlock(this.position, spot, end);
	    }
	}

	/**********************************************************************
	 * playCard() *********************************************************
	 **********************************************************************
	 * Flips a card over, removes it from the hand, and places it in the **
	 * correct position in the center of the screen ***********************
	 **********************************************************************/
	public void playCard(OpenBridgeGUI g, int num) {

	    (this.cards[num]).flipCard(g, num);
	    removeCard(g, num);
	    g.playComputer(this.position, this.numCrds);

	}

	/**********************************************************************
	 * computerPlay() *****************************************************
	 **********************************************************************
	 * Determines what card the computer should play: *********************
	 **********************************************************************/
	public Card computePlay(OpenBridgeGUI g, Hand dummy, int suit, int trump, Card[] curr_hand, boolean[][] alreadyPlayed) {
	    int tmp_suit = 0;
	    int spot = -1;
	    int dummyPos = -1;
	    int[] places = new int[3];
	    Card tmp_card = new Card();
	    boolean OnSuitFlag = false;
	    boolean TrumpFlag = false;


	    if(this.position == "WEST") {
		places[0] = 3;
		places[1] = 2;
		places[2] = 0;
	    } else if(this.position == "EAST") {
		places[0] = 1;
		places[1] = 0;
		places[2] = 2;
	    } else if(this.position == "NORTH") {
		places[0] = 0;
		places[1] = 3;
		places[2] = 1;
	    } else if(this.position == "SOUTH") {
		places[0] = 2;
		places[1] = 1;
		places[2] = 3;
	    }

	    if(dummy.getPosition() == "WEST") {
		dummyPos = 1;
	    } else if(dummy.getPosition() == "EAST") {
		dummyPos = 3;
	    } else if(dummy.getPosition() == "NORTH") {
		dummyPos = 2;
	    } else if(dummy.getPosition() == "SOUTH") {
		dummyPos = 0;
	    }


	    // if lead
	    if(suit == -1) {
		//if partner dummy
		if((this.position == "WEST" && dummy.getPosition() == "EAST") ||
		   (this.position == "EAST" && dummy.getPosition() == "WEST") ||
		   (this.position == "NORTH" && dummy.getPosition() == "SOUTH")) {

		    for(int i=0; i<dummy.getNumCrds(); ++i) {
			if ((dummy.getCard(i)).getValue() == highestPlayable(alreadyPlayed, dummy.getCard(i).getNumSuit())) {
			    for(int j=0; j<this.numCrds; ++j) {
				if(dummy.getCard(i).getNumSuit() == this.cards[j].getNumSuit()) {
				    return playWinningCard(g, j);
				}
			    } 
			}
		    }
		    for(int k=0; k<this.numCrds; ++k) {
			if(this.cards[k].getValue() == highestPlayable(alreadyPlayed, this.cards[k].getNumSuit()))
			    return playWinningCard(g, k);
		    }
		    return playLowestNontrump(g, trump);

		} else {
		    for(int z=0; z<this.numCrds; ++z) {
			if(this.cards[z].getValue() == highestPlayable(alreadyPlayed, this.cards[z].getNumSuit()))
			    return playWinningCard(g, z);
		    }
		    return playLowestNontrump(g, trump);
		}
	    }
	    //else not lead
	    else {
		for(int z=0; z<this.numCrds; ++z) {
		    //if have suit
		    if((this.cards[z]).getNumSuit() == suit) {

			OnSuitFlag = true;

			//if partner dummy
			if((this.position == "WEST" && dummy.getPosition() == "EAST") ||
			   (this.position == "EAST" && dummy.getPosition() == "WEST") ||
			   (this.position == "NORTH" && dummy.getPosition() == "SOUTH")) {
			    return notLeadPartnerDummy(curr_hand, g, dummy, suit, trump, places, alreadyPlayed);

			} else { //partner not dummy
				//am i the dummy?
				if(this.position == dummy.getPosition()) {
				    if(curr_hand[(places[0])] == null) {
					if(this.cards[z].getValue() > curr_hand[(places[2])].getValue())
					    return playWinningCard(g, z);

				    } else {
					if(curr_hand[(places[1])] == null) {
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						curr_hand[(places[2])].getNumSuit() == suit) ||
					       (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

						return playLowestSuit(g, suit);
					    } else {
						if(this.cards[z].getValue() > curr_hand[(places[2])].getValue() &&
						   curr_hand[(places[2])].getNumSuit() == suit) {
							return playWinningCard(g, z);
						}
					    }
					} else {
					    if(curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue() &&
						curr_hand[(places[0])].getNumSuit() == suit) {
						if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						    curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
						   (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

						    return playLowestSuit(g, suit);
						} else {
						    if(this.cards[z].getValue() > curr_hand[(places[2])].getValue() &&
						       this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit())
							return playWinningCard(g, z);
						}
					    } else {
						if(this.cards[z].getValue() > curr_hand[(places[1])].getValue()) {
						    if(this.cards[z].getValue() > curr_hand[(places[2])].getValue() &&
						       curr_hand[(places[2])].getNumSuit() == suit)
							return playWinningCard(g, z);
						}
					    }
					}
				    }
				}

				//if dummy hasn't played yet
				if(curr_hand[dummyPos] == null) {

				    tmp_card = checkDummy(dummy, suit, trump);

				    //can the dummy be beat?
				    if(tmp_card.getNumSuit() == suit) {
					    if(this.cards[z].getNumSuit() == suit) {
						if(this.cards[z].getValue() > tmp_card.getValue()) {
						    if(curr_hand[(places[0])] != null) {
							if(curr_hand[(places[0])].getNumSuit() == tmp_card.getNumSuit() &&
							    curr_hand[(places[0])].getValue() > tmp_card.getValue()) {

							    if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
								curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
							       (curr_hand[(places[2])].getNumSuit() != curr_hand[(places[0])].getNumSuit() &&
								curr_hand[(places[2])].getNumSuit() != trump)) {
								return playLowestSuit(g, suit);
							    } else {
								if(this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
								   this.cards[z].getValue() > curr_hand[(places[2])].getValue())
								    return playWinningCard(g, z);
							    }
							} else {
							    if(this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
							       this.cards[z].getValue() > curr_hand[(places[2])].getValue())
								return playWinningCard(g, z);
							}
						    } else {
							if(this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
							   this.cards[z].getValue() > curr_hand[(places[2])].getValue()) {
							    return playWinningCard(g, z);
							}
						    }
						}
					    }

				    } else if(tmp_card.getNumSuit() == -1) {
					//$$$can beat other opponent?
					if(curr_hand[(places[0])] != null) {
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
					       (curr_hand[(places[2])].getNumSuit() != curr_hand[(places[0])].getNumSuit() &&
						curr_hand[(places[2])].getNumSuit() != trump)) {
						return playLowestSuit(g, suit);
					    }
					}
					if(this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
					   this.cards[z].getValue() > curr_hand[(places[2])].getValue()) {
						return playWinningCard(g, z);
					}

				    } else { //dummy has trump and i do not
					return playLowestSuit(g, suit);
				    }
				} else { //dummy has played
				    if(curr_hand[(places[0])] == null) {
					    if(this.cards[z].getNumSuit() == suit && this.cards[z].getValue() > curr_hand[(places[2])].getValue()) {
						return playWinningCard(g, z);
					    }
				    } else {
					if(curr_hand[(places[1])] == null) {
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
					        curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
					       (curr_hand[(places[0])].getNumSuit() != curr_hand[(places[2])].getNumSuit() &&
						curr_hand[(places[2])].getNumSuit() != trump)) {
						return playLowestSuit(g, suit);

					    } else {
						if(curr_hand[(places[2])].getNumSuit() == trump && suit != trump) {
						    return playLowestSuit(g, suit);
						} else {
							if(this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
							   this.cards[z].getValue() > curr_hand[(places[2])].getValue()) {
							    return playWinningCard(g, z);
							}
						}
					    }
					} else {
					    //last to play
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue() &&
						curr_hand[(places[0])].getNumSuit() == curr_hand[(places[1])].getNumSuit()) ||
					       (curr_hand[(places[1])].getNumSuit() != trump && curr_hand[(places[0])].getNumSuit() == trump)) {
						if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						    curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
						   (curr_hand[(places[2])].getNumSuit() != trump && curr_hand[(places[0])].getNumSuit() == trump)) {

						    return playLowestSuit(g, suit);
						} else {
						    if(this.cards[z].getValue() > curr_hand[(places[2])].getValue() &&
						       this.cards[z].getNumSuit() == curr_hand[(places[2])].getNumSuit())
							return playWinningCard(g, z);
						}

					    } else { //partner losing
						if((curr_hand[(places[1])].getNumSuit() == trump ||
						    curr_hand[(places[2])].getNumSuit() == trump) &&
						    trump != suit) {
						    return playLowestSuit(g, suit);
						} else {
						    if(this.cards[z].getNumSuit() == suit &&
						       this.cards[z].getValue() > curr_hand[(places[1])].getValue()) {

							if((curr_hand[(places[2])].getNumSuit() == suit &&
							    this.cards[z].getValue() > curr_hand[(places[2])].getValue()) ||
							   (curr_hand[(places[2])].getNumSuit() != suit &&
							    curr_hand[(places[2])].getNumSuit() != trump)) {
							    return playWinningCard(g, z);
							}
						    }
						}
					    }
					}
				    }
				}
			}
		    }
		}

		if(OnSuitFlag) {
		    return playLowestSuit(g, suit);
		}

		if(trump != suit && trump != 4) {
		    for(int i=0; i<this.numCrds; ++i) {
			if(this.cards[i].getNumSuit() == trump) {

			    TrumpFlag = true;

			    //if partner dummy
			    if((this.position == "EAST" && dummy.getPosition() == "WEST") ||
			       (this.position == "WEST" && dummy.getPosition() == "EAST") ||
			       (this.position == "NORTH" && dummy.getPosition() == "SOUTH")) {
				if(curr_hand[(places[0])] == null) {
				    for(int j=0; j<dummy.getNumCrds(); ++j) {
					if(dummy.getCard(j).getNumSuit() == suit && dummy.getCard(j).getValue() > curr_hand[(places[2])].getValue()) {
					    return playLowestCard(g);
					}
				    }
				    return playWinningCard(g, i);
				} else {
				    if(curr_hand[(places[1])] == null) {
					if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
					    curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
					   (curr_hand[(places[0])].getNumSuit() != curr_hand[(places[2])].getNumSuit() &&
					    curr_hand[(places[2])].getNumSuit() != trump)) {

					    return playLowestNontrump(g, trump);
					} else {

					    return playWinningCard(g, i);
					}
				    } else {
				    //last to play
					if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[1])].getNumSuit() &&
					    curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue()) ||
					   (curr_hand[(places[0])].getNumSuit() != curr_hand[(places[1])].getNumSuit() &&
					    curr_hand[(places[0])].getNumSuit() == trump)) {
					    if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
						curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
					       (curr_hand[(places[0])].getNumSuit() != curr_hand[(places[2])].getNumSuit() &&
						curr_hand[(places[2])].getNumSuit() != trump)) {

						return playLowestNontrump(g, trump);
					    } else {
						if(curr_hand[(places[2])].getNumSuit() != trump || (curr_hand[(places[2])].getNumSuit() == trump &&
						   curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {

						    return playWinningCard(g, i);
						}
					    }
					} else {
					    if(curr_hand[(places[2])].getNumSuit() != trump || (curr_hand[(places[2])].getNumSuit() == trump &&
					       curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {

						return playWinningCard(g, i);
					    }
					}
				    }
				}
			    } else {
			    //am i the dummy?
				if(this.position == dummy.getPosition()) {
				    if(curr_hand[(places[0])] == null) {
					return playWinningCard(g, i);

				    } else {
					if(curr_hand[(places[1])] == null) {
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						curr_hand[(places[2])].getNumSuit() == suit) ||
					       (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

						return playLowestNontrump(g, trump);
					    } else {
						if((this.cards[i].getValue() > curr_hand[(places[2])].getValue() &&
						    curr_hand[(places[2])].getNumSuit() == trump) ||
						    curr_hand[(places[2])].getNumSuit() != trump) {
							return playWinningCard(g, i);
						}
					    }
					} else {
					    if((curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue() &&
						curr_hand[(places[0])].getNumSuit() == suit) || curr_hand[(places[0])].getNumSuit() == trump) {
						if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
						    curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
						   (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

						    return playLowestNontrump(g, trump);
						} else {
						    if((this.cards[i].getValue() > curr_hand[(places[2])].getValue() &&
							this.cards[i].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
							curr_hand[(places[2])].getNumSuit() != trump)
							return playWinningCard(g, i);
						}
					    } else {
						if((this.cards[i].getValue() > curr_hand[(places[1])].getValue() &&
						    this.cards[i].getNumSuit() == suit) || curr_hand[(places[1])].getNumSuit() != trump) {
						    if((this.cards[i].getValue() > curr_hand[(places[2])].getValue() &&
						        this.cards[i].getNumSuit() == suit) || curr_hand[(places[2])].getNumSuit() != trump)
							return playWinningCard(g, i);
						}
					    }
					}
				    }
				}

			    //partner not dummy
				if(curr_hand[dummyPos] == null) {
				    tmp_card = checkDummy(dummy, suit, trump);

				    //dummy has to follow suit
				    if(tmp_card.getNumSuit() == suit) {
					if(curr_hand[(places[0])] == null) {
					    return playWinningCard(g, i);
					} else {
					    if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
						curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
					       (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

						if(curr_hand[(places[0])].getValue() > tmp_card.getValue()) {
						    return playLowestNontrump(g, trump);
						} else {
						    return playWinningCard(g, i);
						}
					    } else {
						if(curr_hand[(places[2])].getNumSuit() == suit ||
						  (curr_hand[(places[2])].getNumSuit() == trump &&
						   curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {
						    return playWinningCard(g, i);
						}
					    }
					}
				    } else {
					if(tmp_card.getNumSuit() == trump) {
					    if(curr_hand[(places[0])] == null) {
						if(this.cards[i].getValue() > tmp_card.getValue()) {
						    return playWinningCard(g, i);
						}
					    } else {
						if(this.cards[i].getValue() > tmp_card.getValue()) {
						    if(curr_hand[(places[2])].getNumSuit() == suit ||
						      (curr_hand[(places[2])].getNumSuit() == trump &&
						       curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {

							return playWinningCard(g, i);
						    }
						}
					    }
					}
				    }
				} else { //dummy has played
				    if(curr_hand[(places[0])] == null) {
					return playLowestSuit(g, trump);

				    } else {
					if(curr_hand[(places[1])] == null) {
					    if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
						curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
					       (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {
						return playLowestNontrump(g, trump);
					    } else {
						if(curr_hand[(places[2])].getNumSuit() != trump ||
						  (curr_hand[(places[2])].getNumSuit() == trump &&
						   curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {

						    return playWinningCard(g, i);
						}
					    }
					} else {
					    if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[1])].getNumSuit() &&
						curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue()) ||
					       (curr_hand[(places[0])].getNumSuit() == trump && curr_hand[(places[1])].getNumSuit() != trump)) {
						if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
						    curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
						   (curr_hand[(places[0])].getNumSuit() != curr_hand[(places[2])].getNumSuit() &&
						    curr_hand[(places[2])].getNumSuit() != trump)) {
						    return playLowestNontrump(g, trump);
						} else {
						    if(curr_hand[(places[2])].getNumSuit() != trump ||
						      (curr_hand[(places[2])].getNumSuit() == trump &&
						       curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {
							return playWinningCard(g, i);
						    }
						}
					    } else {
						if(curr_hand[(places[2])].getNumSuit() != trump ||
						  (curr_hand[(places[2])].getNumSuit() == trump &&
						   curr_hand[(places[2])].getValue() < this.cards[i].getValue())) {
						    return playWinningCard(g, i);
						}
					    }
					}
				    }
				}
			    }
			}
		    }

		    if(TrumpFlag) {
			return playLowestNontrump(g, trump);
		    }

		    return playLowestCard(g);
		}
		return playLowestCard(g);
	    }
	}


	private Card notLeadPartnerDummy(Card[] curr_hand, OpenBridgeGUI g, Hand dummy, int suit, int trump, int[] places, boolean[][] alreadyPlayed) {
	    Card tmp_card = new Card();

	    //partner hasn't played
	    if(curr_hand[(places[0])] == null) {
		//can partner beat places[2]?
		for(int i=0; i<dummy.getNumCrds(); ++i) {
		    if((dummy.getCard(i)).getNumSuit() == suit && (dummy.getCard(i)).getValue() == highestPlayable(alreadyPlayed, suit)) {
			if((dummy.getCard(i)).getValue() > curr_hand[(places[2])].getValue()) {
			    return playLowestSuit(g, suit);
			}
		    }
		}
		for(int j=0; j<this.numCrds; ++j) {
		    if(this.cards[j].getNumSuit() == suit && this.cards[j].getValue() == highestPlayable(alreadyPlayed, suit)) {
			if(this.cards[j].getValue() > curr_hand[(places[2])].getValue()) {
			    return playWinningCard(g, j);
			}
		    } else {
			return playLowestSuit(g, suit);
		    }
		}
	    }
	    //partner has played
	    else {
		if(curr_hand[(places[1])] != null) {
		    //if partner winnig
		    if((curr_hand[(places[0])].getValue() > curr_hand[(places[1])].getValue() &&
			curr_hand[(places[0])].getNumSuit() == curr_hand[(places[1])].getNumSuit()) ||
		       (curr_hand[(places[0])].getNumSuit() == trump && curr_hand[(places[1])].getNumSuit() != trump)) {
			if((curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue() &&
			    curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit()) ||
			   (curr_hand[(places[0])].getNumSuit() == trump && curr_hand[(places[2])].getNumSuit() != trump)) {

			    //play lowest on suit
			    return playLowestSuit(g, suit);
			} else {
			    if(suit != trump && curr_hand[(places[2])].getNumSuit() == trump) {
				return playLowestSuit(g, suit);
			    } else {
				for(int k=0; k<this.numCrds; ++k) {
				    if(this.cards[k].getValue() > curr_hand[(places[2])].getValue() &&
				       this.cards[k].getNumSuit() == curr_hand[(places[2])].getNumSuit()) {
					return playWinningCard(g, k);
				    }
				}
				return playLowestSuit(g, suit);
			    }
			}
		    } else { //partner losing

			//if cannot win
			if(suit != trump && curr_hand[(places[2])].getNumSuit() == trump) {
			    //play lowest on suit
			    return playLowestSuit(g, suit);
			}
			else {
			    //else if can win
			    for(int l=0; l<this.numCrds; ++l) {
				if(this.cards[l].getNumSuit() == suit) {
				    if((this.cards[l].getValue() > curr_hand[(places[1])].getValue() && curr_hand[(places[1])].getNumSuit() == suit) &&
				       (this.cards[l].getValue() > curr_hand[(places[2])].getValue() && curr_hand[(places[2])].getNumSuit() == suit)) {
					return playWinningCard(g, l);
				    }
				}
			    }
			    //else throw low
			    return playLowestSuit(g, suit);
			}
		    }
		} else {
		    if((curr_hand[(places[0])].getNumSuit() == curr_hand[(places[2])].getNumSuit() &&
			curr_hand[(places[0])].getValue() > curr_hand[(places[2])].getValue()) ||
		       (curr_hand[(places[2])].getNumSuit() != suit && curr_hand[(places[2])].getNumSuit() != trump)) {

			//play lowest on suit
			return playLowestSuit(g, suit);
		    } else {
			if(suit != trump && curr_hand[(places[2])].getNumSuit() == trump) {
			    return playLowestSuit(g, suit);
			} else {
			    for(int m=0; m<this.numCrds; ++m) {
				if(this.cards[m].getValue() > curr_hand[(places[2])].getValue() &&
				   this.cards[m].getNumSuit() == curr_hand[(places[2])].getNumSuit()) {
				    return playWinningCard(g, m);
				}
			    }
			    return playLowestSuit(g, suit);
			}
		    }
		}
	    }
	    return tmp_card;
	}

	/**********************************************************************
	 * highestPlayable() **************************************************
	 **********************************************************************
	 * Determines the value of the highest card not yet played for a ******
	 * given suit based on the previous hands *****************************
	 **********************************************************************/
	private int highestPlayable(boolean[][] aP, int suit) {

	    for(int i=0; i<13; ++i) {
		if(aP[suit][i] == false) {
		    return (14 - i);
		}
	    }
	    return -1;
	}

	/**********************************************************************
	 * playLowestSuit() ***************************************************
	 **********************************************************************
	 * Plays the lowest value card of a given suit ************************
	 **********************************************************************/
	private Card playLowestSuit(OpenBridgeGUI g, int suit) {
	    Card tmp_card = new Card();

	    for(int i=0; i<this.numCrds; ++i) {
		if((this.cards[i]).getNumSuit() == suit) {
		    tmp_card = this.cards[i];
		    playCard(g, i);
		    return tmp_card;
		}
	    }
	    return tmp_card;
	}

	/**********************************************************************
	 * playLowestNonTrump() ***********************************************
	 **********************************************************************
	 * Determines the lowest value card that is not of trump, if no card **
	 * exists in the hand, the lowest value trump is played ***************
	 **********************************************************************/
	private Card playLowestNontrump(OpenBridgeGUI g, int trump) {
	    Card tmp_card = new Card();
	    int spot = 0;

	    tmp_card = this.cards[spot];

	    for(int i=0; i<this.numCrds; ++i) {
		if(this.cards[i].getNumSuit() != trump) {
		    if(tmp_card.getNumSuit() == trump) {
			tmp_card = this.cards[i];
			spot = i;
		    } else if(this.cards[i].getValue() < tmp_card.getValue()) {
			tmp_card = this.cards[i];
			spot = i;
		    }
		}
	    }

	    return playWinningCard(g, spot);
	}

	/**********************************************************************
	 * playLowestCard() ***************************************************
	 **********************************************************************
	 * Determines and plays the lowest value card in the hand  ************
	 * regardless of the cards suit ***************************************
	 **********************************************************************/
	private Card playLowestCard(OpenBridgeGUI g) {
	    Card tmp_card = new Card();
	    int spot = 0;

	    tmp_card = this.cards[0];

	    for(int i=1; i<this.numCrds; ++i) {
		if(this.cards[i].getValue() < tmp_card.getValue()) {
		    tmp_card = this.cards[i];
		    spot = i;
		}
	    }
	    return playWinningCard(g, spot);
	}

	/**********************************************************************
	 * playWinningCard() **************************************************
	 **********************************************************************
	 * Plays a card in a specific position of the hand and returns ********
	 * the same card ******************************************************
	 **********************************************************************/
	private Card playWinningCard(OpenBridgeGUI g, int pos) {
	    Card tmp_card = new Card();

	    tmp_card = this.cards[pos];
	    playCard(g, pos);
	    return tmp_card;
	}

	/**********************************************************************
	 * checkDummy() *******************************************************
	 **********************************************************************
	 * Returns the highest on suit card in the dummys hand, or the ********
	 * highest trump in the dummys hand, if the dummy has no cards of *****
	 * the suit or trump, a blank card is returned ************************
	 **********************************************************************/
	private Card checkDummy(Hand dummy, int suit, int trump) {
	    Card tmp_card = new Card();

	    //does dummy have any of the suit lead?
	    for(int i=0; i<dummy.getNumCrds(); ++i) {
		if(dummy.getCard(i).getNumSuit() == suit) {
		    if(dummy.getCard(i).getValue() > tmp_card.getValue())
			tmp_card = dummy.getCard(i);
		}
	    }
	    //dummy doesn't have any suit lead, what about trump
	    if(tmp_card.getValue() == -1) {
		for(int j=0; j<dummy.getNumCrds(); ++j) {
		    if(dummy.getCard(j).getNumSuit() == trump) {
			tmp_card = dummy.getCard(j);
		    }
		}
	    }

	    return tmp_card;
	}

	/**********************************************************************
	 * bidStuff() *********************************************************
	 **********************************************************************
	 * Sets up the bid object for the hand and determines what the ********
	 * maximum the hand should be bidding *********************************
	 **********************************************************************/
	public void bidstuff() {
		bid = new Bid();
		bid.calculateStats(this.cards);
		//bid.debug();
	}

	/**********************************************************************
	 * getMaxSuit() *******************************************************
	 **********************************************************************
	 * Returns the suit the hand should bid as an Int *********************
	 **********************************************************************/
	public int getMaxSuit() {
	    return bid.getFinalSuit();
	}

	/**********************************************************************
	 * getMaxValue() ******************************************************
	 **********************************************************************
	 * Returns the number of tricks the hand should bid as an Int *********
	 **********************************************************************/
	public int getMaxValue() {
	    return bid.getFinalNum();
	}
}
