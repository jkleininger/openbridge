/*****************************************************************************
* Bid.java                                                                   *
*                                                                            *
* Initial code by Scott DiTomaso, 2009 - 2010                                *
* Kettering University                                                       *
* Additional work by Jason K Leininger,  2012                                *
*****************************************************************************/

package openbridge;

import java.util.ArrayList;

public class Bid {

  private int[] numSuit;
  private int[] numScore;
  private int   finalSuit;
  private int   finalNum;


  Bid () {
    this.numSuit   = new int[4];
    this.numScore  = new int[4];
    this.finalSuit = -1;
    this.finalNum  = -1;
  }

  /*********************************************************************
  * calculateStats()                                                   *
  *                                                                    *
  * Adds up the point value for each suit in the computers hand:       *
  * Aces are 4 pts, Kings are 3, Queens are 2, and Jacks are 1 pt,     *
  * also, the number of cards for each suit are counted                *
  *********************************************************************/
  public void calculateStats(ArrayList<Card> card) {
    int place = -1;

    for(Card c : card) {
      place = c.getNumSuit();
      numSuit[place]++;
      if(c.getValue()>10 && c.getValue()<15) { numScore[place]+=(c.getValue()-10); }
    }
    calculateBid();
  }

  /*********************************************************************
  * calculateBid()                                                     *
  *                                                                    *
  * Determines what the max bid for the computer should be based on    *
  * the number and point value for each suit                           *
  *********************************************************************/
  private void calculateBid() {
    int     tmpMax     = 0;
    int     tmpSuit    = 0;
    int     totalScore = 0;
    boolean balanced   = true;

    for(int j=0; j<4; ++j) {
      switch(numSuit[j]) {
        case 13:
          if(numScore[j] >= 9) { finalSuit = j; finalNum = 7; }        break;
        case 12:
          if(numScore[j] >= 9) { finalSuit = j; finalNum = 7; }
          else if(numScore[j] >= 7) { finalSuit = j; finalNum = 6; }   break;
        case 11:
          if(numScore[j] >= 9) { finalSuit = j; finalNum = 7; }
          else if(numScore[j] >= 7) { finalSuit = j; finalNum = 6; }   break;
        case 10: if(numScore[j] >= 7) { finalSuit = j; finalNum = 6; } break;
        case 8: numScore[j] += 4; break;
        case 7: numScore[j] += 3; break;
        case 6: numScore[j] += 2; break;
        case 5: numScore[j] += 1; break;
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
      if(numSuit[k] < 2 || numSuit[k] > 4) balanced = false;
    }

    totalScore=totalScore<11?10:(totalScore>32?33:totalScore);

    switch(totalScore) {
      case 10:  finalNum=0; break;
      case 11:
      case 12:
      case 13:
      case 14:  finalNum=1; break;
      case 15:
      case 16:
      case 17:  finalNum=balanced?1:2;
                tmpSuit=balanced?4:tmpSuit; break;
      case 18:
      case 19:
      case 20:  finalNum=balanced?2:3;
                tmpSuit=balanced?4:tmpSuit; break;
      case 21:
      case 22:
      case 23:  finalNum=balanced?3:4;
                tmpSuit=balanced?4:tmpSuit; break;
      case 24:
      case 25:
      case 26:  finalNum=balanced?4:5;
                tmpSuit=balanced?4:tmpSuit; break;
      case 27:
      case 28:
      case 29:  finalNum=5;
                tmpSuit=4;                  break;
      case 30:
      case 31:
      case 32:  finalNum=6;
                tmpSuit=4;                  break;
      case 33:  finalNum=7;
                tmpSuit=4;
      default:  break;
    }

    finalSuit = tmpSuit;
}


  /*********************************************************************
  * getFinalSuit()                                                     *
  *                                                                    *
  * Returns the suit that the computer should bid as trump             *
  *********************************************************************/
  public int getFinalSuit() { return finalSuit; }

  /*********************************************************************
  * getFinalNum()                                                      *
  *                                                                    *
  * Returns the maximum number of tricks the computer should bid       *
  *********************************************************************/
  public int getFinalNum() { return finalNum; }

  /*********************************************************************
  * debug()                                                            *
  *                                                                    *
  * Used to see what the value for each suit is and what the final     *
  * bid ended up being for debugging purposes                          *
  *********************************************************************/
  public void debug() {
    System.out.println("");
      for(int z=0; z<4; ++z) {
        System.out.println("Suit: " + z + "   Num: " + numSuit[z] + "   Score: " + numScore[z]);
      }
    System.out.println("fS: " + finalSuit + "fN: " + finalNum + "\n");
  }

}
