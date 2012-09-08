package openbridge;

/**********************
 * WorkerThread class *
 **********************/
class WorkerThread implements Runnable {

    /*************************
     * Private class members *
     *************************/
	private Contract contract;
	private BidFrame BidFrame;
	private OpenBridgeGUI window;
	private Hand dealer;
	private Hand declarer;
	private Hand dummy;
	private Card[] curr_hand = new Card[4];
	private boolean[][] alreadyPlayed = new boolean[4][13];
	private volatile boolean stop_var;
	private int nsTricks;
	private int weTricks;
	private int nsScore;
	private int weScore;
	private int nsAboveLine;
	private int weAboveLine;
	private int nsGames;
	private int weGames;
	private int vulnerable;

    /***************
     * Constructor *
     ***************/
	public WorkerThread(OpenBridgeGUI w, Hand d) {
	    this.window = w;
	    this.dealer = d;
	    this.nsTricks = 0;
	    this.weTricks = 0;
	    this.nsScore = 0;
	    this.weScore = 0;
	    this.nsAboveLine = 0;
	    this.weAboveLine = 0;
	    this.nsGames = 0;
	    this.weGames = 0;
	    this.vulnerable = 0x0000;
	    this.stop_var = false;

	    clearAlreadyPlayed();
	}

    /*************************
     * Private class methods *
     *************************/

	/**********************************************************************
	 * computerDeclarer() *************************************************
	 **********************************************************************
	 * Determines the order of play if either of the computer players won *
	 * the bid or the previous hand ***************************************
	 **********************************************************************/
	private void computerDeclarer(String position) {
	    int hand_suit = -1;

	    if(position.equals("WEST")) {
		curr_hand[1] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		hand_suit = curr_hand[1].getNumSuit();

		declarer = declarer.getLeft();
		if(dummy.getPosition().equals("NORTH")) {
		    declarer.unlock(window, hand_suit);

		    while(window.isReady()) { zzz(0); }

		    curr_hand[2] = declarer.getCard(window.getLastPlayed());
		    declarer.blankCard(window.getLastPlayed());
		} else {
          //zzz(600);
		    curr_hand[2] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		}

          //zzz(600);

		declarer = declarer.getLeft();
		curr_hand[3] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

		declarer = declarer.getLeft();
		if(dummy.getPosition().equals("SOUTH")) {
          //zzz(600);

		    curr_hand[0] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		} else {
		    declarer.unlock(window, hand_suit);

		    while(window.isReady()) { zzz(0); }

		    curr_hand[0] = declarer.getCard(window.getLastPlayed());
		    declarer.blankCard(window.getLastPlayed());
		}

		declarer = declarer.getLeft();

	    } else if(position.equals("EAST")) {
		curr_hand[3] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		hand_suit = curr_hand[3].getNumSuit();
		declarer = declarer.getLeft();
		if(dummy.getPosition().equals("SOUTH")) {
			//zzz(600);
			curr_hand[0] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		} else {
			declarer.unlock(window, hand_suit);
			while(window.isReady()) { zzz(0); }
			curr_hand[0] = declarer.getCard(window.getLastPlayed());
			declarer.blankCard(window.getLastPlayed());
		}

          //zzz(600);

		declarer = declarer.getLeft();
		curr_hand[1] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

		declarer = declarer.getLeft();
		if(dummy.getPosition().equals("NORTH")) {
		    declarer.unlock(window, hand_suit);

		    while(window.isReady()) { zzz(0); }

		    curr_hand[2] = declarer.getCard(window.getLastPlayed());
		    declarer.blankCard(window.getLastPlayed());
		} else {
          //zzz(600);

		    curr_hand[2] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
		}

		declarer = declarer.getLeft();
	    }
	}

	/**********************************************************************
	 * playerDeclarer() ***************************************************
	 **********************************************************************
	 * Determines the order of events if the player won the bidding or ****
	 * the previous hand **************************************************
	 **********************************************************************/
	private void playerDeclarer() {
	    int hand_suit = -1;

	    if(dummy.getPosition() == "SOUTH") {
		curr_hand[0] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
	    } else {
		declarer.unlock(window, 4);

		while(window.isReady()) { zzz(0); }

		curr_hand[0] = declarer.getCard(window.getLastPlayed());
		declarer.blankCard(window.getLastPlayed());
	    }
	    hand_suit = curr_hand[0].getNumSuit();

          //zzz(600);

	    declarer = declarer.getLeft();
	    curr_hand[1] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

	    declarer = declarer.getLeft();
	    if(dummy.getPosition() == "NORTH") {
		declarer.unlock(window, hand_suit);

		while(window.isReady()) { zzz(0); }

		curr_hand[2] = declarer.getCard(window.getLastPlayed());
		declarer.blankCard(window.getLastPlayed());
	    } else {
          //zzz(600);

		curr_hand[2] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
	    }

          //zzz(600);

	    declarer = declarer.getLeft();
	    curr_hand[3] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

	    declarer = declarer.getLeft();
	}

	/**********************************************************************
	 * partnerDeclarer() **************************************************
	 **********************************************************************
	 * Determines the order of events if the players partner won the bid **
	 * or the previous hand ***********************************************
	 **********************************************************************/
	private void partnerDeclarer() {
	    int hand_suit = -1;

	    if(dummy.getPosition() == "NORTH") {
		declarer.unlock(window, 4);

		while(window.isReady()) { zzz(0); }

		curr_hand[2] = declarer.getCard(window.getLastPlayed());
		declarer.blankCard(window.getLastPlayed());
	    } else {
		curr_hand[2] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
	    }
	    hand_suit = curr_hand[2].getNumSuit();

          //zzz(600);

	    declarer = declarer.getLeft();
	    curr_hand[3] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

	    declarer = declarer.getLeft();
	    if(dummy.getPosition() == "SOUTH") {
          //zzz(600);

		curr_hand[0] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);
	    } else {
		declarer.unlock(window, hand_suit);

		while(window.isReady()) { zzz(0); }

		curr_hand[0] = declarer.getCard(window.getLastPlayed());
		declarer.blankCard(window.getLastPlayed());
	    }

          //zzz(600);

	    declarer = declarer.getLeft();
	    curr_hand[1] = declarer.computePlay(window, dummy, hand_suit, contract.getTrump(), curr_hand, alreadyPlayed);

	    declarer = declarer.getLeft();
	}

	/**********************************************************************
	 * findWinner() *******************************************************
	 **********************************************************************
	 * Determines who won the hand based on the suit and trump, also ******
	 * the cards that were played are remembered by the computer players **
	 **********************************************************************/
	private void findWinner() {
	    int start = -1;
	    int pos = -1;
	    int entered;
	    int trump = contract.getTrump();


	    for(int i=0; i<4; ++i) {
		alreadyPlayed[curr_hand[i].getNumSuit()][(14 - curr_hand[i].getValue())] = true;
	    }


	    if(declarer.getPosition().equals("WEST"))
		start = 1;
	    else if(declarer.getPosition().equals("EAST"))
		start = 3;
	    else if(declarer.getPosition().equals("NORTH"))
		start = 2;
	    else
		start = 0;

	    entered = start;

            for(pos=start+1;pos>3;pos-=4);

	    for(int j=0; j<3; ++j) {
		if((curr_hand[pos].getNumSuit() == trump && curr_hand[start].getNumSuit() != trump) ||
		   (curr_hand[pos].getNumSuit() == curr_hand[start].getNumSuit() && curr_hand[pos].getValue() > curr_hand[start].getValue())) {
			start = pos;
		}
		pos=pos<3?pos++:0;
	    }

	    if(start % 2 == 0) {
		nsTricks++;
		window.updateTricks(0, nsTricks);
	    } else {
		weTricks++;
		window.updateTricks(1, weTricks);
	    }

	    if(start < entered) {
		for(int k=0; k < (4-(entered-start)); ++k) {
		    declarer = declarer.getLeft();
		}
	    } else {
		for(int k=0; k<(start-entered); ++k) {
		    declarer = declarer.getLeft();
		}
	    }
	}

	/**********************************************************************
	 * clearAlreadyPlayed()************************************************
	 **********************************************************************
	 * Sets the values for all cards to false, indicating they have not ***
	 * been played yet for this round *************************************
	 **********************************************************************/
	private void clearAlreadyPlayed() {

	    for(int i=0; i<4; ++i)
		for(int j=0; j<13; ++j)
		    this.alreadyPlayed[i][j] = false;
	}

	/**********************************************************************
	 * calculateScore() ***************************************************
	 **********************************************************************
	 * Determines who gets what points at the end of the round based on ***
	 * the contract and the number of tricks taken ************************
	 **********************************************************************/
	private void calculateScore() {
	    int     tricks = 0;
	    int     below = 0;
	    int     above = 0;
	    int     tmp = 0;
	    int     multi = 1;
	    int     redouble = 0;
	    boolean vuln = false;

	    if(contract.getWinner() == "SOUTH" || contract.getWinner() == "NORTH") {
          tricks = nsTricks;
		  if((vulnerable & 0x0001) != 0) vuln = true;
	    } else if(contract.getWinner() == "WEST" || contract.getWinner() == "EAST") {
		  tricks = weTricks;
		  if((vulnerable & 0x0002) != 0) vuln = true;
	    }

	    if(contract.getConditions() == "DBL") {
          multi = 2;
		  above = 50;
	    } else if(contract.getConditions() == "RDBL") {
		  redouble = 1;
		  multi = 4;
	    }


	    if((tricks - 6) >= contract.getTricks()) {
		if(contract.getTrump() == 4) {
		    tmp = 30;
		    below = (40 + ((contract.getTricks() - 1) * 30)) * multi;
		}
		else if(contract.getTrump() == 0 || contract.getTrump() == 1) {
		    tmp = 20;
		    below = (contract.getTricks() * 20) * multi;
		}
		else if(contract.getTrump() == 2 || contract.getTrump() == 3) {
		    tmp = 30;
		    below = (contract.getTricks() * 30) * multi;
		}

		if(vuln) {
		    if(contract.getConditions() == "DBL")
			tmp = 200;
		    else if(contract.getConditions() == "RDBL")
			tmp = 400;
		} else {
		    if(contract.getConditions() == "DBL")
			tmp = 100;
		    else if(contract.getConditions() == "RDBL")
			tmp = 200;
		}
		above += ((tricks - 6) - contract.getTricks()) * tmp;

	    } else {
		if(vuln) {
		    if(contract.getConditions() == "DBL" || contract.getConditions() == "RDBL") {
			above = (200 + ((contract.getTricks() - (tricks -6) -1) * 300)) * (redouble * 2);
		    } else {
			above = (100 * (contract.getTricks() - (tricks - 6)));
		    }
		} else {
		    if(contract.getConditions() == "DBL" || contract.getConditions() == "RDBL") {
			if((contract.getTricks() - (tricks - 6)) > 3) {
			    above = (500 + ((contract.getTricks() - (tricks - 6) - 3) * 300)) * (redouble * 2);
			} else {
			    above = (100 + ((contract.getTricks() - (tricks - 6) - 1) * 200)) * (redouble * 2);
			}
		    } else {
			above = (50 * (contract.getTricks() - (tricks - 6)));
		    }
		}
	    }

	    if(contract.getWinner() == "SOUTH" || contract.getWinner() == "NORTH") {
		if((tricks - 6) >= contract.getTricks()) {
		    window.belowLine(0, below);
		    window.aboveLine(0, above);
		    nsAboveLine += above;
		    nsAboveLine += below;
		    nsScore += below;

		    if(contract.getTricks() == 7) {
			if(vuln) {
			    window.aboveLine(0, 1500);
			    nsAboveLine += 1500;
			} else {
			    window.aboveLine(0, 1000);
			    nsAboveLine += 1000;
			}
		    } else if(contract.getTricks() == 6) {
			if(vuln) {
			    window.aboveLine(0, 750);
			    nsAboveLine += 750;
			} else {
			    window.aboveLine(0, 500);
			    nsAboveLine += 500;
			}
		    }
		} else {
		    window.aboveLine(1, above);
		    weAboveLine += above;
		}
	    } else if(contract.getWinner() == "WEST" || contract.getWinner() == "EAST") {
		if((tricks - 6) >= contract.getTricks()) {
		    window.belowLine(1, below);
		    window.aboveLine(1, above);
		    weAboveLine += above;
		    weAboveLine += below;
		    weScore += below;

		    if(contract.getTricks() == 7) {
			if(vuln) {
			    window.aboveLine(0, 1500);
			    weAboveLine += 1500;
			} else {
			    window.aboveLine(0, 1000);
			    weAboveLine += 1000;
			}
		    } else if(contract.getTricks() == 6) {
			if(vuln) {
			    window.aboveLine(0, 750);
			    weAboveLine += 750;
			} else {
			    window.aboveLine(0, 500);
			    weAboveLine += 500;
			}
		    }
		} else {
		    window.aboveLine(0, above);
		    nsAboveLine += above;
		}
	    }

            System.out.println("setting all trick counts to 0");
	    nsTricks = 0;
	    weTricks = 0;
	    window.updateTricks(2, 0);

	    window.clearContract();
	    window.removeDeclarer(contract.getWinner());
	    window.removeDealer(dealer.getPosition());
	}

/*********************************************************************
* getRubberBonus() ***************************************************
**********************************************************************
* Determines the bonus to be applied for winning the rubber **********
* depending on how many games were won by ****************************
*********************************************************************/
  private void getRubberBonus(int pos) {
    if(pos == 0) {
      if(weGames == 0) {
        nsAboveLine += 700;
        window.belowLine(0, 700);
      } else if(weGames == 1) {
        nsAboveLine += 500;
        window.belowLine(0, 500);
      }
    } else if(pos == 1) {
      if(nsGames == 0){
        weAboveLine += 700;
        window.belowLine(1, 700);
      } else if(nsGames == 1) {
        weAboveLine += 500;
        window.belowLine(1, 500);
      }
    }
  }


    /************************
     * Public class methods *
     ************************/

	/**********************************************************************
	 * stopThread() *******************************************************
	 **********************************************************************
	 * Changes the value of the volatile variable stop_var to close the ***
	 * run method and stop the thread *************************************
	 **********************************************************************/
  public void stopThread() { stop_var = true; }

	/**********************************************************************
	 * run() **************************************************************
	 **********************************************************************
	 * Run is the part of the thread that plays the game.  Starting with **
	 * the bidding, then each and played, calculating the score and *******
	 * starting over again until there is a winner ************************
	 **********************************************************************/
  public void run() {

    while(nsGames <= 2 && weGames <= 2) {
      while(nsScore < 100 && weScore < 100) {
        contract = new Contract();
        alreadyPlayed = new boolean[4][13];
        clearAlreadyPlayed();

        for(int i=0; i<4; ++i) {
          dealer.resetHand();
          dealer = dealer.getLeft();
        }

        window.showVulnerable(vulnerable);
        Deck deck = new Deck();
        deck.shuffle();
        dealer = dealer.getLeft();
        deck.deal(dealer);
        window.placeDealer(dealer);

        for(int j=0; j<4; ++j) {
          dealer.drawHand(window);
          dealer.bidstuff();
          dealer = dealer.getLeft();
        }

        window.repaint();
        window.Lock("ALL");
        window.createBidFrame(dealer, contract);

        while (!(contract.isFinal())) { zzz(0); }

        zzz(2000);

        window.closeBidFrame();

        if(!contract.getWinner().equals("PASS")) {
          window.ShowContract(contract);
          declarer = dealer;
        }

        while(declarer.getPosition() != contract.getWinner()) {
          declarer = declarer.getLeft();
        }

        window.placeDeclarer(declarer);

        declarer = declarer.getLeft();
        declarer = declarer.getLeft();
        declarer.flipHand(window);
        dummy = declarer;
        declarer = declarer.getLeft();
        declarer = declarer.getLeft();

        if(declarer.getPosition() != "SOUTH" && dummy.getPosition() != "NORTH") {
          zzz(2000);
        }

        for(int k=0; k<13; ++k) {
          if(stop_var) return;

          curr_hand = new Card[4];

          String dPos = declarer.getPosition();
          if(dPos.equals("EAST"))  { System.out.println("east should go now."); computerDeclarer(dPos); } else
          if(dPos.equals("SOUTH")) { playerDeclarer();       } else
          if(dPos.equals("WEST"))  { computerDeclarer(dPos); } else
          if(dPos.equals("NORTH")) { partnerDeclarer();      }

          zzz(400);
          findWinner();
          zzz(1500);

          window.clearCenter();

          dPos = declarer.getPosition();
          if(!dummy.getPosition().equals("NORTH")) {
            if(dPos.equals("EAST") || dPos.equals("WEST") || dPos.equals("NORTH") || dPos.equals("SOUTH")) {
              zzz(300);
            }
          }

}
          calculateScore();

		if(nsScore >= 100) {
		    nsGames++;
		    nsScore = 0;
		    weScore = 0;
		    vulnerable |= 0x0001;
		}
		if(weScore >= 100) {
		    weGames++;
		    nsScore = 0;
		    weScore = 0;
		    vulnerable |= 0x0002;
		}

		window.syncBelowLine();
		nsScore = 0;
		weScore = 0;

	    }

	    window.gameOverBar();

	    if(nsGames > 2) getRubberBonus(0);
	    else if(weGames > 2) getRubberBonus(1);

	    window.removeDeclarer(declarer.getPosition());
	    window.removeDealer(dealer.getPosition());
	    window.gameTotal(nsAboveLine, weAboveLine);

      }
	}

  private void zzz(int t) {
    try{ Thread.sleep(t);} catch( InterruptedException ie) {}
  }

}