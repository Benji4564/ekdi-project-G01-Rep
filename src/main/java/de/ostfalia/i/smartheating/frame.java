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
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.charts.dataviewer.utils.TraceColour;

import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.ComponentOrientation;
import java.awt.SystemColor;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;

import javax.swing.AbstractListModel;
public class frame {

	private JFrame frmSmartheater;
	private JFrame frmAnzeige;
	private JFrame frmHeizwerte;
	private JTable table;
	private JTextField txtBox_Tag;
	private JTextField txtBox_Monat;
	private JTextField txtBox_Jahr;
	private JTextField txtBox_Stunde;
	private JTextField txtBox_Minute;
	private JTextField txtBox_Heizwert;
	DefaultTableModel model;
	private JTextField txtBox_Raum;
	private JTextField textField_2;
    public static JComboBox comboBox_Was;
    private JTextField txtBox_StartTag;
    private JTextField txtBox_StartMonat;
    private JTextField txtBox_StartJahr;
    public static JComboBox comboBox_Abstand;
    public int abstand;
    public static int day = 1;
    public static int month = 1;
    public static int year = 2022;
    public static String room = "Schlafzimmer";
    public static List<String> allRooms;
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

	/**
	 * Create the application.
	 */
	public frame() {
        try {
            SmartHeating.init();
            initialize();
            
        } catch (Exception x) {
            // TODO: handle exception
        }
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
			}
		});
		btnAnzeige.setForeground(new Color(82, 121, 111));
		btnAnzeige.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		btnAnzeige.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, Color.DARK_GRAY, null));
		btnAnzeige.setBackground(Color.WHITE);
		btnAnzeige.setBounds(395, 367, 215, 39);
		frmSmartheater.getContentPane().add(btnAnzeige);
		
		// Button für die Einstellungen
		JButton btnSettings = new JButton("");
		btnSettings.setBounds(5, 5, 55, 52);
		frmSmartheater.getContentPane().add(btnSettings);
		
		
		
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
				txtBox_Raum.setText(model.getValueAt(i, 0).toString());
				txtBox_Heizwert.setText(model.getValueAt(i, 1).toString());
				txtBox_Tag.setText(model.getValueAt(i, 2).toString());
				txtBox_Monat.setText(model.getValueAt(i, 3).toString());
				txtBox_Jahr.setText(model.getValueAt(i, 4).toString());
				txtBox_Stunde.setText(model.getValueAt(i, 5).toString());
			}
		});
		table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		model = new DefaultTableModel();		
		Object[] column = {"Raum", "Heizk\u00F6rperwert", "Tag", "Monat", "Jahr", "Stunde"};
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
			}
		});
		btnAnsicht.setBounds(48, 31, 89, 23);
		frmHeizwerte.getContentPane().add(btnAnsicht);
		
		JButton btnHome = new JButton("Hauptmenü");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmHeizwerte.setVisible(false);
				frmAnzeige.setVisible(false);
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
		lblZeitpunkt.setBounds(10, 110, 113, 22);
		panel.add(lblZeitpunkt);
		lblZeitpunkt.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		JLabel lblDatum = new JLabel("Datum");
		lblDatum.setBounds(10, 143, 113, 22);
		panel.add(lblDatum);
		lblDatum.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Tag = new JTextField();
		txtBox_Tag.setBounds(10, 165, 40, 20);
		panel.add(txtBox_Tag);
		txtBox_Tag.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Tag.setColumns(10);
		
		JLabel lblPunkt = new JLabel(".");
		lblPunkt.setBounds(47, 163, 13, 22);
		panel.add(lblPunkt);
		lblPunkt.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Monat = new JTextField();
		txtBox_Monat.setBounds(58, 165, 40, 20);
		panel.add(txtBox_Monat);
		txtBox_Monat.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Monat.setColumns(10);
		
		JLabel lblPunkt_1 = new JLabel(".");
		lblPunkt_1.setBounds(96, 163, 13, 22);
		panel.add(lblPunkt_1);
		lblPunkt_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunkt_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Jahr = new JTextField();
		txtBox_Jahr.setBounds(110, 165, 63, 20);
		panel.add(txtBox_Jahr);
		txtBox_Jahr.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Jahr.setColumns(10);
		
		JLabel lblUhrzeit = new JLabel("Uhrzeit");
		lblUhrzeit.setBounds(10, 193, 113, 22);
		panel.add(lblUhrzeit);
		lblUhrzeit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Stunde = new JTextField();
		txtBox_Stunde.setBounds(10, 217, 40, 20);
		panel.add(txtBox_Stunde);
		txtBox_Stunde.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Stunde.setColumns(10);
		
		JLabel lblDoppelpunkt = new JLabel(":");
		lblDoppelpunkt.setBounds(47, 215, 13, 22);
		panel.add(lblDoppelpunkt);
		lblDoppelpunkt.setHorizontalAlignment(SwingConstants.CENTER);
		lblDoppelpunkt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		txtBox_Minute = new JTextField();
		txtBox_Minute.setBounds(58, 217, 40, 20);
		panel.add(txtBox_Minute);
		txtBox_Minute.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Minute.setColumns(10);
		
		JLabel lblHeizkörperwert = new JLabel("Heizkörperwert");
		lblHeizkörperwert.setBounds(10, 285, 113, 22);
		panel.add(lblHeizkörperwert);
		lblHeizkörperwert.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		JLabel lblHeizkörperwertEinheit = new JLabel("m³");
		lblHeizkörperwertEinheit.setBounds(110, 308, 31, 22);
		panel.add(lblHeizkörperwertEinheit);
		lblHeizkörperwertEinheit.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		
		txtBox_Heizwert = new JTextField();
		txtBox_Heizwert.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Heizwert.setColumns(10);
		txtBox_Heizwert.setBounds(10, 310, 88, 20);
		panel.add(txtBox_Heizwert);
		
		txtBox_Raum = new JTextField();
		txtBox_Raum.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
		txtBox_Raum.setColumns(10);
		txtBox_Raum.setBounds(10, 42, 88, 20);
		panel.add(txtBox_Raum);
		
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
		
		JButton btnAdd = new JButton("Hinzufügen");
		btnAdd.setBounds(285, 509, 129, 23);
		frmHeizwerte.getContentPane().add(btnAdd);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(196, 509, 79, 23);
		frmHeizwerte.getContentPane().add(btnUpdate);
		
		JButton btnClear = new JButton("Leeren");
		btnClear.setBounds(48, 509, 89, 23);
		frmHeizwerte.getContentPane().add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBox_Raum.setText("");
				txtBox_Heizwert.setText("");
				txtBox_Tag.setText("");
				txtBox_Monat.setText("");
				txtBox_Jahr.setText("");
				txtBox_Stunde.setText("");
			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				model.setValueAt(txtBox_Raum.getText(), i, 0);
				model.setValueAt(txtBox_Heizwert.getText(), i, 1);
				model.setValueAt(txtBox_Tag.getText(), i, 2);
				model.setValueAt(txtBox_Monat.getText(), i, 3);
				model.setValueAt(txtBox_Jahr.getText(), i, 4);
				model.setValueAt(txtBox_Stunde.getText(), i, 5);
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtBox_Heizwert.getText().equals("")||txtBox_Tag.getText().equals("")||txtBox_Monat.getText().equals("")||txtBox_Jahr.getText().equals("")||txtBox_Stunde.getText().equals("")||txtBox_Minute.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.");
				} 
				else {
					row[0] = txtBox_Raum.getText();;
					row[1] = txtBox_Heizwert.getText();
					row[2] = txtBox_Tag.getText();
					row[3] = txtBox_Monat.getText();
					row[4] = txtBox_Jahr.getText();
					row[5] = txtBox_Stunde.getText();
	 				model.addRow(row);
	 				
	 				txtBox_Raum.setText("");
	 				txtBox_Heizwert.setText("");
					txtBox_Tag.setText("");
					txtBox_Monat.setText("");
					txtBox_Jahr.setText("");
					txtBox_Stunde.setText("");
					
					JOptionPane.showMessageDialog(null, "Wert wurde erfolgreich hinzugefügt.");
				}				
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
                System.out.println("Anzeigen");
                try {
                    day = Integer.parseInt(txtBox_StartTag.getText());
                    month = Integer.parseInt(txtBox_StartMonat.getText());
                    year = Integer.parseInt(txtBox_StartJahr.getText());
                    SmartHeating[] allMeasuSmartHeatings = new SmartHeating[allRooms.size()];
                    switch (abstand) {
                        case 0:
                            try {

                                int index = 0;
                                for(String i: allRooms){
                                    SmartHeating smartheating = SmartHeating.getDayMeasurememt(year, month, day, i, false, TraceColour.PURPLE)[0];
                                    allMeasuSmartHeatings[index] = smartheating;
                                    index++;
                                }
                                SmartHeating.graphConfig.x = "Stunden";
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMeasuSmartHeatings);
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                        
                            
                            break;
                        case 1:
                            try {
                                
                                int index = 0;
                                for(String i: allRooms){
                                    SmartHeating smartheating = SmartHeating.getWeekData(year, month, day, i)[0];
                                    allMeasuSmartHeatings[index] = smartheating;
                                    index++;
                                }

                                SmartHeating.graphConfig.x = "Tag";
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMeasuSmartHeatings);
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            break;
                        case 2:
                            try {
                                
                                int index = 0;
                                for(String i: allRooms){
                                    SmartHeating smartheating = SmartHeating.getMonthMeasurement(year, month,  i, false, TraceColour.ORANGE)[0];
                                    allMeasuSmartHeatings[index] = smartheating;
                                    index++;
                                }
                                SmartHeating.graphConfig.x = "Tag";
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMeasuSmartHeatings);
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            
                            break;

                        case 3:
                            try {
                                int index = 0;
                                for(String i: allRooms){
                                    SmartHeating smartheating = SmartHeating.getYearData(year,  i)[0];
                                    allMeasuSmartHeatings[index] = smartheating;
                                    index++;
                                }

                                SmartHeating.graphConfig.x = "Monat";
                                
                                SmartHeating.drawLinePlot(SmartHeating.graphConfig, allMeasuSmartHeatings);
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
		btnShow.setBounds(434, 455, 143, 52);
		frmAnzeige.getContentPane().add(btnShow);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblFilter.setBounds(48, 119, 61, 26);
		frmAnzeige.getContentPane().add(lblFilter);
		
        JScrollPane scrollPane_Room = new JScrollPane();
		scrollPane_Room.setBounds(10, 36, 323, 74);
		panel2.add(scrollPane_Room);

		JList list_Room = new JList();
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
                    allRooms = list_Room.getSelectedValuesList();
 
                }
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
		comboBox_Was.setModel(new DefaultComboBoxModel(new String[] {"Heizkörperwerte", "Kosten"}));
		comboBox_Was.setBounds(10, 113, 224, 22);
        comboBox_Was.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
		panel_1.add(comboBox_Was);


        
		
		JComboBox comboBox_Abstand = new JComboBox();
		comboBox_Abstand.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		comboBox_Abstand.setModel(new DefaultComboBoxModel(new String[] {"Tage", "Wochen", "Monate", "Jahre"}));
		comboBox_Abstand.setBounds(10, 39, 224, 22);
        comboBox_Abstand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abstand = comboBox_Abstand.getSelectedIndex();
                System.out.println(abstand);
            }
        });
		panel_1.add(comboBox_Abstand);



		
		JLabel lblGaspreis = new JLabel("Gaspreis");
		lblGaspreis.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		lblGaspreis.setBounds(10, 146, 163, 26);
		panel_1.add(lblGaspreis);
		
		textField_2 = new JTextField();
		textField_2.setBounds(10, 177, 96, 20);
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
	}		
}

    

