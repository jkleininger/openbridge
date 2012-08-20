/******************************************************************************
 ********************************* Deck.java **********************************
 * @author Scott DiTomaso *****************************************************
 * Kettering University *******************************************************
 ******************************************************************************
 * Created on December 12, 2009 ***********************************************
 ******************************************************************************/

package openbridge;

/***********
 * Imports *
 ***********/
import java.util.*;
import java.io.*;

/**************
 * Deck class *
 **************/
public class Deck {

    /*************************
     * Private class members *
     *************************/
    private Card[] cards;

    /***************
     * Constructor *
     ***************/
    Deck() {

        cards = new Card[52];

        for(int z=0; z<52; ++z) {
            cards[z] = new Card((z/13), (z%13)+2, "/openbridge/cards/"+z+".gif", "/openbridge/cards/back.gif");
        }
    }


    /************************
     * Public class methods *
     ************************/

	/**********************************************************************
	 * shuffle() **********************************************************
	 **********************************************************************
	 * Randomly shuffles the cards in the deck ****************************
	 **********************************************************************/
	public void shuffle() {
		Card temp;
		Random randomGenerator = new Random();
        int y;

        for(int z=0; z<52; ++z) {
			y = randomGenerator.nextInt(52);
			temp = cards[z];
			cards[z] = cards[y];
			cards[y] = temp;
		}
	}

	/**********************************************************************
	 * getCard() **********************************************************
	 **********************************************************************
	 * Returns a card from a specific position in the deck ****************
	 **********************************************************************/
	public Card getCard(int c) {
           return this.cards[c];
	}

	/**********************************************************************
	 * deal() *************************************************************
	 **********************************************************************
	 * Distributes the cards, one at a time, clockwise around the table ***
	 * starting with the player to the left of the dealer *****************
	 **********************************************************************/
	public void deal(Hand dealer) {
           Hand curr;

           /** Start with the player to left of the dealer */
           curr = dealer.getLeft();

           for(int z=0; z<13; ++z) {

		for(int y=0; y<4; ++y) {

                   /* add the card to the current hand */
                   curr.addCard(this.cards[y+(z*4)]);

                   /* Switch curr to next player */
                   curr = curr.getLeft();
		}
           }
	}
}
