/*****************************************************************************
* openbridge.java                                                            *
*                                                                            *
* Initial code by Scott DiTomaso, 20 December 2009                           *
* Kettering University                                                       *
* Additional work by Jason K Leininger 2012                                  *
*****************************************************************************/

package openbridge;

public class openbridge extends Thread {

  private static Hand           Player;
  private static Hand           Partner;
  private static Hand           Computer1;
  private static Hand           Computer2;
  private static Hand           Dealer;
  private static OpenBridgeGUI  Windowx;
  private static Thread         BidThread = null;
  private static WorkerThread   BidWorker;

  /*********************************************************************
  * main()                                                             *
  *                                                                    *
  * Creates the underlying GUI                                         *
  *********************************************************************/
  public static void main(String args[]) {
    Windowx = new OpenBridgeGUI();
  }

  /*********************************************************************
  * newGame() **********************************************************
  * When a new game is called for, stop the currently running thread   *
  * if it exists and set up the window and players, then start the     *
  * thread to handle the gameplay                                      *
  *********************************************************************/
  public static void newGame() {
    if(BidThread != null) BidWorker.stopThread();

    Windowx.reset();
    Windowx.initComponents();

    Player    = new Hand(true,             "SOUTH");
    Computer2 = new Hand(false, Player,    "EAST");
    Partner   = new Hand(false, Computer2, "NORTH");
    Computer1 = new Hand(false, Partner,   "WEST");
    Dealer    = Partner;

    Player.addLeft(Computer1);

    BidWorker = new WorkerThread(Windowx, Dealer);
    BidThread = new Thread(BidWorker);
    BidThread.start();
  }
}
