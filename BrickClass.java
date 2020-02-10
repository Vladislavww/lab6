package task6;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class BrickClass{
	private int x;
	private int y; //расположение в координатах матрицы(не окна)
	private double win_x0;
	private double win_y0; //расположение углов в координатах окна
	private double win_x1;
	private double win_y1;
	private Field field;
	private Color color;// = new Color(125, 125, 125);
	private int health = 5;
	private static final int BRICKS_IN_WIDTH = 20;//эти константы влияют на размеры кирпича(чем больше значение
	private static final int BRICKS_IN_HEIGHT = 20; //тем меньше кирпич
	public BrickClass(Field field, int x, int y){
		this.field = field;
		this.x = x;
		this.y = y;
		//color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
	}
	
	private void get_win_coords(){
		win_x0 = x*field.getWidth()/BRICKS_IN_WIDTH;
		win_x1 = x*field.getWidth()/BRICKS_IN_WIDTH + field.getWidth()/BRICKS_IN_WIDTH;
		win_y0 = y*field.getHeight()/BRICKS_IN_HEIGHT;
		win_y1 = y*field.getHeight()/BRICKS_IN_HEIGHT + field.getHeight()/BRICKS_IN_HEIGHT;
	}
	public void paint(Graphics2D canvas){
		if(health >0){
			color = new Color(140, 50*health, 0);
			canvas.setColor(color);
			canvas.setPaint(color);
			get_win_coords();
			Rectangle2D.Double brick = new Rectangle2D.Double(win_x0, win_y0, win_x1-win_x0, win_y1-win_y0);
			canvas.draw(brick);
			canvas.fill(brick);
		}
	}
	
	public synchronized double get_coordX(int num){
		if(num==0){
			return win_x0;
		}
		if(num==1){
			return win_x1;
		}
		return -1;
	}
	public synchronized double get_coordY(int num){
		if(num==0){
			return win_y0;
		}
		if(num==1){
			return win_y1;
		}
		return -1;
	}
	public synchronized void strike(){
		if(health > 0){
			health -= 1;
		}
	}
	public boolean check_strikable(){
		if(health>0){
			return true;
		}
		return false;
	}

}
