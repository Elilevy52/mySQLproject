package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface MainMethods {

	void mangerMethods() throws ClassNotFoundException, SQLException;

	void mainMenu() throws FileNotFoundException, IOException, ClassNotFoundException;

	boolean importQuestions() throws ClassNotFoundException, IOException;
}
