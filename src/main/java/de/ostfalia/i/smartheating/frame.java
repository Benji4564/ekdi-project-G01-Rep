package de.ostfalia.i.smartheating;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Color;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;

import org.charts.dataviewer.utils.TraceColour;

import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;

import javax.swing.AbstractListModel;

class Average{
	String name = "";
	double percent = 0;
}
public class frame {
	

	private JFrame frmSmartheater;
	private JFrame frmAnzeige;
	private JFrame frmHeizwerte;
	private JTable table;
	private JTextField txtBox_Tag;
	private JTextField txtBox_Monat;
	private JTextField txtBox_Jahr;
	private JTextField txtBox_Stunde;
	private JTextField txtBox_Heizwert;
	DefaultTableModel model;
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
	public static double erlaubteAbweichung = 0.5; //hier
	public static double prozentAbweichungen = 5; //hier
	public static boolean showAvg = false;
	public static int wasAnzeige = 0;
	public static SmartHeating[] allAverages = null;	
	public static JTextField txtBox_erlaubteAbweichung;
	public static JTextField txtBox_prozentAbweichungen;


	public static SmartHeating erstellungGraphen(TraceColour[] allColors, int colorIndex, int index, SmartHeating object, String i){
		SmartHeating.graphConfig.y = "Messwerte";
		if(wasAnzeige == 1){
			SmartHeating.graphConfig.y = "Kosten in cent";
			SmartHeating preisWerte = new SmartHeating();
			for(double u: object.getMeasurements()){				
				preisWerte.addMeasurement(u * Float.parseFloat(textField_2.getText()));
				System.out.println(u * Float.parseFloat(textField_2.getText()));
			}
			
			object = preisWerte;

		}

		
		erlaubteAbweichung = Double.parseDouble(txtBox_erlaubteAbweichung.getText());
		prozentAbweichungen = Double.parseDouble(txtBox_prozentAbweichungen.getText());

		Average[] averagesUp = getDeviationUp(erlaubteAbweichung, object);
		for(Average avg: averagesUp){
			if(avg.percent>=prozentAbweichungen){
				System.out.println("Abweichung in: "+ avg.name + ": " + avg.percent + "%");
				JOptionPane.showMessageDialog(null, "Abweichung in/im " + avg.name + " liegt um " + avg.percent + "% über dem Durchschnittswert.");
			}										
		}
		Average[] averagesDown = getDeviationDown(erlaubteAbweichung, object);
		for(Average avg: averagesDown){
			if(avg.percent>=prozentAbweichungen){
				System.out.println("Abweichung in: "+avg.name + ": " + avg.percent + "%");
				//JOptionPane.showMessageDialog(null, "Abweichung in/im "+ avg.name + " liegt um " + avg.percent + "% unter dem Durchschnittswert.");
			}										
		}
		TraceColour color = allColors[colorIndex];
		object.setTraceColour(color);

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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


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
		lblHeadline.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadline.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblHeadline.setBounds(267, 105, 459, 89);
		frmSmartheater.getContentPane().add(lblHeadline);
		
		// Button Heizkörperwerte hinzufügen
		JButton btnHeizwerte = new JButton("Heizkörperwerte");
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
		btnHeizwerte.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, Color.DARK_GRAY, null));
		btnHeizwerte.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		btnHeizwerte.setBounds(339, 290, 325, 39);
		frmSmartheater.getContentPane().add(btnHeizwerte);
		
		// Button Heizkörperwerte Ansicht
		JButton btnAnzeige = new JButton("Ansicht");
		btnAnzeige.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(true);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnAnzeige.setForeground(new Color(82, 121, 111));
		btnAnzeige.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		btnAnzeige.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, Color.DARK_GRAY, null));
		btnAnzeige.setBackground(Color.WHITE);
		btnAnzeige.setBounds(395, 367, 215, 39);
		frmSmartheater.getContentPane().add(btnAnzeige);
		
		
		//------------------------------------------------------------------------------------
		//---------------------------------Heizkörperwerte------------------------------------
		//------------------------------------------------------------------------------------
		
		// Fenster für das Hinzufügen der Heizkörperwerte
		frmHeizwerte = new JFrame();
		frmHeizwerte.getContentPane().setBackground(new Color(237, 237, 233));
		frmHeizwerte.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frmHeizwerte.setTitle("Heizkörperwerte hinzufügen");
		frmHeizwerte.setResizable(false);
		frmHeizwerte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmHeizwerte.setBounds(100, 100, 1024, 640);
		frmHeizwerte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmHeizwerte.getContentPane().setLayout(null);
		
		
		JScrollPane scrollPane = new JScrollPane();
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
		lblbersicht.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblbersicht.setBounds(511, 122, 96, 22);
		frmHeizwerte.getContentPane().add(lblbersicht);
		

		JLabel lblHeizkoerperwertHinzufuegen = new JLabel("Heizkörperwert hinzufügen");
		lblHeizkoerperwertHinzufuegen.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblHeizkoerperwertHinzufuegen.setBounds(48, 122, 265, 22);
		frmHeizwerte.getContentPane().add(lblHeizkoerperwertHinzufuegen);
		

		JButton btnAnsicht = new JButton("Ansicht");
		btnAnsicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(true);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnAnsicht.setBounds(48, 31, 89, 23);
		frmHeizwerte.getContentPane().add(btnAnsicht);
		

		JButton btnHome = new JButton("Hauptmenü");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(false);
				day = java.time.LocalDate.now().getDayOfMonth();
				month = java.time.LocalDate.now().getMonthValue();
				year = java.time.LocalDate.now().getYear();
			}
		});
		btnHome.setBounds(828, 31, 135, 23);
		frmHeizwerte.getContentPane().add(btnHome);
		

		Panel panel = new Panel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(48, 162, 366, 341);
		frmHeizwerte.getContentPane().add(panel);
		panel.setLayout(null);
		

		JLabel lblRaumauswahl = new JLabel("Raumauswahl");
		lblRaumauswahl.setBounds(10, 11, 88, 22);
		panel.add(lblRaumauswahl);
		lblRaumauswahl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		

		JLabel lblZeitpunkt = new JLabel("Zeitpunkt");
		lblZeitpunkt.setBounds(10, 156, 113, 22);
		panel.add(lblZeitpunkt);
		lblZeitpunkt.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		

		JLabel lblDatum = new JLabel("Datum");
		lblDatum.setBounds(10, 180, 113, 22);
		panel.add(lblDatum);
		lblDatum.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		

		txtBox_Tag = new JTextField();
		txtBox_Tag.setBounds(10, 202, 40, 20);
		panel.add(txtBox_Tag);
		txtBox_Tag.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Tag.setColumns(10);
		txtBox_Tag.setText(Integer.toString(day));
		

		JLabel lblPunkt = new JLabel(".");
		lblPunkt.setBounds(47, 200, 13, 22);
		panel.add(lblPunkt);
		lblPunkt.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		

		txtBox_Monat = new JTextField();
		txtBox_Monat.setBounds(58, 202, 40, 20);
		panel.add(txtBox_Monat);
		txtBox_Monat.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Monat.setColumns(10);
		txtBox_Monat.setText(Integer.toString(month));
		

		JLabel lblPunkt_1 = new JLabel(".");
		lblPunkt_1.setBounds(96, 200, 13, 22);
		panel.add(lblPunkt_1);
		lblPunkt_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		

		txtBox_Jahr = new JTextField();
		txtBox_Jahr.setBounds(110, 202, 63, 20);
		panel.add(txtBox_Jahr);
		txtBox_Jahr.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Jahr.setColumns(10);
		txtBox_Jahr.setText(Integer.toString(year));
		

		JLabel lblUhrzeit = new JLabel("Uhrzeit");
		lblUhrzeit.setBounds(10, 230, 113, 22);
		panel.add(lblUhrzeit);
		lblUhrzeit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		

		txtBox_Stunde = new JTextField();
		txtBox_Stunde.setBounds(10, 254, 40, 20);
		panel.add(txtBox_Stunde);
		txtBox_Stunde.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Stunde.setColumns(10);
		txtBox_Stunde.setText(Integer.toString(hour));
		

		JLabel lblHeizkörperwert = new JLabel("Heizkörperwert");
		lblHeizkörperwert.setBounds(10, 285, 113, 22);
		panel.add(lblHeizkörperwert);
		lblHeizkörperwert.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		
		JLabel lblHeizkörperwertEinheit = new JLabel("kWh");
		lblHeizkörperwertEinheit.setBounds(110, 308, 31, 22);
		panel.add(lblHeizkörperwertEinheit);
		lblHeizkörperwertEinheit.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		

		txtBox_Heizwert = new JTextField();
		txtBox_Heizwert.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Heizwert.setColumns(10);
		txtBox_Heizwert.setBounds(10, 310, 88, 20);
		panel.add(txtBox_Heizwert);
		txtBox_Heizwert.setText("0");


		JLabel lblRoomAdd = new JLabel("Raum hinzufügen");
		lblRoomAdd.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblRoomAdd.setBounds(10, 126, 99, 22);
		panel.add(lblRoomAdd);
		

		txtBox_AddRoom = new JTextField();
		txtBox_AddRoom.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_AddRoom.setColumns(10);
		txtBox_AddRoom.setBounds(110, 127, 192, 20);
		panel.add(txtBox_AddRoom);
		

		JButton btnAddRoom = new JButton("+");
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
		btnExport.setBounds(511, 509, 135, 23);
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
					row[0] = roomToAdd;
					row[1] = txtBox_Heizwert.getText();
					row[2] = txtBox_Tag.getText();
					row[3] = txtBox_Monat.getText();
					row[4] = txtBox_Jahr.getText();
					row[5] = txtBox_Stunde.getText();
	 				model.addRow(row);
					
					//JOptionPane.showMessageDialog(null, "Wert wurde erfolgreich hinzugefügt.");
				}				
			}
		}); 


		JButton btnClear = new JButton("Leeren");
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

		
		
		//------------------------------------------------------------------------------------
		//----------------------------------------Ansicht-------------------------------------
		//------------------------------------------------------------------------------------

		// Fenster für die Auswahl der Filter für die Anzeige
		frmAnzeige = new JFrame();
		frmAnzeige.getContentPane().setBackground(new Color(237, 237, 233));
		frmAnzeige.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 11));
		frmAnzeige.setTitle("Filter für die Anzeige");
		frmAnzeige.setResizable(false);
		frmAnzeige.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		frmAnzeige.setBounds(100, 100, 1024, 640);
		frmAnzeige.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAnzeige.getContentPane().setLayout(null);
		

		JButton btnHome2 = new JButton("Hauptmenü");
		btnHome2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnzeige.setVisible(false);
			}
		});
		btnHome2.setBounds(828, 31, 135, 23);
		frmAnzeige.getContentPane().add(btnHome2);
		

		JButton btnUebersicht = new JButton("Übersicht");
		btnUebersicht.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAnzeige.setVisible(false);
				frmHeizwerte.setVisible(true);
			}
		});
		btnUebersicht.setBounds(48, 31, 89, 23);
		frmAnzeige.getContentPane().add(btnUebersicht);
		

		JPanel panel2 = new JPanel();
		panel2.setBounds(48, 155, 366, 208);
		panel2.setBackground(new Color(255, 255, 255));
		frmAnzeige.getContentPane().add(panel2);
		panel2.setLayout(null);
		

		JLabel lblRooms = new JLabel("Räume");
		lblRooms.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblRooms.setBounds(10, 11, 61, 26);
		panel2.add(lblRooms);
		

		JLabel lblTime = new JLabel("Zeitraum");
		lblTime.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblTime.setBounds(10, 120, 61, 26);
		panel2.add(lblTime);
		

        JLabel lblBis = new JLabel("bis");
		lblBis.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblBis.setBounds(10, 144, 20, 26);
		panel2.add(lblBis);
		

		txtBox_StartTag = new JTextField();
		txtBox_StartTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBox_StartTag.setBounds(40, 148, 37, 20);
        txtBox_StartTag.setText(Integer.toString(day));
		panel2.add(txtBox_StartTag);
		txtBox_StartTag.setColumns(10);
        

		txtBox_StartMonat = new JTextField();
		txtBox_StartMonat.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBox_StartMonat.setColumns(10);
		txtBox_StartMonat.setBounds(87, 147, 37, 20);
        txtBox_StartMonat.setText(Integer.toString(month));
		panel2.add(txtBox_StartMonat);
		

		txtBox_StartJahr = new JTextField();
		txtBox_StartJahr.setFont(new Font("Segoe UI", Font.PLAIN, 11));
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
		lblVon_Punkt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblVon_Punkt.setBounds(81, 144, 20, 26);
		panel2.add(lblVon_Punkt);
		

		JLabel lblVon_Punkt_1 = new JLabel(".");
		lblVon_Punkt_1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblVon_Punkt_1.setBounds(127, 144, 20, 26);
		panel2.add(lblVon_Punkt_1);
		

		JButton btnShow = new JButton("Anzeigen");
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
									System.out.println("Room: " + i);																		
                                    SmartHeating object = SmartHeating.getDayMeasurememt(year, month, day, i, false, TraceColour.PURPLE)[0];
                                    
									
									object = erstellungGraphen(allColors, colorIndex, index, object, i);
									allMeasuSmartHeatings[index] = object;
									for (double z: object.getMeasurements()) {
										System.out.println(z);
									}
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
                                    SmartHeating object = SmartHeating.getWeekData(year, month, day, i)[0];
                                    
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
                                System.out.println(a);
                            }
                            break;
                        case 2:
                            try {
                                
                                int index = 0;
								int colorIndex = 0;
                                for(String i: allRooms){
                                    SmartHeating object = SmartHeating.getMonthMeasurement(year, month,  i, false, TraceColour.ORANGE)[0];
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
                                    SmartHeating object = SmartHeating.getYearData(year,  i)[0];
                                    
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
		btnShow.setBounds(820, 439, 143, 52);
		frmAnzeige.getContentPane().add(btnShow);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblFilter.setBounds(48, 119, 61, 26);
		frmAnzeige.getContentPane().add(lblFilter);
		
        JScrollPane scrollPane_Room = new JScrollPane();
		scrollPane_Room.setBounds(10, 36, 323, 74);
		panel2.add(scrollPane_Room);

		list_Room = new JList();
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
				System.out.println("Liste: " + list_Room.getSelectedValuesList());
				allRooms = list_Room.getSelectedValuesList();
            }
        });
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(597, 155, 366, 208);
		panel_1.setBackground(new Color(255, 255, 255));
		frmAnzeige.getContentPane().add(panel_1);
		

		JLabel lblAbstnde = new JLabel("Abstände");
		lblAbstnde.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblAbstnde.setBounds(10, 11, 61, 26);
		panel_1.add(lblAbstnde);
		

		JLabel lblWasSollAngezeigt = new JLabel("Was soll angezeigt werden?");
		lblWasSollAngezeigt.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblWasSollAngezeigt.setBounds(10, 83, 163, 26);
		panel_1.add(lblWasSollAngezeigt);
		

		JComboBox comboBox_Was = new JComboBox();
		comboBox_Was.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		comboBox_Was.setModel(new DefaultComboBoxModel(new String[] {"Verlauf Verbrauch", "Verlauf Kosten", "Verlauf Heizkörperwerte"}));
		comboBox_Was.setBounds(10, 113, 224, 22);
        comboBox_Was.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				wasAnzeige = comboBox_Was.getSelectedIndex();
				System.out.println(wasAnzeige);
            }
        });
		panel_1.add(comboBox_Was);

		//checkbox für Durchschnitt
		chckbxDurchschnitt = new JCheckBox("Anzeige \r⌀");
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
		comboBox_Abstand.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		comboBox_Abstand.setModel(new DefaultComboBoxModel(new String[] {"Tage", "Wochen", "Monate", "Jahre"}));
		comboBox_Abstand.setBounds(10, 39, 224, 22);
        comboBox_Abstand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abstand = comboBox_Abstand.getSelectedIndex();
            }
        });
		panel_1.add(comboBox_Abstand);

		
		JLabel lblGaspreis = new JLabel("Gaspreis");
		lblGaspreis.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblGaspreis.setBounds(10, 146, 163, 26);
		panel_1.add(lblGaspreis);
		
		//Textfeld für Gaspreis
		textField_2 = new JTextField();
		textField_2.setBounds(10, 177, 96, 20);
		textField_2.setFont(new Font("Segoe UI", Font.BOLD, 11));
		textField_2.setText("18");
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lbl_PreisEinheit = new JLabel("ct/kWh");
		lbl_PreisEinheit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lbl_PreisEinheit.setBounds(116, 177, 163, 26);
		panel_1.add(lbl_PreisEinheit);

		JLabel lblAnzeigeeinstellungen = new JLabel("Anzeigeeinstellungen");
		lblAnzeigeeinstellungen.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblAnzeigeeinstellungen.setBounds(597, 119, 231, 26);
		frmAnzeige.getContentPane().add(lblAnzeigeeinstellungen);


		JPanel panel_Alarm = new JPanel();
		panel_Alarm.setLayout(null);
		panel_Alarm.setBackground(Color.WHITE);
		panel_Alarm.setBounds(48, 424, 366, 67);
		frmAnzeige.getContentPane().add(panel_Alarm);
		
		JLabel lbl_erlaubteAbweichung = new JLabel("Erlaubte Abweichung");
		lbl_erlaubteAbweichung.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lbl_erlaubteAbweichung.setBounds(10, 11, 152, 26);
		panel_Alarm.add(lbl_erlaubteAbweichung);
		
		JLabel lbl_prozentAbweichungen = new JLabel("Prozent Abweichungen");
		lbl_prozentAbweichungen.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lbl_prozentAbweichungen.setBounds(172, 11, 152, 26);
		panel_Alarm.add(lbl_prozentAbweichungen);
		
		txtBox_erlaubteAbweichung = new JTextField();
		txtBox_erlaubteAbweichung.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBox_erlaubteAbweichung.setColumns(10);
		txtBox_erlaubteAbweichung.setBounds(10, 35, 62, 20);
		panel_Alarm.add(txtBox_erlaubteAbweichung);
		
		JLabel lbl_Prozent = new JLabel("%");
		lbl_Prozent.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lbl_Prozent.setBounds(82, 32, 20, 26);
		panel_Alarm.add(lbl_Prozent);
		
		JLabel lbl_Prozent2 = new JLabel("%");
		lbl_Prozent2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lbl_Prozent2.setBounds(244, 32, 20, 26);
		panel_Alarm.add(lbl_Prozent2);
		
		txtBox_prozentAbweichungen = new JTextField();
		txtBox_prozentAbweichungen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		txtBox_prozentAbweichungen.setColumns(10);
		txtBox_prozentAbweichungen.setBounds(172, 35, 62, 20);
		panel_Alarm.add(txtBox_prozentAbweichungen);
		
		JLabel lbl_Alarm = new JLabel("Alarme");
		lbl_Alarm.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lbl_Alarm.setBounds(48, 387, 98, 26);
		frmAnzeige.getContentPane().add(lbl_Alarm);
	}		
}