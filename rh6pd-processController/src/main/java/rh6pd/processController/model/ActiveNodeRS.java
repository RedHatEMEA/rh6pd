package rh6pd.processController.model;

import java.io.Serializable;

public class ActiveNodeRS implements Serializable {

	private static final long serialVersionUID = 7748925264158288464L;

	private String name;
	private String x;
	private String y;
	private String width;
	private String height;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

}