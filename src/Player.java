import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Player extends JFrame implements ActionListener {
    private static String name;
    private String opponentName;
    private boolean isTheirTurn;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private DefaultListModel<String> playersModel;
    private JLabel chatArea = null;

    private JLabel prompt;
    private JTextField enterName;
    private JButton connectButton;
    private JButton playButton;
    private JButton playRandButton;
    private JPanel rightPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JPanel listPanel;
    private ShipPlacementUI sui;
    private GameUI gui;

    public static void main(String[] args) {
        new Player().setVisible(true);
    }

    public Player() {
        super("Welcome");
        initialiseGUI();
        setSize(new Dimension(260, 325));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called
        // after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void newConnection() {
        try {

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            opponentName = null;
            isTheirTurn = false;
            out.writeObject(new Request("UserJoinedLobby", name));
            out.writeObject(new Request("RetrieveLobby", name));

            Thread receivingThread = new Thread() {
                Request input;

                @Override
                public void run() {
                    try {

                        while ((input = (Request) in.readObject()) != null) {
                            if (input.getActionType().equals("UserJoinedLobby")) {
                                if (!input.getObject().equals(name)) {
                                    playersModel.addElement((String) input
                                            .getObject());
                                }
                            } else if (input.getActionType().equals(
                                    "UserLeftLobby")) {
                                playersModel.removeElement(input.getObject());
                            } else if (input.getDestination().equals(name)) {
                                if (input.getActionType().equals("GameRequest")) {
                                    // check if there is already a dialog box
                                    // opened.
                                    // if user accepts one of the game requests
                                    // - the other dialogs are closed
                                    // and an answer 'no' is sent to the server
                                    int requestAnswer = JOptionPane
                                            .showConfirmDialog(null,
                                                    "Do you want to play a game with "
                                                            + input.getOrigin()
                                                            + "?",
                                                    "Game Request",
                                                    JOptionPane.YES_NO_OPTION);
                                    // no = 1, yes = 0
                                    if (requestAnswer == 1) {
                                        out.writeObject(new Request(
                                                "GameRequestAnswer", name,
                                                input.getOrigin(), "No"));
                                    }
                                    if (requestAnswer == 0) {
                                        out.writeObject(new Request(
                                                "GameRequestAnswer", name,
                                                input.getOrigin(), "Yes"));
                                        gameFrame(input.getOrigin());
                                        opponentName = input.getOrigin();
                                    }
                                } else if (input.getActionType().equals(
                                        "GameRequestAnswer")) {
                                    if (input.getObject().equals("Yes")) {
                                        gameFrame(input.getOrigin());
                                        opponentName = input.getOrigin();
                                        isTheirTurn = true;
                                    } else if (input.getObject().equals("No")) {
                                        JOptionPane.showMessageDialog(null,
                                                "Game request denied from "
                                                        + input.getOrigin()
                                                        + ".",
                                                "Game request denied",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                } else if (input.getActionType().equals(
                                        "RetrieveLobby")) {
                                    ArrayList<String> playersList = (ArrayList<String>) input
                                            .getObject();
                                    for (int i = 0; i < playersList.size(); i++) {
                                        if (!playersList.get(i).equals(name)) {
                                            // System.out.println(playersList.get(i));
                                            playersModel.addElement(playersList
                                                    .get(i).toString());
                                        }
                                    }
                                } else if (input.getActionType().equals(
                                        "ReceiveMessage")) {
                                    gui.appendMessage((String) input.getObject(), input.getOrigin());
                                } else if (input.getActionType().equals(
                                        "UserLeftGame")) {
                                    // Quitting game on selection screen
                                    mainGUI();
                                    setVisible(true);
                                    opponentName = null;
                                    sui.dispose();
                                    out.writeObject(new Request(
                                            "UserJoinedLobby", name));
                                    out.writeObject(new Request(
                                            "RetrieveLobby", name));
                                    JOptionPane
                                            .showMessageDialog(
                                                    null,
                                                    "Your opponent quit! You win (by default)",
                                                    "Opponent Quit",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                } else if (input.getActionType().equals(
                                        "UserLeftGame2")) {
                                    // Quitting game on game screen
                                    mainGUI();
                                    setVisible(true);
                                    opponentName = null;
                                    out.writeObject(new Request(
                                            "UserJoinedLobby", name));
                                    out.writeObject(new Request(
                                            "RetrieveLobby", name));
                                    gui.dispose();
                                    JOptionPane
                                            .showMessageDialog(
                                                    null,
                                                    "Your opponent quit! You win (by default)",
                                                    "Opponent Quit",
                                                    JOptionPane.INFORMATION_MESSAGE);

                                } else if (input.getActionType().equals(
                                        "RandomGameRequestFail")) {
                                    JOptionPane
                                            .showMessageDialog(
                                                    null,
                                                    "We couldn't find any active players at the moment. Please try again later.",
                                                    "Error",
                                                    JOptionPane.ERROR_MESSAGE);

                                } else if (input.getActionType().equals("MoveResult")) {
                                    GameMove gm = (GameMove) input.getObject();
                                    Point coordinates = gm.getMoveCoordinates();
                                    String playerName = gm.getPlayerName();
                                    String outcome = gm.getMoveResult();

                                    if (name.equals(playerName)) {
                                        //update enemy board
                                        if(outcome.equals("hit") || outcome.equals("destroyed")) {
                                            isTheirTurn = true;
                                        }
                                        gui.updateEnemyBoard(outcome, coordinates);
                                    } else {
                                        //update own board
                                        if(!outcome.equals("hit") && !outcome.equals("destroyed")) {
                                            isTheirTurn = true;
                                        }
                                        gui.updateOwnBoard(outcome, coordinates);
                                    }
                                } else if(input.getActionType().equals("GameStart")) {
                                    sui.startGame();
                                }
                                else {
                                    System.out.println(input);
                                }
                            }
                        }
                    } catch (EOFException e) {
                        // EOFException - if this input stream reaches the end
                        // before reading eight bytes
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            receivingThread.start();
        } catch (EOFException e) {
            // EOFException - if this input stream reaches the end before
            // reading eight bytes
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null,
                    "Failed to connect to the server", "Server Error",
                    JOptionPane.WARNING_MESSAGE);

        }
    }

    public void initialiseGUI() {

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        prompt = new JLabel();
        prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                + "Enter a nickname for players to identify you with, "
                + "then hit connect!" + "</p></html>");
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        add(prompt, gc);

        enterName = new JTextField(30);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0.5;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(10, 10, 10, 10);
        add(enterName, gc);

        connectButton = new JButton();
        connectButton.setText("Connect");
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(5, 5, 10, 5);
        add(connectButton, gc);

        // Handles any players closing their game
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (socket != null) {
                    try {

                        out.writeObject(new Request("UserClosed", name));

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        // Enter button actionlistener
        connectButton.addActionListener(this);
        // Text field listener
        enterName.addActionListener(this);
    }

    public void mainGUI() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());
        GridBagConstraints gc = new GridBagConstraints();

        listPanel = new JPanel(new GridBagLayout());
        add(listPanel, BorderLayout.CENTER);

        playersModel = new DefaultListModel<String>();
        final JList<String> players = new JList<String>(playersModel);
        players.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(players);
        TitledBorder b = new TitledBorder("Currently online:");
        b.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(b);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        listPanel.add(scrollPane, gc);

        rightPanel = new JPanel(new GridBagLayout());
        add(rightPanel, BorderLayout.EAST);

        infoPanel = new JPanel();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(infoPanel, gc);

        buttonPanel = new JPanel(new GridLayout());
        playButton = new JButton("Play");
        playRandButton = new JButton("Play Random");
        buttonPanel.add(playButton);
        buttonPanel.add(playRandButton);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        gc.weighty = 0;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(buttonPanel, gc);

        playRandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("here");
                    out.writeObject(new Request("RandomGameRequest", name));
                } catch (IOException e1) {
                    System.out.println("CATCH FROM RANDOM BTN"
                            + e1.getMessage());
                }
            }
        });

        players.addMouseListener(new MouseAdapter() {
            // Repeated Code
            @Override
            public void mouseClicked(MouseEvent evt) {
                // need to disable clicked item until a response has been
                // received.
                if (evt.getClickCount() == 2) {
                    String playerName = playersModel.getElementAt(players
                            .getSelectedIndex());
                    if (!(name.equals(playerName))) {
                        try {
                            out.writeObject(new Request("GameRequest", name,
                                    playerName));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        playButton.addActionListener(new ActionListener() {
            // Repeated Code
            @Override
            public void actionPerformed(ActionEvent e) {
                if (players.getSelectedIndex() >= 0) {
                    String playerName = playersModel.getElementAt(players
                            .getSelectedIndex());
                    if (!(name.equals(playerName))) {
                        try {
                            out.writeObject(new Request("GameRequest", name,
                                    playerName));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        setMinimumSize(new Dimension(520, 370));
        pack();

    }

    public void gameFrame(String opponentName) {

        setVisible(false);
        sui = new ShipPlacementUI(this, out, in, name, opponentName);
        sui.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            // TODO not have the portnumber and ip hardcoded
            socket = new Socket("localhost", 4446);
            if(socket != null){
        		String nameToCheck = enterName.getText().toString();
        		boolean isUnique = checkName(nameToCheck);
        		System.out.println(isUnique);
        		if(isUnique == true){
        			out.writeObject(new Request("Accepted"));
        			name = nameToCheck;
        			this.setTitle("You are logged in as: " + name);
        			mainGUI();
                    newConnection();
        		}
        		else{
        			out.writeObject(new Request("Rejected"));
        			out.close();in.close();
        			socket.close();
        			//Message Appears on screen telling user, name not unique
        		}
            }
            
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "Failed to connect to the server", "Server Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void sendServerRequest(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placementFinished(GameGrid grid, Board b) {
        gui = new GameUI(grid, out, in, this, b, opponentName);
        gui.setVisible(true);
        try {
            out.writeObject(new Request("GameBoard", name, "SERVER", b));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean makeMove (Request request) throws IOException {
        if (isTheirTurn) {
            out.writeObject(request);
            isTheirTurn = false;
            return true;
        }  else {
            JOptionPane.showMessageDialog(
                    gui,
                    "It's not your turn yet! Please wait for your turn.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getName() {
        return name;
    }
    
    public boolean checkName(String nameToCheck){
    	try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			Request input;
			while ((input = (Request) in.readObject()) != null) {
				if (input.getActionType().equals("RetrieveLobby")) {
					ArrayList<String> playersList = (ArrayList<String>) input.getObject();
					if(playersList != null){
						for (int i = 0; i < playersList.size(); i++) {
							System.out.println(playersList);
							if (playersList.get(i).equals(nameToCheck)) {
								return false;
							}
						}	
					}
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
    }
}
