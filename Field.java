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
	// Флаг приостановленности движения
	private boolean paused;
	// Динамический список скачущих мячей
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
	private LinkedList<BrickClass> bricks = new LinkedList<BrickClass>();
	// Класс таймер отвечает за регулярную генерацию событий ActionEvent
	// При создании его экземпляра используется анонимный класс,
	// реализующий интерфейс ActionListener
	private int num = 0;
	private Timer repaintTimer = new Timer(10, new ActionListener(){
	public void actionPerformed(ActionEvent ev){
		// Задача обработчика события ActionEvent - перерисовка окна
		repaint();
		}
	});
	private static final int BRICKS_IN_WIDTH = 3;
	private static final int BRICKS_IN_HEIGHT = 1; //колическтво кирпичей по длине и ширине
	// Конструктор класса BouncingBall
	public Field() {
		// Установить цвет заднего фона белым
		setBackground(Color.WHITE);
		// Запустить таймер
		repaintTimer.start();
	}
	// Унаследованный от JPanel метод перерисовки компонента
	public void paintComponent(Graphics g){
		// Вызвать версию метода, унаследованную от предка
		super.paintComponent(g);
		Graphics2D canvas = (Graphics2D) g;
		// Последовательно запросить прорисовку от всех мячей из списка
		
		for (BrickClass brick: bricks){
			brick.paint(canvas);
		}
		for (BouncingBall ball: balls){
			ball.paint(canvas);
		}
	}
	// Метод добавления нового мяча в список
	public void addBall(){
		//Заключается в добавлении в список нового экземпляра BouncingBall
		// Всю инициализацию положения, скорости, размера, цвета
		// BouncingBall выполняет сам в конструкторе
		balls.add(new BouncingBall(this, bricks));
	}
	public void addBrick(){
		for(int i=0; i<BRICKS_IN_WIDTH; i++){
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				//System.out.println('+');
				bricks.add(new BrickClass(this, i, j));
				
			}
		}
		
	}
	// Метод синхронизированный, т.е. только один поток может
	// одновременно быть внутри
	public synchronized void pause(){
		// Включить режим паузы
		paused = true;
		System.out.println(num);
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
