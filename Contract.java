/*********************************************************************
* Contract.java                                                      *
* @author Scott DiTomaso                                             *
* Kettering University                                               *
*                                                                    *
* Created on January 02, 2010                                        *
*********************************************************************/

package openbridge;

public class Contract {

/************************
* Private class members *
************************/
	private volatile String  Winner;
	private String           condition;
	private int              tricks;
	private int              trump;
  private boolean          cFinal;

  /**************
  * Constructor *
  **************/
	public Contract() {
    Winner    = "None";
    condition = "None";
    tricks    = 0;
    trump     = 0;
    cFinal    = false;
  }

	/***********************************************************************
  * setContract()                                                        *
  *                                                                      *
  * Sets the values of the contract to the values supplied               *
	***********************************************************************/
	public void setContract(String w, int t, int suit, String c) {
    this.Winner    = w;
	  this.condition = c;
	  this.tricks    = t;
	  this.trump     = suit;
    this.cFinal    = true;
	}

	/***********************************************************************
  * getWinner()                                                          *
  *                                                                      *
  * Returns the position of the final person to bid as a String          *
  ***********************************************************************/
  String getWinner()     { return this.Winner;    }

  /***********************************************************************
  * getSuit()                                                            *
  *                                                                      *
  * Returns any conditions such as double or redouble that are           *
  * associated with the contract                                         *
  ***********************************************************************/
	String getConditions() { return this.condition; }

	/***********************************************************************
  * getTricks()                                                          *
  *                                                                      *
  * Returns the number of tricks the winner bid as an Int                *
  ***********************************************************************/
	int getTricks()        { return this.tricks;    }

	/***********************************************************************
	* getTrump()                                                           *
	*                                                                      *
	* Returns the trump suit of the contarct as an Int                     *
  ***********************************************************************/
	int getTrump()         {  return this.trump;    }

	/***********************************************************************
  * isFinal()                                                            *
	*                                                                      *
  * Returns false if the Winner field has not been set and true if       *
  * the field is anything but the default "None"                         *
  ***********************************************************************/
  boolean isFinal()      { return (this.cFinal); }

}
