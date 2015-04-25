package com.laituanmanh.AI2048.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bulenkov.game2048.Game2048;
import com.laituanmanh.AI2048.gamesolver.BFSAlgorithm;
import com.laituanmanh.AI2048.gamesolver.DFSAlgorithm;
import com.laituanmanh.AI2048.gamesolver.GameSolver;
import com.laituanmanh.AI2048.gamesolver.SimpleHillAlgorithm;
import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;
import com.laituanmanh.commontools.MemoryMeasure;
import com.laituanmanh.commontools.MemoryMeasure.NoEndPointChecked;
import com.laituanmanh.commontools.ReschedulableTimer;
import com.laituanmanh.commontools.TimeMeasure;

public class GameUIAdapter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1219140228522083415L;

	private Game2048 gameBoard;
	private GameSolver solver;
	private GameUIAdapter thisInstance;
	private ReschedulableTimer timer = new ReschedulableTimer(1);

	private JRadioButton dfs;
	private JRadioButton bfs;
	private JRadioButton simpleHill;
	private JSlider speedBar;
	private JComboBox<Integer> goal;
	private JButton solveButton;

	private Thread solverThread;

	private final int delayTimer = 1; // 1s
	private boolean isSolving = false;

	private static String dfsString = "DFS";
	private static String brfsString = "BFS";
	private static String hillString = "Simple hill";

	public GameUIAdapter() {
		thisInstance = this;
		// super();
		this.setTitle("2048 AI");
		this.setResizable(false);
		this.setSize(600, 370);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		// Add board game UI to interface.
		gameBoard = new Game2048();
		gameBoard.setBounds(0, 0, 340, 340);
		// this.getContentPane().add(gameBoard);

		// Init interface
		JLabel algoLbl = new JLabel("Seaching algorithm");
		algoLbl.setBounds(345, 0, 300, 50);

		// Create algorithm select button.
		ButtonGroup group = new ButtonGroup();

		dfs = new JRadioButton(dfsString);
		dfs.setSelected(true);
		dfs.setBounds(350, 40, 200, 50);

		bfs = new JRadioButton(brfsString);
		bfs.setBounds(350, 80, 200, 50);

		simpleHill = new JRadioButton(hillString);
		simpleHill.setBounds(350, 120, 200, 50);

		JLabel speedLbl = new JLabel("Running speed");
		speedLbl.setBounds(345, 160, 200, 50);

		speedBar = new JSlider(1, 30);
		speedBar.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (isSolving)
					timer.rescheduleInterval(delayTimer * 1000
							/ speedBar.getValue());
			}
		});
		speedBar.setBounds(350, 190, 200, 50);

		JLabel goalLbl = new JLabel("Goal: ");
		goalLbl.setBounds(345, 260, 80, 25);

		goal = new JComboBox<Integer>();
		goal.addItem(4);
		goal.addItem(8);
		goal.addItem(16);
		goal.addItem(32);
		goal.addItem(64);
		goal.addItem(128);
		goal.addItem(256);
		goal.addItem(512);
		goal.addItem(1024);
		goal.addItem(2048);
		goal.addItem(4096);
		goal.addItem(8192);
		goal.setBounds(450, 260, 80, 25);

		JButton inputButton = new JButton("Nhập");
		inputButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSolving) {
					timer.cancel();
					toggleButton();
				}

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"Text file", "txt"));
				if (fileChooser.showOpenDialog(thisInstance) == JFileChooser.APPROVE_OPTION) {
					try {

						Scanner scanner = new Scanner(new File(fileChooser
								.getSelectedFile().toString()));
						int[] board = new int[16];
						for (int i = 0; i < 4; i++)
							for (int j = 0; j < 4; j++) {
								board[i * 4 + j] = scanner.nextInt();
							}
						scanner.close();
						gameBoard.setTiles(board);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		inputButton.setBounds(345, 300, 100, 25);

		solveButton = new JButton("Giải");
		solveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleButton();
			}
		});
		solveButton.setBounds(450, 300, 100, 25);

		group.add(dfs);
		group.add(bfs);
		group.add(simpleHill);

		this.add(gameBoard);
		this.add(algoLbl);
		this.add(dfs);
		this.add(bfs);
		this.add(simpleHill);
		this.add(speedLbl);
		this.add(speedBar);
		this.add(goalLbl);
		this.add(goal);
		this.add(inputButton);
		this.add(solveButton);

		gameBoard.setFocusable(true);
		gameBoard.enableListener();

		gameBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gameBoard.requestFocus();
			}
		});
	}

	private void toggleButton() {
		if (isSolving) {
			solverThread.interrupt();
			timer.cancel();
			isSolving = false;
			solveButton.setText("Giải");
			gameBoard.enableListener();
			gameBoard.requestFocus();

		} else {
			solverThread = new Thread(new SolvingGameTask());
			solverThread.start();
			solveButton.setText("Dừng");
			isSolving = true;

			gameBoard.disableListener();
		}
	}

	public class SolutionPresenter implements Runnable {

		SolutionPath path;

		public SolutionPresenter(SolutionPath solution) {
			this.path = solution;
		}

		@Override
		public void run() {
			if (path == null) {
				toggleButton();
				timer.cancel();
			}
			gameBoard.setTiles(path.getState().flat());
			path = path.getNextState();
		}
	}

	public class SolvingGameTask implements Runnable {

		@Override
		public void run() {
			if (dfs.isSelected())
				solver = new DFSAlgorithm();
			if (bfs.isSelected())
				solver = new BFSAlgorithm();
			if (simpleHill.isSelected())
				solver = new SimpleHillAlgorithm();

			GameState state = new GameState(gameBoard.getState());
			state.setGoal((int) goal.getSelectedItem());
			TimeMeasure.reset();
			MemoryMeasure.reset();
			SolutionPath path = solver.findSolution(state);
			if (path != null) {
				printSol(path);
				System.out.println("Solution Found");
				try {
					System.out.println("It takes "
							+ MemoryMeasure.getUsageMemoryMeasureInKB()
							+ "kb memory.");
					System.out.println("It takes "
							+ TimeMeasure.getUsageMemoryMeasureInSec()
							+ "s to find solution.");
				} catch (NoEndPointChecked e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (com.laituanmanh.commontools.TimeMeasure.NoEndPointChecked e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				timer = new ReschedulableTimer(1);
				timer.scheduleInterval(new SolutionPresenter(path), delayTimer
						* 1000 / speedBar.getValue());
			} else {
				System.out.println("Solution Not Found");
			}

		}

		public void printSol(SolutionPath path) {
			if (path.getNextState() == null) {
				path.getState().PrintState();
				System.out.println("Now, win!");
				return;
			}
			path.getState().PrintState();
			// System.out.print("step " + step + " ");
			System.out.print(path.getAction() + "\n-----------\n");
			printSol(path.getNextState());

		}
	}
}
