/*****************************************************************************
* Deck.java                                                                  *
*                                                                            *
* Initial code by Scott DiTomaso, 2009 - 2010                                *
* Kettering University                                                       *
* Additional work by Jason K Leininger,  2012                                *
*****************************************************************************/

package openbridge;

import java.util.*;
import java.io.*;

public class Deck {

  private ArrayList<Card> theCards;

  Deck() {
    theCards = new ArrayList<Card>();
    for(int c=0;c<52;c++) {
      theCards.add(new Card(c/13,(c%13)+2,"/openbridge/cards/"+c+".gif","/openbridge/cards/back.gif"));
    }
  }

  /*********************************************************************
  * shuffle() **********************************************************
  **********************************************************************
  * Randomly shuffles the cards in the deck ****************************
  *********************************************************************/
  public void shuffle() {
    Collections.shuffle(theCards);
  }

  /**********************************************************************
  * getCard() **********************************************************
  **********************************************************************
  * Returns a card from a specific position in the deck ****************
  **********************************************************************/
  public Card getCard(int c) {
    return theCards.get(c);
  }

  /**********************************************************************
  * deal() *************************************************************
  **********************************************************************
  * Distributes the cards, one at a time, clockwise around the table ***
  * starting with the player to the left of the dealer *****************
  **********************************************************************/
  public void deal(Hand dealer) {
    Hand curr;

    curr = dealer.getLeft();
    for(Card thisCard : theCards) {
      curr.addCard(thisCard);
      curr=curr.getLeft();
    }
  }

}
