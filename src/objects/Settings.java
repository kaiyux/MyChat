package objects;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Settings extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Settings");// 设置窗体的标题

		GridPane grid = new GridPane();// 创建一个GridPane面板，并给其命名为grid。
		grid.setAlignment(Pos.CENTER);// 改变grid的默认位置，默认情况下，grid的位置是在其父容器的左上方，此处父容器是Scene，现在将grid移至Scene的中间。
		grid.setHgap(10);// 设置该网格每行和每列之间的水平间距和垂直间距。
		grid.setVgap(10);
		// 设置了环绕在该网格面板上的填充距离，这里网格默认被设为在场景容器中居中，这里的填充距离是表示网格边缘距离里面内容的距离。
		// 传入的是一个Insets对象，该insets对象的参数是：上，左，下，右。
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Account Info");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userID = new Label("User ID:");
		grid.add(userID, 0, 1);
		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);
		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		Label username = new Label("User Name:");
		grid.add(username, 0, 3);
		TextField usernameTextField = new TextField();
		grid.add(usernameTextField, 1, 3);

		Label introduce = new Label("Introduce:");
		grid.add(introduce, 0, 4);
		TextField introduceTextField = new TextField();
		grid.add(introduceTextField, 1, 4);

		Label gender = new Label("Gender:");
		grid.add(gender, 0, 5);
		ChoiceBox<String> cb = new ChoiceBox<String>(FXCollections.observableArrayList("Male", "Female"));
		grid.add(cb, 1, 5);

		Button btn = new Button("Save");
		HBox hbBtn = new HBox(10);// 创建了一个HBox布局面板，并初始放置在其中的每个子组件的水平间距为10像素
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);// 设置该面板的相对于父容器的位置为右下
		hbBtn.getChildren().addAll(btn);// 将按钮作为子元素添加至其中
		grid.add(hbBtn, 1, 8);

		Scene scene = new Scene(grid, 300, 400);// 为该舞台创建一个场景
		primaryStage.setScene(scene);// 将场景加入舞台中

		primaryStage.show();// 让窗体显示
		
		btn.setOnAction(new EventHandler<ActionEvent>() {
			// 保存按钮

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				primaryStage.close(); // 准备页面跳转
			}
		});
	}

}
