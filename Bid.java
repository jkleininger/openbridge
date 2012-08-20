/******************************************************************************
 ****************************** Bid.java **************************************
 * @author Scott DiTomaso******************************************************
 ******************************************************************************
 * Created on December 20, 2009 ***********************************************
 ******************************************************************************/

package openbridge;

/*************
 * Bid class *
 *************/
public class Bid {

    /*************************
     * Private class members *
     *************************/
	private int[] numSuit;
	private int[] numScore;
	private int finalSuit;
	private int finalNum;


	/***************
	 * Constructor *
	 ***************/
	Bid () {
	    this.numSuit = new int[4];
	    this.numScore = new int[4];
	    this.finalSuit = -1;
	    this.finalNum = -1;
	}


    /************************
     * Public class members *
     ************************/

	/**********************************************************************
	 * calculateStats() ***************************************************
	 **********************************************************************
	 * Adds up the point value for each suit in the computers hand: *******
	 * Aces are 4 pts, Kings are 3, Queens are 2, and Jacks are 1 pt, *****
	 * also, the number of cards for each suit are counted ****************
	 **********************************************************************/
	public void calculateStats(Card[] card) {
	    int place = -1;

	    for(int i=0; i<13; ++i) {
		place = card[i].getNumSuit();

		numSuit[place]++;

		switch(card[i].getValue()) {
		    case 11: numScore[place] += 1;
			     break;
		    case 12: numScore[place] += 2;
			     break;
		    case 13: numScore[place] += 3;
			     break;
		    case 14: numScore[place] += 4;
			     break;
		}
	    }

		calculateBid();
	}

	/**********************************************************************
	 * calculateBid() *****************************************************
	 **********************************************************************
	 * Determines what the max bid for the computer should be based on ****
	 * the number and point value for each suit ***************************
	 **********************************************************************/
	private void calculateBid() {
	    int tmpMax = 0;
	    int tmpSuit = 0;
	    int totalScore = 0;
	    boolean balanced = true;

	    for(int j=0; j<4; ++j) {
		switch(numSuit[j]) {
		    case 13: if(numScore[j] >= 9) {
				finalSuit = j;
				finalNum = 7;
			     }
			     break;
		    case 12: if(numScore[j] >= 9) {
				finalSuit = j;
				finalNum = 7;
			     } else if(numScore[j] >= 7) {
				finalSuit = j;
				finalNum = 6;
			     }
			     break;
		    case 11: if(numScore[j] >= 9) {
				finalSuit = j;
				finalNum = 7;
			     } else if(numScore[j] >= 7) {
				finalSuit = j;
				finalNum = 6;
			     }
			     break;
		    case 10: if(numScore[j] >= 7) {
				finalSuit = j;
				finalNum = 6;
			     }
			     break;
		    case 8: numScore[j] += 4;
			    break;
		    case 7: numScore[j] += 3;
			    break;
		    case 6: numScore[j] += 2;
			    break;
		    case 5: numScore[j] += 1;
			    break;
		}

		if(numScore[j] > tmpMax) {
		    tmpMax = numScore[j];
		    tmpSuit = j;
		} else if ((numScore[j] == tmpMax) && (numSuit[j] > numSuit[tmpSuit])) {
		    tmpMax = numScore[j];
		    tmpSuit = j;
		}

		totalScore += numScore[j];
	    }

	    for(int k=0; k<4; ++k) {
		if(numSuit[k] < 2 || numSuit[k] > 4)
		    balanced = false;
	    }

	    if(totalScore < 11)
		finalNum = 0;
	    else if(totalScore > 10 && totalScore < 15)
		finalNum = 1;
	    else if(totalScore > 14 && totalScore < 18) {
		if(balanced) {
		    finalNum = 1;
		    tmpSuit = 4;
		} else
		    finalNum = 2;
	    } else if(totalScore > 17 && totalScore < 21) {
		if(balanced) {
		    finalNum = 2;
		    tmpSuit = 4;
		} else
		    finalNum = 3;
	    } else if(totalScore > 20 && totalScore < 24) {
		if(balanced) {
		    finalNum = 3;
		    tmpSuit = 4;
		} else
		    finalNum = 4;
	    } else if(totalScore > 23 && totalScore < 27) {
		if(balanced) {
		    finalNum = 4;
		    tmpSuit = 4;
		} else
		    finalNum = 5;
	    } else if(totalScore > 26 && totalScore < 30) {
		finalNum = 5;
		tmpSuit = 4;
	    } else if(totalScore > 29 && totalScore < 33) {
		finalNum = 6;
		tmpSuit = 4;
	    } else if (totalScore > 32) {
		finalNum = 7;
		tmpSuit = 4;
	    }

	    finalSuit = tmpSuit;
	}

	/**********************************************************************
	 * getFinalSuit() *****************************************************
	 **********************************************************************
	 * Returns the suit that the computer should bid as trump *************
	 **********************************************************************/
	public int getFinalSuit() {
	    return finalSuit;
	}

	/**********************************************************************
	 * getFinalNum() ******************************************************
	 **********************************************************************
	 * Returns the maximum number of tricks the computer should bid *******
	 **********************************************************************/
	public int getFinalNum() {
	    return finalNum;
	}

	/**********************************************************************
	 * debug() ************************************************************
	 **********************************************************************
	 * Used to see what the value for each suit is and what the final *****
	 * bid ended up being for debugging purposes **************************
	 **********************************************************************/
	public void debug() {

	    System.out.println("");

	    for(int z=0; z<4; ++z) {
		System.out.println("Suit: " + z + "   Num: " + numSuit[z] + "   Score: " + numScore[z]);
	    }

	    System.out.println("fS: " + finalSuit + "fN: " + finalNum + "\n");
	    }
}
