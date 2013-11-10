import java.io.*;
import java.lang.*;
import java.math.*;
public class Basicpso{
	public static int t=4, n=4;
	public static double[][] agents={{59.4,290.8,3.0,29.0,0.0},{57.6,285.9,3.0,29.0,4.0},{55.4,278.9,3.0,28.9,8.0},{53.4,264.0,3.0,28.0,12.0},{51.6,250.8,3.0,27.9,16.0},
					{49.5,245.6,3.0,28.2,20.0},{47.4,237.8,3.0,28.1,24.0},{45.6,230.1,3.0,28.0,28.0},{44.8,217.6,3.0,27.9,32.0},{42.2,197.0,3,27.9,36.0},{41.0,192.6,3.0,27.1,40.0},
					{39.0,180.6,3.0,27.8,44.0},{38.4,176.1,3.0,27.5,48.0},{37.1,177.4,3.0,27.5,52.0},
					{37.4,179.3,3.0,27.8,56.0},{39.4,184.8,3.0,27.6,60.0},{41.3,190.9,3.0,27.4,64.0},{44.4,217.5,3.0,26.9,68.0},{42.9,200.8,3.0,27.0,72.0},{41.0,192.6,3.0,27.1,76.0},
					{42.2,197.0,3.0,27.9,80.0},{40.8,194.6,3.0,27.6,84.0},{39.6,180.5,3.0,28.8,88.0},{38.4,177.8,3.0,28.6,92.0},
					{35.2,163.0,3,29.5,96.0},{34.2,158.9,3,29.4,100.0},{31.9,124.5,3,30.2,104.0},
					{31.5,139.3,4,30.4,108.0},{29.4,127.8,4.0,31.5,112.0},{27.7,119.2,4.0,32.0,116.0},{25.8,111.7,4,32.5,120.0},{23.4,95.9,4.0,30.6,124.0},{20.4,88.8,4.0,38.5,128.0}};
	public static void main(String[] args){
		int i,j,k; 
		
		double[] agentbestpos = new double[n];
		double[] globbestpos = new double[n];
		double[][] velocities = new double[agents.length][n];
		double[][] acc = new double[agents.length+1][n+1];
		double[] fitness = new double[agents.length] ;
		double[][] pr1=new double[agents.length][n];
		
		double[][] weightcoeff = new double[agents.length][n];
		double[][] weightcoeff1 = new double[agents.length][n];
		double[][] v = new double[agents.length][n];
		double[][] s = new double[agents.length][n];

		//Fuzzification
		for( i=1;i<agents.length;i++)
		{
		
			if((agents[i][0]<=20) || (agents[i][0]>=80))
				agents[i][0]=agents[i-1][0];
			if((agents[i][1]<=90) || (agents[i][1]>=460))
				agents[i][1]=agents[i-1][1];
			if((agents[i][2]>=0) || (agents[i][2]<=4))
				agents[i][2]=agents[i-1][2];
			if((agents[i][3]<=12) || (agents[i][3]>=40))
				agents[i][3]=agents[i-1][3];
		}
		
		for( i=1;i<agents.length;i++){
			double timeinter=agents[i][4]-agents[i-1][4];
			for( j=0; j<4; j++){
				velocities[i][j] = (agents[i][j] - agents[i-1][j]) / timeinter;
				acc[i][j] = velocities[i][j] / timeinter;
				//System.out.println("["+ i + "," + j + "]" +velocities[i][j]+"	"+"Time"+timeinter+"    ");
				//System.out.println("["+ i + "," + j + "]" +acc[i][j]+"	"+"Time"+timeinter+"    ");
			}
			acc[i][4] = acc[i][0];
			acc[0][0]=acc[3][0];
			acc[0][1]=acc[3][1];
			acc[0][2]=acc[3][2];
			acc[0][3]=acc[3][3];
			//System.out.print(acc[i][0]+"	"+ acc[i][4]+" "+acc[0][0]+" "+acc[3][0]);
			
		}

		for(i=0;i<n;i++){
			for(j=0;j<=n;j++){
				//System.out.println("["+ i + "," + j + "]" +acc[i][j]+"	");
				}
		}
		
		
		//Prediction of next state
		
		
		for( k=0; k<agents.length-3; k++){
			for( j=0; j<n; j++){
				double a=0.0;
				int ti=0;
				for( i=k; i<k+t; i++){
					a += agents[i][j] * i;
					ti += i;
				}
			pr1[k][j] = a / ti;
			//System.out.println("Predicted value [" + k + "]" + "[" + j + "]" +" = "+pr1[k][j]);
			}	
			
		//Fuzzification of Predicted value
			int k1=k;
			statePrediction(pr1, k1);
		
		//PSO based weights determination
			//Weight Coefficient
			
			for(j=0 ; j<n ; j++){		
				weightcoeff[k][j] = agents[k+3][j] / pr1[k][j];
				//System.out.println("Weight coefficient = "+ weightcoeff[k][j]);
			}

			fitness[k] = fitness(k,weightcoeff[k], pr1[k]);
			System.out.println("Fitness = " + fitness[k]);
			
			for(i=k;i<k+t;i++){
				for(j=0 ;j<n ;j++){
					v[i][j] = velocities[i][j];
					s[i][j] = agents[i][j];
				}
			}			

			for(int l=0; l<20;l++){
		
				double[] p = new double[n];
				double[] g = new double[n];
				double[] vi = new double[n];
				double[] si = new double[n];
				double[] rp = {0.035,0.015,0.02,0.025};
				double[] rg = {0.03,0.015,0.02,0.025};
				for(j=0;j<n;j++){
					agentbestpos[j] = agents[k+3][j];
					globbestpos[j] = agents[k+2][j];
				}
					
			//Velocity and Position updation
				for(j=0;j<n;j++){
					
					for(i=k;i<k+t;i++){
						p[j] = agentbestpos[j];
					 	g[j] = globbestpos[j];
					 	double phip = acc[i][j] * rp[j];
					 	double phig = acc[i+1][j] * rg[j];
					 	v[i][j] = v[i][j] + phip * ( p[j] - s[i][j] ) + phig * (g[j] - s[i][j]);
					 	s[i][j] = s[i][j] + v[i][j];
					}
				}
			
			
				for(j=0 ; j<n ; j++){
					weightcoeff1[k][j] = s[k+3][j] / pr1[k][j];
					if(weightcoeff1[k][j] < 0)
						weightcoeff1[k][j] = 0.1;
					else if(weightcoeff1[k][j] > 1)
						weightcoeff1[k][j] = 1.0;
					
				}
			
				double[] fitness1= new double[agents.length];
				double fitnessval1 = 0.0;
				double diff1 = 0.0;
				for( j=0 ; j<n ; j++){
			//Fitness
					for( i=k ; i<k+4 ; i++){
						//System.out.println("agents[" + i + "][" + j + "]= " + agents[i][j] + " weightcoeff["+i+"]["+j+"] " + weightcoeff[i][j] + "prstate["+i+"]["+j+"]" + prstate[i][j]); 
						diff1 += ((s[i][j] - (weightcoeff1[k][j] * pr1[k][j]))*(s[i][j] - (weightcoeff1[k][j] * pr1[k][j]))) ;
					}	
					//System.out.print("Diff11 = "+ diff1+"	");
				
					fitnessval1 += ( diff1 / t);
				}
				fitness1[k] = fitnessval1;
				//System.out.println("Fitness1 = "+ fitness1[k] +"	");
			}
			
			for(i=0;i<n;i++){
				//System.out.println("Agent["+ k + ","+i +"]" + s[k][i]);
				System.out.println("Weight coefficient1 = "+ weightcoeff1[k][i]);
			}
		
		//INFERENCING
			double[][] crstate = new double[agents.length][n];
			
			if(pr1[k][0] >= 80)  
				crstate[k][0] = 1.0;
			else if((pr1[k][0] < 80) && (pr1[k][0] > 49))
				crstate[k][0] = 2.0;
			else if((pr1[k][0] <= 49) && (pr1[k][0] > 35))
				crstate[k][0] = 3.0;
			else if(pr1[k][0] <= 35) 
				crstate[k][0] = 4.0;
			
			if(pr1[k][1] >= 350)  
				crstate[k][1] = 1.0;
			else if((pr1[k][1] < 350) && (pr1[k][1] >= 250 ))
				crstate[k][1] = 2.0;
			else if((pr1[k][1] <= 249) && (pr1[k][1] >= 150 ))
				crstate[k][1] = 3.0;
			else if(pr1[k][1] <= 149) 
				crstate[k][1] = 4.0;

			if((pr1[k][2] == 0) || (pr1[k][2] == 1))  
				crstate[k][2] = 1.0;
			else if(pr1[k][2] == 2)
				crstate[k][2] = 1.7;
			else if(pr1[k][2] == 3)
				crstate[k][2] = 2.1;
			else if(pr1[k][2] == 4) 
				crstate[k][2] = 2.5;

			if(pr1[k][3] > 21)  
				crstate[k][3] = 1.0;
			else 
				crstate[k][3] = 2.0;
			double crLow=0.0,crMod=0.0,crHigh=0.0,crSevere=0.0;	
			for( j=0 ; j<n ; j++){
				
				if( crstate[k][j] == 1.0 ){
					crLow += weightcoeff1[k][j] * crstate[k][j];
					//System.out.println("L "+crLow);
				}
				else if( crstate[k][j] == 2.0 || crstate[k][j] == 1.7   ){
					
					crMod += weightcoeff1[k][j] * crstate[k][j];
					//System.out.println("M "+crMod);
				}
				else if( crstate[k][j] == 3.0 || crstate[k][j] == 2.1 ){
					
					crHigh += weightcoeff1[k][j] * crstate[k][j];
					//System.out.println("H "+crHigh);
				}
				else if( crstate[k][j] == 4.0 || crstate[k][j] == 2.5  ){
					
					crSevere += weightcoeff1[k][j] * crstate[k][j];
					//System.out.println("S "+crSevere);
				}
			}
			if((crLow > crMod) && (crLow > crHigh) && (crLow > crSevere))
			{
				
				System.out.println("Predicted state at time t[" + k + "] is Low");
			}
			else if((crMod >= crLow) && (crMod >= crHigh) && (crMod >= crSevere))
			{
				
				System.out.println("Predicted state at time t[" + k + "] is Moderate");
			}
			else if((crHigh >= crLow) && (crMod < crHigh) && (crHigh >= crSevere))
			{
				
				System.out.println("Predicted state at time t[" + k + "] is High");
			}
			else if((crSevere >= crLow) && (crSevere > crHigh) && (crSevere > crMod))
			{
				
				System.out.println("Predicted state at time t[" + k + "] is Severe");
			}
			
			//statePrediction(s, k1);
					 
		}
	}




	public static double fitness(int k1,double[] weightcoeff1,double[] pr){
		double fitnessval = 0.0;
		double diff = 0.0;
		for(int j=0 ; j<n ; j++){
			//Fitness
			for(int i=k1 ; i<k1+4 ; i++){
				//System.out.println("agents[" + i + "][" + j + "]= " + agents[i][j] + " weightcoeff["+i+"]["+j+"] " + weightcoeff[i][j] + "prstate["+i+"]["+j+"]" + prstate[i][j]); 
				diff += ((agents[i][j] - (weightcoeff1[j] * pr[j]))*(agents[i][j] - (weightcoeff1[j] * pr[j]))) ;
			}
		fitnessval += ( diff / t);
		}
	return fitnessval;	
	}

	public static void statePrediction(double[][] pr,int k){
		double[][] prstate = new double[agents.length][n];
		double[] predstate = new double[agents.length];
		int low=0,high=0,severe=0,moderate=0,j;
		if(pr[k][0] >= 80)  
			prstate[k][0] = 0.0;
		else if((pr[k][0] < 80) && (pr[k][0] > 49))
			prstate[k][0] = 1.0;
		else if((pr[k][0] <= 49) && (pr[k][0] > 35))
			prstate[k][0] = 2.0;
		else if(pr[k][0] <= 35) 
			prstate[k][0] = 3.0;
		
		if(pr[k][1] >= 350)  
			prstate[k][1] = 0.0;
		else if((pr[k][1] < 350) && (pr[k][1] >= 250 ))
			prstate[k][1] = 1.0;
		else if((pr[k][1] <= 249) && (pr[k][1] >= 150 ))
			prstate[k][1] = 2.0;
		else if(pr[k][1] <= 149) 
			prstate[k][1] = 3.0;

		if((pr[k][2] == 0) || (pr[k][2] == 1))  
			prstate[k][2] = 0.0;
		else if(pr[k][2] == 2)
			prstate[k][2] = 1.0;
		else if(pr[k][2] == 3)
			prstate[k][2] = 2.0;
		else if(pr[k][2] == 4) 
			prstate[k][2] = 3.0;

		if(pr[k][3] > 21)  
			prstate[k][3] = 0.0;
		else 
			prstate[k][3] = 1.0;
				
		for( j=0 ; j<n ; j++){
			if( prstate[k][j] == 0.0 ){
				System.out.println("Low");
				low++;
			}
			else if( prstate[k][j] == 1.0 ){
				System.out.println("Moderate");
				moderate++;
			}
			else if( prstate[k][j] == 2.0 ){
				System.out.println("High");
				high++;
			}
			else if( prstate[k][j] == 3.0 ){
				System.out.println("Severe");
				severe++;
			}
		}
			
		if((low > moderate) && (low > high) && (low > severe))
		{
			predstate[k] = 0.0;
			System.out.println("Predicted state at time t[" + k + "] is Low");
		}
		else if((moderate >= low) && (moderate >= high) && (moderate >= severe))
		{
			predstate[k] = 1.0;
			System.out.println("Predicted state at time t[" + k + "] is Moderate");
		}
		else if((high >= low) && (moderate < high) && (high >= severe))
		{
			predstate[k] = 2.0;
			System.out.println("Predicted state at time t[" + k + "] is High");
		}
		else if((severe >= low) && (severe > high) && (severe > moderate))
		{
			predstate[k] = 3.0;
			System.out.println("Predicted state at time t[" + k + "] is Severe");
		}
	}
}
