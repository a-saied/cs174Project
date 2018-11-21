import java.io.*;
import javafx.*;
import java.util.*;


public class MainApp extends Application { 

	Stage window;

	Scene openingScene, buttonScene1, buttonScene2;

	public static void main(String[] args){
		launch(args);
	}

	@Override 
	public void start(Stage s){
		s.setTitle("Banking App");

		Label welcome = new Label("Welcome to the Banking App");
		Label createdBy = new Label("Created by Ahmed Saied and Lindsey Nguyen");


		Button atmButton = new Button("I'm an ATM user");
		Button tellerButton = new Buton("I'm a bank teller");

		StackPane layout = new StackPane();

		layout.getChildren().addAll(welcome, createdBy, atmButton, tellerButton);

		openingScene = new Scene(layout);

		s.setScene(openingScene);

	}

}