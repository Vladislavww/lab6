package task6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")

public class Field extends JPanel{
	private boolean paused;
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
	
	// Класс таймер отвечает за регулярную генерацию событий ActionEvent
	// При создании его экземпляра используется анонимный класс,
	// реализующий интерфейс ActionListener
	private int num = 0;
	private Timer repaintTimer = new Timer(1000, new ActionListener(){
	public void actionPerformed(ActionEvent ev){
		// Задача обработчика события ActionEvent - перерисовка окна
		repaint();
		}
	});
	private static final int BRICKS_IN_WIDTH = 20;
	private static final int BRICKS_IN_HEIGHT = 10; //колическтво кирпичей по длине и ширине
	private BrickClass[][] bricks = new BrickClass[BRICKS_IN_WIDTH][BRICKS_IN_HEIGHT];
	private boolean added_bricks = false;//флаг наличия кирпичей

	public Field() {
		setBackground(Color.WHITE);
		repaintTimer.start();
	}
	// Унаследованный от JPanel метод перерисовки компонента
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D canvas = (Graphics2D) g;
		//Прорисовать все кирпичи
		if(added_bricks){
			for(int i=0;i<BRICKS_IN_WIDTH; i++){
				for(int j=0; j<BRICKS_IN_HEIGHT; j++){
					bricks[i][j].paint(canvas);
				}
			}
		}
		// Последовательно запросить прорисовку от всех мячей из списка
		for (BouncingBall ball: balls){
			ball.paint(canvas);
		}
	}
	// Метод добавления нового мяча в список
	public void addBall(){
		//Заключается в добавлении в список нового экземпляра BouncingBall
		// Всю инициализацию положения, скорости, размера, цвета
		// BouncingBall выполняет сам в конструкторе
		if(added_bricks){
			balls.add(new BouncingBall(this, bricks, BRICKS_IN_WIDTH, BRICKS_IN_HEIGHT));
		}
		else{
			balls.add(new BouncingBall(this, bricks, -1, -1));
		}
	}
	
	//Метод создания объектов-кирпчей в матрице
	public void addBrick(){
		for(int i=0; i<BRICKS_IN_WIDTH; i++){
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				BrickClass brick = new BrickClass(this, i, j);
				bricks[i][j] = brick;
				
			}
		}
		added_bricks = true;
		
	}
	// Метод синхронизированный, т.е. только один поток может
	// одновременно быть внутри
	public synchronized void pause(){
		// Включить режим паузы
		paused = true;
	}
	// Метод синхронизированный, т.е. только один поток может
	// одновременно быть внутри
	public synchronized void resume(){
		// Выключить режим паузы
		paused = false;
		// Будим все ожидающие продолжения потоки
		notifyAll();
	}
	// Синхронизированный метод проверки, может ли мяч двигаться
	// (не включен ли режим паузы?)
	public synchronized void canMove(BouncingBall ball) throws InterruptedException{
		if (paused){
			// Если режим паузы включен, то поток, зашедший
			// внутрь данного метода, засыпает
			wait();
		}
	}
}
