/******************************************************************************
 *************************** OpenBridgeGUI.java *******************************
 * @author Scott DiTomaso *****************************************************
 * Kettering University *******************************************************
 ******************************************************************************
 * Created on December 8, 2009 ************************************************
 ******************************************************************************/

package openbridge;

/***********
 * Imports *
 ***********/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/***********************
 * OpenBridgeGUI class *
 ***********************/
public class OpenBridgeGUI extends javax.swing.JFrame {

    /*************************
     * Private class members *
     *************************/
	private JPanel MainPanel;
	private JPanel GamePanel;
	private JPanel SidePanel;
	private JPanel ContractPanel;
	private JPanel TrickPanel;
	private JPanel ScorePanel;
	private JPanel ScorePanelLeft;
	private JPanel ScorePanelRight;
	private JPanel North;
	private JPanel NorthContainer;
	private JPanel South;
	private JPanel SouthContainer;
	private JPanel West;
	private JPanel WestContainer;
	private JPanel East;
	private JPanel EastContainer;
	private JPanel Center;
	private JPanel CenterContainer;
	private JPanel NorthVuln1;
	private JPanel NorthVuln2;
	private JPanel SouthVuln1;
	private JPanel SouthVuln2;
	private JPanel WestVuln1;
	private JPanel WestVuln2;
	private JPanel EastVuln1;
	private JPanel EastVuln2;

	private JLabel dealString;
	private JLabel declareString;
	private JLabel[][] image = new JLabel[HANDS][CARDS];
	private JLabel contract;
	private JLabel nsTricks;
	private JLabel weTricks;

	private BidFrame bidWindow;

	private static final int FRAME_W = 900;
	private static final int FRAME_H = 750;
	private static final int HANDS = 4;
	private static final int CARDS = 13;
	private static int lastPlayed;
	private static int sNum;
	private static int nNum;
	private static int weAboveLine;
	private static int theyAboveLine;
	private static int weBelowLine;
	private static int theyBelowLine;

	private boolean[] Nlock = new boolean[CARDS];
	private boolean[] Slock = new boolean[CARDS];
	private boolean wait;

	private JMenu fileMenu;


	/********************************
	 * initComponents ***************
	 ********************************
	 * Initializes the GUI when the *
	 * game is started **************
	 ********************************/
	public void initComponents() {
	   JMenuItem item;

	   Container contentPane = getContentPane();

	    Lock("ALL");

	    /** Create the Menu */
	    fileMenu = new JMenu("File");

	    item = new JMenuItem("New Game");
	    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
	    item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    fileMenuNewGameActionPerformed(evt);
                }
	    });
	    fileMenu.add(item);

	    fileMenu.addSeparator();

	    item = new JMenuItem("Quit");
	    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
	    item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    fileMenuQuitActionPerformed(evt);
                }
	    });
	    fileMenu.add(item);

	    JMenuBar menuBar = new JMenuBar();
	    menuBar.add(fileMenu);
	    setJMenuBar(menuBar);

	    /** Set up the content pane */
	    MainPanel = new JPanel();
	    MainPanel.setLayout(new BorderLayout());

	    contentPane.add(MainPanel);
	    contentPane.addComponentListener( new ComponentListener(){
		public void componentShown( ComponentEvent e ) {}
		public void componentHidden( ComponentEvent e ) {}
		public void componentMoved( ComponentEvent e ) {}
		public void componentResized(ComponentEvent e) {
			//code to reposition bid window
		}
	     });

	    dealString = new JLabel("DEALER");
	    declareString = new JLabel("Declarer");
	    NorthVuln1 = new JPanel();
	    NorthVuln2 = new JPanel();
	    SouthVuln1 = new JPanel();
	    SouthVuln2 = new JPanel();
	    WestVuln1 = new JPanel();
	    WestVuln2 = new JPanel();
	    EastVuln1 = new JPanel();
	    EastVuln2 = new JPanel();

	    image = new JLabel[HANDS][CARDS];

	    lastPlayed = -1;
	    nNum = 0;
	    sNum = 0;
	    wait = true;

	    /** Create the layout for the Game and Score areas */
	    SetupGamePanel();
	    SetupSidePanel();
  
	    MainPanel.add(GamePanel, BorderLayout.CENTER);
	    MainPanel.add(SidePanel, BorderLayout.EAST);

	    setVisible(true);
	}


	/******************************
	 * SetupGamePanel *************
	 ******************************
	 * Creates the game panel and *
	 * its children panels ********
	 ******************************/
	private void SetupGamePanel() {

	    GamePanel = new JPanel();
	    GamePanel.setBackground(new Color(67, 178, 39));
	    GamePanel.setPreferredSize(new Dimension(650, 850));
	    GamePanel.setLayout(new BorderLayout());
	    GamePanel.setBorder(BorderFactory.createLoweredBevelBorder());

		North = new JPanel();
		North.setBackground(new Color(67, 178, 39));
		North.setPreferredSize(new Dimension(625, 120));
		North.setLayout(null);

		South = new JPanel();
		South.setBackground(new Color(67, 178, 39));
		South.setPreferredSize(new Dimension(625, 120));
		South.setLayout(null);

		West = new JPanel();
		West.setBackground(new Color(67, 178, 39));
		West.setPreferredSize(new Dimension(95, 420));
		West.setLayout(null);

		East = new JPanel();
		East.setBackground(new Color(67, 178, 39));
		East.setPreferredSize(new Dimension(95, 420));
		East.setLayout(null);

		Center = new JPanel();
		Center.setBackground(new Color(67,178,39));
		Center.setPreferredSize(new Dimension(225,200));
		Center.setLayout(null);

		    NorthContainer = new JPanel();
		    NorthContainer.setLayout(new FlowLayout());
		    NorthContainer.setBackground(new Color(67, 178, 39));
		    NorthContainer.setPreferredSize(new Dimension(500, 125));
		    NorthContainer.add(North);

		    SouthContainer = new JPanel();
		    SouthContainer.setLayout(new FlowLayout());
		    SouthContainer.setBackground(new Color(67, 178, 39));
		    SouthContainer.setPreferredSize(new Dimension(500, 125));
		    SouthContainer.add(South);

		    WestContainer = new JPanel();
		    WestContainer.setLayout(new GridBagLayout());
		    WestContainer.setBackground(new Color(67, 178, 39));
		    WestContainer.setPreferredSize(new Dimension(100, 660));
		    WestContainer.add(West);

		    EastContainer = new JPanel();
		    EastContainer.setLayout(new GridBagLayout());
		    EastContainer.setBackground(new Color(67, 178, 39));
		    EastContainer.setPreferredSize(new Dimension(100, 660));
		    EastContainer.add(East);

		    CenterContainer = new JPanel();
		    CenterContainer.setLayout(new GridBagLayout());
		    CenterContainer.setBackground(new Color(67, 178, 39));
		    CenterContainer.add(Center);

		GamePanel.add(NorthContainer, BorderLayout.NORTH);
		GamePanel.add(SouthContainer, BorderLayout.SOUTH);
		GamePanel.add(WestContainer, BorderLayout.WEST);
		GamePanel.add(EastContainer, BorderLayout.EAST);
		GamePanel.add(CenterContainer, BorderLayout.CENTER);

	}


	/*******************************
	 * SetupSidePanel **************
	 *******************************
	 * Creates the score panel and *
	 * its children panels *********
	 *******************************/
	private void SetupSidePanel() {

	    SidePanel = new JPanel();
	    SidePanel.setLayout(new FlowLayout());
	    SidePanel.setBackground(new Color(169, 169, 169));
	    SidePanel.setPreferredSize(new Dimension(200, 750));

		ContractPanel = new JPanel();
		ContractPanel.setLayout(new FlowLayout());
		ContractPanel.setBackground(new Color(169, 169, 169));
		ContractPanel.setBorder(BorderFactory.createTitledBorder("Contract:"));
		ContractPanel.setPreferredSize(new Dimension(190,60));
		    contract = new JLabel();
		    ContractPanel.add(contract);

		TrickPanel = new JPanel();
		TrickPanel.setLayout(new GridLayout(2,2));
		TrickPanel.setBackground(new Color(169, 169, 169));
		TrickPanel.setBorder(BorderFactory.createTitledBorder("Tricks:"));
		TrickPanel.setPreferredSize(new Dimension(195,80));
		TrickPanel.add(new JLabel("N/S -"));
		nsTricks = new JLabel("0");
		TrickPanel.add(nsTricks);
		TrickPanel.add(new JLabel("E/W -"));
		weTricks = new JLabel("0");
		TrickPanel.add(weTricks);

		SetupScorePanel();

		SidePanel.add(ContractPanel);
		SidePanel.add(TrickPanel);
		SidePanel.add(ScorePanel);

	}

	private void SetupScorePanel() {
	    JSeparator lineL;
	    JSeparator lineR;

	    weAboveLine = 140;
	    theyAboveLine = 140;
	    weBelowLine = 160;
	    theyBelowLine = 160;

		ScorePanel = new JPanel();
		ScorePanel.setLayout(new GridLayout(1,2));
		ScorePanel.setBackground(new Color(169, 169, 169));
		ScorePanel.setBorder(BorderFactory.createTitledBorder("Score:"));

		    ScorePanelLeft = new JPanel();
		    ScorePanelLeft.setLayout(null);
		    ScorePanelLeft.setBackground(new Color(169,169,169));
		    ScorePanelLeft.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.black));
		    ScorePanelLeft.setPreferredSize(new Dimension(95, 500));

			JLabel weLabel = new JLabel("We");
			weLabel.setBounds(36,0,95,20);

			lineL = new JSeparator();
			lineL.setBorder(BorderFactory.createLineBorder(Color.black));
			lineL.setBounds(0,155,95,1);

		    ScorePanelLeft.add(weLabel);
		    ScorePanelLeft.add(lineL);

		    ScorePanelRight = new JPanel();
		    ScorePanelRight.setLayout(null);
		    ScorePanelRight.setBackground(new Color(169,169,169));
		    ScorePanelRight.setPreferredSize(new Dimension(95, 500));

			JLabel theyLabel = new JLabel("They");
			theyLabel.setBounds(26,0,95,20);

			lineR = new JSeparator();
			lineR.setBorder(BorderFactory.createLineBorder(Color.black));
			lineR.setBounds(0,155,95,1);

		    ScorePanelRight.add(theyLabel);
		    ScorePanelRight.add(lineR);

		ScorePanel.add(ScorePanelLeft);
		ScorePanel.add(ScorePanelRight);
	}


	/**********************************
	 * fileMenuNewGameActionPerformed *
	 **********************************
	 * Method to handle when New Game *
	 * is selected from the menu ******
	 **********************************/
	private void fileMenuNewGameActionPerformed(ActionEvent evt) {

	    //close threads if open
	    openbridge.newGame();

	}

	/*******************************
	 * fileMenuQuitActionPerformed *
	 *******************************
	 * Method to handle when Quit **
	 * is selected from the menu ***
	 *******************************/
	private void fileMenuQuitActionPerformed(ActionEvent evt) {

	    System.exit(0);

	}




    /************************
     * Public class members *
     ************************/

	/**********************************
	 * OpenBridgeGUI ******************
	 **********************************  constructor
	 * Creates new form OpenBridgeGUI *
	 **********************************/
	public OpenBridgeGUI() {

	    setTitle("OpenBridge");
	    setSize(FRAME_W, FRAME_H);
	    setResizable(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

            initComponents();

	}

	/**********************************
	 * Display ************************
	 **********************************
	 * Used to display the cards in a *
	 * certain position ***************
	 **********************************/
	public void display(String thumbnail, String pos, int x) {

	    final int z = x;

	    if(pos == "NORTH") {
		++nNum;
		image[0][x] = new JLabel(new ImageIcon(getClass().getResource(thumbnail)));
		image[0][x].setSize(74,98);
		image[0][x].setBounds(425-(25*x),10,74,98);
		image[0][x].addMouseListener(new MouseAdapter(){
		    public void mouseReleased(final MouseEvent e) {

			if (Nlock[z] == false) {
			   --nNum;
			   North.remove(image[0][z]);
			   moveCards(z, nNum, "NORTH", false);
			   Center.add(image[0][z]);
			   image[0][z].setLocation(75,0);
			   North.revalidate();
			   North.repaint();

			   Lock("NORTH");

			   lastPlayed = z;
			   wait = false;
			}
		    }
		});
		North.add(image[0][x]);
	    }			
	    else if(pos == "SOUTH") {
		++sNum;
		image[1][z] = new JLabel(new ImageIcon(getClass().getResource(thumbnail)));
		image[1][z].setSize(74,98);
		image[1][z].setBounds(425-(25*x),10,74,98);
		image[1][z].addMouseListener(new MouseAdapter(){
		    public void mouseReleased(final MouseEvent e) {

			if (Slock[z] == false) {
			   --sNum;
			   South.remove(image[1][z]);
			   moveCards(z, sNum,  "SOUTH", false);
			   Center.add(image[1][z]);
			   image[1][z].setLocation(75,100);
			   South.revalidate();
			   South.repaint();

			   Lock("SOUTH");

			   lastPlayed = z;
			   wait = false;
			}
		    }
		});
		South.add(image[1][x]);
	    }
	    else if(pos == "WEST") {
		image[2][x] = new JLabel(new ImageIcon(getClass().getResource(thumbnail)));
		image[2][x].setSize(74,98);
		image[2][x].setBounds(10,310-(25*x),74,98);
		West.add(image[2][x]);
	    }
	    else if (pos == "EAST"){
		image[3][x] = new JLabel(new ImageIcon(getClass().getResource(thumbnail)));
		image[3][x].setSize(74,98);
		image[3][x].setBounds(10,310-(25*x),74,98);
		East.add(image[3][x]);
	    }
	}


	/*****************************
	 * flip **********************
	 *****************************
	 * Used to flip a card to  a *
	 * face up position **********
	 *****************************/
	public void flip(String thumbnail, String Pos, int x) {

	    int position = -1;

	    if(Pos == "NORTH") 
		position = 0;
	    else if(Pos == "WEST")
		position = 2;
	    else if(Pos == "EAST")
		position = 3;
	    else if(Pos == "SOUTH")
		position = 1;

	    image[position][x].setIcon(new ImageIcon(getClass().getResource(thumbnail)));
	}


	public void playComputer(String pos, int num) {

	    if(pos == "WEST") {
		West.remove(image[2][num]);
		Center.add(image[2][num]);
		image[2][num].setBounds(0,52,74,98);
		West.revalidate();
		West.repaint();
	    } else if(pos == "EAST") {
		East.remove(image[3][num]);
		Center.add(image[3][num]);
		image[3][num].setBounds(150,52,74,98);
		East.revalidate();
		East.repaint();
	    } else if(pos == "NORTH") {
		North.remove(image[0][num]);
		Center.add(image[0][num]);
		image[0][num].setBounds(75,0,74,98);
		North.revalidate();
		North.repaint();
	    } if(pos == "SOUTH") {
		South.remove(image[1][num]);
		Center.add(image[1][num]);
		image[1][num].setBounds(75,100,74,98);
		South.revalidate();
		South.repaint();
	    }
	}


	public boolean isReady() {

		return wait;
	}



	/**********************************
	 * placeDealer ********************
	 **********************************
	 * Places the dealer identifier ***
	 * depending on who the dealer is *
	 **********************************/
	public void placeDealer(Hand Dealer) {

	    String place = Dealer.getPosition();

	    if(place == "NORTH") {
		dealString.setBounds(500,10,74,20);
		North.add(dealString);
	    }
	    if(place == "EAST") {
		dealString.setBounds(20,410,74,18);
		East.add(dealString);
	    }
	    if(place == "SOUTH") {
		dealString.setBounds(500,95,74,20);
		South.add(dealString);
	    }
	    if(place == "WEST") {
		dealString.setBounds(20,410,74,18);
		West.add(dealString);
	    }
	}

	public void removeDealer(String pos) {

	    if(pos == "NORTH") {
		North.remove(dealString);
		North.repaint();
	    }
	    if(pos == "EAST") {
		East.remove(dealString);
		East.repaint();
	    }
	    if(pos == "SOUTH") {
		South.remove(dealString);
		South.repaint();
	    }
	    if(pos == "WEST") {
		West.remove(dealString);
		West.repaint();
	    }
	}

	/************************************
	 * placeDeclarer ********************
	 ************************************
	 * Places the declarer identifier ***
	 * depending on who the declarer is *
	 ************************************/
	public void placeDeclarer(Hand Declarer) {

	    String place = Declarer.getPosition();

	    if(place == "NORTH") {
		declareString.setBounds(500,95,74,20);
		North.add(declareString);
	    }
	    if(place == "EAST") {
		declareString.setBounds(17,0,74,18);
		East.add(declareString);
	    }
	    if(place == "SOUTH") {
		declareString.setBounds(500,10,74,20);
		South.add(declareString);
	    }
	    if(place == "WEST") {
		declareString.setBounds(17,0,74,18);
		West.add(declareString);
	    }

		repaint();
	}

	public void removeDeclarer(String place) {

	    if(place == "NORTH") {
		North.remove(declareString);
		North.repaint();
	    }
	    if(place == "EAST") {
		East.remove(declareString);
		East.repaint();
	    }
	    if(place == "SOUTH") {
		South.remove(declareString);
		South.repaint();
	    }
	    if(place == "WEST") {
		West.remove(declareString);
		West.repaint();
	    }

	}

	/**********************************
	 * showVulnerable *****************
	 **********************************
	 * Places red bars on the table ***
	 * depending on who is vulnerable *
	 **********************************/
	public void showVulnerable(int vuln) {

	    if(vuln == 0) {
		return;
	    }
	    if((vuln & 0x0001) != 0) {

		NorthVuln1.setBounds(125,0,375,10);
		NorthVuln2.setBounds(125,110,375,10);
		NorthVuln1.setBackground(new Color(255, 0, 0));
		NorthVuln2.setBackground(new Color(255, 0, 0));
		North.add(NorthVuln1);
		North.add(NorthVuln2);

		SouthVuln1.setBounds(125,0,375,10);
		SouthVuln2.setBounds(125,110,375,10);
		SouthVuln1.setBackground(new Color(255, 0, 0));
		SouthVuln2.setBackground(new Color(255, 0, 0));
		South.add(SouthVuln1);
		South.add(SouthVuln2);
	    }
	    if((vuln & 0x0002) != 0) {

		East.setBackground(new Color(255,0,0));
		West.setBackground(new Color(255,0,0));

		/*WestVuln1.setBounds(0,10,10,400);
		WestVuln2.setBounds(85,10,10,400);
		WestVuln1.setBackground(new Color(255, 0, 0));
		WestVuln2.setBackground(new Color(255, 0, 0));
		West.add(WestVuln1);
		West.add(WestVuln2);

		EastVuln1.setBounds(0,10,10,400);
		EastVuln2.setBounds(85,10,10,400);
		EastVuln1.setBackground(new Color(255, 0, 0));
		EastVuln2.setBackground(new Color(255, 0, 0));
		East.add(EastVuln1);
		East.add(EastVuln2);*/
	    }
	}

	/**********************************
	 * Reset **************************
	 **********************************
	 * Clear the frame for a new game *
	 **********************************/
	public void reset() {

	remove(MainPanel);

	}

	/**********************************
	 * Lock ***************************
	 **********************************
	 * Lock the user from playing a ***
	 * card. **************************
	 **********************************/
	public void Lock(String position) {

	    if(position == "SOUTH") {
		for(int i=0; i<CARDS; ++i)
		    Slock[i] = true;
	    } else if(position == "NORTH") {
		for(int j=0; j<CARDS; ++j)
		    Nlock[j] = true;
	    } else if(position == "ALL") {
		for(int k=0; k<CARDS; ++k) {
		    Slock[k] = true;
		    Nlock[k] = true;
		}
	    }
	}

	/**********************************
	 * Unlock *************************
	 **********************************
	 * Unlock the user to allow them **
	 * to play a card *****************
	 **********************************/
	public void Unlock(String position, int start, int end) {

	    if(position == "NORTH") {
		for(int i=start; i<=end; ++i)
		    Nlock[i] = false;
	    } else if(position == "SOUTH") {
		for(int j=start; j<=end; ++j)
		    Slock[j] = false;
	    }

	    wait = true;
	}


	public void ShowContract(Contract c) {
	    String string = null;

	    if(c.getWinner() == "NORTH") 
		string = "North/South";
	    else if (c.getWinner() == "SOUTH")
		string = "South/North";
	    else if (c.getWinner() == "WEST")
		string = "West/East";
	    else if (c.getWinner() == "EAST")
		string = "East/West";
	    
	    string = string + " - " + c.getTricks();

	    if(c.getTrump() == 4)
		string = string + "NT";
	    else
		contract.setIcon(new ImageIcon(getClass().getResource("/openbridge/cards/s"+c.getTrump()+".gif")));

	    contract.setText(string);
	    contract.setHorizontalTextPosition(JLabel.LEFT);
	    contract.setVisible(true);
	}

	public int getLastPlayed() {

	    this.wait = true;	
	    return this.lastPlayed;
	}

	public void updateTricks(int pos, int tricks) {

	    if(pos == 2) {
		nsTricks.setText(Integer.toString(tricks));
		weTricks.setText(Integer.toString(tricks));
	    } else if(pos == 0)
		nsTricks.setText(Integer.toString(tricks));
	    else if(pos == 1)
		weTricks.setText(Integer.toString(tricks));
	}

	public void clearCenter() {

	    Center.removeAll();
	    Center.updateUI();

	}

	public void moveComputerCards(int start, int length, String pos) {
	    int posNum = -1;
	    JLabel tmp = new JLabel();

	    if(pos == "WEST")
		posNum = 2;
	    else if(pos == "EAST")
		posNum = 3;
	    else if(pos == "NORTH")
		posNum = 0;
	    else if(pos == "SOUTH")
		posNum = 1;

	    tmp.setIcon(image[posNum][start].getIcon());
	    for(int i=start; i<length; ++i) {
		image[posNum][i].setIcon(image[posNum][(i+1)].getIcon());
	    }
	    image[posNum][length].setIcon(tmp.getIcon());

	    moveCards(start, length, pos, true);
	}

	public void moveCards(int start, int length, String pos, boolean computerFlag) {
	    int posNum = -1;
	    int x=0;
	    int y=0;
	    float side = 0;

	    if(pos == "NORTH")
		posNum = 0;
	    else if(pos == "SOUTH")
		posNum = 1;
	    else if(pos == "WEST")
		posNum = 2;
	    else if(pos == "EAST")
		posNum = 3;


	    if(!computerFlag) {
	        for(int i=start; i<13; ++i) {
		    x = (int)image[posNum][i].getLocation().getX();
		    y = (int)image[posNum][i].getLocation().getY();

		    image[posNum][i].setLocation((x+25), y);
		}
	    }

	    if(length % 2 == 1) {
		if((pos == "NORTH" || pos == "SOUTH") && !computerFlag)
		    length = 13;

		for(int j=0; j<length; ++j) {
		    x = (int)image[posNum][j].getLocation().getX();
		    y = (int)image[posNum][j].getLocation().getY();

		    if(posNum == 0 || posNum == 1)
		    	image[posNum][j].setLocation((x-25), y);
		    if(posNum == 2 || posNum == 3)
			image[posNum][j].setLocation(x, (y-25));
		}
	    }
	}

	public void clearContract() {

	    contract.setVisible(false);
	    contract.setIcon(null);
	}

	public void belowLine(int side, int value) {
	    JLabel tmp;

	    if(side == 0) {
		if(weBelowLine >= 440)
		    return;

		tmp = new JLabel(Integer.toString(value));
		tmp.setBounds(36, weBelowLine, 95, 20);
		ScorePanelLeft.add(tmp);
		weBelowLine += 15;

	    } else if(side == 1) {
		if(theyBelowLine >= 440)
		    return;

		tmp = new JLabel(Integer.toString(value));
		tmp.setBounds(36, theyBelowLine, 95, 20);
		ScorePanelRight.add(tmp);
		theyBelowLine += 15;
	   }
	}

	public void aboveLine(int side, int value) {
	    JLabel tmp;

	    if(value == 0) {}
	    else {
		if(side == 0) {
		    if(weAboveLine <=10)
			return;

		    tmp = new JLabel(Integer.toString(value));
		    tmp.setBounds(36, weAboveLine, 95, 20);
		    ScorePanelLeft.add(tmp);
		    weAboveLine -=15;

		} else if(side == 1) {
		    if(theyAboveLine <=10)
			return;

		    tmp = new JLabel(Integer.toString(value));
		    tmp.setBounds(36, theyAboveLine, 95, 20);
		    ScorePanelRight.add(tmp);
		    theyAboveLine -=15;

		}
	    }
	}

	public void syncBelowLine() {
	    JSeparator tmpL;
	    JSeparator tmpR;

	    if(weBelowLine >= 440 || theyBelowLine >= 440) {
		weBelowLine = 440;
		theyBelowLine = 440;
		return;
	    }

	    if(theyBelowLine > weBelowLine)
		weBelowLine = theyBelowLine;
	    else
		theyBelowLine = weBelowLine;

	    tmpL = new JSeparator();
	    tmpR = new JSeparator();
	    tmpL.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpR.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpL.setBounds(0,weBelowLine,95,1);
	    tmpR.setBounds(0,theyBelowLine,95,1);
	    ScorePanelLeft.add(tmpL);
	    ScorePanelRight.add(tmpR);

	    weBelowLine += 5;
	    theyBelowLine += 5;

	}


	public void createBidFrame(Hand d, Contract c) {

	    bidWindow = new BidFrame(d, c);

	    CenterContainer.remove(Center);

	    CenterContainer.add(bidWindow);
	    CenterContainer.revalidate();
	    CenterContainer.repaint();
	}

	public void closeBidFrame() {

	    CenterContainer.remove(bidWindow);
	    CenterContainer.add(Center);
	}

	public void gameOverBar() {
	    JSeparator tmpL;
	    JSeparator tmpR;

	    tmpL = new JSeparator();
	    tmpR = new JSeparator();
	    tmpL.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpR.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpL.setBounds(0,(weBelowLine - 10),95,5);
	    tmpR.setBounds(0,(theyBelowLine - 10),95,5);
	    ScorePanelLeft.add(tmpL);
	    ScorePanelRight.add(tmpR);
	
	}

	public void gameTotal(int nsNum, int weNum) {
	    JSeparator tmpL;
	    JSeparator tmpR;

	    if(theyBelowLine > weBelowLine)
		weBelowLine = theyBelowLine;
	    else
		theyBelowLine = weBelowLine;

	    tmpL = new JSeparator();
	    tmpR = new JSeparator();
	    tmpL.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpR.setBorder(BorderFactory.createLineBorder(Color.black));
	    tmpL.setBounds(0,weBelowLine,95,5);
	    tmpR.setBounds(0,theyBelowLine,95,5);
	    ScorePanelLeft.add(tmpL);
	    ScorePanelRight.add(tmpR);

	    weBelowLine += 10;
	    theyBelowLine += 10;

	    JLabel tmpNS = new JLabel(Integer.toString(nsNum));
	    tmpNS.setBounds(36, weBelowLine, 95, 20);
	    ScorePanelLeft.add(tmpNS);

	    JLabel tmpWE = new JLabel(Integer.toString(weNum));
	    tmpWE.setBounds(36, theyBelowLine, 95, 20);
	    ScorePanelRight.add(tmpWE);

	    ScorePanel.validate();
	    ScorePanel.repaint();

	    gameOver();
	}

	public void gameOver() {

	    CenterContainer.remove(Center);

	    JPanel gameOverPanel = new JPanel();

	    gameOverPanel.setPreferredSize(new Dimension(350,120));
		JLabel top = new JLabel("GAME OVER");
		top.setFont(new Font("Serif", Font.PLAIN, 50));
		top.setBounds(75, 0, 200, 900);

		JLabel bottom = new JLabel("Ctrl+N for new game");
		bottom.setBounds(100,180, 50, 20);

	    gameOverPanel.add(top);
	    gameOverPanel.add(bottom);

	    CenterContainer.add(gameOverPanel);
	    CenterContainer.revalidate();
	    CenterContainer.repaint();
	}
}
