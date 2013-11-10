/**
 * 
 */
import java.util.Scanner;

/**
 * @author AbbyAppuCutie
 * 
 */
public class MainClass {

	private static double minrange = 0.0, maxrange =100.0;
	private static double wtmin=-0.5,wtmax=0.5;
	private static double[] t=new double[30];
	private static double[] p=new double[30];
	private static double[] wt = new double[12];
	public static int i = 3;
	public static int j = 0;
	
	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);

		System.out.println("Enter the initial weight value(-0.05 to +0.05)");
		double temp = s.nextDouble();
		wt[0] = wt[1] = wt[2] = wt[3] = wt[4] = wt[5] = wt[6] = wt[7] = wt[8] = wt[9] = wt[10]=wt[11]=temp;
		System.out.println("Enter time series data");

		t[0] = s.nextDouble();
		t[1] = s.nextDouble();
		t[2] = s.nextDouble();
		while (i != 40) {
			t[i] = s.nextDouble();
			i++;
			System.out.println("Input"+i+":["+t[i - 4]+ " "+ t[i - 3]+ " "+ t[i - 2]+" "+ t[i - 1]+"]");
			p[j++] = predict(t[i - 4], t[i - 3], t[i - 2], t[i - 1])+(t[i-1]+t[i-2]+t[i-3]+t[i-4])/4;
			System.out.println("For time " + i + " the predicted value is"
					+ p[j - 1]);
		}
	}

	/**
	 * @param w
	 *            the weight matrix
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 *            6 intermediate neural values-hidden layer output
	 * @param o
	 *            output layer
	 * @return updated weight by backpropagation algorithm
	 */
	private static double[] updatewt( double a, double b, double c,
			double d, double e, double f, double o) {
		if(o>=0.1){
		wt[0] =normalize( wt[0] - a*b*c*d);
		wt[1] =normalize( wt[1] - b*c*d*e);
		wt[2] =normalize( wt[2] - c*d*e*f);
		wt[3] =normalize( wt[3] - d*e*f*a);
		wt[4] =normalize( wt[4] - e*f*a*b);
		wt[5] =normalize( wt[5] - f*a*b*c);
		wt[6] =normalize( wt[6]- (p[j]-t[i])/2);
		wt[7] =normalize( wt[7]-(p[j-1]-t[i-1])/2);
		wt[8] =normalize( wt[8]-(p[j-2]-t[i-2])/2);
		wt[9] =normalize( wt[9]-(p[j-3]-t[i-3])/2);
		wt[10] =normalize( wt[10]-(p[j-4]-t[i-4])/2);
		wt[11] =normalize( wt[11]-(p[j-5]-t[i-5])/2);
		
		for(double r:wt)
		System.out.println("w  "+r);
		}
		return wt;

	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 *            4 time series values
	 * @param wt
	 *            weight matrix
	 * @return
	 */
	private static double predict(double a, double b, double c, double d) {
		// Neurons in Input Layer
		double p1 = normalize(a);
		double p2 = normalize(b);
		double p3 = normalize(c);
		double p4 = normalize(d);

		// Neurons in Hidden Layer
		double n1 = normalize((p1 * wt[0] + p2 * wt[1] + p3 * wt[2] + p4
				* wt[3]));
		double n2 = normalize((p1 * wt[1] + p2 * wt[2] + p3 * wt[3] + p4
				* wt[4]));
		double n3 = normalize((p1 * wt[2] + p2 * wt[3] + p3 * wt[4] + p4
				* wt[5]));
		double n4 = normalize((p1 * wt[3] + p2 * wt[4] + p3 * wt[5] + p4
				* wt[0]));
		double n5 =normalize((p1 * wt[4] + p2 * wt[5] + p3 * wt[0] + p4
				* wt[1]));
		double n6 = normalize((p1 * wt[5] + p2 * wt[0] + p3 * wt[1] + p4
				* wt[2]));
		System.out.println("Intermediate neuron values:["+n1+"  "+n2+"  "+n3+"  "+n4+"  "+n5+"  "+n6);
		// One Neuron in the output Layer
		double out = normalize(n1*wt[6]+n2*wt[7]+n3*wt[8]+n4*wt[9]+n5*wt[10]+n6*wt[11]);
		wt = updatewt(n1, n2, n3, n4, n5, n6, out);
		return denormalize(out)*100;
	}

	/**
	 * @param out
	 *            output normal
	 * @return denormalized value
	 */
	private static double denormalize(double out) {

		return  (out * maxrange);
	}

	/**
	 * @param d
	 *            input vector
	 * @return normalized vector
	 */
	private static double normalize(double d) {
		//System.out.println("norm "+ ((double)(maxrange - d)) / ((double)(maxrange - minrange)));
		double temp=1.0-((double)(maxrange) - (d)) / ((double)(maxrange - minrange));
		if(temp>1.0){
			temp=normalize(temp);
		}
		return temp;
	}
	private static double normalize(int d) {
		//System.out.println("norm "+ ((double)(maxrange - d)) / ((double)(maxrange - minrange)));
		double temp=1.0-((double)(maxrange - d)) / ((double)(maxrange - minrange));
		if(temp>1.0){
			temp=normalize(temp);
		}
		return temp;
	}
}
