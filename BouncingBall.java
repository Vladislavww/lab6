package task6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

public class BouncingBall implements Runnable{
	private static final int MAX_RADIUS = 40;
	private static final int MIN_RADIUS = 3;
	private static final int MAX_SPEED = 10;
	private int BRICKS_IN_WIDTH;//переделать
	private int BRICKS_IN_HEIGHT;//переделать
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
	private BrickClass[][] bricks; //матрица кирпичей
	// Конструктор класса BouncingBall
	public BouncingBall(Field field, BrickClass[][] bricks, final int BRICKS_WIDTH, final int BRICKS_HEIGHT){
		// Необходимо иметь ссылку на поле, по которому прыгает мяч,
		// чтобы отслеживать выход за его пределы 
		// через getWidth(), getHeight()
		//Также необходимо иметь ссылку на матрицу препятствий для отслеживания столкновений
		this.field = field;
		this.bricks = bricks;
		BRICKS_IN_WIDTH = BRICKS_WIDTH;
		BRICKS_IN_HEIGHT = BRICKS_HEIGHT;
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
		// Цвет мяча выбирается случайно
		color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
		// Начальное положение мяча случайно
		do{
			x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
			y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
		}while(brick_collide(true)==true); //Цикл нужен, чтобы мячи не появились в кирпичах
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
		for(int i=0; i<BRICKS_IN_WIDTH; i++){ //Проверка по всем кирпичам
			for(int j=0; j<BRICKS_IN_HEIGHT; j++){
				if(bricks[i][j].check_strikable()){
					//Координаты углов 
					double win_x0 = bricks[i][j].get_coordX(0);
					double win_x1 = bricks[i][j].get_coordX(1);
					double win_y0 = bricks[i][j].get_coordY(0);
					double win_y1 = bricks[i][j].get_coordY(1);
					//Координаты середины
					double win_xc = (win_x0 + win_x1)/2;
					double win_yc = (win_y0 + win_y1)/2;
					//Положения краев мяча с учетом скорости
					double y0 = y + speedY + radius;
					double y1 = y + speedY - radius;
					double x0 = x + speedX + radius;
					double x1 = x + speedX - radius;
					//Провекрка на столкновение с верхней/нижней стороной
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
					//Провекрка на столкновение с левой/правой стороной
					else if((y>=win_y0 && y<= win_y1)||(y>=win_y0 && y<= win_y1)){
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
					//Провекрка на столкновение с углами
					/*Здесь сраавнивается расстояние от центра шара до центра кирпича
					суммой половины диагонали и радиуса. Коеффициент 1.4 служит для уменьшения правой части неравенства
					для корректной работы.
					*/
					else if(Math.sqrt(Math.pow(win_yc-y, 2)+Math.pow(win_xc-x, 2))<=(Math.sqrt(Math.pow(win_yc-win_y0, 2)+Math.pow(win_xc-win_x0, 2))+radius)/1.4){
						speedX = -speedX;
						speedY = -speedY;
						x += speedX;
						y += speedY;
						collided = true;
						
					}
					else if(ball_creating == true){
						if(Math.sqrt(Math.pow(win_yc-y, 2)+Math.pow(win_xc-x, 2))<=(Math.sqrt(Math.pow(win_yc-win_y0, 2)+Math.pow(win_xc-win_x0, 2))+radius)*2){
							//Исправление бага, когда мяч появляется в стене
							return true;
						}

					}
					if(collided){
						if(ball_creating == false){
							bricks[i][j].strike(); //уменьшить прочность кирпича, если это не этап создания шара
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
					
