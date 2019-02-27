package frontend;

import frame.Warehouse;

import optimization.Loss;
import optimization.State;
import optimization.Optimizers;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

import java.lang.Thread;
import java.lang.Runnable;

import java.text.DecimalFormat;

public class Window {

	// ------------- GUI ATTRIBUTES ------------- \\
	private static final int SPACING = 5;

	private static final Color ORANGE = new Color(255, 153, 0);
	private static final Color GREEN = new Color(0, 200, 0);

	private static final String COLOR_STYLE = "color style";

	private static final String NORTH = SpringLayout.NORTH;
	private static final String EAST = SpringLayout.EAST;
	private static final String SOUTH = SpringLayout.SOUTH;
	private static final String WEST = SpringLayout.WEST;

	private static final int WAREHOUSE = 0;
	private static final int ORDER = 1;

	// ------------- ALGORITHM ATTRIBUTES ------------- \\
	private static final String[] algorithmNames = {
		"Hill climbing",
		"First choice hill climbing",
		"Local beam search",
		"Parallel hill climbing",
		"Simulated annealing"
	};

	private static final int[] stateCountRequiredIndices = {2, 3};

	// ------------- LAYOUT COMPONENTS ------------- \\
	private JFrame frame;

	private JTextField warehouseFileTxt;
	private JTextField orderFileTxt;
	private JComboBox<String> algorithmBox;
	private JTextField stateCountTxt;
	private JTextPane outputPane;
	private JButton startBtn;
	private JButton openWarehouseBtn;
	private JButton openOrderBtn;

	public Window() {
		frame = new JFrame("AI Programming Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// initialize content pane with a spring layout
		Container contentPane = frame.getContentPane();
		SpringLayout layout = new SpringLayout();
		contentPane.setLayout(layout);

		// ----------------------- WAREHOUSE FILE SECTION -----------------------
		JLabel warehouseFileLbl = new JLabel("Warehouse file");
		warehouseFileTxt = new JTextField(25);
		warehouseFileTxt.setEditable(false);
		openWarehouseBtn = new JButton("Open");
		openWarehouseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			        openFile(WAREHOUSE);
			}
		});

		// add components to the layout
		contentPane.add(warehouseFileLbl);
		contentPane.add(warehouseFileTxt);
		contentPane.add(openWarehouseBtn);

		// ----------------------- ORDER FILE SECTION -----------------------

		JLabel orderFileLbl = new JLabel("Order file");
		orderFileTxt = new JTextField();
		orderFileTxt.setEditable(false);
		openOrderBtn = new JButton("Open");
		openOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			        openFile(ORDER);
			}
		});

		// add components to the layout
		contentPane.add(orderFileLbl);
		contentPane.add(orderFileTxt);
		contentPane.add(openOrderBtn);

		// ----------------------- ALGORITHM SECTION -----------------------

		JLabel algorithmLbl = new JLabel("Algorithm");
		algorithmBox = new JComboBox<>(algorithmNames);
		algorithmBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			        updateStateCountTxt();
			}
		});

		// add components to the layout
		contentPane.add(algorithmLbl);
		contentPane.add(algorithmBox);

		// ----------------------- STATE COUNT SECTION -----------------------

		JLabel stateCountLbl = new JLabel("Number of states");
		stateCountTxt = new JTextField();

		// add components to the layout
		contentPane.add(stateCountLbl);
		contentPane.add(stateCountTxt);

		// ----------------------- START SECTION -----------------------

		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			        startAlgorithm();
			}
		});

		// add the component to the layout
		contentPane.add(startBtn);

		// ----------------------- OUTPUT SECTION -----------------------

		outputPane = new JTextPane();
		outputPane.setEditable(false);
		outputPane.addStyle(COLOR_STYLE, null);
		outputPane.setPreferredSize(new Dimension(600, 600));
		JScrollPane scrollPane = new JScrollPane(outputPane);

		// add the component to the layout
		contentPane.add(scrollPane);

		// ----------------------- WAREHOUSE FILE SECTION CONSTRAINTS -----------------------

		// warehouseFileLbl
		layout.putConstraint(WEST, warehouseFileLbl, SPACING, WEST, contentPane);
		layout.putConstraint(NORTH, warehouseFileLbl, SPACING, NORTH, contentPane);
		layout.putConstraint(EAST, warehouseFileLbl, 0, EAST, openWarehouseBtn);

		layout.putConstraint(NORTH, warehouseFileTxt, SPACING, SOUTH, warehouseFileLbl);
		layout.putConstraint(WEST, warehouseFileTxt, 0, WEST, warehouseFileLbl);

		layout.putConstraint(WEST, openWarehouseBtn, SPACING, EAST, warehouseFileTxt);
		layout.putConstraint(EAST, openWarehouseBtn, 0, EAST, openOrderBtn);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, openWarehouseBtn, 0, SpringLayout.VERTICAL_CENTER, warehouseFileTxt);

		// ----------------------- ORDER FILE SECTION CONSTRAINTS -----------------------

		layout.putConstraint(WEST, orderFileLbl, 0, WEST, warehouseFileLbl);
		layout.putConstraint(NORTH, orderFileLbl, SPACING, SOUTH, warehouseFileTxt);
		layout.putConstraint(EAST, orderFileLbl, 0, EAST, openWarehouseBtn);

		layout.putConstraint(NORTH, orderFileTxt, SPACING, SOUTH, orderFileLbl);
		layout.putConstraint(WEST, orderFileTxt, 0, WEST, orderFileLbl);
		layout.putConstraint(EAST, orderFileTxt, 0, EAST, warehouseFileTxt);

		layout.putConstraint(WEST, openOrderBtn, SPACING, EAST, orderFileTxt);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, openOrderBtn, 0, SpringLayout.VERTICAL_CENTER, orderFileTxt);

		// ----------------------- ALGORITHM SECTION CONSTRAINTS -----------------------

		layout.putConstraint(WEST, algorithmLbl, 0, WEST, warehouseFileLbl);
		layout.putConstraint(NORTH, algorithmLbl, SPACING, SOUTH, orderFileTxt);
		layout.putConstraint(EAST, algorithmLbl, 0, EAST, openWarehouseBtn);

		layout.putConstraint(NORTH, algorithmBox, SPACING, SOUTH, algorithmLbl);
		layout.putConstraint(WEST, algorithmBox, 0, WEST, algorithmLbl);
		layout.putConstraint(EAST, algorithmBox, 0, EAST, openWarehouseBtn);

		// ----------------------- STATE COUNT SECTION CONSTRAINTS -----------------------

		layout.putConstraint(WEST, stateCountLbl, 0, WEST, warehouseFileLbl);
		layout.putConstraint(NORTH, stateCountLbl, SPACING, SOUTH, algorithmBox);
		layout.putConstraint(EAST, stateCountLbl, 0, EAST, openWarehouseBtn);

		layout.putConstraint(WEST, stateCountTxt, 0, WEST, warehouseFileLbl);
		layout.putConstraint(NORTH, stateCountTxt, SPACING, SOUTH, stateCountLbl);
		layout.putConstraint(EAST, stateCountTxt, 0, EAST, openWarehouseBtn);

		// ----------------------- START SECTION CONSTRAINTS -----------------------

		layout.putConstraint(NORTH, startBtn, SPACING, SOUTH, stateCountTxt);
		layout.putConstraint(WEST, startBtn, 0, WEST, algorithmLbl);
		layout.putConstraint(EAST, startBtn, 0, EAST, openWarehouseBtn);

		// ----------------------- OUTPUT SECTION CONSTRAINTS -----------------------

		layout.putConstraint(NORTH, scrollPane, 0, NORTH, warehouseFileLbl);
		layout.putConstraint(WEST, scrollPane, SPACING * 2, EAST, openWarehouseBtn);

		// ----------------------------------------------

		layout.putConstraint(EAST, contentPane, SPACING, EAST, scrollPane);
		layout.putConstraint(SOUTH, contentPane, SPACING, SOUTH, scrollPane);

		updateStateCountTxt();

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void openFile(int type) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			String filePath = chooser.getSelectedFile().getAbsolutePath();

			switch (type) {
				case WAREHOUSE:
					if (Warehouse.readWarehouseFile(filePath)) {
						warehouseFileTxt.setText(filePath);
					} else {
						parsingError(type);
						warehouseFileTxt.setText("");
					}
					break;

				case ORDER:
					if (Warehouse.readOrderFile(filePath)) {
						orderFileTxt.setText(filePath);
					} else {
						parsingError(type);
						orderFileTxt.setText("");
					}
					break;
			}
		}
	}

	private void parsingError(int fileType) {
		String fileName = fileType == WAREHOUSE ? "warehouse" : "order";
		print("ERROR: The selected " + fileName + " file could not be parsed", Color.red);
		if (fileType == ORDER) {
			print(" (a warehouse file must be selected before selecting the order file)", Color.red);
		}
		print("\n");
	}

	private void updateStateCountTxt() {
		stateCountTxt.setEnabled(algorithmNeedsStateCount());
	}

	public void println(String str) {
		print(str + "\n", Color.black);
	}

	private void println(String str, Color col) {
		print(str + "\n", col);
	}

	public void print(String str) {
		print(str, Color.black);
	}

	private void print(String str, Color col) {
		StyledDocument doc = outputPane.getStyledDocument();
		Style style = outputPane.getStyle(COLOR_STYLE);
		StyleConstants.setForeground(style, col);

		try {
			doc.insertString(doc.getLength(), str, style);
			outputPane.select(doc.getLength(), doc.getLength());
		} catch (BadLocationException e) {
			System.err.println(e.getMessage());
		}
	}

	private boolean algorithmNeedsStateCount() {
		String selected = (String) algorithmBox.getSelectedItem();
		for (int i = 0; i < stateCountRequiredIndices.length; i++) {
			if (selected.equals(algorithmNames[stateCountRequiredIndices[i]])) {
				return true;
			}
		}
		return false;
	}

	private void startAlgorithm() {
		if (!outputPane.getText().isEmpty()) {
			print("\n");
		}
		if (checkInputFields()) {
			new Thread(new Runnable() {
				public void run() {
				        optimize();
				}
			}).start();
		}
	}

	private void optimize() {
		startBtn.setEnabled(false);
		openWarehouseBtn.setEnabled(false);
		openOrderBtn.setEnabled(false);

		String selected = (String) algorithmBox.getSelectedItem();
		int stateCount = -1;
		if (!stateCountTxt.getText().isEmpty()) {
			stateCount = Integer.parseInt(stateCountTxt.getText());
		}

		print("<<------------------ ");
		print("Starting optimization", ORANGE);
		println(" ------------------>>");
		println("Selected optimizer: " + selected);

		long startTime = System.nanoTime();

		boolean[] optimized = null;
		if (selected.equals(algorithmNames[0])) {
			optimized = Optimizers.hillClimbing(Warehouse.psuCount());
		} else if (selected.equals(algorithmNames[1])) {
			optimized = Optimizers.firstChoiceHillClimbing(Warehouse.psuCount());
		} else if (selected.equals(algorithmNames[2])) {
			// TODO: implement Local beam search
			println("ERROR: Not yet implemented", Color.red);
		} else if (selected.equals(algorithmNames[3])) {
			optimized = Optimizers.parallelHillClimbing(Warehouse.psuCount(), stateCount);
		} else if (selected.equals(algorithmNames[4])) {
			optimized = Optimizers.simulatedAnnealing(Warehouse.psuCount());
		}
		startBtn.setEnabled(true);
		openWarehouseBtn.setEnabled(true);
		openOrderBtn.setEnabled(true);

		if (optimized != null) {
			print("Optimization finished ", ORANGE);
			float deltaTime = (System.nanoTime() - startTime) / 1e9f;
			DecimalFormat format = new DecimalFormat("#.##");
			println("(" + format.format(deltaTime) + " seconds)");

			print("\nNumber of used PSUs: ");
			println(Integer.toString(Loss.numPSUsUsed(optimized)), GREEN);

			print("Number of carried items: ");
			print(Integer.toString(Warehouse.numItemsCarried(optimized)), GREEN);
			print(" (individual: ");
			print(Integer.toString(Warehouse.maskedItems(optimized).size()), GREEN);
			println(")");

			print("Loss: ");
			println(Float.toString(Loss.loss(optimized)), GREEN);

			print("\n");
			for (int i = 0; i < optimized.length; i++) {
				if (optimized[i]) {
					print("PSU identifier: ");
					println(Integer.toString(i), Color.blue);
					print("Items: ");
					println(Warehouse.getPSU(i).itemsToString(), Color.gray);
				}
			}
		} else {
			println("ERROR: Optimizer returned null", Color.red);
		}
	}

	private boolean checkInputFields() {
		if (warehouseFileTxt.getText().isEmpty()) {
			println("Please select a warehouse file", Color.red);
			return false;
		} else if (orderFileTxt.getText().isEmpty()) {
			println("Please select an order file", Color.red);
			return false;
		} else if (algorithmNeedsStateCount() && stateCountTxt.getText().isEmpty()) {
			println("Please choose the state count for " + (String) algorithmBox.getSelectedItem(), Color.red);
			return false;
		} else if (algorithmNeedsStateCount() && !stateCountTxt.getText().matches("[1-9]\\d*")) {
			println("Please select an integer greater than 0", Color.red);
			return false;
		}
		return true;
	}

}