package task6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

public class BouncingBall implements Runnable{
	// Максимальный радиус, который может иметь мяч
	private static final int MAX_RADIUS = 40;
	// Минимальный радиус, который может иметь мяч
	private static final int MIN_RADIUS = 3;
	// Максимальная скорость, с которой может летать мяч
	private static final int MAX_SPEED = 10;
	private static final int BRICKS_IN_WIDTH = 20;
	private static final int BRICKS_IN_HEIGHT = 4;
	private Field field;
	private int radius;
	private Color color;
	// Текущие координаты мяча
	private double x;
	private double y;
	// Вертикальная и горизонтальная компонента скорости
	private int speed;
	private double speedX;
	private double speedY;
	private BrickClass[][] bricks;
	// Конструктор класса BouncingBall
	public BouncingBall(Field field, BrickClass[][] bricks){
		// Необходимо иметь ссылку на поле, по которому прыгает мяч,
		// чтобы отслеживать выход за его пределы 
		// через getWidth(), getHeight()
		this.field = field;
		this.bricks = bricks;
		// Радиус мяча случайного размера
		radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
		// Абсолютное значение скорости зависит от диаметра мяча,
		// чем он больше, тем медленнее
		speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
		if (speed>MAX_SPEED){
			speed = MAX_SPEED;
		}
		// Начальное направление скорости тоже случайно,
		// угол в пределах от 0 до 2PI
		double angle = Math.random()*2*Math.PI;
		// Вычисляются горизонтальная и вертикальная компоненты скорости
		speedX = 3*Math.cos(angle);
		speedY = 3*Math.sin(angle);
		//speedX = -1;
		//speedY = -1;
		// Цвет мяча выбирается случайно
		color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		// Начальное положение мяча случайно
		do{
			x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
			y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
		}while(brick_collide(true)==true);
		//x = 100;
		//y = 100;
		// Созда?м новый экземпляр потока, передавая аргументом
		// ссылку на класс, реализующий Runnable (т.е. на себя)
		Thread thisThread = new Thread(this);
		// Запускаем поток
		thisThread.start();
	}
	
	// Метод run() исполняется внутри потока. Когда он завершает работу,
	// то завершается и поток
	public void run(){
		try {
			// Крутим бесконечный цикл, т.е. пока нас не прервут,
			// мы не намерены завершаться
			while(true){
				// Синхронизация потоков на самом объекте поля
				// Если движение разрешено - управление будет
				// возвращено в метод
				// В противном случае - активный поток засн?т
				field.canMove(this);
				if(window_collide()==false && brick_collide(false)==false){
					// Просто смещаемся
					x += speedX;
					y += speedY;
				}
				// Засыпаем на X миллисекунд, где X определяется
				// исходя из скорости
				// Скорость = 1 (медленно), засыпаем на 15 мс.
				// Скорость = 15 (быстро), засыпаем на 1 мс.
				Thread.sleep(16-speed);
			}
		} 
		catch (InterruptedException ex){
			// Если нас прервали, то ничего не делаем
			// и просто выходим (завершаемся)
		}
	}
	
	private boolean window_collide(){
		if (x + speedX <= radius){
			// Достигли левой стенки, отскакиваем право
			speedX = -speedX;
			x = radius;
		}
		else if (x + speedX >= field.getWidth() - radius){
			// Достигли правой стенки, отскок влево
			speedX = -speedX;
			x=new Double(field.getWidth()-radius).intValue();
		} 
		else if (y + speedY <= radius){
			// Достигли верхней стенки
			speedY = -speedY;
			y = radius;
		} 
		else if (y + speedY >= field.getHeight() - radius){
			// Достигли нижней стенки
			speedY = -speedY;
			y=new Double(field.getHeight()-radius).intValue();
		}
		else{
			return false;
		}
		return true;
	}
	
	private boolean brick_collide(boolean ball_creating){
		boolean collided = false;
		for(int i=0; i<BRICKS_IN_WIDTH; i++){
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				if(bricks[i][j].check_strikable()){
					double win_x0 = bricks[i][j].get_coordX(0);
					double win_x1 = bricks[i][j].get_coordX(1);
					double win_y0 = bricks[i][j].get_coordY(0);
					double win_y1 = bricks[i][j].get_coordY(1);
					double win_xc = (win_x0 + win_x1)/2;
					double win_yc = (win_y0 + win_y1)/2;
					double y0 = y + speedY + radius;
					double y1 = y + speedY - radius;
					double x0 = x + speedX + radius;
					double x1 = x + speedX - radius;
					if((x>=win_x0 && x<= win_x1)||(x>=win_x0 && x<= win_x1)){
						
						if(y0 >= win_y0 && y0 <= win_yc){
							speedY = -speedY;
							
							y=new Double(win_y0-radius).intValue();
							collided = true;
						}
						else if(y1 <= win_y1 && y1 >= win_yc){
							speedY = -speedY;
							
							y=new Double(win_y1+radius).intValue();
							collided = true;
						}
					}
					else if((y>=win_y0 && y<= win_y1)||(y>=win_y0 && y<= win_y1)){
						
						//System.out.println('y');
						if(x0 >= win_x0 && x0 <= win_xc){
							speedX = -speedX;
							x=new Double(win_x0-radius).intValue();
							collided = true;
						}
						else if(x1 <= win_x1 && x1 >= win_xc){
							speedX = -speedX;
							x=new Double(win_x1+radius).intValue();
							collided = true;
						}
					}
					else if(Math.sqrt(Math.pow(win_yc-y, 2)+Math.pow(win_xc-x, 2))<=(Math.sqrt(Math.pow(win_yc-win_y0, 2)+Math.pow(win_xc-win_x0, 2))+radius)/1.4){
							
							speedX = -speedX;
							speedY = -speedY;
							x += speedX;
							y += speedY;
							collided = true;
						
					}
					
					
					
					if(collided){
						if(ball_creating == false){
							bricks[i][j].strike();
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	// Метод прорисовки самого себя
	public void paint(Graphics2D canvas){
		canvas.setColor(color);
		canvas.setPaint(color);
		Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);
		canvas.draw(ball);
		canvas.fill(ball);
	}
}
					
