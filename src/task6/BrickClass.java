package task6;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class BrickClass{
	//расположение в координатах матрицы(не окна)
	private int x;
	private int y;
	//расположение углов в координатах окна
	private double win_x0;
	private double win_y0; 
	private double win_x1;
	private double win_y1;
	//Координаты центра
	private double win_xc;
	private double win_yc;
	private Field field;
	private Color color;
	private int health = 5;//Прочность кирпича
	private static final int BRICKS_IN_WIDTH = 20;//эти константы влияют на размеры кирпича(чем больше значение
	private static final int BRICKS_IN_HEIGHT = 20; //тем меньше кирпич)
	public BrickClass(Field field, int x, int y){
		this.field = field;
		this.x = x;
		this.y = y;
	}
	
	//Вычисление координат кирпича в окне(они зависят от размера окна) 
	private void get_win_coords(){
		win_x0 = x*field.getWidth()/BRICKS_IN_WIDTH;
		win_x1 = x*field.getWidth()/BRICKS_IN_WIDTH + field.getWidth()/BRICKS_IN_WIDTH;
		win_y0 = y*field.getHeight()/BRICKS_IN_HEIGHT;
		win_y1 = y*field.getHeight()/BRICKS_IN_HEIGHT + field.getHeight()/BRICKS_IN_HEIGHT;
		win_xc = (win_x0+win_x1)/2;
		win_yc = (win_y0+win_y1)/2;
	}
	
	//Метод прорисовки самого себя
	public void paint(Graphics2D canvas){
		if(health >0){ //если кирпич сломали(health=0), то не рисую его
			color = new Color(140, 50*health, 0);
			canvas.setColor(color);
			canvas.setPaint(color);
			get_win_coords();
			Rectangle2D.Double brick = new Rectangle2D.Double(win_x0, win_y0, win_x1-win_x0, win_y1-win_y0);
			canvas.draw(brick);
			canvas.fill(brick);
			paint_label(canvas);
		}
	}
	private void paint_label(Graphics2D canvas){
		Integer to_string = health;
		String str = to_string.toString();
		// Вывести надпись в точке с вычисленными координатами 
		canvas.setColor(Color.BLACK);
		canvas.drawString(str, (float)win_xc, (float)win_yc);

	}
	//Получение координат кирпича по X
	public double get_coordX(int num){
		if(num==0){
			return win_x0;
		}
		if(num==1){
			return win_x1;
		}
		return -1;
	}
	//Получение координат кирпича по Y
	public double get_coordY(int num){
		if(num==0){
			return win_y0;
		}
		if(num==1){
			return win_y1;
		}
		return -1;
	}
	//Уменьшить прочность при столкновении
	public void strike(){
		if(health > 0){
			health -= 1;
		}
	}
	//Проверка на наличие прочности
	public boolean check_strikable(){
		if(health>0){
			return true;
		}
		return false;
	}

}
