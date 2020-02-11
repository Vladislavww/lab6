package task6;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
@SuppressWarnings("serial")

public class MainFrame extends JFrame{
	// Константы, задающие размер окна приложения, если оно
	// не распахнуто на весь экран
	private static final int WIDTH = 800;
	private static final int HEIGHT = 860;
	private JMenuItem addBallMenuItem;
	private JMenuItem pauseMenuItem;
	private JMenuItem resumeMenuItem;
	private JMenuItem brickMenuItem;
	private final int MAX_BALLS = 100; //Несмотря на задание, это ограничение можно безболезненно снять
	private int balls_number = 0;
	// Поле, по которому прыгают мячи
	private Field field = new Field();

	public MainFrame(){
		super("Программирование и синхронизация потоков");
		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		// Отцентрировать окно приложения на экране
		setLocation((kit.getScreenSize().width - WIDTH)/2,
		(kit.getScreenSize().height - HEIGHT)/2);
		// Установить начальное состояние окна разв?рнутым на весь экран
		//setExtendedState(MAXIMIZED_BOTH);
		// Создать меню
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu ballMenu = new JMenu("Мячи");
		Action addBallAction = new AbstractAction("Добавить мяч"){
			public void actionPerformed(ActionEvent event){
				if(balls_number < MAX_BALLS){
					field.addBall();
					balls_number += 1;
					if (!pauseMenuItem.isEnabled() &&!resumeMenuItem.isEnabled()){
						// Ни один из пунктов меню не являются
						// доступными - сделать доступным "Паузу"
						pauseMenuItem.setEnabled(true);
						brickMenuItem.setEnabled(false); //первый шар запустили - поздно ставить препятствия
					}
				}
				if(balls_number >= MAX_BALLS){
					addBallMenuItem.setEnabled(false);
				}
			}
		};
		addBallMenuItem = ballMenu.add(addBallAction);
		menuBar.add(ballMenu);
		
		JMenu controlMenu = new JMenu("Управление");
		menuBar.add(controlMenu);
		Action pauseAction = new AbstractAction("Приостановить движение"){
			public void actionPerformed(ActionEvent event){
				field.pause();
				pauseMenuItem.setEnabled(false);
				resumeMenuItem.setEnabled(true);
			}
		};
		pauseMenuItem = controlMenu.add(pauseAction);
		pauseMenuItem.setEnabled(false);
		Action resumeAction = new AbstractAction("Возобновить движение"){
			public void actionPerformed(ActionEvent event){
				field.resume();
				pauseMenuItem.setEnabled(true);
				resumeMenuItem.setEnabled(false);
			}
		};
		resumeMenuItem = controlMenu.add(resumeAction);
		resumeMenuItem.setEnabled(false);
		
		JMenu brickMenu = new JMenu("Кирпичи");
		menuBar.add(brickMenu);
		Action addBricksAction = new AbstractAction("Добавить кирпичи(сплошная стена)"){
			public void actionPerformed(ActionEvent event){
				field.addBrick();
				brickMenuItem.setEnabled(false); //только один раз ставлю кипрпичи
			}
		};
		brickMenuItem = brickMenu.add(addBricksAction);
		
		// Добавить в центр граничной компоновки поле Field
		getContentPane().add(field, BorderLayout.CENTER);
		
	}

	public static void main(String[] args){
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}