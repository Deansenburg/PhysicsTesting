package GxEngine3D.CalculationHelper;

import GxEngine3D.Model.RefPoint3D;

public class DistanceCalc {

	public static double getDistance(double[] d1, double[] d2) {
		double total = 0;
		// assumes same dimensions
		for (int i = 0; i < d1.length; i++) {
			total += (d1[i] - d2[i]) * (d1[i] - d2[i]);
		}
		return Math.sqrt(total);
	}

	public static double getDistanceNoRoot(double[] d1, double[] d2) {
		double total = 0;
		// assumes same dimensions
		for (int i = 0; i < d1.length; i++) {
			total += (d1[i] - d2[i]) * (d1[i] - d2[i]);
		}
		return total;
	}
}
