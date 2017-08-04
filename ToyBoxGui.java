/** 
*	Author: Brandon Kucybala
*	Project: "toybox" [final java gui project] - collectables inventory system
*	Date: 07/15/05
**/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import  java.io.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;

public class ToyBoxGui
{
	// general class-wide variable declarations
	private static JTextField txtId, txtName, txtYear, txtComments, txtOrigPrice, txtCurrentPrice, txtProfit, txtTotalAmount;
	private static JButton cmdOpen, cmdAdd, cmdRemove, cmdEdit, cmdCancel, cmdFirst, cmdLast, cmdNext, cmdPrev;
	private static String[] arrTempDb;
	private static String[] arrCondition={"poor", "moderate", "good", "mint"};
	private static JMenuItem mnuFile, mnuAdd, mnuAddType, mnuAddSeries, mnuAddCondition, mnuViewTotalProfit, mnuExit, mnuView, mnuSearch, mnuSearchType, mnuSearchId, mnuSearchName;
	private static JMenuBar mBar;
	private static JList lstDisplay;
	private static JComboBox cmbCondition, cmbType, cmbSeries;
	private static ImageIcon imgLogo;
	private static Statement stat;
	private static ResultSet toyRecords, typeRecords, condRecords, idRecords, searchType, searchName, searchId, addNewType, findProfit, seriesRecords, displaySearch;
	private static Connection conn;

	public static void main(String[] args) 
	{
		// gui setup
		JFrame gui = new JFrame("toybox ver 0.6b");
		Container cp = gui.getContentPane();
		dbHandler dbh = new dbHandler();
		MenuHandler mh = new MenuHandler();
		ListHandler lh = new ListHandler();

		// ***top logo***
		JPanel pnlLogo = new JPanel();
		imgLogo = new ImageIcon("toybox.jpg");
		JLabel lblLogo = new JLabel("", imgLogo, SwingConstants.CENTER);
		pnlLogo.add(lblLogo);

		// ***menu ***
		mBar = new JMenuBar();
		JMenu mnuFile = new JMenu("File");
		mnuFile.setMnemonic('F');
		mBar.add(mnuFile);
			JMenu mnuAdd = new JMenu("Add");
			mnuAdd.setMnemonic('A');
			mnuFile.add(mnuAdd);
			mnuAddType = new JMenuItem("New Type");
			mnuAddType.setMnemonic('T');
			mnuAddType.addActionListener(mh);
			mnuAdd.add(mnuAddType);
			mnuAddSeries = new JMenuItem("New Series");
			mnuAddSeries.setMnemonic('S');
			mnuAddSeries.addActionListener(mh);
			mnuAdd.add(mnuAddSeries);
			mnuExit = new JMenuItem("Exit");
			mnuExit.setMnemonic('x');
			mnuExit.addActionListener(mh);
			mnuFile.add(mnuExit);
		JMenu mnuView = new JMenu("View");
		mnuView.setMnemonic('V');
		mBar.add(mnuView);
			mnuViewTotalProfit = new JMenuItem("Total Profit");
			mnuViewTotalProfit.setMnemonic('T');
			mnuViewTotalProfit.addActionListener(mh);
			mnuView.add(mnuViewTotalProfit);
		JMenu mnuSearch = new JMenu("Search");
		mnuSearch.setMnemonic('S');
		mBar.add(mnuSearch);
			mnuSearchName = new JMenuItem("By Name");
			mnuSearchName.setMnemonic('N');
			mnuSearchName.addActionListener(mh);
			mnuSearch.add(mnuSearchName);
			mnuSearchType = new JMenuItem("By Type");
			mnuSearchType.setMnemonic('T');
			mnuSearchType.addActionListener(mh);
			mnuSearch.add(mnuSearchType);
			mnuSearchId = new JMenuItem("By Id");
			mnuSearchId.setMnemonic('I');
			mnuSearchId.addActionListener(mh);
			mnuSearch.add(mnuSearchId);	
		gui.setJMenuBar(mBar);

		mnuAddType.setEnabled(false);
		mnuAddSeries.setEnabled(false);
		mnuViewTotalProfit.setEnabled(false);
		mnuSearchName.setEnabled(false);
		mnuSearchId.setEnabled(false);
		mnuSearchType.setEnabled(false);
		
		// *** textbox panel ***
		// create and set up the text box/combo box components
		txtId = new JTextField(5);
		txtId.setEditable(false);
		cmbType = new JComboBox();
		cmbType.setEnabled(false);
		txtName = new JTextField(15);
		txtName.setEditable(false);
		cmbSeries = new JComboBox();
		cmbSeries.setEnabled(false);
		txtYear = new JTextField(5);
		txtYear.setEditable(false);
		cmbCondition = new JComboBox(arrCondition);
		cmbCondition.setEnabled(false);
		txtComments = new JTextField(20);
		txtComments.setEditable(false);
		txtOrigPrice = new JTextField(10);
		txtOrigPrice.setEditable(false);
		txtCurrentPrice = new JTextField(10);
		txtCurrentPrice.setEditable(false);
		txtProfit = new JTextField(10);
		txtProfit.setEditable(false);

		// lay out the label fields & layout(s)
		JPanel pnlFields = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbCon = new GridBagConstraints();
		pnlFields.setLayout(gbl);

		// set initial gridbag constraints, apply constraints after object creation
		gbCon.gridy = 5;
		gbCon.gridx = 0;
		gbCon.gridy = 0;
		gbCon.gridheight = 1;
		gbCon.gridwidth = 1;
		gbCon.ipady = 10;
		gbCon.anchor = GridBagConstraints.EAST;
		gbCon.fill = GridBagConstraints.VERTICAL;
	
		JLabel lblId = new JLabel("ID:");
		gbl.setConstraints(lblId, gbCon);
		pnlFields.add(lblId);
		JLabel lblType = new JLabel("Type:");
		gbCon.gridy = 1;
		gbl.setConstraints(lblType, gbCon);
		pnlFields.add(lblType);
		JLabel lblName = new JLabel("Name:");
		gbCon.gridy = 2;
		gbl.setConstraints(lblName, gbCon);
		pnlFields.add(lblName);
		JLabel lblSeries = new JLabel("Series:");
		gbCon.gridy = 3;
		gbl.setConstraints(lblSeries, gbCon);
		pnlFields.add(lblSeries);
		JLabel lblYear = new JLabel("Year:");
		gbCon.gridy = 4;
		gbl.setConstraints(lblYear, gbCon);
		pnlFields.add(lblYear);
		JLabel lblCondition = new JLabel("Condition:");
		gbCon.gridy = 5;
		gbl.setConstraints(lblCondition, gbCon);
		pnlFields.add(lblCondition);
		JLabel lblComments = new JLabel("Comments:");
		gbCon.gridy = 6;
		gbl.setConstraints(lblComments, gbCon);
		pnlFields.add(lblComments);
		JLabel lblOrigPrice = new JLabel("Original Price ($):");
		gbCon.gridy = 7;
		gbl.setConstraints(lblOrigPrice, gbCon);
		pnlFields.add(lblOrigPrice);
		JLabel lblCurPrice = new JLabel("Current Price ($):");
		gbCon.gridy = 8;
		gbl.setConstraints(lblCurPrice, gbCon);
		pnlFields.add(lblCurPrice);
		JLabel lblProfit = new JLabel("Profit ($):");
		gbCon.gridy = 9;
		gbl.setConstraints(lblProfit, gbCon);
		pnlFields.add(lblProfit);

		// ** input panel **
		JPanel pnlInput = new JPanel(new GridLayout(10,1));
		//create and set up textboxes
		pnlInput.add(txtId);
		pnlInput.add(cmbType);
		pnlInput.add(txtName);
		pnlInput.add(cmbSeries);
		pnlInput.add(txtYear);
		pnlInput.add(cmbCondition);
		pnlInput.add(txtComments);
		pnlInput.add(txtOrigPrice);
		pnlInput.add(txtCurrentPrice);
		pnlInput.add(txtProfit);

		// ** text/label panel
		JPanel pnlTextLabel = new JPanel(new GridBagLayout());
		pnlTextLabel.add(pnlFields);
		pnlTextLabel.add(pnlInput);

		// ** list panel **
		JPanel pnlDisplay = new JPanel(new FlowLayout());
		pnlDisplay.setBorder(BorderFactory.createLineBorder(Color.gray));
		// create and set up listbox components
		lstDisplay = new JList();
		lstDisplay.setVisibleRowCount(16);
		lstDisplay.setFixedCellWidth(300);
		lstDisplay.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION );
		lstDisplay.addMouseListener(lh);
		//add scrollbars to listbox
		JScrollPane sp = new JScrollPane(lstDisplay);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		pnlDisplay.add(sp);

		// *** nav button panel ***
		JPanel pnlNavButtons = new JPanel(new GridLayout(1, 1));
		// create and set up the button components
		cmdOpen = new JButton("Open");
		cmdOpen.setMnemonic('O');
		pnlNavButtons.add(cmdOpen);
		cmdOpen.addActionListener(dbh);
		cmdPrev = new JButton("Prev");
		cmdPrev.setMnemonic('P');
		pnlNavButtons.add(cmdPrev);
		cmdPrev.addActionListener(dbh);
		cmdPrev.setEnabled(false);
		cmdNext = new JButton("Next");
		cmdNext.setMnemonic('N');
		pnlNavButtons.add(cmdNext);
		cmdNext.addActionListener(dbh);
		cmdNext.setEnabled(false);
		cmdFirst = new JButton("First");
		cmdFirst.setMnemonic('F');
		pnlNavButtons.add(cmdFirst);
		cmdFirst.addActionListener(dbh);
		cmdFirst.setEnabled(false);
		cmdLast = new JButton("Last");
		cmdLast.setMnemonic('L');
		pnlNavButtons.add(cmdLast);
		cmdLast.addActionListener(dbh);
		cmdLast.setEnabled(false);

		// *** db mod button panel ***
		JPanel pnlModButtons = new JPanel(new GridLayout(4,1));
		// create and set up the button components
		cmdAdd = new JButton("Add");
		cmdAdd.setMnemonic('A');
		pnlModButtons.add(cmdAdd);
		cmdAdd.addActionListener(dbh);
		cmdRemove = new JButton("Remove");
		cmdRemove.setMnemonic('R');
		pnlModButtons.add(cmdRemove);
		cmdRemove.addActionListener(dbh);
		cmdEdit = new JButton("Edit");
		cmdEdit.setMnemonic('E');
		pnlModButtons.add(cmdEdit);
		cmdEdit.addActionListener(dbh);
		cmdCancel = new JButton("Cancel");
		cmdCancel.setMnemonic('C');
		pnlModButtons.add(cmdCancel);
		cmdCancel.addActionListener(dbh);
		DisableMod();

		//main button panel composed of nav & mod button panels
		JPanel pnlButtons = new JPanel(new GridLayout(1,1));
		pnlButtons.add(pnlNavButtons);

		// add panels to content pane
		cp.add(pnlLogo, BorderLayout.NORTH);
		cp.add(pnlTextLabel, BorderLayout.WEST);
		cp.add(pnlDisplay, BorderLayout.CENTER);
		cp.add(pnlModButtons, BorderLayout.EAST);
		cp.add(pnlButtons, BorderLayout.SOUTH);

		try
		{
			// connect to db
			SimpleDataSource.init("db.properties");
			conn = SimpleDataSource.getConnection();
			// use media as current db - allows for first, last, next & prev
			stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stat.executeUpdate("USE toybox");
			fillTypeField();
			fillSeriesField();
		}
		catch (Exception s)
		{
			s.printStackTrace();
		}

		// gui standards
		//gui.setSize(500,415); //** may use predefined size?
		gui.setResizable(false);
		gui.pack();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);

	}

	// ################################################ end main ###########################################

	// disables all nav buttons
	private static void DisableNav()
	{
		cmdPrev.setEnabled(false);
		cmdNext.setEnabled(false);
		cmdFirst.setEnabled(false);
		cmdLast.setEnabled(false);
	}

	// enables all nav buttons
	private static void EnableNav()
	{
		cmdPrev.setEnabled(true);
		cmdNext.setEnabled(true);
		cmdFirst.setEnabled(true);
		cmdLast.setEnabled(true);
	}

	// disables all database modification buttons
	private static void DisableMod()
	{
		cmdAdd.setEnabled(false);
		cmdRemove.setEnabled(false);
		cmdEdit.setEnabled(false);
		cmdCancel.setEnabled(false);
	}

	private static void EnableMod()
	{
		// enables all database modification buttons
		cmdAdd.setEnabled(true);
		cmdRemove.setEnabled(true);
		cmdEdit.setEnabled(true);
		cmdCancel.setEnabled(true);
	}

	private static void EnableInput()
	{
		// enable ability to modify textboxes
		txtId.setEditable(false);
		cmbType.setEnabled(true);
		txtName.setEditable(true);
		cmbSeries.setEnabled(true);
		txtYear.setEditable(true);
		cmbCondition.setEnabled(true);
		txtComments.setEditable(true);
		txtOrigPrice.setEditable(true);
		txtCurrentPrice.setEditable(true);
	}

	private static void DisableInput()
	{
		// enable ability to modify textboxes
		txtId.setEditable(false);
		cmbType.setEnabled(false);
		txtName.setEditable(false);
		cmbSeries.setEnabled(false);
		txtYear.setEditable(false);
		cmbCondition.setEnabled(false);
		txtComments.setEditable(false);
		txtOrigPrice.setEditable(false);
		txtCurrentPrice.setEditable(false);
	}

	private static void textDisplayRecord(ResultSet r) throws Exception
	{
		//set up number formatting
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		String ProfitFigure;
		double CurrentPrice;
		double OrigPrice;
		//set index of condition combobox to toy.toy_cond value
		int inType = r.getInt(2);
		int inCond = r.getInt(6);
		int inSeries = r.getInt(4);
		//set textbox text to field values from SQL db current query
		txtId.setText(r.getString(1));
		cmbType.setSelectedIndex(inType);
		txtName.setText(r.getString(3));
		cmbSeries.setSelectedIndex(inSeries);
		txtYear.setText(r.getString(5));
		cmbCondition.setSelectedIndex(inCond);
		txtOrigPrice.setText (String.valueOf(nf.format(r.getDouble(8))));
		txtCurrentPrice.setText (String.valueOf(nf.format(r.getDouble(9))));
		txtComments.setText(r.getString(7));
		//convert to double & perform calculations
		CurrentPrice = r.getDouble(9);
		OrigPrice = r.getDouble(8);
		double result = CurrentPrice - OrigPrice;
		ProfitFigure = String.valueOf(nf.format(result));
		//set calculated profit to last textbox
		txtProfit.setText(ProfitFigure);
	}

	// changes mod-add button text to "Save" and corresponding mnemonic
	private static void AddSaveChange()
	{
			cmdAdd.setText("Save");
			cmdAdd.setMnemonic ('S');
	}

	// changes mod-add button text to "Save" and corresponding mnemonic
	private static void EditSaveChange()
	{
			cmdEdit.setText("Save");
			cmdEdit.setMnemonic ('S');
	}

	// populates "type" combobox
	private static void fillTypeField()
	{
		try
		{
			typeRecords = stat.executeQuery("SELECT * FROM toy_type");

			while (typeRecords.next())
			{
				cmbType.addItem(String.valueOf(typeRecords.getInt(1)) + " - " + typeRecords.getString(2));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// populates "condition" combobox
	private static void fillSeriesField()
	{
		try
		{
			seriesRecords = stat.executeQuery("SELECT * from series");
			while (seriesRecords.next())
			{
				cmbSeries.addItem(String.valueOf(seriesRecords.getInt(1)) + " - " + seriesRecords.getString(3));
			}
		}
		catch (Exception ej)
		{
			ej.printStackTrace();
		}
	}

	//listbox action listener
	private static class ListHandler implements MouseListener
	{
		public void mouseClicked(MouseEvent klik)
		{
			try
			{
				int clickCount = klik.getClickCount();
				if (clickCount == 2)
				{
					String selectedRecord = String.valueOf(lstDisplay.getSelectedValue());
					System.out.println(selectedRecord); //test
					int pos = selectedRecord.indexOf(":") ;
					String selectedId = selectedRecord.substring(0, pos);
					//String sqlPullSearchRecord = "SELECT t.toy_id, tt.type_id,  t.name, s.series_id, t.release_year, t.toy_cond, t.comments, t.release_price, t.current_price FROM toy t, series s, toy_type tt WHERE t.type_id = tt.type_id AND t.series_id = s.series_id AND t.toy_id = " +selectedId;
					//System.out.println(sqlPullSearchRecord); //test
					//displaySearch = stat.executeQuery(sqlPullSearchRecord);	
					//textDisplayRecord(displaySearch);
					
					toyRecords.first();
					while( selectedId != String.valueOf( toyRecords.getInt(1) ) )
					{
						toyRecords.next();
					}
					textDisplayRecord(toyRecords);
				}
			}
			catch (Exception hj)
			{
				hj.printStackTrace();
			}
		}
		public void mousePressed (MouseEvent klik)
		{
		}
		public void mouseReleased (MouseEvent klik)
		{
		}
		public void mouseEntered (MouseEvent klik)
		{
		}
		public void mouseExited (MouseEvent klik)
		{
		}
	}
	// controls menu items
	private static class MenuHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent m)
		{
			// search.bytype menu item
			if (m.getSource() == mnuSearchType)
			{
				//set up number formatting
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
				try
				{	
					int x=0;
					int y=0;
					// create sql (with user input) & execute query
					String searchTypeInput = JOptionPane.showInputDialog("Please enter a toy type. Even a few letters will do..");
					String sqlSearchType = "SELECT toy_id, name, release_year, release_price, current_price FROM toy WHERE type_id IN ( SELECT type_id FROM toy_type WHERE type_name LIKE " + "'%" + searchTypeInput  + "%')";
					searchType=stat.executeQuery(sqlSearchType);
					while (searchType.next())
					{
						y++;
					}

					searchType.first();
					searchType.previous();
					String[] tempArray = new String[y];
					while (searchType.next())
					{
						//add results to array list
						String displayItem = String.valueOf(searchType.getInt(1)) + ":" + searchType.getString(2) + " ---- " + searchType.getString(3) + " ---- " + String.valueOf(nf.format(searchType.getDouble(4)) + " ---- " + String.valueOf(nf.format(searchType.getDouble(5))));
						//System.out.println(displayItem);
						tempArray[x] = displayItem;
						x = x + 1;
					}//end while
					lstDisplay.setListData(tempArray);
					lstDisplay.setSelectedIndex(1);
					System.out.println(lstDisplay.getSelectedValue());
				}//end try
				catch (Exception mnuEx)
				{
					mnuEx.printStackTrace();
				}//end catch
			}//end if
			
			// search.byname menu item
			if (m.getSource() == mnuSearchName)
			{

				//set up number formatting
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
				try
				{	
					int x=0;
					int y=0;
					// create sql (with user input) & execute query
					String searchNameInput = JOptionPane.showInputDialog("Please enter a toy name. Even a few letters will do..");
					String sqlSearchName = "SELECT toy_id, name, release_year, release_price, current_price FROM toy WHERE name LIKE " + "'%" + searchNameInput  + "%'";
					System.out.println(sqlSearchName);
					searchName=stat.executeQuery(sqlSearchName);
					while (searchName.next())
					{
						y++;
					}

					searchName.first();
					searchName.previous();
					String[] tempArray2 = new String[y];
					while (searchName.next())
					{
						//add results to array list
						String displayItem = String.valueOf(searchName.getInt(1)) + ":" + searchName.getString(2) + " ---- " + searchName.getString(3) + " ---- " + String.valueOf(nf.format(searchName.getDouble(4)) + " ---- " + String.valueOf(nf.format(searchName.getDouble(5))));
						tempArray2[x] = displayItem;
						x = x + 1;
					}//end while
					lstDisplay.setListData(tempArray2);
				}//end try
				catch (Exception mnuEx2)
				{
					mnuEx2.printStackTrace();
				}//end catch
			}//end if

			// search.byid menu item
			if (m.getSource() == mnuSearchId)
			{
				//set up number formatting
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
				try
				{	
					int x=0;
					int y=0;
					// create sql (with user input) & execute query
					String searchIdInput = JOptionPane.showInputDialog("Please enter a toy id you wish to search for.");
					String sqlSearchId = "SELECT toy_id, name, release_year, release_price, current_price FROM toy WHERE toy_id LIKE " + "'%" + searchIdInput  + "%'";
					System.out.println(sqlSearchId);
					searchId=stat.executeQuery(sqlSearchId);
					while (searchId.next())
					{
						y++;
					}

					searchId.first();
					searchId.previous();
					String[] tempArray3 = new String[y];
					while (searchId.next())
					{
						//add results to array list
						String displayItem = String.valueOf(searchId.getInt(1)) + ":" + searchId.getString(2) + " ---- " + searchId.getString(3) + " ---- " + String.valueOf(nf.format(searchId.getDouble(4)) + " ---- " + String.valueOf(nf.format(searchId.getDouble(5))));
						tempArray3[x] = displayItem;
						x = x + 1;
					}//end while
					lstDisplay.setListData(tempArray3);
				}//end try
				catch (Exception mnuEx3)
				{
					mnuEx3.printStackTrace();
				}//end catch
			}//end if

			// add.newtype menu item
			if (m.getSource() == mnuAddType)
			{
				try
				{	
					// create sql (with user input) & execute query
					String addTypeInput = JOptionPane.showInputDialog("Please enter a toy type you wish to add.");
					String sqlAddType = "INSERT INTO toy_type (type_name) VALUES ('" + addTypeInput + "');";
					System.out.println(sqlAddType);
					stat.executeUpdate(sqlAddType);
					JOptionPane.showMessageDialog(null, "New Toy Type Added" , "Creation Successful", JOptionPane.INFORMATION_MESSAGE);
				}//end try
				catch (Exception mnuEx3)
				{
					mnuEx3.printStackTrace();
				}//end catch
			}//end if

			// add.newtype menu item
			if (m.getSource() == mnuAddSeries)
			{
				try
				{	
					// create sql (with user input) & execute query
					String addSeriesInput = JOptionPane.showInputDialog("Please enter a toy series you wish to add.");
					String sqlAddSeries = "INSERT INTO series (series_description) VALUES ('" + addSeriesInput + "');";
					System.out.println(sqlAddSeries);
					stat.executeUpdate(sqlAddSeries);
					JOptionPane.showMessageDialog(null, "New Toy Series Added" , "Creation Successful", JOptionPane.INFORMATION_MESSAGE);
				}//end try
				catch (Exception mnuEx3)
				{
					mnuEx3.printStackTrace();
				}//end catch
			}//end if


			// add.newtype menu item
			if (m.getSource() == mnuViewTotalProfit)
			{

				double origPrice;
				double currentPrice;
				double allProfit=0;
				String strProfit;

				//set up number formatting
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setGroupingUsed(false);
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);

				try
				{	
					// create sql & execute query
					String sqlFindProfit = "SELECT release_price, current_price FROM toy";
					findProfit=stat.executeQuery(sqlFindProfit);
					while (findProfit.next())
					{
						origPrice = findProfit.getDouble(1);
						currentPrice = findProfit.getDouble(2);
						allProfit = allProfit + (currentPrice - origPrice);
					}
					strProfit = String.valueOf(nf.format(allProfit));
					String profitMsg = "Total Profits: $ " + strProfit;
					JOptionPane.showMessageDialog(null, profitMsg, "Total Profit", JOptionPane.INFORMATION_MESSAGE);

				}//end try
				catch (Exception mnuEx4)
				{
					mnuEx4.printStackTrace();
				}//end catch
			}//end if

			if (m.getSource() == mnuExit)
			{
				//exit program
				System.exit(0);
			}
		}//end actionPerformed
	}//end MenuHandler class

	// controls all command buttons
	private static class dbHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				//nav-open button
				if (e.getSource() == cmdOpen)
				{
					//execute query
					toyRecords = stat.executeQuery("SELECT t.toy_id, tt.type_id,  t.name, s.series_id, t.release_year, t.toy_cond, t.comments, t.release_price, t.current_price FROM toy t, series s, toy_type tt WHERE t.type_id = tt.type_id AND t.series_id = s.series_id ORDER BY t.toy_id WHERE ");															

					if (toyRecords.next())
					{
						// displays first record and enables buttons
						textDisplayRecord(toyRecords);

						cmdOpen.setEnabled(false);
						cmdFirst.setEnabled(true);
						cmdLast.setEnabled(true);
						cmdNext.setEnabled(true);
						cmdPrev.setEnabled(true);
						cmdAdd.setEnabled(true);
						cmdRemove.setEnabled(true);
						cmdEdit.setEnabled(true);

						mnuAddType.setEnabled(true);
						mnuAddSeries.setEnabled(true);
						mnuViewTotalProfit.setEnabled(true);
						mnuSearchName.setEnabled(true);
						mnuSearchId.setEnabled(true);
						mnuSearchType.setEnabled(true);
					}
				}
		
				// mod-add button
				else if (e.getSource() == cmdAdd)
				{
					Statement statAdd = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					
					//user - friendly button hide/show
					DisableNav();
					cmdEdit.setEnabled(false);
					cmdRemove.setEnabled(false);
					cmdCancel.setEnabled(true);
					// enable input in textboxes
					EnableInput();
					
					// only if button displays "save"
					if (cmdAdd.getText() == "Save" )
					{
						try
						{
							int typeIndex = cmbType.getSelectedIndex();
							int condIndex = cmbCondition.getSelectedIndex();
							int seriesId = cmbSeries.getSelectedIndex();
							String name = txtName.getText();
							String OrigPrice = txtOrigPrice.getText();
							String CurrentPrice = txtCurrentPrice.getText();
							String sqlAdd = "INSERT into toy (type_id, name, series_id, release_year, toy_cond, comments, release_price, current_price) VALUES ('" + typeIndex + "', '" +  name + "', '" +  seriesId + "', '" +  txtYear.getText() + "','" + condIndex  + "', '" +  txtComments.getText() + "'," + OrigPrice + ", " +  CurrentPrice + ");";
							statAdd.executeUpdate(sqlAdd);
							JOptionPane.showMessageDialog(null, "New Entry Added" , "Creation Successful", JOptionPane.INFORMATION_MESSAGE);
							DisableInput();
							EnableNav();
							EnableMod();
							cmdCancel.setEnabled(false);
							textDisplayRecord(toyRecords);
							cmdAdd.setText("Add");
							cmdAdd.setMnemonic('A');
							textDisplayRecord(toyRecords);
						}
						catch (Exception b)
						{
							b.printStackTrace();
						}
					}
					else 
					{	
						//change button text
						AddSaveChange();
						// clear input textboxes
						txtId.setText("");
						txtName.setText("");
						txtYear.setText("");
						txtComments.setText("");
						txtOrigPrice.setText("");
						txtCurrentPrice.setText("");
						txtProfit.setText("");
					}
				}
		
				// mod-edit button
				else if (e.getSource() == cmdEdit)
				{
					Statement statEdit = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
					//user-friendly button hide/show
					DisableNav();
					cmdAdd.setEnabled(false);
					cmdRemove.setEnabled(false);
					cmdCancel.setEnabled(true);
					// enable input in textboxes
					EnableInput();
					
					if (cmdEdit.getText() == "Save" )
					{
						try
						{
							EnableInput();
							DisableMod();
							DisableNav();

							int typeIndex = cmbType.getSelectedIndex();
							int condIndex = cmbCondition.getSelectedIndex();
							int seriesIndex = cmbSeries.getSelectedIndex();
							String currentId = txtId.getText();
							String name = txtName.getText();
							String OrigPrice = txtOrigPrice.getText();
							String CurrentPrice = txtCurrentPrice.getText();
							String sqlEdit = "UPDATE toy SET type_id = " +  typeIndex + ", name = '" + name + "', series_id = " + seriesIndex + ", release_year = '" + txtYear.getText() + "', toy_cond = " + condIndex + ", comments = '" + txtComments.getText() + "', release_price = " + OrigPrice + ", current_price = " + CurrentPrice + " WHERE toy_id = " + currentId + ";";
							statEdit.executeUpdate(sqlEdit);
							JOptionPane.showMessageDialog(null, "Record Modification Applied", "Alteration Successful", JOptionPane.INFORMATION_MESSAGE);
							EnableMod();
							cmdCancel.setEnabled(false);
							DisableInput();
							EnableNav();
							textDisplayRecord(toyRecords);
							cmdEdit.setText("Edit");
							cmdAdd.setMnemonic('E');
						}
						catch (Exception g)
						{
							g.printStackTrace();
						}
					}
					else 
					{	
						EditSaveChange();
					}
				}

				// mod-remove button
				else if (e.getSource() == cmdRemove)
				{
					try
					{
						int UserConfirm = JOptionPane.showConfirmDialog(null, "Remove Displayed Record?", "Confirm Record Deletion", JOptionPane.WARNING_MESSAGE);
						if (UserConfirm == JOptionPane.YES_OPTION)
						{
							String currentId = txtId.getText();
							String sqlDelete = "Delete FROM toy WHERE toy_id = " + currentId + ";";
							System.out.println(sqlDelete);
							stat.executeUpdate(sqlDelete);
							JOptionPane.showMessageDialog(null, "Record Deletion Applied", "Record Successfully Deleted", JOptionPane.OK_OPTION);
							EnableMod();
							EnableNav();
							cmdCancel.setEnabled(false);
						}
					}
					catch (Exception f)
					{
						f.printStackTrace();
					}
				}

				// mod-cancel button
				else if (e.getSource() == cmdCancel)
				{
					try
					{
						int UserConfirm = JOptionPane.showConfirmDialog(null, "Cancel Input?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
						if (UserConfirm == JOptionPane.YES_OPTION)
						{
							// destroy values in textboxes and enable mod&nav
							DisableInput();
							EnableMod();
							EnableNav();
							// reset add& edit buttons and refresh db query
							cmdAdd.setText("Add");
							cmdAdd.setMnemonic('A');
							cmdEdit.setText("Edit");
							cmdEdit.setMnemonic('E');
							textDisplayRecord(toyRecords);
							cmdCancel.setEnabled(false);
						}
					}
					catch(Exception cc)
					{
						cc.printStackTrace();
					}
				}

				//nav-first button
				else if (e.getSource() == cmdFirst)
				{
					toyRecords.first();
					textDisplayRecord(toyRecords);
				}
				//nav-last button
				else if (e.getSource() == cmdLast)
				{
					toyRecords.last();
					textDisplayRecord(toyRecords);
				}
				//nav-next button
				else if (e.getSource() == cmdNext)
				{
					if (!toyRecords.isLast())
					{
						toyRecords.next();
						textDisplayRecord(toyRecords);
					}
				}
				//nav-prev button
				else if (e.getSource() == cmdPrev)
				{
					if (!toyRecords.isFirst())
					{
						toyRecords.previous();
						textDisplayRecord(toyRecords);
					}
				}
			}// end try
			catch (Exception dbEx)
			{
				dbEx.printStackTrace();
			} // end catch
		}
	}
}
