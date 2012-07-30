package rh6pd.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import rh6pd.web.common.Car;

@ManagedBean(name="vehicleData")
@SessionScoped
public class VehicleData {
	
	private static List<Car> vehicleInfo;
	
	@PostConstruct
	public static void buildVehicleList() {
		
	     //Build Vehicle Array List   
		setVehicleInfo(new ArrayList<Car>());          
		getVehicleInfo().add(new Car("ford", "fiesta", 2.0));  
		getVehicleInfo().add(new Car("audi", "a5", 2.0));  
		getVehicleInfo().add(new Car("bmw", "3 Series", 2.0));  
		getVehicleInfo().add(new Car("volvo", "d90", 3.0));  
		getVehicleInfo().add(new Car("nissan", "350z", 2.5)); 
		
	}

	/**
	 * @return the vehicleInfo
	 */
	public static List<Car> getVehicleInfo() {
		return vehicleInfo;
	}

	/**
	 * @param vehicleInfo the vehicleInfo to set
	 */
	public static void setVehicleInfo(List<Car> vehicleInfo) {
		VehicleData.vehicleInfo = vehicleInfo;
	}

}
