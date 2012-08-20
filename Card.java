package openbridge;

import javax.swing.*;
import java.awt.*;

public class Card {

  private int suit;
  private int value;
  private String face;
  private String back;
  private JLabel image;
  private String pos;

  Card(int s, int v, String f, String b) {
    this.suit = s;
    this.value = v;
    this.face = f;
    this.back = b;
    this.pos = null;
  }
  Card() {
    this.suit = -1;
    this.value = -1;
    this.face = null;
    this.back = null;
    this.pos = null;
  }

  public String getSuit() {

  switch(this.suit) {
    case 0:
      return "Club";
    case 1:
      return "Diamond";
    case 2:
      return "Heart";
    case 3:
      return "Spade";
    default:
      return "ERROR";
    }
  }

  public int getNumSuit() {
    return this.suit;
  }

  public int getValue() {
    switch (this.value) {
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
        return this.value;
      default:
        return -1;
    }
  }

  public int getSortValue() {
    return( (13*this.suit) + (this.value) );
  }

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
