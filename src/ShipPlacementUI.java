import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ShipPlacementUI extends JFrame implements ActionListener,
        MouseListener {

    private JPanel content;
    private JPanel pnlNorth;
    private JLabel lblTitle;
    
    private JPanel pnlGrid;

    private GameGrid gameGrid;
    private JComponent[][] arrayGrid;

    private int rows = 10;
    private int cols = 10;

    private int shipSize = 5;
    private int shipHorVert = 1;
    private int shipsLeftToPlace = 5;

    private ButtonGroup btgShips;
    private ArrayList<DefaultButtonModel> arrayShipButtons;
    private JRadioButton btnShip5;
    private JRadioButton btnShip4;
    private JRadioButton btnShip3a;
    private JRadioButton btnShip3b;
    private JRadioButton btnShip2;

    private JPanel pnlShip5;
    private JPanel pnlShip4;
    private JPanel pnlShip3a;
    private JPanel pnlShip3b;
    private JPanel pnlShip2;
    private JPanel pnlShipHolder;

    private ButtonGroup btgHorizontalVertical;
    private JRadioButton btnHorizontal;
    private JRadioButton btnVertical;

    private JPanel pnlHorizontalVertical;
    private JLabel lblHorizontal;
	private JLabel lblVertical;
    
	private String playerName;
    private String opponentName;

    private JPanel pnlConfirmHome;
    private JPanel buttonGroup;
    private JLabel placementStatus;
    private JButton btnConfirm;
    private JButton btnHome;
    private Border bdrRaisedButton;
    private Border bdrLoweredButton;
    
    private Board b;

    private Player player;

    //private Color backroundColor = new Color(0,24,42);
    private Color backroundColor = new Color(44, 62, 80);
    
    //private Color textColor = new Color(45,190,209);
    private Color textColor = new Color(236, 240, 241);
    
    // public static void main(String args[]) {
    // new ShipPlacementUI().setVisible(true);
    // }

    public ShipPlacementUI(final Player player, final ObjectOutputStream out,
                           final ObjectInputStream in, final String name,
                           final String opponentName) {

        super("Place Your Ships!");
        this.player = player;
        b = new Board();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setSize(500, 650);
        content = new JPanel(new BorderLayout(0, 5));
        content.setBorder(BorderFactory.createLineBorder(backroundColor, 5));
        content.setBackground(backroundColor);
        setContentPane(content);

        playerName = name;
        this.opponentName = opponentName;
        btnShip5 = new JRadioButton();
        btnShip5.setActionCommand("5");
        btnShip5.addActionListener(this);
        btnShip5.setSelected(true);
        btnShip5.setBackground(backroundColor);
        
        btnShip4 = new JRadioButton();
        btnShip4.addActionListener(this);
        btnShip4.setActionCommand("4");
        btnShip4.setBackground(backroundColor);
        
        btnShip3a = new JRadioButton();
        btnShip3a.addActionListener(this);
        btnShip3a.setActionCommand("3");
        btnShip3a.setBackground(backroundColor);
        
        btnShip3b = new JRadioButton();
        btnShip3b.addActionListener(this);
        btnShip3b.setActionCommand("3");
        btnShip3b.setBackground(backroundColor);
       
        btnShip2 = new JRadioButton();
        btnShip2.addActionListener(this);
        btnShip2.setActionCommand("2");
        btnShip2.setBackground(backroundColor);
        
        arrayShipButtons = new ArrayList<DefaultButtonModel>();
        arrayShipButtons.add((DefaultButtonModel) btnShip5.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip4.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip3a.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip3b.getModel());
        arrayShipButtons.add((DefaultButtonModel) btnShip2.getModel());

        btgShips = new ButtonGroup();
        btgShips.add(btnShip5);
        btgShips.add(btnShip4);
        btgShips.add(btnShip3a);
        btgShips.add(btnShip3b);
        btgShips.add(btnShip2);

        pnlShip5 = new JPanel(new GridLayout(1, 6));
        pnlShip5.setBackground(backroundColor);
        GameButton[] ship5Array = new GameButton[5];
        for (int a = 0; a < 5; a++) {
            ship5Array[a] = new GameButton();
            ship5Array[a].setEnabled(false);
        }

        for (int a = 0; a < 5; a++) {
            pnlShip5.add(ship5Array[a]);
        }

        pnlShip5.add(btnShip5);

        pnlShip4 = new JPanel(new GridLayout(1, 6));
        pnlShip4.setBackground(backroundColor);
        GameButton[] ship4Array = new GameButton[5];
        for (int a = 0; a < 5; a++) {
            ship4Array[a] = new GameButton();
            ship4Array[a].setEnabled(false);
        }

        for (int a = 0; a < 5; a++) {
            pnlShip4.add(ship4Array[a]);
        }

        ship4Array[4].setVisible(false);
        pnlShip4.add(btnShip4);

        pnlShip3a = new JPanel(new GridLayout(1, 6));
        pnlShip3a.setBackground(backroundColor);
        GameButton[] ship3aArray = new GameButton[5];
        for (int a = 0; a < 5; a++) {
            ship3aArray[a] = new GameButton();
            ship3aArray[a].setEnabled(false);
        }

        for (int a = 0; a < 5; a++) {
            pnlShip3a.add(ship3aArray[a]);
        }

        ship3aArray[3].setVisible(false);
        ship3aArray[4].setVisible(false);
        pnlShip3a.add(btnShip3a);

        pnlShip3b = new JPanel(new GridLayout(1, 6));
        pnlShip3b.setBackground(backroundColor);
        GameButton[] ship3bArray = new GameButton[5];
        for (int a = 0; a < 5; a++) {
            ship3bArray[a] = new GameButton();
            ship3bArray[a].setEnabled(false);
        }

        for (int a = 0; a < 5; a++) {
            pnlShip3b.add(ship3bArray[a]);
        }

        ship3bArray[3].setVisible(false);
        ship3bArray[4].setVisible(false);
        pnlShip3b.add(btnShip3b);

        pnlShip2 = new JPanel(new GridLayout(1, 6));
        pnlShip2.setBackground(backroundColor);
        GameButton[] ship2Array = new GameButton[5];
        for (int a = 0; a < 5; a++) {
            ship2Array[a] = new GameButton();
            ship2Array[a].setEnabled(false);
        }

        for (int a = 0; a < 5; a++) {
            pnlShip2.add(ship2Array[a]);
        }

        ship2Array[2].setVisible(false);
        ship2Array[3].setVisible(false);
        ship2Array[4].setVisible(false);
        pnlShip2.add(btnShip2);

        pnlShipHolder = new JPanel(new GridLayout(3, 2, 0, 5));
        pnlShipHolder.setBackground(backroundColor);
        
        btnHorizontal = new JRadioButton();
        btnHorizontal.addActionListener(this);
        btnHorizontal.setActionCommand("1");
        btnHorizontal.setSelected(true);
        btnHorizontal.setBackground(backroundColor);
        
        btnVertical = new JRadioButton();
        btnVertical.addActionListener(this);
        btnVertical.setActionCommand("0");
        btnVertical.setBackground(backroundColor);
        
        btgHorizontalVertical = new ButtonGroup();
        btgHorizontalVertical.add(btnHorizontal);
        btgHorizontalVertical.add(btnVertical);

        lblHorizontal = new JLabel("<html><b>HORIZONTAL");
        lblHorizontal.setBackground(backroundColor);
        lblHorizontal.setForeground(textColor);
        
        lblVertical = new JLabel("<html><b>VERTICAL</b></html>");
        lblVertical.setBackground(backroundColor);
        lblVertical.setForeground(textColor);
        
        pnlHorizontalVertical = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnlHorizontalVertical.setBackground(backroundColor);
        pnlHorizontalVertical.add(lblHorizontal);
        pnlHorizontalVertical.add(btnHorizontal);
        pnlHorizontalVertical.add(lblVertical);
        pnlHorizontalVertical.add(btnVertical);

        pnlShipHolder.add(pnlShip5);
        pnlShipHolder.add(pnlShip4);
        pnlShipHolder.add(pnlShip3a);
        pnlShipHolder.add(pnlShip3b);
        pnlShipHolder.add(pnlShip2);
        pnlShipHolder.add(pnlHorizontalVertical);

        pnlNorth = new JPanel(new BorderLayout(0, 5));
        pnlNorth.setBackground(backroundColor);
       
        lblTitle =  new JLabel("<html><b>SELECT THE SHIP YOU WISH TO PLACE</b></html>",SwingConstants.CENTER);
        lblTitle.setForeground(textColor);
        
        pnlNorth.add(lblTitle,BorderLayout.NORTH);
        pnlNorth.add(pnlShipHolder, BorderLayout.CENTER);

        gameGrid = new GameGrid(rows, cols);
        pnlGrid = new JPanel(new GridLayout(11, 11));
        pnlGrid.setBackground(backroundColor);
        
        arrayGrid = new JComponent[11][11];
        arrayGrid[0][0] = new JLabel("");
        arrayGrid[0][0].setBackground(backroundColor);
        arrayGrid[0][0].setOpaque(true);
        
        for (int a = 1; a < 11; a++) {
            arrayGrid[a][0] = new JLabel(Integer.toString(a),
                    SwingConstants.CENTER);
            arrayGrid[0][a] = new JLabel(Character.toString((char) (a + 64)),
                    SwingConstants.CENTER);
            arrayGrid[a][0].setBackground(backroundColor);
            arrayGrid[0][a].setBackground(backroundColor);
            arrayGrid[a][0].setFont(new Font("DejaVu Sans", Font.BOLD,12));
            arrayGrid[0][a].setFont(new Font("DejaVu Sans", Font.BOLD,12));
            arrayGrid[a][0].setForeground(textColor);
            arrayGrid[0][a].setForeground(textColor);
            arrayGrid[a][0].setOpaque(true);
            arrayGrid[0][a].setOpaque(true);
        }

        for (int a = 1; a < 11; a++) {
            for (int b = 1; b < 11; b++) {
                gameGrid.getButton(a - 1, b - 1).addMouseListener(this);
                arrayGrid[a][b] = (gameGrid.getButton(a - 1, b - 1));
            }
        }

        for (int a = 0; a < 11; a++) {
            for (int b = 0; b < 11; b++) {
                pnlGrid.add(arrayGrid[a][b]);
            }
        }

        buttonGroup = new JPanel(new GridLayout(1, 2));
        pnlConfirmHome = new JPanel(new GridLayout(2, 1));
        pnlConfirmHome.setBackground(backroundColor);
        pnlConfirmHome.setBorder(BorderFactory.createLineBorder(backroundColor, 2));
        
        bdrRaisedButton = BorderFactory.createRaisedBevelBorder();
        bdrLoweredButton = BorderFactory.createLoweredBevelBorder();

        placementStatus = new JLabel("<html><p style=\"color:white; font-size:16px;\"><b>Place your ships!</b></p><br></html>");
        placementStatus.setHorizontalAlignment(JLabel.CENTER);
        btnConfirm = new JButton("<html><b>CONFIRM</b></html>");
        btnConfirm.setEnabled(false);
        btnConfirm.setBackground(backroundColor);
        btnConfirm.setForeground(Color.GRAY);
        btnConfirm.setBorder(bdrLoweredButton);
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            	if (shipsLeftToPlace == 0) {
                    player.sendServerRequest(new Request("PlayerReady", playerName, opponentName));
                    placementStatus.setText("<html><p style=\"color:white; font-size:16px;\"><b>Waiting for opponent...</b></p><br></html>");
                    btnConfirm.setEnabled(false);
                    btnConfirm.setBorder(bdrLoweredButton);
                    btnConfirm.setForeground(Color.GRAY);
                }
            }
        });
        
        btnHome = new JButton("<html><b>HOME</b></html>");
        btnHome.setBackground(backroundColor);
        btnHome.setForeground(textColor);
        btnHome.setBorder(bdrRaisedButton);
        btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.sendServerRequest(new Request("UserWentBackToLobby", name, opponentName));
				player.reshowLobby();
				
			}
		});

        buttonGroup.add(btnConfirm);
        buttonGroup.add(btnHome);
        pnlConfirmHome.add(placementStatus);
        pnlConfirmHome.add(buttonGroup);
        
        content.add(pnlNorth, BorderLayout.NORTH);
        content.add(pnlGrid, BorderLayout.CENTER);
        content.add(pnlConfirmHome, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    player.sendServerRequest(new Request("UserLeftGame", name,
                            opponentName));
            }

        });
    }

    public boolean validPathCheck(int col, int row) {
        boolean validPath = true;

        if (shipSize == 0) {
            validPath = false;
        } else {
            if (shipHorVert == 1) {
                if (!(col < ((cols - shipSize) + 1))) {
                    validPath = false;
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (gameGrid.getButton(row, col + i).isOccupied()) {
                            validPath = false;
                        }
                    }
                }
            } else if (shipHorVert == 0) {
                if (!(row < ((rows - shipSize) + 1))) {
                    validPath = false;
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (gameGrid.getButton(row + i, col).isOccupied()) {
                            validPath = false;
                        }
                    }
                }
            }
        }

        return validPath;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer actionNumber = Integer.parseInt(e.getActionCommand());

        if (actionNumber.equals(null)) {
            shipSize = 0;
        } else if (actionNumber < 2) {
            shipHorVert = actionNumber;
        } else {
            shipSize = actionNumber;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).setEnabled(false);
                    gameGrid.getButton(row, col + i).setIcon(null);
                    gameGrid.getButton(row, col + i).setBackground(Color.RED);
                }
            } else if (shipHorVert == 0) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).setEnabled(false);
                    gameGrid.getButton(row + i, col).setIcon(null);
                    gameGrid.getButton(row + i, col).setBackground(Color.RED);
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).setEnabled(true);
                    gameGrid.getButton(row, col + i).setDefaultIcon();
                    gameGrid.getButton(row, col + i).setBackground(null);
                }
            } else if (shipHorVert == 0) {
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).setEnabled(true);
                    gameGrid.getButton(row + i, col).setDefaultIcon();
                    gameGrid.getButton(row + i, col).setBackground(null);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = ((GameButton) e.getSource()).getRow();
        int col = ((GameButton) e.getSource()).getColumn();

        if (validPathCheck(col, row)) {
            if (shipHorVert == 1) {
                b.addShip(new Ship(new Point(row, col), new Point(row, col
                        + shipSize - 1), 'H'));
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row, col + i).removeMouseListener(this);
                    gameGrid.getButton(row, col + i).setEnabled(false);
                    gameGrid.getButton(row, col + i)
                            .setOccupied(true, shipSize);
                    gameGrid.getButton(row, col + i).setIcon(null);
                    gameGrid.getButton(row, col + i).setBackground(Color.GRAY);
                    gameGrid.getButton(row, col + i).setBorderToDark();
                }
            } else if (shipHorVert == 0) {
                b.addShip(new Ship(new Point(row, col), new Point(row
                        + shipSize - 1, col), 'V'));
                for (int i = 0; i < shipSize; i++) {
                    gameGrid.getButton(row + i, col).removeMouseListener(this);
                    gameGrid.getButton(row + i, col).setEnabled(false);
                    gameGrid.getButton(row + i, col)
                            .setOccupied(true, shipSize);
                    gameGrid.getButton(row + i, col).setIcon(null);
                    gameGrid.getButton(row + i, col).setBackground(Color.GRAY);
                    gameGrid.getButton(row, col + i).setBorderToDark();
                }
            }
            shipsLeftToPlace--;

            int selectedButton = arrayShipButtons.indexOf(btgShips
                    .getSelection());

            btgShips.getSelection().setEnabled(false);
            btgShips.clearSelection();

            arrayShipButtons.remove(selectedButton);

            if (shipsLeftToPlace != 0) {
                btgShips.setSelected(arrayShipButtons.get(0), true);
                shipSize = Integer.parseInt(arrayShipButtons.get(0)
                        .getActionCommand());
                placementStatus.setText("<html><p style=\"color:white; font-size:16px;\"><b> (" + shipsLeftToPlace + ") ships left to place.</b></p><br></html>");
            } else {
                shipSize = 0;
                btgHorizontalVertical.clearSelection();
                btnHorizontal.setEnabled(false);
                btnVertical.setEnabled(false);
                placementStatus.setText("<html><p style=\"color:white; font-size:16px;\"><b>Finished! Press confirm to continue.</b></p><br></html>");
                btnConfirm.setEnabled(true);
                btnConfirm.setBorder(bdrRaisedButton);
                btnConfirm.setForeground(textColor);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void startGame() {
        setVisible(false);
        player.placementFinished(gameGrid, b);
        dispose();
    }
}
