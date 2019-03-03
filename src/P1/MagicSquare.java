package P1;

import java.io.*;
import java.util.Scanner;

public class MagicSquare {

	
	public static void main(String[]args) throws FileNotFoundException {
		 MagicSquare m = new MagicSquare();
	        for(int i=1; i<=5; i++)
	            System.out.println(i+".txt "+m.isLegalMagicSquare("src/P1/txt/"+i+".txt"));
//	        int n = 5;
//	        System.out.println(m.generateMagicSquare(n));
	        int num;
	        Scanner scan = new Scanner(System.in);
	        num = scan.nextInt();
	        if(generateMagicSquare(num))
	            System.out.println("6.txt "+m.isLegalMagicSquare("src/P1/txt/6.txt"));
	        scan.close();
	}
			
	
	
	boolean isLegalMagicSquare(String fileName) throws FileNotFoundException {
		final int MAX = 1000;
		FileReader fileReader = null;
		File file = new File(fileName);
		fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		if (file.isFile()&& file.canRead()) {
			String line = "";
			int[][]matrix = new int[MAX][MAX];
			int row = 0,col = 0;
			try {
				while((line = bufferedReader.readLine()) !=null ) {
					String[] num = line.split("\t");
					int len = num.length;
                    if((row==0&&len>col)||(row!=0&&len==col))
                        col=len;
                    else
                        return false;
                    for(int i=0; i<len; i++)
                    {
                        if(num[i].contains(".")||num[i].contains(" ")||num[i].contains("-"))
                        {
                            return false;
                        }
                        matrix[row][i]=Integer.valueOf(num[i]);
                    }
                    row++;
				}
				
                if(row!=col)
                    return false;
                int sum1,sum2,sum3,sum4;
                sum1=sum2=sum3=sum4=0;
                for(int j=0; j<col; j++)
                {
                    sum1+=matrix[0][j];
                }
                for(int i=0; i<col; i++)
                {
                    sum2=0;
                    for(int j=0; j<col; j++)
                        sum2+=matrix[i][j];
                    if(sum1!=sum2)
                        return false;
                }
                for(int i=0; i<col; i++)
                {
                    sum2=0;
                    for(int j=0; j<col; j++)
                        sum2+=matrix[j][i];
                    if(sum1!=sum2)
                        return false;
                }
                for(int i=0; i<col; i++)
                    sum3+=matrix[i][i];
                if(sum1!=sum3)
                    return false;
                for(int i=0; i<col; i++)
                    sum4+=matrix[col-1-i][i];
                if(sum1!=sum4)
                    return false;
			
                bufferedReader.close();
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	public static boolean generateMagicSquare(int n) {
		
		if(n<0||n%2==0)
        {
            System.out.println("ÊäÈëÓÐ´íÎó");
            return false;
        }
		
		int magic[][] = new int[n][n];
		int row = 0, col = n / 2, i, j, square = n * n;

		for (i = 1; i <= square; i++) {
			magic[row][col] = i;
			if (i % n == 0)
				row++;
			else {
				if (row == 0)
					row = n - 1;
				else
					row--;
				if (col == (n - 1))
					col = 0;
				else
					col++;
			}
		}

		try {
            File file = new File("src/P1/txt/6.txt");
            BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file));
            if(file.isFile() && file.canWrite())
            {
                for (i = 0; i < n; i++) {    
                    for (j = 0; j < n; j++)     
                        bufferedwriter.write(magic[i][j] + "\t");
                    bufferedwriter.newLine();
                }
            }
            bufferedwriter.close();
        }
        catch(IOException e)
        {
           System.out.print(e);
        }
return true;
}


}
