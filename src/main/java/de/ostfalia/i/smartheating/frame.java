package de.ostfalia.i.smartheating;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputAdapter;

import org.apache.poi.ss.formula.functions.Mode;
import org.charts.dataviewer.utils.TraceColour;

import com.mysql.cj.result.BufferedRowList;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;

class Average{
	String name = "";
	double percent = 0;
}
public class frame {
	private JFrame frmSmartheater;
	private JFrame frmAnzeige;
	private JFrame frmHeizwerte;
	private static JTable table;
	private JTextField txtBox_Tag;
	private JTextField txtBox_Monat;
	private JTextField txtBox_Jahr;
	private JTextField txtBox_Stunde;
	private JTextField txtBox_Heizwert;
	static DefaultTableModel model;
	public static JTextField textField_2;
    public static JComboBox comboBox_Was;
    private JTextField txtBox_StartTag;
    private JTextField txtBox_StartMonat;
    private JTextField txtBox_StartJahr;
    public static JComboBox comboBox_Abstand;
    public int abstand;
    public static int day = java.time.LocalDate.now().getDayOfMonth();
    public static int month = java.time.LocalDate.now().getMonthValue();
    public static int year = java.time.LocalDate.now().getYear();
	public static int hour = java.time.LocalTime.now().getHour();
    public static List<String> allRooms;
	public static int valueToAdd = 0;
	public static String roomToAdd = "";
	public static JList list_Room;	
	public static JList list_Raum;	
	private JTextField txtBox_AddRoom;
	public static JCheckBox chckbxDurchschnitt;
	public static double erlaubteAbweichung = 0.5;
	public static double prozentAbweichungen = 5; 
	public static boolean showAvg = false;
	public static int wasAnzeige = 0;
	public static SmartHeating[] allAverages = null;	
	public static JTextField txtBox_erlaubteAbweichung;
	public static JLabel lbl_TempAnzeige = new JLabel("");
	public static JTextField txtBox_prozentAbweichungen;
	public static List<JLabel> labels = new ArrayList<JLabel>();
	public static List<JButton> buttons = new ArrayList<JButton>();
	public static List<JPanel> panelLight = new ArrayList<JPanel>();
	public static List<JFrame> frames = new ArrayList<JFrame>();
	public static List<JComboBox> comboBoxes = new ArrayList<JComboBox>();
	public static List<JTextField> textFields = new ArrayList<JTextField>();
	public static List<JList> lists = new ArrayList<JList>();
	public static List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	public static boolean darkmode = false;

	//Einstellungen Darkmode
	public static void darkmode() {
		darkmode = true;
		for(JLabel label : labels) {
			label.setForeground(Color.WHITE);
		}

		for(JButton button : buttons) {
			button.setForeground(Color.WHITE);
			button.setBackground(new Color(87, 87, 87));
		}

		for(JPanel panel : panelLight) {
			panel.setBackground(new Color(52, 52, 52));
		}

		for(JFrame frame : frames) {
			frame.getContentPane().setBackground(new Color(35, 35, 35));
		}

		for(JComboBox comboBox : comboBoxes) {
			comboBox.setBackground(Color.DARK_GRAY);
			comboBox.setForeground(Color.WHITE);
		}

		for(JTextField textField : textFields) {
			textField.setBackground(Color.DARK_GRAY);
			textField.setForeground(Color.WHITE);
		}

		for(JList list : lists) {
			list.setBackground(Color.DARK_GRAY);
			list.setForeground(Color.WHITE);
		}

		for(JCheckBox checkBox : checkBoxes) {
			checkBox.setBackground(new Color(70, 70, 70));
			checkBox.setForeground(Color.WHITE);
		}

		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
	}

	//Einstellungen Lightmode
	public static void lightMode(){
		darkmode = false;
		for(JLabel label : labels) {
			label.setForeground(Color.BLACK);
		}

		for(JButton button : buttons) {
			button.setForeground(Color.BLACK);
			button.setBackground(Color.WHITE);
		}

		for(JPanel panel : panelLight) {
			panel.setBackground(new Color(248, 248, 245));
		}

		for(JFrame frame : frames) {
			frame.getContentPane().setBackground(new Color(237, 237, 233));
		}

		for(JComboBox comboBox : comboBoxes) {
			comboBox.setBackground(Color.WHITE);
			comboBox.setForeground(Color.BLACK);
		}

		for(JTextField textField : textFields) {
			textField.setBackground(Color.WHITE);
			textField.setForeground(Color.BLACK);
		}

		for(JList list : lists) {
			list.setBackground(Color.WHITE);
			list.setForeground(Color.BLACK);
		}

		for(JCheckBox checkBox : checkBoxes) {
			checkBox.setBackground(Color.WHITE);
			checkBox.setForeground(Color.BLACK);
		}

		table.setBackground(Color.WHITE);
		table.setForeground(Color.BLACK);
	}


	public static SmartHeating erstellungGraphen(TraceColour[] allColors, int colorIndex, int index, SmartHeating object, String i){
		SmartHeating.graphConfig.y = "Messwerte";
		if(wasAnzeige == 1){
			SmartHeating.graphConfig.y = "Kosten in cent";
			SmartHeating preisWerte = new SmartHeating();
			for(double u: object.getMeasurements()){				
				preisWerte.addMeasurement(u * Float.parseFloat(textField_2.getText()));

				preisWerte.setName(i);
			}
			
			object = preisWerte;

		}

		
		erlaubteAbweichung = Double.parseDouble(txtBox_erlaubteAbweichung.getText())/100;
		prozentAbweichungen = Double.parseDouble(txtBox_prozentAbweichungen.getText());
		/* Here is the explanation for the code above:
		1. The method getDeviationUp() is a method which calculates the deviation of the values of the objects and returns an array.
		2. The for loop is used to get the values of the array.
		3. If the value is higher than the proportion it will be printed in the console and a message box will be shown. 
		*/
		Average[] averagesUp = getDeviationUp(erlaubteAbweichung, object);
		for(Average avg: averagesUp){
			if(avg.percent>=prozentAbweichungen){
				JOptionPane.showMessageDialog(null, "Abweichung in/im " + avg.name + " liegt um " + avg.percent + "% über dem Durchschnittswert.");
			}										
		}


		/* Here is the explanation for the following code:
		1. First, I need to get the averages of all the objects. That's what getDeviationDown does.
		2. I put all the averages in a list of averages.
		3. I check if the current average is higher than my threshold.
		4. If it is, I print it out.
		5. If it is not, I don't do anything. 
		*/
		Average[] averagesDown = getDeviationDown(erlaubteAbweichung, object);
		for(Average avg: averagesDown){
			if(avg.percent>=prozentAbweichungen){
				JOptionPane.showMessageDialog(null, "Abweichung in/im "+ avg.name + " liegt um " + avg.percent + "% unter dem Durchschnittswert.");
			}										
		}
		TraceColour color = allColors[colorIndex];
		object.setTraceColour(color);
		/* Here is the explanation for the code above:
		1. Create a new SmartHeating object
		2. Get the average value of the SmartHeating object
		3. Fill the new SmartHeating object with the average value
		4. Set the color of the average line
		5. Set the name of the average line
		6. Put the new SmartHeating object to the allAverages array 
		*/
		if(showAvg){
			
			SmartHeating object_avg = new SmartHeating();
			double avg = object.getAverage();
			for(int q = 0; q < object.getMeasurements().size(); q++){
				object_avg.addMeasurement(avg);
			}
			object_avg.setTraceColour(color);
			object_avg.setRoomName("Durchschnitt " + i);

			allAverages[index] = object_avg;
			
		}

		return object;
	}

	/* Here is the explanation for the following code:
	1. First, I calculate the total average of all the data using a for loop.
	2. Then, I create an array of Average objects, which is a class that I created in order to store the name and percentage of the deviations.
	3. Next, I create a for loop that goes through each SmartHeating object in the data array.
	4. Then, I create a counter that will keep track of how many measurements are above the threshold.
	5. Next, I create a variable that will keep track of the total number of measurements.
	6. Then, I create a for loop that goes through each measurement in the SmartHeating object.
	7. Next, I increase the total variable by 1.
	8. Then, I check if the measurement is above the threshold. If it is, I increase the counter by 1.
	9. After that, I create an Average object.
	10. Then, I set the name of the Average object to the name of the SmartHeating object.
	11. Next, I calculate the percentage of measurements that are above the threshold. I round the percentage to 2 decimal places.
	12. After that, I add the Average object to the averages array.
	13. Finally, I return the averages array. 
	*/

    public static Average[] getDeviationUp(double threshold, SmartHeating...data){
        double totalAvg = 0;
        for(SmartHeating s: data){
            totalAvg += s.getAverage();
        }
        totalAvg /= data.length;
        Average[] averages = new Average[data.length];
        int index = 0;
        for(SmartHeating s: data){
            
            int counter = 0;
            int total = 0;
            for(double m: s.getMeasurements()){
                total++;
                if(m >= totalAvg * (1 + threshold)){
                    counter++;
                }
            }
            Average a = new Average();
            a.name = s.getName();

			//Round to 2 decimal places
			a.percent = Math.round((double)counter / total * 10000) / 100.0;
            averages[index] = a;
            index++;
        }
        return averages;
		
    }

	/* Here is the explanation for the following code:
	1. The function is called getDeviationDown and takes a double threshold and a SmartHeating vararg. The vararg is an array of SmartHeating objects, and the function should therefore return an array of Average objects.
	2. The first line is just a simple for loop that adds all the averages of the SmartHeating objects to a variable called totalAvg.
	3. The next line divides the totalAvg by the length of the SmartHeating vararg, which is the number of SmartHeating objects in the vararg, and assigns it to the variable totalAvg.
	4. The next line creates an array of Average objects, and the length of the array is the same as the number of SmartHeating objects in the vararg.
	5. The next line creates an integer variable called index, and assigns it the value 0.
	6. The next line is a for loop that goes through all the SmartHeating objects in the vararg.
	7. The next line creates an integer variable called counter, and assigns it the value 0.
	8. The next line creates an integer variable called total, and assigns it the value 0.
	9. The next line is a for loop that goes through all the measurements in the current SmartHeating object in the vararg.
	10. The next line increments the total variable with 1.
	11. The next line checks if the current measurement is less than or equal to the totalAvg multiplied by 1 +- threshold, and if it is, it increments the counter variable with 1.
	12. The next line creates an Average object called a.
	13. The next line assigns the name of the current SmartHeating object to the name variable in the Average object.
	14. The next line assigns the percentage of measurements that were less than or equal to the totalAvg multiplied by 1 +- threshold to the percent variable in the Average object. The percentage is calculated by dividing the counter variable by the total variable, then multiplying the result by 100, and then rounding the result to 2 decimal places.
	15. The next line assigns the Average object a to the index'th position in the array of Average objects.
	16. The next line increments the index variable with 1.
	17. The next line returns the array of Average objects. */
	public static Average[] getDeviationDown(double threshold, SmartHeating...data){
        double totalAvg = 0;
        for(SmartHeating s: data){
            totalAvg += s.getAverage();
        }
        totalAvg /= data.length;
        Average[] averages = new Average[data.length];
        int index = 0;
        for(SmartHeating s: data){
            
            int counter = 0;
            int total = 0;
            for(double m: s.getMeasurements()){
                total++;
                if(m <= totalAvg * (1 +-threshold)){
                    counter++;
                }
            }
            Average a = new  Average();
            a.name = s.getName();
            a.percent = Math.round((double)counter / total * 10000) / 100.0;
            averages[index] = a;
            index++;
        }
        return averages;
    }


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					frame window = new frame();
					window.frmSmartheater.setVisible(true);
					window.frmHeizwerte.setVisible(false);
					window.frmAnzeige.setVisible(false);
					darkmode();
					lightMode();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		updateTemperature();
	}

	public static void updateTemperature() {
		utils.getTemperature();
		lbl_TempAnzeige.setText(SmartHeating.temperature + "°C");
		//wait for 5 seconds
		try {
			Thread.sleep(5000);
			updateTemperature();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}


	/* Here is the explanation for the following code:
	1. I have a mainframe window, which contains 3 other windows. One is for the main menu, another one for the heating values and another one for the display. The main menu is the first window that should appear, so I set the visibility of the main menu window to true and the visibility of the other windows to false. This is done by the following lines of code:

	window.frmSmartheater.setVisible(true);
	window.frmHeizwerte.setVisible(false);
	window.frmAnzeige.setVisible(false);

	2. I have a button in the main menu window, which should open the heating values window. So I added an actionListener to the button, which starts a new thread. I want to do this in a new thread, because I want to have the main menu window still visible while the heating values window is opened. I think this is not possible with a normal actionListener. So I added the following code to the actionListener:

	new Thread(new Runnable() {
		public void run() {
			try {
				frame window = new frame();
				window.frmSmartheater.setVisible(false);
				window.frmHeizwerte.setVisible(true);
				window.frmAnzeige.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}).start();
	*/

	public static void update(){
        SmartHeating.räume = utils.getAvailableRooms();
		list_Raum.setModel(new AbstractListModel() {
			String[] values =  SmartHeating.räume;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});	
		list_Room.setModel(new AbstractListModel() {
			String[] values =  SmartHeating.räume;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});		
    }

	/**
	 * Create the application.
	 */
	public frame() {
        try {
            SmartHeating.init();
			
            initialize();
			update();
            
        } catch (Exception x) {
            // TODO: handle exception
        }
		
	}


	/**
	 * Initialize the contents of the frame.
	 */
	void initialize() {
		// Fenster für die Startseite
		frmSmartheater = new JFrame();
		frames.add(frmSmartheater);
		frmSmartheater.getContentPane().setBackground(new Color(237, 237, 233));
		frmSmartheater.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frmSmartheater.setTitle("SmartHeater");
		frmSmartheater.setResizable(false);
		frmSmartheater.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmSmartheater.setBounds(100, 100, 1024, 640);
		frmSmartheater.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSmartheater.getContentPane().setLayout(null);		
										
		// Überschrift
		JLabel lblHeadline = new JLabel("SMART HEATER");
		labels.add(lblHeadline);
		lblHeadline.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadline.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblHeadline.setBounds(267, 105, 459, 89);
		frmSmartheater.getContentPane().add(lblHeadline);

		//Label
		JPanel panel_Home = new JPanel();
		panelLight.add(panel_Home);
		panel_Home.setBackground(new Color(248, 248, 245));
		panel_Home.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_Home.setBounds(267, 232, 459, 210);
		frmSmartheater.getContentPane().add(panel_Home);
		panel_Home.setLayout(null);

		// Button Heizkörperwerte hinzufügen
		JButton btnHeizwerte = new JButton("Heizkörperwerte");
		buttons.add(btnHeizwerte);
		btnHeizwerte.setBounds(124, 55, 215, 39);
		panel_Home.add(btnHeizwerte);
		btnHeizwerte.setFocusPainted(false);
		btnHeizwerte.setForeground(new Color(47, 62, 70));
		btnHeizwerte.setBackground(new Color(255, 255, 255));
		btnHeizwerte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnzeige.setVisible(false);
				frmHeizwerte.setVisible(true);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
				
			}
		});
		btnHeizwerte.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		
		// Button Heizkörperwerte Ansicht
		JButton btnAnzeige = new JButton("Ansicht");
		buttons.add(btnAnzeige);
		btnAnzeige.setBounds(124, 120, 215, 39);		
		btnAnzeige.setFocusPainted(false);		
		btnAnzeige.setForeground(new Color(82, 121, 111));		
		btnAnzeige.setBackground(Color.WHITE);
		btnAnzeige.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(true);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnAnzeige.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		panel_Home.add(btnAnzeige);
		
		//Button Push
		JButton btn_pushHome = new JButton("push data");
		buttons.add(btn_pushHome);
		btn_pushHome.setForeground(new Color(53, 79, 82));
		btn_pushHome.setFocusPainted(false);
		btn_pushHome.setBackground(new Color(224, 228, 222));
		btn_pushHome.setIcon(null);
		btn_pushHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utils.push();
			}
		});
		btn_pushHome.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btn_pushHome.setBounds(395, 542, 89, 23);
		frmSmartheater.getContentPane().add(btn_pushHome);
		
		//Button Pull
		JButton btn_pullHome = new JButton("pull data");
		buttons.add(btn_pullHome);
		btn_pullHome.setForeground(new Color(47, 62, 70));
		btn_pullHome.setBackground(new Color(202, 210, 197));
		btn_pullHome.setFocusPainted(false);
		btn_pullHome.setIcon(null);
		btn_pullHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utils.pull();
			}
		});
		btn_pullHome.setFont(new Font("Segoe UI Black", Font.PLAIN, 11));
		btn_pullHome.setBounds(521, 542, 89, 23);
		frmSmartheater.getContentPane().add(btn_pullHome);

		//Hinweis
		JLabel lblIconHinweis = new JLabel("");
		labels.add(lblIconHinweis);
		lblIconHinweis.setIcon(new ImageIcon("src/icons/icons8-benutzerhandbuch-50.png"));
		lblIconHinweis.setBounds(10, 11, 50, 50);
		lblIconHinweis.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Benutzerhinweis: \r\nDas eigenständige Achten auf die sinnvolle Installation und Benutzung \r\nder Heizkörper und Messgeräte wird vorausgesetzt.");
			}
		});
		frmSmartheater.getContentPane().add(lblIconHinweis);
		
		//Dark-/Lightmode
		JLabel lblMode = new JLabel("");
		lblMode.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(darkmode) lightMode();
				else darkmode();
			}
		});
		lblMode.setIcon(new ImageIcon("src/icons/icons8-nightmode-32.png"));
		lblMode.setBounds(946, 11, 56, 50);
		frmSmartheater.getContentPane().add(lblMode);


		//------------------------------------------------------------------------------------
		//---------------------------------Heizkörperwerte------------------------------------
		//------------------------------------------------------------------------------------
		
		// Fenster für das Hinzufügen der Heizkörperwerte
		frmHeizwerte = new JFrame();
		frames.add(frmHeizwerte);
		frmHeizwerte.getContentPane().setBackground(new Color(237, 237, 233));
		frmHeizwerte.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frmHeizwerte.setTitle("Übersicht Heizkörperwerte");
		frmHeizwerte.setResizable(false);
		frmHeizwerte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmHeizwerte.setBounds(100, 100, 1024, 640);
		frmHeizwerte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmHeizwerte.getContentPane().setLayout(null);
			
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		scrollPane.setBounds(511, 164, 452, 339);
		frmHeizwerte.getContentPane().add(scrollPane);

		table = new JTable();	
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				int index = 0;
				roomToAdd = table.getValueAt(i, 0).toString();
				txtBox_Heizwert.setText(model.getValueAt(i, 1).toString());
				txtBox_Tag.setText(model.getValueAt(i, 2).toString());
				txtBox_Monat.setText(model.getValueAt(i, 3).toString());
				txtBox_Jahr.setText(model.getValueAt(i, 4).toString());
				txtBox_Stunde.setText(model.getValueAt(i, 5).toString());
			}
		});
		table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		model = new DefaultTableModel();
			
		Object[] column = {"Raum", "Zählerstand", "Tag", "Monat", "Jahr", "Stunde"};
		final Object[] row = new Object[6];
		model.setColumnIdentifiers(column);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		JLabel lblbersicht = new JLabel("Übersicht");
		labels.add(lblbersicht);
		lblbersicht.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblbersicht.setBounds(511, 122, 96, 22);
		frmHeizwerte.getContentPane().add(lblbersicht);
		
		JLabel lblHeizkoerperwertHinzufuegen = new JLabel("Heizkörperwert hinzufügen");
		labels.add(lblHeizkoerperwertHinzufuegen);
		lblHeizkoerperwertHinzufuegen.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblHeizkoerperwertHinzufuegen.setBounds(48, 122, 265, 22);
		frmHeizwerte.getContentPane().add(lblHeizkoerperwertHinzufuegen);
		
		JButton btnAnsicht = new JButton("Ansicht");
		buttons.add(btnAnsicht);
		btnAnsicht.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnAnsicht.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnAnsicht.setForeground(new Color(0, 0, 0));
		btnAnsicht.setBackground(new Color(255, 255, 255));
		btnAnsicht.setFocusPainted(false);
		btnAnsicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(true);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnAnsicht.setBounds(48, 31, 89, 30);
		frmHeizwerte.getContentPane().add(btnAnsicht);
		
		JButton btnHome = new JButton("Hauptmenü");
		buttons.add(btnHome);
		btnHome.setIcon(new ImageIcon("src/icons/icons8-startseite-24.png"));
		btnHome.setFocusPainted(false);
		btnHome.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnHome.setForeground(new Color(0, 0, 0));
		btnHome.setBackground(new Color(255, 255, 255));
		btnHome.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(false);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnHome.setBounds(828, 31, 135, 30);
		frmHeizwerte.getContentPane().add(btnHome);
		
		JPanel panel = new JPanel();
		panelLight.add(panel);

		panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBackground(new Color(245, 245, 243));
		panel.setBounds(48, 162, 366, 341);
		frmHeizwerte.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblRaumauswahl = new JLabel("Raumauswahl");
		labels.add(lblRaumauswahl);
		lblRaumauswahl.setBounds(10, 11, 88, 22);
		panel.add(lblRaumauswahl);
		lblRaumauswahl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		JLabel lblZeitpunkt = new JLabel("Zeitpunkt");
		labels.add(lblZeitpunkt);
		lblZeitpunkt.setBounds(10, 156, 113, 22);
		panel.add(lblZeitpunkt);
		lblZeitpunkt.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		JLabel lblDatum = new JLabel("Datum");
		labels.add(lblDatum);
		lblDatum.setBounds(10, 180, 113, 22);
		panel.add(lblDatum);
		lblDatum.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Tag = new JTextField();
		textFields.add(txtBox_Tag);
		txtBox_Tag.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_Tag.setBounds(10, 200, 40, 20);
		panel.add(txtBox_Tag);
		txtBox_Tag.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Tag.setColumns(10);
		txtBox_Tag.setText(Integer.toString(day));
		
		JLabel lblPunkt = new JLabel(".");
		labels.add(lblPunkt);
		lblPunkt.setBounds(47, 200, 13, 22);
		panel.add(lblPunkt);
		lblPunkt.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Monat = new JTextField();
		textFields.add(txtBox_Monat);
		txtBox_Monat.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_Monat.setBounds(58, 200, 40, 20);
		panel.add(txtBox_Monat);
		txtBox_Monat.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Monat.setColumns(10);
		txtBox_Monat.setText(Integer.toString(month));
		
		JLabel lblPunkt_1 = new JLabel(".");
		labels.add(lblPunkt_1);
		lblPunkt_1.setBounds(96, 200, 13, 22);
		panel.add(lblPunkt_1);
		lblPunkt_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Jahr = new JTextField();
		textFields.add(txtBox_Jahr);
		txtBox_Jahr.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_Jahr.setBounds(110, 200, 63, 20);
		panel.add(txtBox_Jahr);
		txtBox_Jahr.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Jahr.setColumns(10);
		txtBox_Jahr.setText(Integer.toString(year));
		
		JLabel lblUhrzeit = new JLabel("Uhrzeit");
		labels.add(lblUhrzeit);
		lblUhrzeit.setBounds(10, 230, 113, 22);
		panel.add(lblUhrzeit);
		lblUhrzeit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Stunde = new JTextField();
		textFields.add(txtBox_Stunde);
		txtBox_Stunde.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_Stunde.setBounds(10, 254, 40, 20);
		panel.add(txtBox_Stunde);
		txtBox_Stunde.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Stunde.setColumns(10);
		txtBox_Stunde.setText(Integer.toString(hour));
	
		JLabel lblHeizkörperwert = new JLabel("Zählerstand");
		labels.add(lblHeizkörperwert);
		lblHeizkörperwert.setBounds(10, 285, 113, 22);
		panel.add(lblHeizkörperwert);
		lblHeizkörperwert.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
				
		JLabel lblHeizkörperwertEinheit = new JLabel("kWh");
		labels.add(lblHeizkörperwertEinheit);
		lblHeizkörperwertEinheit.setBounds(110, 308, 31, 22);
		panel.add(lblHeizkörperwertEinheit);
		lblHeizkörperwertEinheit.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		txtBox_Heizwert = new JTextField();
		textFields.add(txtBox_Heizwert);
		txtBox_Heizwert.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_Heizwert.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Heizwert.setColumns(10);
		txtBox_Heizwert.setBounds(10, 310, 88, 20);
		panel.add(txtBox_Heizwert);
		txtBox_Heizwert.setText("0");

		JLabel lblRoomAdd = new JLabel("Raum hinzufügen");
		labels.add(lblRoomAdd);
		lblRoomAdd.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblRoomAdd.setBounds(10, 126, 99, 22);
		panel.add(lblRoomAdd);
		
		txtBox_AddRoom = new JTextField();
		textFields.add(txtBox_AddRoom);
		txtBox_AddRoom.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_AddRoom.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_AddRoom.setColumns(10);
		txtBox_AddRoom.setBounds(110, 127, 192, 20);
		panel.add(txtBox_AddRoom);
		
		JButton btnAddRoom = new JButton("+");
		buttons.add(btnAddRoom);
		btnAddRoom.setFocusPainted(false);
		btnAddRoom.setBackground(new Color(255, 255, 255));
		btnAddRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				utils.addRoom(txtBox_AddRoom.getText());

				update();
			}
		});
		btnAddRoom.setBounds(312, 125, 44, 23);
		panel.add(btnAddRoom);

		JScrollPane scrollPane_Raum = new JScrollPane();
		scrollPane_Raum.setBounds(10, 36, 323, 74);
		panel.add(scrollPane_Raum);

		list_Raum = new JList();
		lists.add(list_Raum);
		list_Raum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		list_Raum.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int tempHour = 1;
				model.setRowCount(0);
				try {
					for(int i: utils.getDayData(year, month, day, list_Raum.getSelectedValue().toString())){
						if(i!=0){
							row[0] = roomToAdd;
							row[1] = i;
							row[2] = Integer.toString(day);
							row[3] = Integer.toString(month);
							row[4] = Integer.toString(year);
							row[5] = Integer.toString(tempHour);
							model.addRow(row);
							tempHour++;
						}

	
					}
				} catch (Exception o) {
					// TODO: handle exception
				}
	
			}
		});
		list_Raum.setModel(new AbstractListModel() {
			String[] values =  SmartHeating.räume;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});

		scrollPane_Raum.setViewportView(list_Raum);
		list_Raum.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_Raum.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (list_Raum.getSelectedIndex() != -1) {
					roomToAdd = list_Raum.getSelectedValue().toString();
					
				}
			}
		});
				
		JButton btnDelete = new JButton("Löschen");
		buttons.add(btnDelete);
		btnDelete.setFocusPainted(false);
		btnDelete.setBackground(new Color(202, 210, 197));
		btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i>=0) {
					
					model.removeRow(i);
					JOptionPane.showMessageDialog(null, "Wert wurde erfolgreich gelöscht.");
				}
				else {
					JOptionPane.showMessageDialog(null, "Bitte wählen Sie erst eine Zeile aus.");
				}
			}
		});
		btnDelete.setBounds(850, 509, 113, 23);
		frmHeizwerte.getContentPane().add(btnDelete);
				
		JButton btnExport = new JButton("Exportieren");
		buttons.add(btnExport);
		btnExport.setFocusPainted(false);
		btnExport.setBackground(new Color(255, 255, 255));
		btnExport.setForeground(new Color(47, 62, 70));
		btnExport.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 11));
		btnExport.setBounds(828, 122, 135, 23);
		frmHeizwerte.getContentPane().add(btnExport);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] headers = new String[] {"Heizkörper", "Zählerstand", "Tag", "Monat", "Jahr", "Stunde"};
				List<Measurement> contacts = new ArrayList<>();
				for(int month: utils.getAvailableMonths(year, roomToAdd)){
					for(int day: utils.getAvailableDays(year, month, roomToAdd)){
						int index = 1;
						for(int hour: utils.getDayData(year, month, day, roomToAdd)){
							contacts.add(new Measurement(roomToAdd, Integer.toString(hour) , Integer.toString(day), Integer.toString(month), Integer.toString(year), Integer.toString(index)));
							index++;
						}
					}
				}
				
				
				
				String fileName = day + "_" + month + "_" + year + "_" + hour + "_" + roomToAdd + ".xlsx";
				ExcelFileExporter excelFileExporter = new ExcelFileExporter();
				excelFileExporter.exportExcelFile(contacts, headers, fileName);
			}
		});
		
		JButton btnAdd = new JButton("Hinzufügen");
		buttons.add(btnAdd);
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnAdd.setFocusPainted(false);
		btnAdd.setForeground(new Color(255, 255, 255));
		btnAdd.setBackground(new Color(47, 62, 70));
		btnAdd.setBounds(285, 509, 129, 23);
		frmHeizwerte.getContentPane().add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				year = Integer.parseInt(txtBox_Jahr.getText());
				month = Integer.parseInt(txtBox_Monat.getText());
				day = Integer.parseInt(txtBox_Tag.getText());
				hour = Integer.parseInt(txtBox_Stunde.getText());
				utils.addYear(year, roomToAdd);
				utils.AddMonth(year, month, roomToAdd);
				utils.addDay(year, month, day, roomToAdd);

				int measurement = Integer.parseInt(txtBox_Heizwert.getText());
				if(roomToAdd == "") {
					roomToAdd = SmartHeating.räume[0];
				}
		
				utils.addMeasurementToDay(year, month, day, measurement,hour, roomToAdd);
				if(txtBox_Heizwert.getText().equals("")||txtBox_Tag.getText().equals("")||txtBox_Monat.getText().equals("")||txtBox_Jahr.getText().equals("")||txtBox_Stunde.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.");
				} 
				else {
					int tempHour = 1;
					model.setRowCount(0);
					try {
						for(int i: utils.getDayData(year, month, day, list_Raum.getSelectedValue().toString())){
							if(i!=0){
								row[0] = roomToAdd;
								row[1] = i;
								row[2] = Integer.toString(day);
								row[3] = Integer.toString(month);
								row[4] = Integer.toString(year);
								row[5] = Integer.toString(tempHour);
								model.addRow(row);
								tempHour++;
							}
	
		
						}
					} catch (Exception o) {
						// TODO: handle exception
					}
					
					JOptionPane.showMessageDialog(null, "Wert wurde erfolgreich hinzugefügt.");
				}				
			}
		}); 

		JButton btnClear = new JButton("Leeren");
		buttons.add(btnClear);
		btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnClear.setBackground(new Color(202, 210, 197));
		btnClear.setForeground(new Color(0, 0, 0));
		btnClear.setFocusPainted(false);
		btnClear.setBounds(48, 509, 89, 23);
		frmHeizwerte.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list_Room.setSelectedIndex(-1);
				txtBox_Heizwert.setText("");
				txtBox_Tag.setText("");
				txtBox_Monat.setText("");
				txtBox_Jahr.setText("");
				txtBox_Stunde.setText("");
			}
		});


		JButton btn_pushUebersicht = new JButton("push data");
		buttons.add(btn_pushUebersicht);
		btn_pushUebersicht.setBackground(new Color(255, 255, 255));
		btn_pushUebersicht.setFocusPainted(false);
		btn_pushUebersicht.setForeground(new Color(82, 121, 111));
		btn_pushUebersicht.setIcon(null);
		btn_pushUebersicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utils.push();
			}
		});
		btn_pushUebersicht.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btn_pushUebersicht.setBounds(511, 509, 89, 23);
		frmHeizwerte.getContentPane().add(btn_pushUebersicht);
		
		JButton btn_pullUebersicht = new JButton("pull data");
		buttons.add(btn_pullUebersicht);
		btn_pullUebersicht.setBackground(new Color(255, 255, 255));
		btn_pullUebersicht.setFocusPainted(false);
		btn_pullUebersicht.setForeground(new Color(47, 62, 70));
		btn_pullUebersicht.setIcon(null);
		btn_pullUebersicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utils.pull();
			}
		});
		btn_pullUebersicht.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btn_pullUebersicht.setBounds(606, 509, 89, 23);
		frmHeizwerte.getContentPane().add(btn_pullUebersicht);
		
		JPanel panel_Temperatur = new JPanel();
		panel_Temperatur.setBackground(new Color(89, 98, 94));
		panel_Temperatur.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, new Color(0, 43, 12)));
		panel_Temperatur.setBounds(164, 31, 250, 51);
		frmHeizwerte.getContentPane().add(panel_Temperatur);
		panel_Temperatur.setLayout(null);
		
		JLabel lblTempHead = new JLabel("Aktuelle Temperatur");
		lblTempHead.setForeground(new Color(255, 255, 255));
		lblTempHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblTempHead.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblTempHead.setBounds(10, 18, 154, 14);
		panel_Temperatur.add(lblTempHead);
		
		lbl_TempAnzeige = new JLabel("");
		lbl_TempAnzeige.setText(SmartHeating.temperature + " °C");
		lbl_TempAnzeige.setForeground(new Color(255, 255, 255));
		lbl_TempAnzeige.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_TempAnzeige.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lbl_TempAnzeige.setBounds(165, 11, 67, 25);
		panel_Temperatur.add(lbl_TempAnzeige);
		

		//------------------------------------------------------------------------------------
		//----------------------------------------Ansicht-------------------------------------
		//------------------------------------------------------------------------------------

		// Fenster für die Auswahl der Filter für die Anzeige
		frmAnzeige = new JFrame();
		frames.add(frmAnzeige);
		frmAnzeige.getContentPane().setBackground(new Color(237, 237, 233));
		frmAnzeige.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frmAnzeige.setTitle("Anzeigeeinstellungen");
		frmAnzeige.setResizable(false);
		frmAnzeige.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmAnzeige.setBounds(100, 100, 1024, 640);
		frmAnzeige.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAnzeige.getContentPane().setLayout(null);
		

		JButton btnHome2 = new JButton("Hauptmenü");
		buttons.add(btnHome2);
		btnHome2.setIcon(new ImageIcon("src/src/icons/icons8-startseite-24.png"));
		btnHome2.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnHome2.setBackground(new Color(255, 255, 255));
		btnHome2.setFocusPainted(false);
		btnHome2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnHome2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnzeige.setVisible(false);
			}
		});
		btnHome2.setBounds(828, 31, 135, 30);
		frmAnzeige.getContentPane().add(btnHome2);
		

		JButton btnUebersicht = new JButton("Übersicht");
		buttons.add(btnUebersicht);
		btnUebersicht.setBackground(new Color(255, 255, 255));
		btnUebersicht.setFocusPainted(false);
		btnUebersicht.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnUebersicht.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnUebersicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnzeige.setVisible(false);
				frmHeizwerte.setVisible(true);
			}
		});
		btnUebersicht.setBounds(48, 31, 89, 30);
		frmAnzeige.getContentPane().add(btnUebersicht);
		

		JPanel panel2 = new JPanel();
		panelLight.add(panel2);
		panel2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel2.setBounds(48, 155, 366, 208);
		panel2.setBackground(new Color(245, 245, 243));
		frmAnzeige.getContentPane().add(panel2);
		panel2.setLayout(null);
		

		JLabel lblRooms = new JLabel("Räume");
		labels.add(lblRooms);
		lblRooms.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblRooms.setBounds(10, 11, 61, 26);
		panel2.add(lblRooms);
		

		JLabel lblTime = new JLabel("Zeitraum");
		labels.add(lblTime);
		lblTime.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblTime.setBounds(10, 120, 61, 26);
		panel2.add(lblTime);
		

        JLabel lblBis = new JLabel("bis");
		labels.add(lblBis);
		lblBis.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblBis.setBounds(10, 144, 20, 26);
		panel2.add(lblBis);
		

		txtBox_StartTag = new JTextField();
		textFields.add(txtBox_StartTag);
		txtBox_StartTag.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_StartTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtBox_StartTag.setBounds(40, 148, 37, 20);
        txtBox_StartTag.setText(Integer.toString(day));
		panel2.add(txtBox_StartTag);
		txtBox_StartTag.setColumns(10);
        

		txtBox_StartMonat = new JTextField();
		textFields.add(txtBox_StartMonat);
		txtBox_StartMonat.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_StartMonat.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtBox_StartMonat.setColumns(10);
		txtBox_StartMonat.setBounds(87, 147, 37, 20);
        txtBox_StartMonat.setText(Integer.toString(month));
		panel2.add(txtBox_StartMonat);
		

		txtBox_StartJahr = new JTextField();
		textFields.add(txtBox_StartJahr);
		txtBox_StartJahr.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_StartJahr.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtBox_StartJahr.setColumns(10);
		txtBox_StartJahr.setBounds(134, 147, 52, 20);
        txtBox_StartJahr.setText(Integer.toString(year));
         txtBox_StartJahr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                year = Integer.parseInt(txtBox_StartJahr.getText());
            }
        });
		panel2.add(txtBox_StartJahr);
		

		JLabel lblVon_Punkt = new JLabel(".");
		labels.add(lblVon_Punkt);
		lblVon_Punkt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblVon_Punkt.setBounds(81, 144, 20, 26);
		panel2.add(lblVon_Punkt);
		

		JLabel lblVon_Punkt_1 = new JLabel(".");
		labels.add(lblVon_Punkt_1);
		lblVon_Punkt_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblVon_Punkt_1.setBounds(127, 144, 20, 26);
		panel2.add(lblVon_Punkt_1);
		

		JButton btnShow = new JButton("Anzeigen");
		buttons.add(btnShow);
		btnShow.setForeground(new Color(47, 62, 70));
		btnShow.setFocusPainted(false);
		btnShow.setBackground(new Color(224, 228, 222));
		btnShow.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                try {
					day = Integer.parseInt(txtBox_StartTag.getText());
                    month = Integer.parseInt(txtBox_StartMonat.getText());
                    year = Integer.parseInt(txtBox_StartJahr.getText());
                    SmartHeating[] allMeasuSmartHeatings = new SmartHeating[allRooms.size()];
					allAverages = new SmartHeating[allRooms.size()];
					SmartHeating[] allMax = new SmartHeating[allRooms.size()];
					TraceColour[] allColors = {TraceColour.RED, TraceColour.BLUE, TraceColour.GREEN, TraceColour.YELLOW, TraceColour.ORANGE, TraceColour.PURPLE, TraceColour.CYAN, TraceColour.BLACK};
                    //double kosten = SmartHeating.berechnungVerbrauch(year, month, day, roomToAdd) * Integer.parseInt(textField_2.getText());

					switch (abstand) {
                        case 0:
                            try {
                                int index = 0;
								int colorIndex = 0;
                                for(String i: allRooms){
									SmartHeating object = new SmartHeating();
									if(wasAnzeige == 0 || wasAnzeige == 1){																
                                    	
										object = SmartHeating.getDayMeasurememt(year, month, day, i, false, TraceColour.PURPLE)[0];
									} else if (wasAnzeige == 2){
										object = SmartHeating.getDayMeasurememt(year, month, day, i, true, TraceColour.PURPLE)[1];
									}
									
									object = erstellungGraphen(allColors, colorIndex, index, object, i);
									allMeasuSmartHeatings[index] = object;
									index++;
									colorIndex++;
									if(colorIndex == allColors.length ){
										colorIndex = 0;
									}

									object.setName(i);

                                }

								allMax = allMeasuSmartHeatings;
								if(showAvg){
									
									allMax = new SmartHeating [allRooms.size() *2];
									int c = 0;
									for(SmartHeating s : allMeasuSmartHeatings){
										allMax[c] = s;
										c++;
									}
									for(SmartHeating s : allAverages){
										allMax[c] = s;
										c++;
									}
								}
                                SmartHeating.graphConfig.x = "Stunden";
                                SmartHeating.graphConfig.xArray = new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMax);
                            } catch (Exception a) {
								System.out.println("Fehler bei der Auswertung");
                                System.out.println(a);
                            }
							
                            break;
                        case 1:
                            try {
                                
                                int index = 0;
								int colorIndex = 0;
                                for(String i: allRooms){
									SmartHeating object = new SmartHeating();
									if(wasAnzeige == 0 || wasAnzeige == 1){
                                    	object = SmartHeating.getWeekData(year, month, day, i)[0];
									} else if (wasAnzeige == 2){
										object = SmartHeating.getWeekData(year, month, day, i)[1];
									}
									object = erstellungGraphen(allColors, colorIndex, index, object, i);
									allMeasuSmartHeatings[index] = object;
                                    index++;
									colorIndex++;
									if(colorIndex == allColors.length ){
										colorIndex = 0;
									}

                                }

								allMax = allMeasuSmartHeatings;
								if(showAvg){
									allMax = new SmartHeating [allRooms.size() *2];
									int c = 0;
									for(SmartHeating s : allMeasuSmartHeatings){
										allMax[c] = s;
										c++;
									}
									for(SmartHeating s : allAverages){
										allMax[c] = s;
										c++;
									}
								}
                                SmartHeating.graphConfig.x = "Tag";
								SmartHeating.graphConfig.xArray = new String[] {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
								
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMax);
                            } catch (Exception a) {
                                System.err.println(a);
                            }
                            break;
                        case 2:
                            try {
                                
                                int index = 0;
								int colorIndex = 0;
                                for(String i: allRooms){
									SmartHeating object = new SmartHeating();
									if(wasAnzeige == 0 || wasAnzeige == 1){
										object = SmartHeating.getMonthMeasurement(year, month,  i, false, TraceColour.ORANGE)[0];
									} else if (wasAnzeige == 2){
										object = SmartHeating.getMonthMeasurement(year, month,  i, false, TraceColour.ORANGE)[1];
									}
									
                                    
									object = erstellungGraphen(allColors, colorIndex, index, object, i);
									allMeasuSmartHeatings[index] = object;								
                                    index++;
									colorIndex++;
									if(colorIndex == allColors.length ){
										colorIndex = 0;
									}

                                }

								allMax = allMeasuSmartHeatings;
								if(showAvg){
									allMax = new SmartHeating [allRooms.size() *2];
									int c = 0;
									for(SmartHeating s : allMeasuSmartHeatings){
										allMax[c] = s;
										c++;
									}
									for(SmartHeating s : allAverages){
										allMax[c] = s;
										c++;
									}
								}
                                SmartHeating.graphConfig.x = "Tag";
								SmartHeating.graphConfig.xArray = new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMax);
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            
                            break;

                        case 3:
                            try {
                                int index = 0;
								int colorIndex = 0;
                                for(String i: allRooms){
									SmartHeating object = new SmartHeating();
									if(wasAnzeige == 0 || wasAnzeige == 1){
										object = SmartHeating.getYearData(year,  i)[0];;
									} else if (wasAnzeige == 2){
										object = SmartHeating.getYearData(year,  i)[1];;
									}
									object = erstellungGraphen(allColors, colorIndex, index, object, i);
									allMeasuSmartHeatings[index] = object;									
                                    index++;
									colorIndex++;
									if(colorIndex == allColors.length ){
										colorIndex = 0;
									}

                                }

								allMax = allMeasuSmartHeatings;
								if(showAvg){
									allMax = new SmartHeating [allRooms.size() *2];
									int c = 0;
									for(SmartHeating s : allMeasuSmartHeatings){
										allMax[c] = s;
										c++;
									}
									for(SmartHeating s : allAverages){
										allMax[c] = s;
										c++;
									}
								}
                                SmartHeating.graphConfig.x = "Monat";
								SmartHeating.graphConfig.xArray = new String[]{ "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMax);
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            
                            break;
                        default:
                            break;
                    }
                } catch (Exception b) {
                    System.err.println(b);
                    JOptionPane.showMessageDialog(null, "Ungültige Eingabe.");
                }
                
			}
		});
		btnShow.setBounds(711, 461, 143, 52);
		frmAnzeige.getContentPane().add(btnShow);
		
		JLabel lblFilter = new JLabel("Filter");
		labels.add(lblFilter);	
		lblFilter.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblFilter.setBounds(108, 118, 61, 26);
		frmAnzeige.getContentPane().add(lblFilter);
		
        JScrollPane scrollPane_Room = new JScrollPane();
		scrollPane_Room.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_Room.setBounds(10, 36, 323, 74);
		panel2.add(scrollPane_Room);

		list_Room = new JList();
		lists.add(list_Room);
		
		list_Room.setModel(new AbstractListModel() {
			String[] values = SmartHeating.räume;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});        
        //set selection Model
        list_Room.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollPane_Room.setViewportView(list_Room);

        list_Room.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    

 
                }
				allRooms = list_Room.getSelectedValuesList();
            }
        });
		JPanel panel_1 = new JPanel();
		panelLight.add(panel_1);

		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setLayout(null);
		panel_1.setBounds(597, 155, 366, 208);
		panel_1.setBackground(new Color(245, 245, 243));
		frmAnzeige.getContentPane().add(panel_1);
		

		JLabel lblAbstnde = new JLabel("Abstände");
		labels.add(lblAbstnde);
		lblAbstnde.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblAbstnde.setBounds(10, 11, 61, 26);
		panel_1.add(lblAbstnde);
		

		JLabel lblWasSollAngezeigt = new JLabel("Was soll angezeigt werden?");
		labels.add(lblWasSollAngezeigt);
		lblWasSollAngezeigt.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblWasSollAngezeigt.setBounds(10, 83, 163, 26);
		panel_1.add(lblWasSollAngezeigt);
		

		JComboBox comboBox_Was = new JComboBox();
		comboBoxes.add(comboBox_Was);
		comboBox_Was.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		comboBox_Was.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		comboBox_Was.setModel(new DefaultComboBoxModel(new String[] {"Verlauf Verbrauch", "Verlauf Kosten", "Verlauf Zählerstände"}));
		comboBox_Was.setBounds(10, 113, 224, 30);
        comboBox_Was.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				wasAnzeige = comboBox_Was.getSelectedIndex();
            }
        });
		panel_1.add(comboBox_Was);

		//checkbox für Durchschnitt
		chckbxDurchschnitt = new JCheckBox("Anzeige \r⌀");
		checkBoxes.add(chckbxDurchschnitt);
		chckbxDurchschnitt.setBackground(new Color(245, 245, 243));
		chckbxDurchschnitt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAvg = chckbxDurchschnitt.isSelected();
				System.out.println( "Durchschnitt: " + chckbxDurchschnitt.isSelected());
			}
		});
		chckbxDurchschnitt.setBounds(240, 112, 96, 26);
		panel_1.add(chckbxDurchschnitt);
        
		//combobox für die Anzeige der Auswahl der möglichen Abstände
		JComboBox comboBox_Abstand = new JComboBox();
		comboBoxes.add(comboBox_Abstand);
		comboBox_Abstand.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		comboBox_Abstand.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		comboBox_Abstand.setModel(new DefaultComboBoxModel(new String[] {"Tag", "Woche", "Monat", "Jahr"}));
		comboBox_Abstand.setBounds(10, 39, 224, 30);
        comboBox_Abstand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abstand = comboBox_Abstand.getSelectedIndex();
            }
        });
		panel_1.add(comboBox_Abstand);

		
		JLabel lblGaspreis = new JLabel("Gaspreis");
		labels.add(lblGaspreis);
		lblGaspreis.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblGaspreis.setBounds(10, 146, 163, 26);
		panel_1.add(lblGaspreis);
		
		//Textfeld für Gaspreis
		textField_2 = new JTextField();
		textFields.add(textField_2);
		textField_2.setText("0.00");
		textField_2.setBounds(10, 177, 96, 20);
		textField_2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textField_2.setFont(new Font("Segoe UI", Font.BOLD, 11));
		textField_2.setText("18");
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lbl_PreisEinheit = new JLabel("ct/kWh");
		labels.add(lbl_PreisEinheit);
		lbl_PreisEinheit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lbl_PreisEinheit.setBounds(116, 177, 163, 26);
		panel_1.add(lbl_PreisEinheit);

		JLabel lblAnzeigeeinstellungen = new JLabel("Anzeigeeinstellungen");
		labels.add(lblAnzeigeeinstellungen);
		lblAnzeigeeinstellungen.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblAnzeigeeinstellungen.setBounds(657, 119, 231, 26);
		frmAnzeige.getContentPane().add(lblAnzeigeeinstellungen);


		JPanel panel_Alarm = new JPanel();
		panelLight.add(panel_Alarm);
		panel_Alarm.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_Alarm.setLayout(null);
		panel_Alarm.setBackground(new Color(245, 245, 243));
		panel_Alarm.setBounds(48, 446, 366, 67);
		frmAnzeige.getContentPane().add(panel_Alarm);
		
		JLabel lbl_erlaubteAbweichung = new JLabel("Erlaubte Abweichung");
		labels.add(lbl_erlaubteAbweichung);
		lbl_erlaubteAbweichung.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lbl_erlaubteAbweichung.setBounds(10, 11, 152, 26);
		panel_Alarm.add(lbl_erlaubteAbweichung);
		
		JLabel lbl_prozentAbweichungen = new JLabel("Prozent Abweichungen");
		labels.add(lbl_prozentAbweichungen);
		lbl_prozentAbweichungen.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lbl_prozentAbweichungen.setBounds(172, 11, 152, 26);
		panel_Alarm.add(lbl_prozentAbweichungen);
		
		txtBox_erlaubteAbweichung = new JTextField("30");
		textFields.add(txtBox_erlaubteAbweichung);
		txtBox_erlaubteAbweichung.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_erlaubteAbweichung.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtBox_erlaubteAbweichung.setColumns(10);
		txtBox_erlaubteAbweichung.setBounds(10, 35, 62, 20);
		panel_Alarm.add(txtBox_erlaubteAbweichung);
		
		JLabel lbl_Prozent = new JLabel("%");
		labels.add(lbl_Prozent);
		lbl_Prozent.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lbl_Prozent.setBounds(82, 32, 20, 26);
		panel_Alarm.add(lbl_Prozent);
		
		JLabel lbl_Prozent2 = new JLabel("%");
		labels.add(lbl_Prozent2);
		lbl_Prozent2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lbl_Prozent2.setBounds(244, 32, 20, 26);
		panel_Alarm.add(lbl_Prozent2);
		
		txtBox_prozentAbweichungen = new JTextField("30");
		textFields.add(txtBox_prozentAbweichungen);
		txtBox_prozentAbweichungen.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBox_prozentAbweichungen.setFont(new Font("Segoe UI", Font.BOLD, 11));
		txtBox_prozentAbweichungen.setColumns(10);
		txtBox_prozentAbweichungen.setBounds(172, 35, 62, 20);
		panel_Alarm.add(txtBox_prozentAbweichungen);
		
		JLabel lbl_Alarm = new JLabel("Alarme");
		labels.add(lbl_Alarm);
		lbl_Alarm.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lbl_Alarm.setBounds(108, 409, 98, 26);
		frmAnzeige.getContentPane().add(lbl_Alarm);
		
		JLabel lblIconSettings = new JLabel("");
		labels.add(lblIconSettings);
		lblIconSettings.setIcon(new ImageIcon("src/icons/icons8-zahnrad-50.png"));
		lblIconSettings.setBounds(597, 105, 50, 50);
		frmAnzeige.getContentPane().add(lblIconSettings);
		
		JLabel lblIconAlarm = new JLabel("");
		labels.add(lblIconAlarm);
		lblIconAlarm.setIcon(new ImageIcon("src/icons/icons8-alarm-50.png"));
		lblIconAlarm.setBounds(48, 396, 50, 50);
		frmAnzeige.getContentPane().add(lblIconAlarm);
		
		JLabel lblIconAlarm_1 = new JLabel("");
		labels.add(lblIconAlarm_1);
		lblIconAlarm_1.setIcon(new ImageIcon("src/icons/icons8-filtern-und-sortieren-48.png"));
		lblIconAlarm_1.setBounds(48, 105, 50, 50);
		frmAnzeige.getContentPane().add(lblIconAlarm_1);
		
	}		
}