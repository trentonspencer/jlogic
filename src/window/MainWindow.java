package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import common.GateManager;
import common.sim.GateComponent;
import common.sim.Pin;
import common.sim.PinComponent;
import common.sim.PinType;
import common.sim.Simulation;

/**
 * The Main class of the program
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	/**
	 * Container for GateComponents
	 */
	private GateManager gateManager;
	
	/**
	 * The JPanel the window draws its components to
	 */
	private JPanel contentPane;

	/**
	 * Entry point
	 * @param args Command-line arguments
	 */
	public static void main(String[] args)
	{
		//Try to make the window match the theme of the OS it is on
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		//Wait for the theme to properly set and then show the frame and start the sim
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Ticks the sim every 1ms
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						Simulation.tick();
					}
				}, 0, 1);
			}
		});
	}

	public MainWindow()
	{
		initGateComponents();
		initComponents();
	}
	
	/**
	 * Creates all of the GateComponents and adds them to {@link MainWindow#gateManager}
	 */
	private void initGateComponents()
	{
		gateManager = new GateManager();
		gateManager.addContainer("Inputs");
		gateManager.addContainer("Outputs");
		gateManager.addContainer("Gates");
		
		PinComponent[] inputGates = { new PinComponent("A", PinType.OUTPUT, false, new Point(20, 10)) };
		PinComponent[] outputGates = { new PinComponent("A", PinType.INPUT, true, new Point(10, 20)) };
		
		PinComponent[] threePinGates = {
				new PinComponent("A", PinType.INPUT, true, new Point(0, 30)),
				new PinComponent("B", PinType.INPUT, true, new Point(0, 10)),
				new PinComponent("C", PinType.OUTPUT, false, new Point(50, 20))
		};
		
		PinComponent[] twoPinGates = {
				new PinComponent("A", PinType.INPUT, true, new Point(0, 20)),
				new PinComponent("B", PinType.OUTPUT, false, new Point(40, 20))
		};
		
		PinComponent[] twoPinNOTGates = {
				new PinComponent("A", PinType.INPUT, true, new Point(0, 20)),
				new PinComponent("B", PinType.OUTPUT, false, new Point(50, 20))
		};
		
		gateManager.add("Inputs", new GateComponent("Switch", null, new Dimension(20, 20), inputGates) {
			@Override
			public void draw(Graphics g, Pin[] pins) {
				g.drawRect(0, 0, 20, 20);
				
				if(pins != null && pins[0].getState())
					g.setColor(Color.GREEN);
				g.fillRect(3, 3, 17, 17);
				g.setColor(Color.BLACK);
			}
			
			@Override
			public void onMouseClicked(MouseEvent e, Pin[] pins) {
				pins[0].setState(!pins[0].getState());
			}
		});
		
		gateManager.add("Inputs", new GateComponent("Button", null, new Dimension(20, 20), inputGates) {
			@Override
			public void draw(Graphics g, Pin[] pins) {
				if(pins != null)
					g.setColor(pins[0].getState() ? Color.GREEN : Color.BLACK);
				g.fillRect(0, 0, 20, 20);
				g.setColor(Color.BLACK);
			}
			
			@Override
			public void onMousePressed(MouseEvent e, Pin[] pins) {
				pins[0].setState(true);
			}
			
			@Override
			public void onMouseReleased(MouseEvent e, Pin[] pins) {
				pins[0].setState(false);
			}
		});
		
		gateManager.add("Outputs", new GateComponent("LED", null, new Dimension(20, 20), outputGates) {
			@Override
			public void draw(Graphics g, Pin[] pins) {
				if(pins != null && pins[0].getState())
					g.setColor(Color.RED);
				g.fillOval(0, 0, 20, 20);
				
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, 20, 20);
			}
		});

		gateManager.add("Gates", new GateComponent("Diode", null, "/resources/gates/Diode.png", twoPinGates) {
			@Override
			public void performLogic(Pin[] pins) {
				pins[1].setState(pins[0].getState());
			}
		});
		
		gateManager.add("Gates", new GateComponent("NOT", null, "/resources/gates/NOT.png", twoPinNOTGates) {
			@Override
			public void performLogic(Pin[] pins) {
				pins[1].setState(!pins[0].getState());
			}
		});
		
		gateManager.add("Gates", new GateComponent("AND", null, "/resources/gates/AND.png", threePinGates) {
			@Override
			public void performLogic(Pin[] pins) {
				pins[2].setState(pins[0].getState() && pins[1].getState());
			}
		});
		
		gateManager.add("Gates", new GateComponent("OR", null, "/resources/gates/OR.png", threePinGates) {
			@Override
			public void performLogic(Pin[] pins) {
				pins[2].setState(pins[0].getState() || pins[1].getState());
			}
		});
		
		gateManager.add("Gates", new GateComponent("XOR", null, "/resources/gates/XOR.png", threePinGates) {
			@Override
			public void performLogic(Pin[] pins) {
				pins[2].setState(pins[0].getState() ^ pins[1].getState());
			}
		});
	}
	
	/**
	 * Initializes all of the UI components
	 */
	private void initComponents()
	{
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/icon_16.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/icon_32.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/icon_48.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/icon_64.png")));
		icons.add(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/resources/icon_256.png")));
		setIconImages(icons);
		
		setPreferredSize(new Dimension(800, 600));
		setTitle("JLogic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 665, 490);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulation.clear();
			}
		});
		mnFile.add(mntmNew);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mntmUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Simulation.undo();
			}
		});
		mnFile.add(mntmNew);
		mnEdit.add(mntmUndo);
		
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(800, 600));
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(UIManager.getColor("SplitPane.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		JLogicPanel jsim_simPanel = new JLogicPanel();
		splitPane.setRightComponent(jsim_simPanel);
		
		JScrollPane jsim_itemClassListScrollPane = new JScrollPane();
		jsim_itemClassListScrollPane.setPreferredSize(new Dimension(140, 200));
		
		JTree jsim_itemClassTree = new JTree();
		jsim_itemClassTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Components") {
				{
					Set<String> keys = gateManager.getContainers();
					Iterator<String> it = keys.iterator();
					String key;
					ArrayList<GateComponent> components;
					
					while(it.hasNext()) {
						key = it.next();
						components = gateManager.getContainer(key);
						
						if(components != null && !components.isEmpty()) {
							DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
							
							for(GateComponent component : components) {
								DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(component);
								node.add(leaf);
							}
							
							add(node);
						}
					}
				}
			}
		));
		jsim_itemClassTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object obj = ((DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent()).getUserObject();
				if(obj instanceof GateComponent)
					jsim_simPanel.setUserGateComponent((GateComponent)obj);
				else
					jsim_simPanel.setUserGateComponent(null);
			}
		});
		jsim_itemClassTree.setRootVisible(false);
		jsim_itemClassListScrollPane.setViewportView(jsim_itemClassTree);
		splitPane.setLeftComponent(jsim_itemClassListScrollPane);
	}
}
