package openbridge;

import javax.swing.JLabel;

public class Card {

  private int      suit;
  private int      value;
  private String   face;
  private String   back;
  private String   pos;
  private String[] sSuit = {"Club","Diamond","Heart","Spade"};

  Card(int s, int v, String f, String b) {
    this.suit  = s;
    this.value = v;
    this.face  = f;
    this.back  = b;
    this.pos   = null;
  }

  Card() {
    this.suit  = -1;
    this.value = -1;
    this.face  = null;
    this.back  = null;
    this.pos   = null;
  }

  public String getSuit()      { return sSuit[this.suit]; }
  public int    getNumSuit()   { return this.suit;        }
  public int    getValue()     { return this.value;       }
  public int    getSortValue() { return((13*suit)+value); }

  public void display(boolean isVisible, OpenBridgeGUI g, String pos, int x) {
    this.pos = pos;
    if(isVisible) {
      g.display(this.face, this.pos, x);
    } else {
      g.display(this.back, this.pos, x);
    }
  }

  public void flipCard(OpenBridgeGUI g, int x) {
    g.flip(this.face, this.pos, x);
  }

}
