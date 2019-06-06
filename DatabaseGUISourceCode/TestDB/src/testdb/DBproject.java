/*
 * Partners:
 * Rogelio Macedo SID: 862018331
 * Alexander Yee  SID: 862059658
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 * To run in terminal: source ./run.sh rmace001_DB 9559 rmace001
 */
package testdb;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");

			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}

	public Connection getMyConnection(){
		try{
			return this._connection;
		}catch(Exception e){
			System.out.println("Connection has not been set and thus cannot be accessed.");
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 *
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 *
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 *
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 * obtains the metadata object for the returned result set.  The metadata
		 * contains row and column info.
		*/
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;

		//iterates through the result set and saves the data returned by the query.
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>();
		while (rs.next()){
			List<String> record = new ArrayList<String>();
			for (int i=1; i<=numCol; ++i)
				record.add(rs.getString (i));
			result.add(record);
		}//end while
		stmt.close ();
		return result;
	}//end executeQueryAndReturnResult

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 *
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		while(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
        
        public ResultSet execute (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		return rs;
	}

	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current
	 * value of sequence used for autogenerated keys
	 *
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */

	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();

		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 *
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
	  //**********Q: Do we create triggers for our database
	  //*************in the main function?
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if

		DBproject esql = null;

		try{
			System.out.println("(1)");

			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}

			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];

			esql = new DBproject (dbname, dbport, user, "");

			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Plane");
				System.out.println("2. Add Pilot");
				System.out.println("3. Add Flight");
				System.out.println("4. Add Technician");
				System.out.println("5. Book Flight");
				System.out.println("6. List number of available seats for a given flight.");
				System.out.println("7. List total number of repairs per plane in descending order");
				System.out.println("8. List total number of repairs per year in ascending order");
				System.out.println("9. Find total number of passengers with a given status");
				System.out.println("10. < EXIT");

				switch (readChoice()){
					case 1: AddPlane(esql); break;
					case 2: AddPilot(esql); break;
					case 3: AddFlight(esql); break;
					case 4: AddTechnician(esql); break;
					case 5: BookFlight(esql); break;
					case 6: ListNumberOfAvailableSeats(esql); break;
					case 7: ListsTotalNumberOfRepairsPerPlane(esql); break;
					case 8: ListTotalNumberOfRepairsPerYear(esql); break;
					case 9: FindPassengersCountWithStatus(esql); break;
					case 10: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		// add condition to check if input is between 1 and 10 inclusive
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				if (input < 1 || input > 10){
					throw new RuntimeException("Please enter a valid choice number...");
				}
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static int readStatusChoice() {
		int input;
		// returns only if a correct value is given.
		// add condition to check if input is between 1 and 10 inclusive
		do {
			System.out.println("Please enter the status in the correct format: ");
			System.out.println("\t For \'Waitlisted\': enter 1");
			System.out.println("\t For \'Confirmed\':  enter 2");
			System.out.println("\t For \'Reserved\':   enter 3");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				if (input < 1 || input > 3){
					throw new RuntimeException("Please enter a valid choice number...");
				}
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddPlane(DBproject esql) {//1
		//when you create triggers.sql file, add to createPostgresql.sh file as another psql command

		System.out.println("Please enter the following information\n");

        // Should the user input the id for a plane? Or should this be autogenerated and maintained with java?
		// Waiting on TA response, but for now just prompt user for ID
		int plane_id;
		int age, seats;
		String make, model;

		//******************************************get make from user
		do {
			System.out.print("\t    Enter a make: ");
			try { // read the integer, parse it and break.
				make = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//*****************************************get model from user
		do {
			System.out.print("\t   Enter a model: ");
			try { // read the integer, parse it and break.
				model = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//*******************************************get age from user
		do {
			System.out.print("\t    Enter an age: ");
			try { // read the integer, parse it and break.
				age =  Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//*****************************************get seats from user
		do {
			System.out.print("\t     Enter seats: ");
			try { // read the integer, parse it and break.
				seats =  Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

    //String pilot_query = "INSERT INTO Pilot VALUES(nextval(\'pilot_id_seq\')"  + ", \'" + pilot_name + "\', \'" + pilot_nationality +  "\')";
		String SQL = "INSERT INTO Plane VALUES(nextval(\'plane_id_seq\')"  + ", \'" + make + "\', \'" + model + "\', " + String.valueOf(age) + ", " + String.valueOf(seats) + ")";
		try{
			esql.executeUpdate(SQL);
		} catch(SQLException e){
			System.err.println (e.getMessage());
		}
		// System.out.println(SQL);
	}

	public static void AddPilot(DBproject esql) {//2
		//ID, Fullname, nationality
		int pilot_id;
		String pilot_name, pilot_nationality;

		do {
			System.out.print("\tEnter a Pilot's Full Name: ");
			try { // read the integer, parse it and break.
				pilot_name =  in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter Pilot's Nationality: ");
			try { // read the integer, parse it and break.
				pilot_nationality =  in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//Example : String technician_query = "INSERT INTO Technician VALUES(nextval(\'tech_id_seq\')" + ", \'" + tech_name  +  "\')";
		String pilot_query = "INSERT INTO Pilot VALUES(nextval(\'pilot_id_seq\')"  + ", \'" + pilot_name + "\', \'" + pilot_nationality +  "\')";

    //    	System.out.print(pilot_query);
		try{
			esql.executeUpdate(pilot_query);

		} catch(SQLException e){

			System.err.println (e.getMessage());

		}

	}

	public static void AddFlight(DBproject esql) {//3
		// Given a pilot, plane and flight, adds a flight in the DB
		//prompt the user for a pilot name/id, plane, and flight
		int pilotID = -1;
		int planeID = -1;
		int num_sold = 0;
		double cost = 0;
		int num_stops = 0;
		String actual_arrival_date;
		String actual_departure_date;
		String arrival_airport, departure_airport;

		//Get pilotID and verify exists
		int row_count = 0;
		while(row_count <= 0){
			String pilotID_query = "SELECT * FROM Pilot WHERE id = ";
			do{
				try{
					System.out.print("\tEnter an existing Pilot ID to add to flight: ");
					pilotID  = Integer.parseInt(in.readLine());
					pilotID_query += String.valueOf(pilotID);
					//Check concatenated query
					//System.out.println(p_query);
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				//Since pilot ID is unique from sequence, row_count should always = 1 when exists
				row_count = esql.executeQuery(pilotID_query);
				//Check row_count value
				//System.out.println(row_count);
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
			if (row_count <= 0){
				System.out.println("Your entered pilot does not exist!");
			}

		}
		//Get planeID and verify exists
		//Reset row_count
		row_count = 0;
		while(row_count <= 0){
			String planeID_query = "SELECT * FROM Plane WHERE id = ";
			do{
				try{
					System.out.print("\tEnter an existing Plane ID to add to flight: ");
					planeID  = Integer.parseInt(in.readLine());
					planeID_query += String.valueOf(planeID);
					//Check concatenated query
					System.out.println(planeID_query);
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				row_count = esql.executeQuery(planeID_query);
				//Check row_count value
				System.out.println(row_count);
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
			if (row_count <= 0){
				System.out.println("Your entered plane does not exist!");
			}
		}

		//Get Remaining info for flights
		do {
			System.out.print("\tEnter a cost for the flight: ");
			try { // read the integer, parse it and break.
				cost = Double.parseDouble(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter the number of stops: ");
			try { // read the integer, parse it and break.
				num_stops = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter departure date(Accepted formats YYYY/MM/DD,YYYY-MM-DD, YYYYMMDD):");
			try { // read the integer, parse it and break.
				actual_departure_date = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter arrival date(Accepted formats YYYY/MM/DD,YYYY-MM-DD, YYYYMMDD):");
			try { // read the integer, parse it and break.
				actual_arrival_date = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter departure airport: ");
			try { // read the integer, parse it and break.
				departure_airport = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		do {
			System.out.print("\tEnter arrival airport: ");
			try { // read the integer, parse it and break.
				arrival_airport = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

	//Set Query
	String addFlight_query = "INSERT INTO Flight VALUES(nextval(\'fnum_seq\')" + "," + String.valueOf(cost);
	addFlight_query += "," + String.valueOf(num_sold) + "," + String.valueOf(num_stops) + ",\'" + actual_departure_date + "\'::date,\'" + actual_arrival_date + "\'::date,\'"  +  arrival_airport + "\', \'" + departure_airport + "\')";

	System.out.println(addFlight_query);
	try{
		esql.executeUpdate(addFlight_query);

	} catch(SQLException e){

		System.err.println (e.getMessage());

	}

	String addFlightInfo_query = "INSERT INTO FlightInfo VALUES(nextval(\'fiid_seq\'), currval(\'fnum_seq\')" + "," + String.valueOf(pilotID) + "," + String.valueOf(planeID) + ")";
	System.out.println(addFlightInfo_query);
	try{
		esql.executeUpdate(addFlightInfo_query);

	} catch(SQLException e){

		System.err.println (e.getMessage());

	}
}

	public static void AddTechnician(DBproject esql) {//4
		//Technician Needs id, Fullname
		int tech_id;
		String tech_name;

		do {
			System.out.print("\tEnter a Technician's Full Name: ");
			try { // read the integer, parse it and break.
				tech_name = in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//Set Query
		String technician_query = "INSERT INTO Technician VALUES(nextval(\'tech_id_seq\')" + ", \'" + tech_name  +  "\')";
		System.out.print(technician_query);
			try{
				esql.executeUpdate(technician_query);

			} catch(SQLException e){

				System.err.println (e.getMessage());

			}

	}

	public static void BookFlight(DBproject esql) {//5
		// Given a customer and a flight that he/she wants to book, add a reservation to the DB
		// Prompt the user for the info
		// UPDATE statement should go here
		// we need to query the database to check if the customerID exists
		// use rowCount to check if not == 0
		int fID = -1;
		int custID = -1;
		int row_count = 0;
		int seats_avail = 0;
		String status = "";

		while(row_count <= 0){
			String custID_query = "SELECT * FROM Customer WHERE id = ";
			do{
				try{
					System.out.print("\tEnter a Customer ID: ");
					custID  = Integer.parseInt(in.readLine());
					custID_query += String.valueOf(custID);
					//Check concatenated query
					//System.out.println(p_query);2
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				//Since pilot ID is unique from sequence, row_count should always = 1 when exists
				row_count = esql.executeQuery(custID_query);
				//Check row_count value
				//System.out.println(row_count);
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
			if (row_count <= 0){
				System.out.println("Your entered customer does not exist!");
			}
		}

		//Probably need to check available seats here
		//If flight exists and there are seats remaining then just insert as normal
		//After insert to reservation, need to update Flight. num_sold + 1?
		//Need to set the status
		//Determine if there are seats left, if not add to waitlist?

		//Check Flight exists
		row_count = 0;
		while(row_count <= 0){
			String fID_query = "SELECT * FROM Flight WHERE fnum = ";
			do{
				try{
					System.out.print("\tEnter a Flight Number: ");
					fID = Integer.parseInt(in.readLine());
					fID_query += String.valueOf(fID);
					//Check concatenated query
					//System.out.println(fID_query);
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				row_count = esql.executeQuery(fID_query);
				//Check row_count value
				//System.out.println(row_count);
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
			if (row_count <= 0){
				System.out.println("Your entered flight does not exist!");
			}
		}
		//Check available Seats
		String SQL = "SELECT foo.seats - foo.num_sold FROM (SELECT P.seats, F.num_sold FROM Plane P, Flight F, FlightInfo FI WHERE F.fnum = " + String.valueOf(fID) + " AND FI.flight_id = F.fnum AND P.id = FI.plane_id) as foo";

		try{
			String val = "";
			List<List<String>> tmp = esql.executeQueryAndReturnResult(SQL);

			for (int i = 0; i < tmp.size(); i++){
				for (int j = 0; j < tmp.get(i).size(); j++){
					val = tmp.get(i).get(j);
				}
			}
			//System.out.println("Seats Available for flight " + String.valueOf(fID) + ": " + val );
			seats_avail = Integer.parseInt(val);
		}catch (SQLException e){
			System.err.println (e.getMessage());
		}

		//Check if customer already has a reservation on a flight
		// row_count = 1;
		// while(row_count > 0){
		// 	String checkRes_query = "SELECT * FROM Reservation WHERE cid = " + String.valueOf(custID) + "AND fid = " + String.valueOf(fID);
		// 		try{
		// 			row_count = esql.executeQuery(checkRes_query);
		// 			//Check row_count value
		// 			//System.out.println(row_count);
		// 		} catch(SQLException e){
		// 			System.err.println (e.getMessage());
		// 		}
		// 		if(row_count > 0){
		// 			System.out.println("Customer already has Reservation");
		//
		// 		}
		// }

		//WE now have num_seats remaining if num_seats = 0, start adding to waitlist instead
    //if num seats > 0 add with confirmed status R, if num seats = 0 status = W

		//seats_avail = 0; -- Test plane full
		if(seats_avail > 0){
			status = "R";
		}
		else{
			status = "W";
		}

		//Set Query
		String BookFlight_query = "INSERT INTO Reservation VALUES(nextval(\'res_seq\')," +  String.valueOf(custID) + "," + String.valueOf(fID) + ",\'" + status + "\')";
		//System.out.println(BookFlight_query);
		try{
			esql.executeUpdate(BookFlight_query);

		} catch(SQLException e){

			System.err.println (e.getMessage());

		}

		if(status == "R"){
			String UpdateNumSold_query = "UPDATE Flight SET num_sold = num_sold + 1 WHERE fnum =" + String.valueOf(fID);
			//System.out.println(UpdateNumSold_query);
			try{
				esql.executeUpdate(UpdateNumSold_query);

			} catch(SQLException e){

				System.err.println (e.getMessage());

			}
		}
	}

	public static void ListNumberOfAvailableSeats(DBproject esql) {//6
		// For flight number and date, find the number of availalbe seats (i.e. total plane capacity minus booked seats )
		// SELECT query statement should go here
		int flightNum = 0;
		String date;
		System.out.println("Please enter the following:");
		// prompt user for flight num

		int row_count = 0;
		while(row_count <= 0){
			String fID_query = "SELECT * FROM Flight WHERE fnum = ";
			do{
				try{
					System.out.print("\t     flight number: ");
					flightNum = Integer.parseInt(in.readLine());
					fID_query += String.valueOf(flightNum);
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				row_count = esql.executeQuery(fID_query);
				//Check row_count value
				//System.out.println(row_count);
				if (row_count <= 0){
					System.out.println("Your entered flight does not exist!");
				}
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
		}

		// prompt user for date
		do {
			System.out.print("\t       flight date: ");
			try { // read the integer, parse it and break.
				date =  in.readLine();
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);

		//execute query and get list as return value
		//most likley would have to iterate over the list to display each value
		String SQL = "SELECT foo.seats - foo.num_sold FROM (SELECT P.seats, F.num_sold FROM Plane P, Flight F, FlightInfo FI WHERE F.fnum = " + String.valueOf(flightNum) + " AND FI.flight_id = F.fnum AND P.id = FI.plane_id) as foo";

		try{
			String val = "";
			List<List<String>> tmp = esql.executeQueryAndReturnResult(SQL);

			for (int i = 0; i < tmp.size(); i++){
				for (int j = 0; j < tmp.get(i).size(); j++){
					val = tmp.get(i).get(j);
				}
			}
			System.out.println("Seats Available for flight " + String.valueOf(flightNum) + ": " + tmp.get(0).get(0));
		}catch (SQLException e){
			System.err.println (e.getMessage());
		}

	}

	public static void ListsTotalNumberOfRepairsPerPlane(DBproject esql) {//7
		// Count number of repairs per planes and list them in descending order
		// SELECT query statement that returns the number of rows or something should go here
		// for both we will need ORDER BY
		// group by plane id and count (repair id)
		String SQL = "SELECT R.plane_id, COUNT(R.rid) FROM Repairs R GROUP BY R.plane_id ORDER BY COUNT(R.rid) DESC";
		System.out.println("");
		try{
			int rows = esql.executeQueryAndPrintResult(SQL);
		}catch (SQLException e){
			System.err.println (e.getMessage());
		}
		System.out.println("");

	}

	public static void ListTotalNumberOfRepairsPerYear(DBproject esql) {//8
		// Count repairs per year and list them in ascending order
		// SELECT query statement that returns the number or rows or something should go here
		// group by year and count someting
		// for both we will need ORDER BY
		// select extract year from date?
		String SQL = "SELECT EXTRACT(YEAR FROM R.repair_date), COUNT(R.rid) FROM Repairs R GROUP BY EXTRACT(YEAR FROM R.repair_date) ORDER BY COUNT(R.rid) ASC";
		System.out.println("");
		try{
			int rows = esql.executeQueryAndPrintResult(SQL);
		}catch (SQLException e){
			System.err.println (e.getMessage());
		}
		System.out.println("");

	}

	public static void FindPassengersCountWithStatus(DBproject esql) {//9
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number
		String status = "";
		switch(readStatusChoice()){
			case 1: status = "W"; break;
			case 2: status = "C"; break;
			case 3: status = "R"; break;
		}
		System.out.println("Status chosen = " + status);
		int row_count = 0;
		int flightNum = 0;
		while(row_count <= 0){
			String fID_query = "SELECT * FROM Flight WHERE fnum = ";
			do{
				try{
					System.out.print("Enter a flight number: ");
					flightNum = Integer.parseInt(in.readLine());
					fID_query += String.valueOf(flightNum);
					break;
				}catch (Exception e){
					System.out.println("Your input is invalid!");
					continue;
					}
			}while(true);
			try{
				row_count = esql.executeQuery(fID_query);
				//Check row_count value
				//System.out.println(row_count);
				if (row_count <= 0){
					System.out.println("Your entered flight does not exist!");
				}
			} catch(SQLException e){
				System.err.println (e.getMessage());
			}
		}
		int rows = 0;
		String SQL = "SELECT * FROM Reservation R, Flight F WHERE F.fnum = "+ String.valueOf(flightNum) +" AND R.status = \'" + status + "\' AND R.fid = F.fnum";
		try{
			rows = esql.executeQuery(SQL);
			System.out.println("Total Passengers with selected status: " + String.valueOf(rows));
		}catch (SQLException e){
			System.err.println (e.getMessage());
		}
		System.out.println("");
	}
}
