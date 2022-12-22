package model;

import java.sql.*;
import java.util.Scanner;

public class DatabaseSQL {

	private Scanner input;
	private int innerOP;

	private ResultSet resultSet;
	private Connection connect;
	private Statement state;
	private PreparedStatement preState;

	public DatabaseSQL() throws ClassNotFoundException, SQLException {
		input = new Scanner(System.in);
		this.innerOP = 0;
		resultSet = null;
		connect = null;
		connectToSql();

	}

	public void connectToSql() throws SQLException, ClassNotFoundException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dbUrl = "jdbc:mysql://localhost:3306/questions";

			connect = DriverManager.getConnection(dbUrl, "root", "1234");

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: " + ex.getMessage());
				ex = ex.getNextException();
			}
		}
		if (connect.isValid(1) == true) {
			System.out.println("Connected to SQL database.");
			createStatement();
			dropOldTables();
			createNewTable();
		} else {
			System.out.println("Fild to connect.");
		}
	}

	public void createStatement() {
		try {
			state = connect.createStatement();
		} catch (SQLException e) {
			System.out.println("SQL exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void createNewTable() throws SQLException {
		preState = connect.prepareStatement("use question");
		preState.execute();

		// create OpenQustion table and 3 Columns: (1)questionID, (2)question, (3)answer
		// (pk_QuestionID)
		preState = connect.prepareStatement(
						"create table OpenQuestions ("
						+ "questionID INT NOT NULL auto_increment, "
						+ "question VARCHAR(100),"
						+ "answer VARCHAR(100), " 
						+ "primary key (questionID))"
						+ "ENGINE = InnoDB;");
		preState.execute();

		// create MultipleChoiceQuestion table and 3 Columns: (1)questionID,
		// (2)answerID, (3)question (pk_questionID)
		preState = connect.prepareStatement(
						"create table MultipleChoiceQuestion("
						+ "questionID INT NOT NULL,"
						+ "answerID INT NOT NULL,"
						+ "question VARCHAR(100),"
						+ "primary key (questionID)"
						+ "ENGINE = InnoDB;");
		preState.execute();

		// create MultipleChoiceAnswers table and 3 columns: (1)answerID, (2)questionID,
		// (3)answer (fk_questionID, pk_answersID)
		preState = connect.prepareStatement(
				"create table MultipleChoiceAnswers("
				+ "answerID INT NOT NULL auto_increment,"
				+ "questionID INT NOT NULL,"
				+ "answer VARCHAR(100),"
				+ "foreign key (questionID) references MultipleChoiceQuestion(questionID),"
				+ "primary key (answerID))"
				+ "ENGINE + InnoDB;");
		preState.execute();

		// create exams table and 3 columns: (1)examID, (2)MultipleChoiceQuestionID,
		// (3)openQuestionID (fk_MultipleChoiceQuestionID, fk_openQuestionID, pk_examID)
		preState = connect.prepareStatement(
				"create table exams("
				+ "examID INT NOT NULL auto_increment,"
				+ "MultipleChoiceQuestionID INT NOT NULL,"
				+ "openQustionID INT NOT NULL,"
				+ "foreign key (MultipleChoiceQuestionID) references MultipleChoiceQuestion(questionID),"
				+ "foreign key (openQuestionID) references OpenQuestions(questionID),"
				+ "primary key (examID))"
				+ "engine = InnoDB;");
		preState.execute();

	}

	private void dropOldTables() throws SQLException {
		preState = connect.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
		preState.execute();

		preState = connect.prepareStatement("drop table if exists OpenQuestions");
		preState.execute();

		preState = connect.prepareStatement("drop table if exists MultipleChoiceQuestion");
		preState.execute();

		preState = connect.prepareStatement("drop table if exists MultipleChoiceAnswers");
		preState.execute();

		preState = connect.prepareStatement("drop table if exists exams");
		preState.execute();
	}

	private void closeConnectionToDB(Connection connect) throws SQLException {
		System.out
				.println("Are you sure you want to close connection with the database?\n Enter '1' to close connection."
						+ "\n Enter '2' to stay connected to the database.");
		innerOP = input.nextInt();
		switch (innerOP) {
		case 1:
			connect.close();
			break;

		case 2:
			System.out.println("Connection to database is still available.");
			break;

		default:
			System.out.println("Please enter valid input");
		}
	}

	public void addMultipleChoiceQuestionToDB(MultilpeChoiceQuestion mcQuestion) throws SQLException {
		int multipleChoiceQuestionID = mcQuestion.getId();
		int multipleChoiceAnswerID = mcQuestion.getAnswerNumber();
		String multipleChoiceQuestion = mcQuestion.getQuestion();

		String query = "INSERT INTO MultilpeChoiceQuestion" + "(questionID, " + "answerID," + "question"
				+ "VALUES (?, ?, ?)";

		preState = connect.prepareStatement(query);
		preState.setInt(1, multipleChoiceQuestionID);
		preState.setInt(2, multipleChoiceAnswerID);
		preState.setString(3, multipleChoiceQuestion);
		preState.executeUpdate();
	}

	public void addMultipleChoiceAnswerToDB() {
		

	}

	public void addOpenQuestionToDB(OpenQuestion oQuestion) throws SQLException {
		int openQuestionID = oQuestion.getId();
		String openQuestion = oQuestion.getOpenQuestion();
		String openAnswer = oQuestion.getOpenAnswer();
		
		String query = "INSERT INTO OpenQuestions (questionID, question, answer)" 
				+ "VALUES (?, ?, ?)";
		preState = connect.prepareStatement(query);
		preState.setInt(1, openQuestionID);
		preState.setString(2, openQuestion);
		preState.setString(3, openAnswer);
		preState.executeUpdate();

	}

	public void addOpenAnswerToDB() {

	}

}
